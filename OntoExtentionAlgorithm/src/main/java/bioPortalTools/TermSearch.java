package bioPortalTools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TermSearch {
	
    static final String REST_URL = "http://data.bioontology.org";
    static final String API_KEY = "aa404b9e-c096-4f22-84b8-ef9f105e0931";
    static final ObjectMapper mapper = new ObjectMapper();
    static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    /*This method calls BioPortal Rest API, As input it takes a string for the class name 
     (search query) and returns an object of type TermSearchResultInformation 
     Which contains the search result*/
    public static ArrayList<TermSearchResultInformation> searchByTermBioportal(String classLabel) throws Exception {
    	
    	//An array list of type TermSearchResultInformation for the results 
        ArrayList<TermSearchResultInformation> searchResults = new ArrayList<TermSearchResultInformation>();
        JsonNode results = jsonToNode(get(REST_URL + "/search?q=" + classLabel)).get("collection");
        if(results.size()==0) {
        	//System.out.println("No ontologies found to match this class.");
        	return null;
        }
        else { 
        /*iterate over each JsonNode in the result which contains information about: 
         * each class such as, id , label, definitions, and synonyms
         * and the ontology it belongs to such as, its id, name, and acronym
         */
        for (JsonNode result : results) {
        	//if the result item is not obsolete
        	if(result.get("obsolete").asText()=="false"){   
    	
            TermSearchResultInformation searchResultItem=new TermSearchResultInformation();	 
            ArrayList<String> ontologyDomains=new ArrayList<String>();
            searchResultItem.setOntologyId(result.get("links").get("ontology").asText());
            searchResultItem.setOntologyName(getName(result.get("links").get("ontology").asText()));
            searchResultItem.setAcronym(result.get("links").get("ontology").asText());
            ontologyDomains=getOntologyDomain(result.get("links").get("ontology").asText());
            if(ontologyDomains.size()>0)
            {
            	for(int i=0; i<ontologyDomains.size(); i++)
            		searchResultItem.setOntologyDomains(ontologyDomains.get(i));
            }
            searchResultItem.setClassId(result.get("@id").asText());        	            	
            if(result.has("prefLabel"))
            	searchResultItem.setPrefLabel(result.get("prefLabel").asText());
            if(result.has("definition"))
            {
            	Iterator<JsonNode> definitions = result.get("definition").elements();
                 while (definitions.hasNext()) 
                 { 
                    JsonNode fieldName = definitions.next();
                    searchResultItem.setClassDefination(fieldName.asText());
                 }
            }
            if(result.has("synonym"))
            {
            	Iterator<JsonNode> synonyms = result.get("synonym").elements();
                while (synonyms.hasNext()) 
                { 
                    JsonNode fieldName = synonyms.next();
                    searchResultItem.setSynonyms(fieldName.asText());
                }
             } 
            searchResults.add(searchResultItem);
           } 
        }       
        return checkForDuplicateOntologies(searchResults);
     }
    }
    
    //to avoid adding two ontologies twice in the returned list
    private static ArrayList<TermSearchResultInformation> checkForDuplicateOntologies
    (ArrayList<TermSearchResultInformation> searchResults){
		
    	List<String> ontologyAcronyms= new ArrayList<String>();
		ArrayList<TermSearchResultInformation> searchResultsNoDuplicate=new ArrayList<TermSearchResultInformation>();
		  for(TermSearchResultInformation temp : searchResults) {
			  if(!ontologyAcronyms.contains(temp.getAcronym())) {
				  ontologyAcronyms.add(temp.getAcronym());
				  searchResultsNoDuplicate.add(temp);
			  }		  
			  else continue;
	      }	
	return searchResultsNoDuplicate;		
    }
    //------------------------------------------------------------
	
    //to populate the OntologyLevel class with information about the ontology given its ontologyID
    public static ArrayList<String> getOntologyDomain(String ontologyID) {
    	String acronym = ontologyID.substring(ontologyID.lastIndexOf('/')+1);
    	JsonNode categories = jsonToNode(get(REST_URL + "/ontologies/" +acronym+"/categories"));
    	ArrayList<String> categoryList = null;
        if(categories.size()!=0) {
        	categoryList = new ArrayList<String>();
            for(JsonNode category : categories) {
            	if (!category.get("name").isNull()) {
            		categoryList.add(category.get("name").asText() +"\n"); 
            	}
            } 	  
        }
        else 
        	System.out.println("No categories for this ontology"); 
        return categoryList;
	}
	//-------------------------------------------------------------------
    
    private static JsonNode jsonToNode(String json) {
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
    
    // To get the Acronym of an ontology link
    private static String getAcronym(String ontologyLink) {
		String acronym = ontologyLink.substring(ontologyLink.lastIndexOf('/')+1);
    	return acronym;
	}
    
    public static String getName(String OntoId) {
    	String name = null;
		String resourcesString = get(REST_URL + "/ontologies"+"/"+getAcronym(OntoId));
        JsonNode ontology = jsonToNode(resourcesString);  
        return name= ontology.get("name").asText();
	}
    
    public static void printOntologyNames (ArrayList<TermSearchResultInformation> searchResults) {
    	if(!(searchResults.size()==0))
    	{	
    		for(int i=0; i<searchResults.size(); i++) 
    			System.out.println("Ontology "+ (i+1) + " Name: "+ searchResults.get(i).getOntologyName()+ 
    				", Acronym: "+ searchResults.get(i).getAcronym());
    	}
    }

    private static String get(String urlToGet) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + API_KEY);
            conn.setRequestProperty("Accept", "application/json");
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}



