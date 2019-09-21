package ontologyHandlerClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLException;

import aml.AML;

public class AMLMappings {
	
	private List<AMLMapping> mappings=new ArrayList<AMLMapping>();
	
	public void setMappings(String sourceFileName, String targetFileName) throws OWLException {
		final Logger log = Logger.getLogger(AMLMappings.class);
		
		AML aml = AML.getInstance();
		log.info("calling AML MyOntologyMatchingAlgorithm...");

		aml.openOntologies(sourceFileName, targetFileName);
		log.info("AML Opened the ontologies...");

		aml.matchAuto();
		log.info("AML made the matching step between the two ontologies...");
		
		//If there exist AML mappings but them in AMLMappings object (arraylist)
		if(aml.getAlignment().size() > 0) { 
			int id=1;
			System.out.println("AML mappings are: "+ aml.getAlignment().size());
			for(int i=0; i<aml.getAlignment().size(); i++) {
				String[] mappingitems=aml.getAlignment().get(i).toString().split("\t");
				mappings.add(new AMLMapping(id,mappingitems[0].trim(),mappingitems[1].trim(),
						mappingitems[2].trim(),mappingitems[3].trim(),
						Double.parseDouble(mappingitems[4].trim())));
				id++;
				} 
			  } else
		  System.out .println("There are no AML mappings!!"); 
		}
	
	public List<AMLMapping> getMappings() {
		return mappings;
	}
	
	public AMLMapping getMappingAtIndex(int i)
	{
		return mappings.get(i);
	}
	
	public void displayMappings() {
		//mappings=getMappings();
		Iterator<AMLMapping> mappingIterator = mappings.iterator();
			while (mappingIterator.hasNext()) {
				AMLMapping mapping = mappingIterator.next();			
				System.out.println(mapping.getMappingId()+"  "+mapping.getSourceName()+"  "+
					mapping.getSourceURI()+"  "+mapping.getTargetName()+"  "+
					mapping.getTargetURI()+"  "+mapping.getSimilarityScore());
		}
		/*for(int i=0; i<mappings.size(); i++) {
			System.out.println(mappings.get(i).getMappingId()+"  "+mappings.get(i).getSourceName()+"  "+
							mappings.get(i).getSourceURI()+"  "+mappings.get(i).getTargetName()+"  "+
							mappings.get(i).getTargetURI()+"  "+mappings.get(i).getSimilarityScore());
		*/
		}	
	
	public int getSizeOfMappings() {
		return mappings.size();
	}
}
