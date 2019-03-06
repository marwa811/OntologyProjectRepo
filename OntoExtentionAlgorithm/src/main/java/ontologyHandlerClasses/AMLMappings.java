package ontologyHandlerClasses;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLException;

import aml.AML;

public class AMLMappings {
	
	private Vector<AMLMapping> mappings=new Vector<AMLMapping>();

	public void setMappings(String sourceFileName, String targetFileName) throws OWLException {
		final Logger log = Logger.getLogger(AMLMappings.class);
		
		AML aml = AML.getInstance();
		log.info("calling AML MyOntologyMatchingAlgorithm...");

		aml.openOntologies(sourceFileName, targetFileName);
		log.info("AML Opened the ontologies...");

		aml.matchAuto();
		log.info("AML made the matching step between the two ontologies...");
		
		if(aml.getAlignment().size() > 0) { 
			for(int i=0; i<aml.getAlignment().size(); i++) {
				AMLMapping mapping=new AMLMapping(); 
				mapping.setMappingId(i+1);
				String[] mappingitems=aml.getAlignment().get(i).toString().split("\t");
				mapping.setSourceURI(mappingitems[0].trim());
				mapping.setSourceName(mappingitems[1].trim());
				mapping.setTargetURI(mappingitems[2].trim());
				mapping.setTargetName(mappingitems[3].trim()); 
				mapping.setSimilarityScore(mappingitems[4].trim());
				mappings.add(mapping);
				} 
			  } else
		  System.out.println("There are no mappings!!"); 
		}
	
	public Vector<AMLMapping> getMappings() {
		return mappings;
	}
	
	public AMLMapping getMappingAtIndex(int i)
	{
		return mappings.get(i);
	}
	
	public void displayMappings() {
		mappings=getMappings();
		for(int i=0; i<mappings.size(); i++) {
			System.out.println(mappings.get(i).getMappingId()+"  "+mappings.get(i).getSourceName()+"  "+
							mappings.get(i).getSourceURI()+"  "+mappings.get(i).getTargetName()+"  "+
							mappings.get(i).getTargetURI()+"  "+mappings.get(i).getSimilarityScore());
		}
			
	}
	
	public int getSizeOfMappings() {
		return mappings.size();
	}
}
