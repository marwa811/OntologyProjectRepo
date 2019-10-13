package evaluations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ontologyHandlerClasses.AMLMapping;

public class FromOAEItoAMLMapping {
	
	private ArrayList<AMLMapping> mappings=new ArrayList<AMLMapping>();
	
	public ArrayList<AMLMapping> getAMLmappingsfromOAEI(String amlResultPath) throws IOException {
		//Read AML Mappings from OAEI results file
		ArrayList<AMLMapping> mappings=new ArrayList<AMLMapping>();
		
		OAEIMappingInfo amlResults=new OAEIMappingInfo(amlResultPath);
		ArrayList<String> amlResultsMappings=amlResults.getMappings();
		
		//copy aml mapping into a list of AMLMappings to run into the algorithm
		Iterator<String> listIterator1 = amlResultsMappings.iterator();
		
		while (listIterator1.hasNext()) {
			AMLMapping m=new AMLMapping();
			String map=listIterator1.next();
			String mParts[]=map.split(",");
			for (String temp : amlResultsMappings) 
			{
				m.setSourceName(mParts[0]);
				m.setTargetName(mParts[1]);
				m.setSourceURI(mParts[2]);
				m.setTargetURI(mParts[3]);
				m.setSimilarityScore("0.0");
			}
			mappings.add(m);
		}
		
		System.out.println("------------------");
		return mappings; 
		}
	
	public ArrayList<AMLMapping> getMappings() {
		return mappings;
		}
	}
