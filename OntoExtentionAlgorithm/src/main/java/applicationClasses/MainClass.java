package applicationClasses;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import ontologyHandlerClasses.LoadOntology;
import ontologyHandlerClasses.AMLMapping;
import ontologyHandlerClasses.AMLMappings;
import ontologyHandlerClasses.ExtractOntologyInformation;
import ontologyHandlerClasses.OntologyMatchingAlgorithm;
import aml.AML;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;


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
		String filename1 = "oboe-core.owl";
		OWLOntology sourceOntology = LoadOntology.laodOntologyUsingFileName(filename1);

		// Ask the user to enter the name of the target ontology(to be used in extending
		// the core ontology)
		// Then it loads it
		System.out.println("Please enter the name of the ontology to be used in the extension:");
		// String filename2=sc.next();
		String filename2 = "d1-ECSO.owl";
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
		//mappings.displayMappings();
		/*AML aml = AML.getInstance();
		log.info("calling AML MyOntologyMatchingAlgorithm...");

		aml.openOntologies(sourcePath, targetPath);
		log.info("AML Opened the ontologies...");

		aml.matchAuto();
		log.info("AML made the matching step between the two ontologies...");

		
		  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Mappings.txt"), "utf-8")); 
		  if(aml.getAlignment().size() > 0) { 
			  for(int i=0; i<aml.getAlignment().size(); i++) {
				  writer.write(aml.getAlignment().get(i).toString()+'\n'); 
				  } 
			  } else
		  System.out.println("There are no mappings!!"); 
		  writer.close();
		 */

		OWLDataFactory sourceFactory = sourceOntology.getOWLOntologyManager().getOWLDataFactory();
		OWLDataFactory targetFactory = targetOntology.getOWLOntologyManager().getOWLDataFactory();

		// for each AML Mapping object get the OWLClass of the source an target
		// ontologies (to be used in the algorithm)
		// convert the textual IRI to OWLClass
		log.info("The algorithm begins...");
		
		if (mappings.getSizeOfMappings() > 0) {
			for (int i = 0; i < mappings.getSizeOfMappings(); i++) {
				OWLClass sourceClass = sourceFactory.getOWLClass(IRI.create(mappings.getMappings().get(i).getSourceURI()));
				OWLClass targetClass = targetFactory.getOWLClass(IRI.create(mappings.getMappings().get(i).getTargetURI()));
				System.out.println(i + " " + sourceClass.toString() + "     " + targetClass.toString());

				// test if the two classes have common "semantic information"
				OntologyMatchingAlgorithm.getSimilarclasses(sourceOntology, sourceClass, targetOntology, targetClass, mappings);
				System.out.println();
			}
			// the function returns the set of classes (in the source ontology) that can be
			// extended using the target ontology
			OntologyMatchingAlgorithm.printSemanticSimilarClasses(sourceOntology);
		} else
			System.out.println("There are no mappings!!");
	}
}
