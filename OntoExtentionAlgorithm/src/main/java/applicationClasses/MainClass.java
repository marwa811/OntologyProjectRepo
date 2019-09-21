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
import ontologyHandlerClasses.LoadOntology;
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

		// Ask the user to enter the name of the source (core) ontology, then load it
		System.out.println("Please enter the name of the core ontology:");
		// Scanner sc=new Scanner(System.in);
		// String filename1=sc.next();
		String filename1 = "conference/cmt.owl";
		OWLOntology sourceOntology = LoadOntology.laodOntologyUsingFileName(filename1);

		// Ask the user to enter the name of the target ontology(to be used in extending
		// the core ontology)
		// Then it loads it
		System.out.println("Please enter the name of the ontology to be used in the extension:");
		// String filename2=sc.next();
		String filename2 = "conference/Conference.owl";
		OWLOntology targetOntology = LoadOntology.laodOntologyUsingFileName(filename2);
		
		/**
		 * Call the AML MyOntologyMatchingAlgorithm To get pairs of matched classes from the source and
		 * target ontologies Result returns in list of mappings (classes or object
		 * properties) using aml.getAlignment() AML Mapping include: <sourceURI TargetURI sim_score>
		 */

		String sourcePath = filename1;
		String targetPath = filename2;
		AMLMappings mappings=new AMLMappings();
		mappings.setMappings(sourcePath, targetPath);
		
		OWLDataFactory sourceFactory = sourceOntology.getOWLOntologyManager().getOWLDataFactory();
		OWLDataFactory targetFactory = targetOntology.getOWLOntologyManager().getOWLDataFactory();

		// for each AML Mapping object get the OWLClass of the source an target
		// ontologies (to be used in the algorithm)
		// convert the textual IRI to OWLClass
		log.info("The algorithm begins...");
		//int i=0;
		if(mappings.getSizeOfMappings() > 0) {		
			Iterator<AMLMapping> mappingIterator = mappings.getMappings().iterator();
			while (mappingIterator.hasNext()) {
				AMLMapping mapping = mappingIterator.next();
				OWLClass sourceClass = sourceFactory.getOWLClass(IRI.create(mapping.getSourceURI()));
				OWLClass targetClass = targetFactory.getOWLClass(IRI.create(mapping.getTargetURI()));
				System.out.println(mapping.getMappingId() + " " + mapping.getSourceURI() + "     " + mapping.getTargetURI());
				// test if the two classes have common "semantic information"
				try {
					OntologyMatchingAlgorithm.getSimilarclasses(sourceOntology, sourceClass, targetOntology, targetClass, mappings.getMappings());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println();
			}
			
			// the function returns the set of classes (in the source ontology) that can be
			// extended using the target ontology
			OntologyMatchingAlgorithm.printSemanticSimilarClasses(sourceOntology);
		} else
			System.out.println("There are no mappings!!");
	}
}
