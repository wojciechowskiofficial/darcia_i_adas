import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.File;
import java.io.IOException;

public class OpenNLP {

    public static String LANG_DETECT_MODEL = "models/langdetect-183.bin";
    public static String TOKENIZER_MODEL = "models/en-token.bin";
    public static String SENTENCE_MODEL = "models/en-sent.bin";
    public static String POS_MODEL = "models/en-pos-maxent.bin";
    public static String CHUNKER_MODEL = "models/en-chunker.bin";
    public static String LEMMATIZER_DICT = "models/en-lemmatizer.dict";
    public static String NAME_MODEL = "models/en-ner-person.bin";
    public static String ENTITY_XYZ_MODEL = "models/en-ner-xyz.bin";

	public static void main(String[] args) throws IOException
    {
		OpenNLP openNLP = new OpenNLP();
		openNLP.run();
	}

	public void run() throws IOException
    {

		//languageDetection();
		//tokenization();
		//sentenceDetection();
		//posTagging();
		//lemmatization();
		//stemming();
		chunking();
		// nameFinding();
	}

	private void languageDetection() throws IOException
    {
		File modelFile = new File(LANG_DETECT_MODEL);
		LanguageDetectorModel ldmodel = new LanguageDetectorModel(modelFile);
		LanguageDetectorME langdetectorME = new LanguageDetectorME(ldmodel);

		String text = "";
		//text = "cats";
		//text = "cats like milk";
		// text = "Many cats like milk because in some ways it reminds them of their
		// mother's milk.";
		// text = "The two things are not really related. Many cats like milk because in
		// some ways it reminds them of their mother's milk.";
		text = "The two things are not really related. Many cats like milk because in some ways it reminds them of their mother's milk. "
				+ "It is rich in fat and protein. They like the taste. They like the consistency . "
				+ "The issue as far as it being bad for them is the fact that cats often have difficulty digesting milk and so it may give them "
				+ "digestive upset like diarrhea, bloating and gas. After all, cow's milk is meant for baby calves, not cats. "
				+ "It is a fortunate quirk of nature that human digestive systems can also digest cow's milk. But humans and cats are not cows.";
		// text = "Many cats like milk because in some ways it reminds them of their
		// mother's milk. Le lait n'est pas forc�ment mauvais pour les chats";
		// text = "Many cats like milk because in some ways it reminds them of their
		// mother's milk. Le lait n'est pas forc�ment mauvais pour les chats. "
		// + "Der Normalfall ist allerdings der, dass Salonl�wen Milch weder brauchen
		// noch gut verdauen k�nnen.";

		System.out.println(langdetectorME.predictLanguage(text));
	}

	private void tokenization() throws IOException
    {
		File modelFile = new File("models/en-token.bin");
		TokenizerModel tmodel = new TokenizerModel(modelFile);
		TokenizerME tokenizerME = new TokenizerME(tmodel);

		String text = "";

		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
				+ "but there may have been instances of domestication as early as the Neolithic from around 9500 years ago (7500 BC).";
		/*text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
				+ "but there may have been instances of domestication as early as the Neolithic from around 9,500 years ago (7,500 BC).";
		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
		 + "but there may have been instances of domestication as early as the Neolithic from around 9 500 years ago ( 7 500 BC).";*/

		String[] tokens = tokenizerME.tokenize(text);
		double[] probabilities = tokenizerME.getTokenProbabilities();

		for (int i=0; i<probabilities.length; i++) {
			System.out.println(tokens[i] + "   probability:  " + probabilities[i]);
		}
	}

	private void sentenceDetection() throws IOException
    {

		File modelFile = new File(SENTENCE_MODEL);
		SentenceModel sentenceModel = new SentenceModel(modelFile);
		SentenceDetectorME detectorME = new SentenceDetectorME(sentenceModel);

		String text = "";
		text = "Hi. How are you? Welcome to OpenNLP. "
				+ "We provide multiple built-in methods for Natural Language Processing.";
		/*text = "Hi. How are you?! Welcome to OpenNLP? "
				+ "We provide multiple built-in methods for Natural Language Processing.";
		text = "Hi. How are you? Welcome to OpenNLP.?? "
				+ "We provide multiple . built-in methods for Natural Language Processing.";
		text = "The interrobang, also known as the interabang (often represented by ?! or !?), "
				+ "is a nonstandard punctuation mark used in various written languages. "
				+ "It is intended to combine the functions of the question mark (?), or interrogative point, "
				+ "and the exclamation mark (!), or exclamation point, known in the jargon of printers and programmers as a \"bang\". ";*/

		String[] sentences = detectorME.sentDetect(text);
		double[] probabilities = detectorME.getSentenceProbabilities();

		for (int i=0; i<probabilities.length; i++) {
			System.out.println(sentences[i] + "   probability:  " + probabilities[i]);
		}
    }

	private void posTagging() throws IOException {

		File modelFile = new File(POS_MODEL);
		POSModel posmodel = new POSModel(modelFile);
		POSTaggerME postaggerME = new POSTaggerME(posmodel);

		String[] sentence = new String[0];
		sentence = new String[] { "Cats", "like", "milk" };
		/*sentence = new String[]{"Cat", "is", "white", "like", "milk"};
		sentence = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
				"built-in", "methods", "for", "Natural", "Language", "Processing" };
		sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };*/

		String[] tags = postaggerME.tag(sentence);
		for (int i=0; i<tags.length; i++) {
			System.out.println(tags[i]);
		}

	}

	private void lemmatization() throws IOException
    {
		File modelFile = new File(LEMMATIZER_DICT);
		DictionaryLemmatizer dl = new DictionaryLemmatizer(modelFile);
		PorterStemmer ps = new PorterStemmer();

		String[] text = new String[0];
		text = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
				"built-in", "methods", "for", "Natural", "Language", "Processing" };
		String[] tags = new String[0];
		tags = new String[] { "NNP", "WRB", "VBP", "PRP", "VB", "TO", "VB", "PRP", "VB", "JJ", "JJ", "NNS", "IN", "JJ",
				"NN", "VBG" };

		String[] lemmatizer = dl.lemmatize(text, tags);
		for (int i=0; i<lemmatizer.length; i++) {
			System.out.println("Lemmatizer: " + lemmatizer[i] + "   stemmer:   " + ps.stem(text[i]));
		}

	}

	private void stemming()
    {
		PorterStemmer porterstemmer = new PorterStemmer();

		String[] sentence = new String[0];
		sentence = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
				"built-in", "methods", "for", "Natural", "Language", "Processing" };

		for (String text : sentence) {
			System.out.println(porterstemmer.stem(text));
		}

	}
	
	private void chunking() throws IOException
    {

		File modelFile = new File(CHUNKER_MODEL);
		ChunkerModel model = new ChunkerModel(modelFile);
		ChunkerME chunkerME = new ChunkerME(model);

		String[] sentence = new String[0];
		sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };

		String[] tags = new String[0];
		tags = new String[] { "PRP", "VBD", "DT", "JJ", "NNS", "IN", "DT", "NN" };

		String[] chunkedsentence = chunkerME.chunk(sentence, tags);
		for (int i = 0; i < sentence.length; i++) {
			System.out.println(sentence[i] + "  " + chunkedsentence[i]);
		}

	}

	private void nameFinding() throws IOException
    {

		File modelFile = new File(ENTITY_XYZ_MODEL);
		TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(modelFile);
		NameFinderME namefinderME = new NameFinderME(tokenNameFinderModel);



		String text = "he idea of using computers to search for relevant pieces of information was popularized in the article "
				+ "As We May Think by Vannevar Bush in 1945. It would appear that Bush was inspired by patents "
				+ "for a 'statistical machine' - filed by Emanuel Goldberg in the 1920s and '30s - that searched for documents stored on film. "
				+ "The first description of a computer searching for information was described by Holmstrom in 1948, "
				+ "detailing an early mention of the Univac computer. Automated information retrieval systems were introduced in the 1950s: "
				+ "one even featured in the 1957 romantic comedy, Desk Set. In the 1960s, the first large information retrieval research group "
				+ "was formed by Gerard Salton at Cornell. By the 1970s several different retrieval techniques had been shown to perform "
				+ "well on small text corpora such as the Cranfield collection (several thousand documents). Large-scale retrieval systems, "
				+ "such as the Lockheed Dialog system, came into use early in the 1970s.";

		String[] splited = text.split(" ");

	}

}
