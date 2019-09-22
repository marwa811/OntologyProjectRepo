package applicationClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

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
		
		String filename1 = "biodiv/flopo.owl";
		String filename2 = "biodiv/pto.owl";
		
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
		
		if(mappings.getSizeOfMappings() > 0) {		
			Iterator<AMLMapping> mappingIterator = mappings.getMappings().iterator();
			while (mappingIterator.hasNext()) {
				AMLMapping mapping = mappingIterator.next();
				OWLClass sourceClass=sourceOntology.getOWLClassfromIRI(mapping.getSourceURI());
				OWLClass targetClass = targetOntology.getOWLClassfromIRI(mapping.getTargetURI());
				System.out.println(mapping.getMappingId() + " " + mapping.getSourceURI() + "     " + mapping.getTargetURI());
				// test if the two classes have common "semantic information"
				testCase.getSimilarclasses(sourceOntology, sourceClass, targetOntology, targetClass, mappings.getMappings());

				System.out.println();
			}
			
			// the function returns the set of classes (in the source ontology) that can be
			// extended using the target ontology
			testCase.printSemanticSimilarClasses(sourceOntology);
		} else
			System.out.println("There are no mappings!!");
	}
}
