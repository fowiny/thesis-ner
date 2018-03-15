import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;



public class TestCRFPPAnnotator extends TestCase {
	
	private AnalysisEngineDescription tokenizer;
	
	private AnalysisEngineDescription postagger;
	
	private AnalysisEngineDescription lemmatizer;
	
	private AnalysisEngineDescription ner;
	

	protected static void setUpBeforeClass() throws Exception {
		
	}

	protected static void tearDownAfterClass() throws Exception {
	}

	
	@Before
	protected void setUp() throws Exception {
		tokenizer = createEngineDescription(OpenNlpSegmenter.class,
				OpenNlpSegmenter.PARAM_LANGUAGE, "it");
		
		postagger = createEngineDescription(OpenNlpPosTagger.class,
				OpenNlpPosTagger.PARAM_VARIANT, "perceptron",
				OpenNlpPosTagger.PARAM_LANGUAGE, "it");
				
		lemmatizer = createEngineDescription(LanguageToolLemmatizer.class);
		
		/* Uncomment this to instantiate the CRF++ ner */
		// ner = createEngineDecription(CRFPPNer.class,
		//		CRFPPNer.PARAM_LANGUAGE, "it");
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private JCas process(String sentence) throws Exception {
		JCas cas = JCasFactory.createJCas();
		cas.setDocumentLanguage("it");
		cas.setDocumentText(sentence);
		
		runPipeline(cas, tokenizer, postagger, lemmatizer);
		return cas;
	}
	
	
	@Test
	public void testCRFPP() throws Exception {
		JCas cas = process("Mario Rossi è una persona.");
		Collection<NamedEntity> entities = JCasUtil.select(cas, NamedEntity.class);
		// You must ensure that the crf++ ner correctly returns the named entity Mario Rossi.
		// At the moment the test fails. It should passes once you correctly implement the CRF++ NEr annotator
		Iterator<NamedEntity> it = entities.iterator(); 
		if (it.hasNext()) {
			    NamedEntity ne = it.next();
		    assertTrue(
		          ne.getCoveredText().equals("Mario Rossi") &&   
				      ne.getValue().equals("PER"));
		} else fail();
	}

}

