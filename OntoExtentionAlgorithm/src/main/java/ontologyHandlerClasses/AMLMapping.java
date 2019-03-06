package ontologyHandlerClasses;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import applicationClasses.MainClass;
import aml.AML;

//A class for AML Mappings//

public class AMLMapping {
	private int mappingId;
	private String sourceURI;
	private String sourceName;
	private String targetURI;
	private String targetName;
	private double similarityScore;
	
	public int getMappingId() {
		return mappingId;
	}
	

	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}

	public String getSourceURI() {
		return sourceURI;
	}

	public void setSourceURI(String sourceURI) {
		this.sourceURI = sourceURI;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getTargetURI() {
		return targetURI;
	}

	public void setTargetURI(String targetURI) {
		this.targetURI = targetURI;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public double getSimilarityScore() {
		return similarityScore;
	}

	public void setSimilarityScore(String similarityScoreString) {
		similarityScore=Double.parseDouble(similarityScoreString);
		this.similarityScore = similarityScore;
	}
}
