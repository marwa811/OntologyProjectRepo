package bioPortalTools;
import java.util.ArrayList;
import java.util.List;

public class TermSearchResultInformation {
	//ontology information
	String ontologyId;
	String ontologyName;
	String acronym;
	List<String> ontologyDomains;
	double ontologyVisitsMean;
	int ontologyProjects;
	double ontologyPopularity;
	
	//class information
	String classId;
	String prefLabel;
	List<String> classDefination;
	List<String> synonyms;
	int subClassNumber;
	
	public TermSearchResultInformation(String ontologyId, String classId) {
		super();
		this.ontologyId = ontologyId;
		this.classId = classId;
	}
	
	public TermSearchResultInformation() {
		super();
		this.ontologyId = null;
		this.ontologyName=null;
		this.acronym=null;
		this.ontologyDomains=new ArrayList<String>();
		this.ontologyVisitsMean=0;
		this.ontologyProjects=0;
		this.ontologyPopularity=0;
		this.classId = null;
		this.prefLabel=null;
		this.classDefination=new ArrayList<String>();
		this.synonyms=new ArrayList<String>();
		this.subClassNumber=0;
	}

	public String getOntologyId() {
		return ontologyId;
	}
	public void setOntologyId(String ontologyId) {
		this.ontologyId = ontologyId;
	}
	public String getOntologyName() {
		return ontologyName;
	}
	public void setOntologyName(String ontologyName) {
		this.ontologyName=ontologyName;
	}
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String ontologyId) {
		this.acronym = ontologyId.substring(ontologyId.lastIndexOf('/')+1);
	}
	public List<String> getOntologyDomains() {
		return ontologyDomains;
	}
	public void setOntologyDomains(String ontologyDomain) {
		 this.ontologyDomains.add(ontologyDomain);
	}
	public double getOntologyVisitsMean() {
		return ontologyVisitsMean;
	}

	public void setOntologyVisitsMean(double ontologyVisitsMean) {
		this.ontologyVisitsMean = ontologyVisitsMean;
	}

	public int getOntologyProjects() {
		return ontologyProjects;
	}

	public void setOntologyProjects(int ontologyProjects) {
		this.ontologyProjects = ontologyProjects;
	}
	public double getOntologyPopularity() {
		return ontologyPopularity;
	}
	public void setOntologyPopularity(double ontologyPopularity) {
		this.ontologyPopularity = ontologyPopularity;
	}
	///////
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public void setPrefLabel(String prefLabel) {
		this.prefLabel = prefLabel;
	}
	public String getPrefLabel() {
		return prefLabel;
	}
	public void setClassDefination(String classDef)
    {
		 this.classDefination.add(classDef);
    }
	public List<String> getClassDefination(){
		return classDefination;
	}
	public void setSynonyms(String synonym)
    {
		 this.synonyms.add(synonym);
    }
	public List<String> getSynonyms(){
		return synonyms;
	}
	public int getSubClassNumber() {
		return subClassNumber;
	}
	public void setSubClassNumber(int subClassNumber) {
		this.subClassNumber = subClassNumber;
	}


	public void add(TermSearchResultInformation e) {
		this.ontologyId=e.getOntologyId();
		this.classId=e.getClassId();
	}
	public String print(){
		String allData=getOntologyId()+ "   "+ getClassId()+'\n';
		allData+= "Ontology Name: "+ getOntologyName()+ '\n';
		if( getClassDefination().size()> 0)
			for(int i=0; i<getClassDefination().size(); i++)
				allData+=getClassDefination().get(i)+'\n';
		if( getSynonyms().size()> 0)
			for(int i=0; i<getSynonyms().size(); i++)
				allData+=getSynonyms().get(i)+'\n';
		return allData;
	}
}//end
