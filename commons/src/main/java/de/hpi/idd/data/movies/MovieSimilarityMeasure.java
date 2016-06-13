package de.hpi.idd.data.movies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import de.hpi.idd.DatasetUtils;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class MovieSimilarityMeasure extends DatasetUtils{

	private NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();
	private double weightcounter;
	
	
	public MovieSimilarityMeasure() {
		datasetThreshold = 0.83;
	}
	
	public static double subsetSim(Collection<? extends Object> c1, Collection<? extends Object> c2){
		HashSet<Object> intersection = new HashSet<Object>(c1);
		intersection.retainAll(c2);
		int minsize = c1.size() < c2.size() ? c1.size() : c2.size();
				
		return (double)intersection.size()/(double)minsize;		
		
	}
	
	@Override
	public Double calculateSimilarity(Map<String, Object> r1, Map<String, Object> r2, Map<String, String> parameters) {
		weightcounter = 0;
		double sim =  getTitleSimilarity((String)r1.get("title"), (String)r2.get("title"))
				  +  getYearSimilarity((String)r1.get("year"), (String)r2.get("year"))
				  +  getGenresSimilarity((String)r1.get("genre"), (String)r2.get("genre"))
				  +  getUrlSimilarity((String)r1.get("url"), (String)r2.get("url"))
				  +  getWriterSimilarity((String)r1.get("writer"), (String)r2.get("writer"))
				  +  getEditorSimilarity((String)r1.get("editor"), (String)r2.get("editor"))
				  +  getDirectorSimilarity((String)r1.get("director name"), (String)r2.get("director name"))
				  +  getActorsSimilarity((String)r1.get("actor name"), (String)r2.get("actor name"));
				
		return sim/weightcounter;		
	}

	
	private double getTitleSimilarity(String s1, String s2){
		int weight = 6;
		weightcounter+=weight;
		char last1 = s1.length() >= 1 ? s1.charAt(s1.length()-1) : '!';
		char last2 = s2.length() >= 1 ? s2.charAt(s2.length()-1) : '!';
		//if the last char is a digit, it must be the same for not finding sequels
		if((('0' <= last1 && last1 <= '9') || ('0' <= last2 && last2 <= '9')) && last1 != last2) return 0;
		double result = levenshtein.similarity(s1, s2);
		if (Double.isNaN(result)) result = 0;
		if(result > 1.0 ) System.out.println("Title " + result);
		return weight*result;
	}
	
	private double getYearSimilarity(String s1, String s2){
		if(s1.equals("") || s2.equals(""))
			return 0;
		int weight = 1;
		weightcounter+=weight;
		return weight*(s1.equals(s2)? 1.0 : 0.0);
	}

	private double getActorsSimilarity(String s1, String s2){
		
		if(s1.equals("") || s2.equals(""))
			return 0;
		String [] actors1 = s1.split("\\|");
		String [] actors2 = s2.split("\\|");
        int weight = 1;
		weightcounter+=weight;
		
		double result = subsetSim(Arrays.asList(actors1), Arrays.asList(actors2));
		if(result > 1.0 ) System.out.println("Actors " + result);
		return weight*result;

		/*
        java.util.Arrays.sort(actors1);
        java.util.Arrays.sort(actors2);
      
        int actors1i = 0, actors2i = 0;
        double intersect = 0;
        while(actors1i < actors1.length && actors2i < actors2.length){
        	String actor1 = actors1[actors1i];
        	String actor2 = actors2[actors2i];
        	if(actor1.equals(actor2)) intersect++;
        	//System.out.println("Comparing " + actor1 + " to " + actor2 + ": " + comp);
        	//intersect+=levenshtein.similarity(actor1, actor2);
   
        	if(actors1i >= actors1.length-1) {
        		actors2i++;
        		continue;
        	}
        	if(actors2i >= actors2.length-1) {
        		actors1i++;
        		continue;
        	}
        	
        	
        	if(actor1.compareTo(actor2) > 0){
        		//negative:actor1 preceeds
        		actors2i++;        		
        	} else {
        		actors1i++;
        	}
        	
        }
		int minsize = actors1.length < actors2.length ? actors1.length : actors2.length;

        //Jaccard similarity        
        //double result = intersect/(actors1.length + actors2.length);
        double result = intersect/(minsize);
        int weight = 1;
		weightcounter+=weight;
        return weight*(result>1? 1: result);*/
	}

	private double getEditorSimilarity(String s1, String s2){
		if(s1.equals("") || s2.equals(""))
			return 0;
		int weight = 1;
		weightcounter+=weight;
		return weight*levenshtein.similarity(s1, s2);

	}
	
	private double getWriterSimilarity(String s1, String s2){
		
		if(s1.equals("") || s2.equals(""))
			return 0;
		int weight = 1;
		weightcounter+=weight;
		return weight*levenshtein.similarity(s1, s2);
	}


	private double getDirectorSimilarity(String s1, String s2){
		
		if(s1.equals("") || s2.equals(""))
			return 0;
		int weight = 1;
		weightcounter+=weight;
		return weight*levenshtein.similarity(s1, s2);
	}
	


	private double getGenresSimilarity(String s1, String s2){
		if(s1.equals("") || s2.equals(""))
			return 0;
		int weight = 1;
		weightcounter+=weight;
		String [] genres1 = s1.split("\\|");
		String [] genres2 = s2.split("\\|");
		return weight*subsetSim(Arrays.asList(genres1), Arrays.asList(genres2));

	
	}

	private double getUrlSimilarity(String s1, String s2){
		
		if(s1.equals("") || s2.equals(""))
			return 0;
		int weight = 1;
		weightcounter+=weight;
		return weight*(s1.equals(s2) ? 1.0 : 0.0);
	}

	public Map<String, Object> parseRecord(String value){
		String[] rec = value.split("\"");
		String id = "";		
		if(rec.length > 1) id = rec[1]; 

		Map<String, Object> record = new HashMap<>();
		record.put("record_id", Integer.parseInt(id));
		record.put("title", rec[3]);
		record.put("year", rec[5]);
		record.put("genres", rec[7]);
		record.put("url", rec[9]);
		record.put("writer", rec[11]);
		record.put("editor", rec[13]);
		record.put("director", rec[15]);
		if(rec.length < 20){
			record.put("actors", "");
		}
		else{
			record.put("actors", rec[19]);		
		}
		return record;
	}

	@Override
	public Map<String, Object> parseRecord(Map<String, String> values) {
		Map<String, Object> record = new HashMap<>();
		for(Entry<String, String> entry:values.entrySet()){
			record.put(entry.getKey(), entry.getValue());
		}
		return record;
	}
}

