package applicationClasses;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import ontologyHandlerClasses.AMLMapping;
import ontologyHandlerClasses.AMLMappings;
import ontologyHandlerClasses.ExtractOntologyEntity;
import ontologyHandlerClasses.OntologyMatchingAlgorithm;
import bioPortalTools.TermSearch;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLException;

public class MainApplication {
	
	final static Logger log = Logger.getLogger(MainApplication.class);
	private static Scanner sc;
	private static int mb = 1024 * 1024; 
	 
	// get Runtime instance
	private static Runtime instance;
	
	public static void main(String[] args) throws Exception {
		
	String inputFileName="";
	
	sc = new Scanner(System.in);
	
	log.info("Your application is strating:");
	
	System.out.println("Please enter the name of your input ontology: "); 
	String name= sc.nextLine();    //read input     
    inputFileName="OWLOntologies/"+name+".owl";
    
    //test the existance of such an owl file in the directory
    //if not found this may not be an OWL file 
    File file = new File(inputFileName);
    while(! file.isFile()) {
    	System.out.println("There is no ontology with this name, maybe not in OWL format");
    	System.out.println("Please enter the name of your input ontology: ");
    	name= sc.nextLine();    //read input     
        inputFileName="OWLOntologies/"+name+".owl";
        file = new File(inputFileName);
    }
    log.info("Your file is:"+ inputFileName);
    
    //load the input ontology and retrieve its class in order to beging the reuse process 
    ExtractOntologyEntity.getClassesLabelsFromInputOntology(inputFileName);
    
	/*Input a class (from the input ontology) to begin the reuse proess and begin 
	 * creating a user profile
	 */
    System.out.println("Please Select a Class to begin the ontology Reuse Process:");
	String className= sc.nextLine();
	System.out.println("You selected: "+className+ " class");
	System.out.println("Loading candidate ontologies...");
	
	/*search the bioportal repository for a match to the selected class,
	 * if found display the candidate ontologies to the user and begin the user profile
	 * create the ontology Level preferences 
	 */
	try {
	TermSearch.printOntologyNames(TermSearch.searchByTermBioportal(className));
	}catch (NullPointerException e) {
		System.out.println("No ontologies found to match this class.");
		return; //end the function here
	}
	
	System.out.println("Please Select the ontology you want to use in the Reuse Process: (if more than one use ',' to seprate)");
	String ontologies= sc.nextLine();
	ArrayList<String> ontologyFilesNames= getOntologyFileNames(ontologies);
	for(int i=0; i<ontologyFilesNames.size(); i++)
		getMapping(inputFileName, "OWLOntologies/"+ontologyFilesNames.get(i));
	}// end main
	
	//-------------------------------------------------------------------------------
	
	//The function takes an input string from the user, split it by ',' then uppercase 
	//the file name and add .owl extension and return a list of files names 
	public static ArrayList<String> getOntologyFileNames(String ontologies){
		ArrayList<String> ontologyList=new ArrayList<String>();
		if(ontologies.contains(",")) {
			String[] result = ontologies.split(",");
			  for (int x=0; x<result.length; x++) {
			      ontologyList.add(result[x].trim().toUpperCase()+".owl");
			      System.out.println("You Choose Ontology:  " +ontologyList.get(x));
			    }
		}
		else {
			ontologyList.add(ontologies+".owl");		
			System.out.println("You selected: "+ontologies.trim().toUpperCase()+ " ontology");
			}
		return ontologyList;
	}
	//------------------------------------------------------------------
	public static void getMapping(String filename1, String filename2) throws OWLException, IOException {
		AMLMappings mappings=new AMLMappings();
		mappings.setMappings(filename1, filename2);
		
		// used memory
		instance = Runtime.getRuntime();
		System.out.println("Used Memory: "+ (instance.totalMemory() - instance.freeMemory()) / mb);
				
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
				System.out.println("The AML mappings:   "+mapping.getMappingId() + " " + mapping.getSourceURI() + "     " + mapping.getTargetURI());
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
		System.out.println("-----------------------------------------");
	}
	
}