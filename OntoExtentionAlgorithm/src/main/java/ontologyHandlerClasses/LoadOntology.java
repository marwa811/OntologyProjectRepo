package ontologyHandlerClasses;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.apache.log4j.Logger;

/**
 * OntologyLoading.java 
 * Purpose: Load an ontology
 * 
 * @author marwa
 * @version 0.1 7/12/2018
 */

public class LoadOntology {

	final static Logger log = Logger.getLogger(LoadOntology.class);

	/**
	 * A method to load an ontology using its file name.
	 *
	 * @param A string for the file name.
	 * @return OWLOntology object for the ontology.
	 */
	public static OWLOntology laodOntologyUsingFileName(String filename)
			throws OWLOntologyCreationException, OWLException {
		OWLOntology ontology = null;
		try {
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			File file = new File(filename);
			ontology = man.loadOntologyFromOntologyDocument(file);
			log.info("Loading " + filename + "...");
		} catch (OWLOntologyCreationException e) {
			log.error("Error in loading the ontology: " + e);
		}
		return ontology;
	}

	/**
	 * A method to get the all imported ontologies for a given OWLOntology.
	 *
	 * @param An input OWLOntology.
	 * @return A Set of imported OWLOntologies.
	 */

	public static Set<OWLOntology> getImportedOntologies(OWLOntology ontology) {
		Set<OWLOntology> allOntologies = new HashSet<OWLOntology>();
		allOntologies = ontology.getOWLOntologyManager().getOntologies();
		return allOntologies;
	}

}
