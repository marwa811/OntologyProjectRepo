package ontologyHandlerClasses;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.apache.log4j.Logger;

/**
 * OntologyMatchingAlgorithm.java Purpose: Make additional semantic matching for a
 * pair of classes
 * 
 * @author marwa
 * @version 0.1 7/12/2018
 */

public class OntologyMatchingAlgorithm {
	final static Logger log = Logger.getLogger(OntologyMatchingAlgorithm.class);

	// set for final source classes that have a match with target classes
	public static Set<OWLClass> semanticSimilarClasses = new HashSet<>();

	public Set<OWLClass> getSemanticSimilarClasses() {
		return semanticSimilarClasses;
	}

	public void setSemanticSimilarClasses(Set<OWLClass> semanticRelatedClasses) {
		this.semanticSimilarClasses = semanticRelatedClasses;
	}

	public static void printSemanticSimilarClasses(OWLOntology ontology) throws IOException {
		System.out.println("Classes that can be extended are:");
		for (OWLClass c : semanticSimilarClasses) {
			System.out.println(ExtractOntologyInformation.getClassLabel(ontology, c));
		}
	}

	/**
	 * A method to test all the conditions of the algorithm to ensure semantic
	 * matching step for a pair of classes. if the pair has a match, add the source
	 * class to "semanticSimilarClasses" Set
	 * @param mappings 
	 * 
	 * @param The source ontology, source class , target ontology, target class.
	 */

	public static void getSimilarclasses(OWLOntology sourceOntolog, OWLClass sourceClass, OWLOntology targetOntolog,
			OWLClass targetClass, AMLMappings mappings) throws IOException {

		// Condition 1: if the two classes have equivalent classes with mappings, then they can
		// be semantically the same
		if (getEquivalentClassesWithMappings(sourceOntolog, sourceClass, targetOntolog, targetClass, mappings)!=null) {
			if (semanticSimilarClasses.size() == 0) {
				semanticSimilarClasses.add(sourceClass);
				System.out.println(sourceClass.getIRI().getFragment() + " added for equivalent classes with mappings");
			} else if (!existin(sourceClass, semanticSimilarClasses)) {
				semanticSimilarClasses.add(sourceClass);
				System.out.println(sourceClass.getIRI().getFragment() + " added for common equivalent classes with mappings");
			} else
				System.out.println(sourceClass.getIRI().getFragment() + " added for common equivalent classes with mappings");
		}

		// Condition 2: if two classes has subclasses with mappings, then they can be
		// semantically the same
		if (getSubClassesWithMappings(sourceOntolog, sourceClass, targetOntolog, targetClass, mappings)!=null) {
			if (semanticSimilarClasses.size() == 0) {
				semanticSimilarClasses.add(sourceClass);
				System.out.println(sourceClass.getIRI().getFragment() + " added for common sub-classes");
			} else if (!existin(sourceClass, semanticSimilarClasses)) {
				semanticSimilarClasses.add(sourceClass);
				System.out.println(sourceClass.getIRI().getFragment() + " added for common sub-classes");
			} else
				System.out.println(sourceClass.getIRI().getFragment() + " added for common sub-classes");
		}
		
		// Condition 3: if two classes has direct superclass with mappings, then they can be
		// semantically the same
		if (getParentClassWithMappings(sourceOntolog, sourceClass, targetOntolog, targetClass, mappings)!=null) {
			if (semanticSimilarClasses.size() == 0) {
				semanticSimilarClasses.add(sourceClass);
				System.out.println(sourceClass.getIRI().getFragment() + " added for common direct parent");
			} else if (!existin(sourceClass, semanticSimilarClasses)) {
				semanticSimilarClasses.add(sourceClass);
				System.out.println(sourceClass.getIRI().getFragment() + " added for common direct parent");
			} else
				System.out.println(sourceClass.getIRI().getFragment() + " added for common direct parent");
			}

		// Condition 4: if two classes has common object properties && has domain/range classes that have mappings 
		// then they are semantically the same.
		if (getCommonObjectProperties(sourceOntolog, sourceClass, targetOntolog, targetClass, mappings)!=null) {
			if (semanticSimilarClasses.size() == 0) {
				semanticSimilarClasses.add(sourceClass);
					System.out.println(sourceClass.getIRI().getFragment() + " added for common object properties");
				} else if (!existin(sourceClass, semanticSimilarClasses)) {
					semanticSimilarClasses.add(sourceClass);
					System.out.println(sourceClass.getIRI().getFragment() + " added for common object properties");
				} else
					System.out.println(sourceClass.getIRI().getFragment() + " added for common object properties");
			}
		}

	/**
	 * A method to get equivalent classes that has a mapping between source class and target
	 * class. For example, if ClassA is equivalent to ClassC and ClassB is
	 * equivalent to ClassC' so, ClassA and ClassB are equivalent iff classC and classC' has a mappings.
	 * This function test if classC and classC' has a mapping
	 * 
	 * @param The source ontology, source class , target ontology, target class, and list of mappings.
	 * @return The source class if there is a mapping between equivalent classes, else return null.
	 */
	public static OWLClass getEquivalentClassesWithMappings(OWLOntology sourceOntolog, OWLClass sourceClass,
			OWLOntology targetOntolog, OWLClass targetClass, AMLMappings mappings ) throws IOException {

		Set<OWLClass> equivalentClassesWithMappings = new HashSet<OWLClass>();
		Set<OWLClass> sourceClassEquavilantClasses= ExtractOntologyInformation.getEquavilantClasses(sourceOntolog, sourceClass);
		Set<OWLClass> targetClassEquavilantClasses= ExtractOntologyInformation.getEquavilantClasses(targetOntolog, targetClass);
		/*System.out.println("source equa classes");
		printSetofClasses(sourceClassEquavilantClasses,sourceOntolog);
		System.out.println("target equa classes");
		printSetofClasses(targetClassEquavilantClasses,targetOntolog);*/
		for (OWLClass c1 : sourceClassEquavilantClasses) {
			for (OWLClass c2 : targetClassEquavilantClasses) {
				if (testMappingExistence(c1, c2, mappings))
				{
						equivalentClassesWithMappings.add(c1);
						//System.out.println(c1.toString()+" added");
				}
			}
		}
		if(equivalentClassesWithMappings.size()> 0)
			return sourceClass;
		else return null;
	}

	/**
	 * A method to get sub-classes that has mappings between source class and target class.
	 * get subclasses of source class
	 * get subclasses of target class
	 * test if there is a match between the two sets of subclasses.
	 *
	 * @param The source ontology, source class , target ontology, target class, and list of mappings.
	 * @return The source class that has mappings between the input classes.
	 */
	public static OWLClass getSubClassesWithMappings(OWLOntology sourceOntolog, OWLClass sourceClass,
			OWLOntology targetOntolog, OWLClass targetClass, AMLMappings mappings) throws IOException {

		Set<OWLClass> subClassesWithMappings = new HashSet<OWLClass>();
		Set<OWLClass> sourceClassSubClasses= ExtractOntologyInformation.getSubClasses(sourceOntolog, sourceClass);
		Set<OWLClass> targetClassSubClasses= ExtractOntologyInformation.getSubClasses(targetOntolog, targetClass);
		
		for (OWLClass c1 : sourceClassSubClasses) {
			for (OWLClass c2 : targetClassSubClasses) {
				if (testMappingExistence(c1, c2, mappings))
					subClassesWithMappings.add(c1);
			}
		}
		if(subClassesWithMappings.size()> 0)
			return sourceClass;
		else return null;
	}
	
	/**
	 * A method to get (direct) parent class that has mapping between source class and target class, if exists.
	 * get superclass of source class
	 * get superclass of target class
	 * test if there is a match between the two superclasses.
	 * 
	 * @param The source ontology, source class , target ontology, target class, and list of mappings.
	 * @return The source class that have common (direct) parent with mappings with the target superclasses.
	 */
	
	public static OWLClass getParentClassWithMappings(OWLOntology sourceOntolog, OWLClass sourceClass,
			OWLOntology targetOntolog, OWLClass targetClass, AMLMappings mappings) throws IOException {

		Set<OWLClass> ParentClassWithMappings = new HashSet<OWLClass>();
		Set<OWLClass> sourceClassDirectSuperClass= ExtractOntologyInformation.getDirectSuperClass(sourceOntolog, sourceClass);
		Set<OWLClass> targetClassDirectSuperClass= ExtractOntologyInformation.getDirectSuperClass(targetOntolog, targetClass);
		
		for (OWLClass c1 : sourceClassDirectSuperClass) {
			for (OWLClass c2 : targetClassDirectSuperClass) {	
			// if direct parent is "Thing" don't count it
			if (c1.getIRI().getFragment() == "Thing" || c2.getIRI().getFragment() == "Thing")
				break;
			if (testMappingExistence(c1, c2, mappings))
				ParentClassWithMappings.add(c1);
			}
		}
		if(ParentClassWithMappings.size()> 0)
			return sourceClass;
		else return null;
	}

	/**
	 * A method to get source class if the two input classes have common object properties,
	 * and there is a mapping between their domain/target classes
	 * 
	 * @param The source ontology, source class , target ontology, target class, and list of mappings.
	 * @return The source class
	 */
	public static OWLClass getCommonObjectProperties(OWLOntology sourceOntolog, OWLClass sourceClass,
			OWLOntology targetOntolog, OWLClass targetClass, AMLMappings mappings) throws IOException {

		Set<OWLClass> commonClasses = new HashSet<OWLClass>();
		Set<OWLObjectProperty> obs=new HashSet<OWLObjectProperty>();
		// get all object properties connected to the source class from the source ontology
		// get all object properties connected to the target class from the target ontology

		for (OWLObjectProperty obSource : ExtractOntologyInformation.getObjectProperties(sourceOntolog, sourceClass)) {
			// if there is a common object property between the two classes (lexical similarity)
			for (OWLObjectProperty obTarget : ExtractOntologyInformation.getObjectProperties(targetOntolog, targetClass)) {
				if (existin(obSource, obTarget)) {
					//if there are any common object properties between the two classes
					//check for any domain/range classes that are mapped
					for(OWLClass cS : ExtractOntologyInformation.getClassesRelatedToObjectProperty(sourceOntolog, obSource))
						for(OWLClass cT : ExtractOntologyInformation.getClassesRelatedToObjectProperty(targetOntolog, obTarget))
							if(testMappingExistence(cS, cT, mappings))
								obs.add(obSource);
				}			
			}
		}
		// if there are any common object properties between the two classes return the source class
		if (obs.size()>0)
			return sourceClass;
		else return null;
	}


	/**
	 * A method to get classes that have common data properties.
	 * 
	 * @param The source ontology, source class , target ontology, target class.
	 * @return set of source classes that have common data properties between the
	 *         target classes.
	 */
	public static Set<OWLClass> getCommonDataProperties(OWLOntology sourceOntolog, OWLClass sourceClass,
			OWLOntology targetOntolog, OWLClass targetClass) throws IOException {

		Set<OWLClass> commonClasses = new HashSet<OWLClass>();
		for (OWLDataProperty db1 : ExtractOntologyInformation.getDataProperties(sourceOntolog, sourceClass)) {
			// if there is a common object property between the two ontologies
			if (existin(db1, ExtractOntologyInformation.getDataProperties(targetOntolog, targetClass)))
				commonClasses.add(sourceClass);
		}

		for (OWLClass c : commonClasses)
			System.out.println("classes with Common data properties are:" + c.getIRI().getFragment());

		return commonClasses;
	}

	// ---------------------------------------------------------------------------------//

	/**
	 * A method to test if a mapping exists between two classes or not 
	 * by test their existence in a list of mappings between two ontologies or not
	 * 
	 * @param The source ontology, source class , target ontology, A set of classes.
	 * @return boolean if true then the class exists in the given set of classes.
	 */

	public static boolean testMappingExistence(OWLClass class1, OWLClass class2, AMLMappings mappings) throws IOException {
		boolean exist = false;
		String sourceClassURI = class1.getIRI().toString();
		String targetClassURI = class2.getIRI().toString();
				
		for (int i=0; i<mappings.getSizeOfMappings(); i++) 
		{
			AMLMapping m=new AMLMapping();
			m=mappings.getMappingAtIndex(i);
			if(m.getSourceURI().trim().equals(sourceClassURI)&&m.getTargetURI().trim().equals(targetClassURI)) {
				exist = true;
				//System.out.println(m.getSourceURI().trim()+" , "+m.getTargetURI().trim()+" has a mapping.");
			}
		}
		return exist;
	}

	/**
	 * A method to test if a class exists in a set of classes
	 * 
	 * @param OWLClass , and a set of classes.
	 * @return boolean if true then the class exists in the given set of classes.
	 */
	public static boolean existin(OWLClass class1, Set<OWLClass> setOFClasses) {
		boolean exist = false;
		for (OWLClass c1 : setOFClasses) {
			if (class1.getIRI().getFragment().equals(c1.getIRI().getFragment()))
				exist = true;
		}
		return exist;
	}

	/**
	 * A method to test if an object property exists in a set of object properties
	 * By comparing the names
	 * 
	 * @param OWLObjectProperty , and a set of OWLObjectProperties.
	 * @return boolean if true then the object property exists in the given set of
	 *         classes.
	 */
	public static boolean existin(OWLObjectProperty ob, Set<OWLObjectProperty> setOFobs) {
		boolean exist = false;
		for (OWLObjectProperty tempOb : setOFobs) {
			if (tempOb.getIRI().getFragment().toLowerCase().equals(ob.getIRI().getFragment().toLowerCase()))
				exist = true;
		}
		return exist;
	}

	/**
	 * A method to test if an object property equals another object property
	 * By comparing the names
	 * 
	 * @param TWO OWLObjectProperties.
	 * @return boolean return true if they are the same
	 */
	public static boolean existin(OWLObjectProperty ob1, OWLObjectProperty ob2) {
		boolean exist = false;
		if (ob1.getIRI().getFragment().toLowerCase().equals(ob2.getIRI().getFragment().toLowerCase()))
				exist = true;
		return exist;
	}

	/**
	 * A method to test if an data property exists in a set of data properties By
	 * comparing the names
	 * 
	 * @param OWLDataProperty , and a set of OWLDataProperties.
	 * @return boolean if true then the data property exists in the given set of
	 *         classes.
	 */
	public static boolean existin(OWLDataProperty db, Set<OWLDataProperty> setOFdbs) {
		boolean exist = false;
		for (OWLDataProperty tempDb : setOFdbs) {
			if ((tempDb.getIRI().getFragment()).toLowerCase().equals((db.getIRI().getFragment()).toLowerCase()))
				exist = true;
		}
		return exist;
	}

	/**
	 * A method to print a set of classes (used for testing)
	 * 
	 * @param OWLOntology , and a set of OWLClasses.
	 */
	public static void printSetofClasses(Set<OWLClass> classes, OWLOntology ontology) throws IOException {
		for (OWLClass c : classes)
			System.out.println(ExtractOntologyInformation.getClassLabel(ontology, c));
	}
}