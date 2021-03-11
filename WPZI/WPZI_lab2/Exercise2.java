import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class Exercise2
{

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private OptimaizeLangDetector langDetector;

    public static void main(String[] args)
    {
        Exercise2 exercise = new Exercise2();
        exercise.run();
    }

    private void run()
    {
        try
        {
            if (!new File("./outputDocuments").exists())
            {
                Files.createDirectory(Paths.get("./outputDocuments"));
            }

            initLangDetector();

            File directory = new File("./documents");
            File[] files = directory.listFiles();

            for (File file : files)
            {
                processFile(file);
            }
       } catch (IOException | SAXException | TikaException | ParseException e)
        {
            e.printStackTrace();
        }

    }

    private void initLangDetector() throws IOException
    {
        // TODO initialize language detector (langDetector)
        langDetector = new OptimaizeLangDetector();
        langDetector.loadModels();
    }

    private void processFile(File file) throws IOException, SAXException, TikaException, ParseException {

        Date creationDate2 = null;
        Date lastModification2= null;
        String content="";

        // TODO: extract content, metadata and language from given file

        InputStream stream = new FileInputStream(file);
        AutoDetectParser adp = new AutoDetectParser();
        Metadata metadata = new Metadata();
        BodyContentHandler handler = new BodyContentHandler();
        adp.parse(stream, handler, metadata, new ParseContext());

        String language= langDetector.detect(handler.toString()).getLanguage();

        metadata.add(Metadata.CREATOR, "Creator");
        String creatorName= metadata.get(TikaCoreProperties.CREATOR);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

        String creationDate1 = metadata.get(TikaCoreProperties.CREATED);
        //System.out.println(creationDate1);
        if(creationDate1!=null) {
            creationDate2= formatter.parse(creationDate1);
        }

        String lastModification1 = metadata.get(TikaCoreProperties.MODIFIED);
        //System.out.println(lastModification1);
        if(lastModification1!=null) {
            lastModification2 = formatter.parse(lastModification1);
        }
        // Define your media type constants here

        Detector detector = adp.getDetector();

        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName() );
        TikaInputStream tikaStream = TikaInputStream.get(stream);
        MediaType mediatype = detector.detect(tikaStream, metadata);



        // call saveResult method to save the data
        saveResult(file.getName(), language, creatorName, creationDate2, lastModification2, mediatype.toString(), handler.toString()); //TODO: fill with proper values
    }

    private void saveResult(String fileName, String language, String creatorName, Date creationDate,
                            Date lastModification, String mimeType, String content)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        int index = fileName.lastIndexOf(".");
        String outName = fileName.substring(0, index) + ".txt";
        try
        {
            PrintWriter printWriter = new PrintWriter("./outputDocuments/" + outName);
            printWriter.write("Name: " + fileName + "\n");
            printWriter.write("Language: " + (language != null ? language : "") + "\n");
            printWriter.write("Creator: " + (creatorName != null ? creatorName : "") + "\n");
            String creationDateStr = creationDate == null ? "" : dateFormat.format(creationDate);
            printWriter.write("Creation date: " + creationDateStr + "\n");
            String lastModificationStr = lastModification == null ? "" : dateFormat.format(lastModification);
            printWriter.write("Last modification: " + lastModificationStr + "\n");
            printWriter.write("MIME type: " + (mimeType != null ? mimeType : "") + "\n");
            printWriter.write("\n");
            printWriter.write(content + "\n");
            printWriter.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
