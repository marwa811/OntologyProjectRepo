package evaluations;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class Evaluations {
	
	int numOfRulesGold=0;
	int numOfRulesMatcher=0;
	int numOfRulesCorrect=0;
	
	public Evaluations(ArrayList<String> mapping, ArrayList<String> reference) {
		// Mapping correct = reference.getIntersection(mapping); 
		ArrayList<String> correct=new ArrayList<String>();
		for (String r : reference) 
		{
			String rParts[]=r.split(",");
			for (String m : mapping) 
			{
				String mParts[]=m.split(",");
				if (rParts[0].equalsIgnoreCase(mParts[0])&& rParts[1].equalsIgnoreCase(mParts[1])) 
				{
					correct.add(r);
				}
				else if(rParts[0].equalsIgnoreCase(mParts[1])&& rParts[1].equalsIgnoreCase(mParts[0]))			
				{
					correct.add(r);
				}		
				/*if(mParts[2].equals("sub"))
					subNumber++;*/
			}
		}	

		Iterator<String> correctItor = correct.iterator();
		while (correctItor.hasNext()) {
			String cor = correctItor.next();
			System.out.println("correct item: "+cor);
		}
		
		//System.out.println("No of mappings: "+ mapping.size());
		this.numOfRulesGold = reference.size();	
		System.out.println("No of Gold rules: "+this.numOfRulesGold);
		//this.numOfRulesMatcher = mapping.size()-subNumber/reference.size();
		this.numOfRulesMatcher =  mapping.size();
		System.out.println("No of rules matcher: "+ this.numOfRulesMatcher);
		this.numOfRulesCorrect = correct.size();
		System.out.println("No of correct mappings: "+this.numOfRulesCorrect);
		
	}	
	
	
	public double getPrecision() {
		return (double)this.numOfRulesCorrect /  (double)this.numOfRulesMatcher;
	}
	
	public double getRecall() {
		return (double)this.numOfRulesCorrect /  (double)this.numOfRulesGold;
	}
	
	public double getFMeasure() {
		if ((this.getPrecision() == 0.0f) || (this.getRecall() == 0.0f)) { return 0.0f; }
		return (2 * this.getPrecision() * this.getRecall()) / (this.getPrecision() + this.getRecall());
	}
	
	public int getMatcherAlignment() {
		return numOfRulesMatcher;
	}
	
	public int getCorrectAlignment() {
		return numOfRulesCorrect;
	}
	
	
	private static String toDecimalFormat(double precision) {
		DecimalFormat df = new DecimalFormat("0.000");
		return df.format(precision).replace(',', '.');
	}
		
	public String toShortDesc() {
		double precision = this.getPrecision();
		double recall = this.getRecall();
		double f = this.getFMeasure();
		return toDecimalFormat(precision) + "  " + toDecimalFormat(recall) + "  " + toDecimalFormat(f);
	}
}
