package DKProCore.it.unitn.ner;

import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;
import org.chasen.crfpp.Tagger;
import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters;

import static org.apache.uima.fit.util.JCasUtil.indexCovered;
import static org.apache.uima.fit.util.JCasUtil.select;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.uima.cas.CASException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.TypeCapability;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

@TypeCapability(inputs = { "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" }, outputs = {
		"de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity" })
public class CRFPPNamedEntityRecognizer extends org.apache.uima.fit.component.JCasAnnotator_ImplBase {

	public static final String PARAM_LANGUAGE = ComponentParameters.PARAM_LANGUAGE;
	@ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = false, defaultValue = "it")
	protected String language;

	private Tagger tagger = null;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {

		super.initialize(aContext);

		// Write here the code frr initializing the CRF++ classifier, e.g: load saved
		// models, config params, etc...
		// almost same code in the test.java class
		 tagger = new Tagger("-m /home/festo/Downloads/CRF++-0.58/java/input/model -v 3 -n2");
	     try (BufferedReader br = new BufferedReader(new FileReader("/home/festo/Downloads/CRF++-0.58/java/input/eng.txt"))) {
	 		String line;
	 		while ((line = br.readLine()) != null) {
	 			tagger.add(line);
	 			System.out.println("Added "+line);

	 		}
	 		}
		 	catch(Exception e){
		 		System.out.println(e);
		 	}
	}

	@Override
	   public void process(JCas aJCas) throws AnalysisEngineProcessException {
		 // Here you should run the CRF++ classifier on the list of token present in each sentence present in hte JCas
	 
		Map<Sentence, Collection<Token>> index = indexCovered(aJCas, Sentence.class, Token.class);
		 for (Sentence sentence :  select(aJCas, Sentence.class)) {
		     // get the document text  
		     List<Token> tokens = new ArrayList<Token>(index.get(sentence));

		     tagger.clear();

		 	
		     for (Token token : tokens)  {
		         
		         // Here er exract features need by the CRFPP classifier in order to be run 
		         // Should have strings in same format of ones u use in the train.icab and test.icab files
		         // e.g.:
		         // Oggi oggi O Og Ogg Oggi i gi ggi Oggi B IU word O O
		         // incontro incontro i in inc inco o ro tro ntro SS l word O O
		         
		       // tagger.add(token.getCoveredText() + " " + token.getPos().getPosValue() + " " + prefix + " " );
		     }
		 
		     if (!tagger.parse()) return;
	 
		     // Get the parsed named entities 
		     String[] tags = new String[(int) tagger.ysize()];
		     for (int i= 0; i < tagger.ysize(); ++i) { 
		         String tag = tagger.yname(i);
		         tags[i] = tag;
		         
		     }

		     // Create a new NamedEntity annotation for each tag annotation output by CRF++ classifier
		     // A NamedEntity annotation may san several tokens...
		      
		     String neType;
		     for (int i = 0; i < tags.length; i++) { 
		        //if neType is 'O' create a O annotation for this single work
		        neType = tags[i];
		        if (neType.equals('O')) {
		        	//Fixme
		            NamedEntity ne;
					try {
						ne = (NamedEntity) aJCas.getCas().createAnnotation(aJCas.getRequiredType(neType), tokens.get(i).getBegin(), tokens.get(i).getEnd());
			            ne.addToIndexes();
					} catch (CASException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
		        } 
		          
		        else if (tags[i].startsWith("B-")) {
		            // if neType is 'B-' loop till you reach the end of the annotation, e.g. B-PER, I-PER, I-PER, O
		            neType = tags[i].substring(2);
		            int begin = tokens.get(i).getBegin();
		            int end = tokens.get(i).getEnd();
		            String neType1 = tags[i++];
		            while (tags[i].equals("I-")) { end = tokens.get(i).getEnd(); i++; }
	  
		            NamedEntity ne = null;
		            ne.addToIndexes();
					try {
						ne = (NamedEntity) aJCas.getCas().createAnnotation(aJCas.getRequiredType(neType), begin, end);
					} catch (CASException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		        }
		     }
		 
		 }
	   }

}
