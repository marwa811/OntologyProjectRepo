package evaluations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.ibm.icu.math.BigDecimal;

public class OAEIMappingInfo  {
	private ArrayList<String> mappings; 
	private ArrayList<ArrayList<String>> MinimcalConflictSet; 
	OntModel ontology = ModelFactory.createOntologyModel();
	
	public OAEIMappingInfo(String MappingPath) throws IOException 
	{
		if(MappingPath.contains(".rdf"))
		{
			mappings=getReference(MappingPath);
		}
		else
		{
			BufferedReader Alignment = new BufferedReader(new FileReader(new File(MappingPath)));
			mappings = new ArrayList<String>();
			String lineTxt = null;
			while ((lineTxt = Alignment.readLine()) != null) {
				String line = lineTxt.trim(); 
				mappings.add(line);
			}
			Alignment.close();
		}
		if(mappings.size()==0)
		{
			mappings=getReference2(MappingPath);
		}
		
	}
	
	
	public ArrayList<String> getMappings()
	{
		return mappings;
	}
	
	public ArrayList<String> getReference(String alignmentFile) throws IOException
	{
		  ArrayList<String> references=new ArrayList<String>();  
		    Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open( alignmentFile );
	        if (in == null) {
	        	System.out.println("File: " + alignmentFile + " not found!");
	            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
	        }
	        model.read(in,"");
		    Property alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1");
			Property alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2");//alignment
			Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentmeasure");
			Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentrelation");

	        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	 
		    Resource temp;
			while(resstmt.hasNext()){
				temp = resstmt.next();
				String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();
				String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();
				String entity1URI = temp.getPropertyResourceValue(alignmententity1).getURI();
				String entity2URI = temp.getPropertyResourceValue(alignmententity2).getURI();

				String Confidence="1";
				if(temp.getProperty(value)!=null)
				 Confidence=temp.getProperty(value).getObject().asLiteral().getString();
				String Relation="=";
				if(temp.getProperty(relation)!=null)
				  Relation=temp.getProperty(relation).getObject().toString();
				
			BigDecimal   b   =   new   BigDecimal(Double.parseDouble(Confidence));  
				Double confidence =   b.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();  
							
				if(Relation.equals("&gt;"))
					continue;
				//references.add(entity1+","+entity2+","+Relation);
				references.add(entity1+","+entity2+","+entity1URI+","+entity2URI);
			}
			in.close();
			return references;
	}
	
	public ArrayList<String> getReference2(String alignmentFile) throws IOException
	{
		  ArrayList<String> references=new ArrayList<String>();  
		    Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open( alignmentFile );
	        if (in == null) {
	        	System.out.println("File: " + alignmentFile + " not found!");
	            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
	        }
	        model.read(in,"");
			
			Property alignmententity1, alignmententity2;
			alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1");
			alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2");
			Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#measure");
			Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#relation");
			
	        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	
		    Resource temp;
			while(resstmt.hasNext()){
				temp = resstmt.next();
				String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();
				String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();
				String entity1URI = temp.getPropertyResourceValue(alignmententity1).getURI();
				String entity2URI = temp.getPropertyResourceValue(alignmententity2).getURI();

				String Confidence="1";
				if(temp.getProperty(value)!=null)
					 Confidence=temp.getProperty(value).getObject().asLiteral().getString();
					String Relation="=";
				if(temp.getProperty(relation)!=null)
					  Relation=temp.getProperty(relation).getObject().toString();
				
				BigDecimal   b   =   new   BigDecimal(Double.parseDouble(Confidence)); 
				Double confidence =   b.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();  
				
				if(Relation.equals("&gt;"))
					continue;
					//references.add(entity1+","+entity2+","+Relation);
					references.add(entity1+","+entity2+","+entity1URI+","+entity2URI);
			}
			in.close();
			return references;
	}
}
