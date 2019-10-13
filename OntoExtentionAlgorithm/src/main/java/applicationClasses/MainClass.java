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
		
		//String filename1 = "biodiv/envo.owl";
		//String filename2 = "biodiv/sweet.owl";

		String refPath="referenceAlignment/ekaw-sigkdd.rdf";
		//String amlResultPath="LogMapResults2018/LogMapLt-iasted-sigkdd.rdf";
		
		//String refPath="biodiv/envo-sweet.rdf";
		
		String filename1 = "conference/ekaw.owl";
		String filename2 = "conference/sigkdd.owl";
		
		/*OWLOntologyInformation sourceOntology=new OWLOntologyInformation();
		OWLOntologyInformation targetOntology=new OWLOntologyInformation();
		
		sourceOntology.laodOntology(filename1);
		targetOntology.laodOntology(filename2);*/
		
		/**
		 * Call the AML MyOntologyMatchingAlgorithm To get pairs of matched classes from the source and
		 * target ontologies Result returns in list of mappings (classes or object
		 * properties) using aml.getAlignment() AML Mapping include: <sourceURI TargetURI sim_score>
		 */
		int mb = 1024 * 1024; 
		 
		// get Runtime instance
		Runtime instance = Runtime.getRuntime();

		// used memory
		System.out.println("Used Memory: "
				+ (instance.totalMemory() - instance.freeMemory()) / mb);
		
		AMLMappings mappings=new AMLMappings();
		mappings.setMappings(filename1, filename2);
		
		// used memory
				System.out.println("Used Memory: "
						+ (instance.totalMemory() - instance.freeMemory()) / mb);
				
		// for each AML Mapping object get the OWLClass of the source an target
		// ontologies (to be used in the algorithm)
		// convert the textual IRI to OWLClass
		
		log.info("The algorithm begins...");
		
		OntologyMatchingAlgorithm testCase= new OntologyMatchingAlgorithm(filename1,filename2);
		System.out.println("The output is:");
		//For each Mapping check to include it or not in our final mappings
		if(mappings.getSizeOfMappings() > 0) {		
			Iterator<AMLMapping> mappingIterator = mappings.getMappings().iterator();
			while (mappingIterator.hasNext()) {
				AMLMapping mapping = mappingIterator.next();
				System.out.println(mapping.getMappingId() + " " + mapping.getSourceURI() + "     " + mapping.getTargetURI());
				testCase.getSimilarclasses(mapping, mappings.getMappings());
				// used memory
				System.out.println("Used Memory: "
						+ (instance.totalMemory() - instance.freeMemory()) / mb);	
				System.out.println();
			}
			testCase.displayFinalMappings();
		} 
		else
			System.out.println("There are no mappings!!");
		
		System.out.println("------------------");
		System.out.println("------------------");
		
		MappingInfo references=new MappingInfo(refPath);
		System.out.println("The references are:");
		ArrayList<String> refMappings=references.getMappings();
		Iterator<String> listIterator = refMappings.iterator();
		while (listIterator.hasNext()) {
			String ref = listIterator.next();
			System.out.println(ref);
		}
		
		System.out.println("------------------");
		
/*		//AML statistics////////////////////////////////////////////////
		MappingInfo amlResults=new MappingInfo(amlResultPath);
		ArrayList<String> amlResultsMappings=amlResults.getMappings();
		Iterator<String> listIterator1 = amlResultsMappings.iterator();
		while (listIterator1.hasNext()) {
			String ref = listIterator1.next();
			System.out.println(ref);
		}
		
		System.out.println("------------------"); 
		//////////////////////////////////////////////////////////////
	*/	
		ArrayList<String> myMappings=testCase.getFinalMappings().getMappingsAsRef();
		Iterator<String> listIterator2 = myMappings.iterator();
		while (listIterator2.hasNext()) {
			String map = listIterator2.next();
			System.out.println(map);
		}
		
		System.out.println("------------------");
		
		Evaluations evaluation=new Evaluations(myMappings,refMappings);
	//	Evaluations evaluation=new Evaluations(amlResultsMappings,refMappings);
		System.out.println("Precision= "+evaluation.getPrecision());
		System.out.println("Recall= "+evaluation.getRecall());
		System.out.println("FMeasure= "+evaluation.getFMeasure());
	
	}
	
	
	//For testing other systems using their OAEI results
/*	public static void main(String[] args) throws Exception {

		log.info("Your application is strating:");
		

		String refPath="referenceAlignment/iasted-sigkdd.rdf";
		String amlResultPath="LogMapResults2018/LogMapLt-iasted-sigkdd.rdf";
		
		String filename1 = "conference/iasted.owl";
		String filename2 = "conference/sigkdd.owl";
		
		OWLOntologyInformation sourceOntology=new OWLOntologyInformation();
		OWLOntologyInformation targetOntology=new OWLOntologyInformation();
		
		sourceOntology.laodOntology(filename1);
		targetOntology.laodOntology(filename2);
		
		/**
		 * get the results from OAEI results for a given ontology matcher (AML or LogMap)
		 * convert the result in AMLMapping class to run my algorithm
		 		
		ArrayList<AMLMapping> mappings= new ArrayList<AMLMapping>();
		FromOAEItoAMLMapping amlMappingsfromOAEI=new FromOAEItoAMLMapping();
		mappings= amlMappingsfromOAEI.getAMLmappingsfromOAEI(amlResultPath);

		// for each AML Mapping object get the OWLClass of the source an target
		// ontologies (to be used in the algorithm)
		// convert the textual IRI to OWLClass
		
		log.info("The algorithm begins...");
		
		OntologyMatchingAlgorithm testCase= new OntologyMatchingAlgorithm();

		if(mappings.size() > 0) {		
			Iterator<AMLMapping> mappingIterator = mappings.iterator();
			while (mappingIterator.hasNext()) {
				AMLMapping mapping = mappingIterator.next();
				System.out.println(mapping.getMappingId() + " " + mapping.getSourceURI() + "     " + mapping.getTargetURI());
				testCase.getSimilarclasses(sourceOntology,targetOntology, mapping, mappings);
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
		Iterator<String> listIterator2 = myMappings.iterator();
		while (listIterator2.hasNext()) {
			String map = listIterator2.next();
			System.out.println(map);
		}
		
		System.out.println("------------------");
		
		Evaluations evaluation=new Evaluations(myMappings,refMappings);
		System.out.println("Precision= "+evaluation.getPrecision());
		System.out.println("Recall= "+evaluation.getRecall());
		System.out.println("FMeasure= "+evaluation.getFMeasure());

	}*/
}
