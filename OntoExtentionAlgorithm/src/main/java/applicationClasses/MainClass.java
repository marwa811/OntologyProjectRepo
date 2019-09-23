package applicationClasses;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import evaluations.Evaluations;
import evaluations.MappingInfo;
import ontologyHandlerClasses.AMLMapping;
import ontologyHandlerClasses.AMLMappings;
import ontologyHandlerClasses.OWLOntologyInformation;
import ontologyHandlerClasses.OntologyMatchingAlgorithm;


public class MainClass {

	final static Logger log = Logger.getLogger(MainClass.class);

	/**
	 * The main method begins execution of the application.
	 *
	 * @param args not used
	 */

	public static void main(String[] args) throws Exception {

		log.info("Your application is strating:");
		
		String filename1 = "conference/cmt.owl";
		String filename2 = "conference/conference.owl";
		String refPath="referenceAlignment/cmt-conference.rdf";
		
		//String filename1 = "biodiv/envo.owl";
		//String filename2 = "biodiv/sweet.owl";
		
		OWLOntologyInformation sourceOntology=new OWLOntologyInformation();
		OWLOntologyInformation targetOntology=new OWLOntologyInformation();
		
		sourceOntology.laodOntology(filename1);
		targetOntology.laodOntology(filename2);
		
		/**
		 * Call the AML MyOntologyMatchingAlgorithm To get pairs of matched classes from the source and
		 * target ontologies Result returns in list of mappings (classes or object
		 * properties) using aml.getAlignment() AML Mapping include: <sourceURI TargetURI sim_score>
		 */
		AMLMappings mappings=new AMLMappings();
		mappings.setMappings(filename1, filename2);
		

		// for each AML Mapping object get the OWLClass of the source an target
		// ontologies (to be used in the algorithm)
		// convert the textual IRI to OWLClass
		
		log.info("The algorithm begins...");
		OntologyMatchingAlgorithm testCase= new OntologyMatchingAlgorithm();
		
		//For each Mapping check to include it or not in our final mappings
		if(mappings.getSizeOfMappings() > 0) {		
			Iterator<AMLMapping> mappingIterator = mappings.getMappings().iterator();
			while (mappingIterator.hasNext()) {
				AMLMapping mapping = mappingIterator.next();
				System.out.println(mapping.getMappingId() + " " + mapping.getSourceURI() + "     " + mapping.getTargetURI());
				testCase.getSimilarclasses(sourceOntology,targetOntology, mapping, mappings.getMappings());
				System.out.println();
			}
			testCase.displayFinalMappings();
		} 
		else
			System.out.println("There are no mappings!!");
		
		System.out.println("------------------");
		
		MappingInfo references=new MappingInfo(refPath);
		ArrayList<String> refMappings=references.getMappings();
		Iterator<String> listIterator = refMappings.iterator();
		while (listIterator.hasNext()) {
			String ref = listIterator.next();
			System.out.println(ref);
		}
		
		System.out.println("------------------");
		
		ArrayList<String> myMappings=testCase.getFinalMappings().getMappingsAsRef();
		Iterator<String> listIterator1 = myMappings.iterator();
		while (listIterator1.hasNext()) {
			String map = listIterator1.next();
			System.out.println(map);
		}
		
		System.out.println("------------------");
		
		Evaluations evaluation=new Evaluations(myMappings,refMappings);
		System.out.println("Precision= "+evaluation.getPrecision());
		System.out.println("Recall= "+evaluation.getRecall());
		System.out.println("FMeasure= "+evaluation.getFMeasure());

	}
}
