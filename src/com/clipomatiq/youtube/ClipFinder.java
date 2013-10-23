package com.clipomatiq.youtube;

import java.util.ArrayList;
import java.util.HashMap;

import com.clipomatiq.ClipomatiqInside;
import com.clipomatiq.db.DBProcessor;

public class ClipFinder {

	private HashMap goodWords;
	
	private String[] badWords={
			"# cover",
			"cover #",
			"# tribute",
			"tribute #",
			"fan video",
			"slideshow"
	};
	
	private String[] addWords={
		"# music video",
		"# video",
		"# official video",
		"# clip"
	};
	
	public String artistName;
	public float totACount;
	
	public ClipFinder(){
		
	}
	/// получить список клипов по артисту.
	/*
	 * для каждого клипа получить 
	 * 1. рейтинг
	 * 2. кол-во просмотров
	 * 3. когда был добавлен
	 * 
	 * Расчитать для каждого клипа:
	 * 1. количество просмотров за день
	 * 
	 * Получить общее кол-во видео для данного исполнителя
	 * Получить мах и мин количество просмотров для исполнителя
	 * получить среднее кол-во просмотров для исполнителя.
	 * 
	 * перейти к семантической обработке комментов
	 * 
	 * */
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ClipFinder cf = new ClipFinder();
//		cf.findClipsByName("Arctic Monkeys");
		//cf.findClipsByName("Richard Ashcroft");
		//cf.artistName = "Coldplay";
		ArrayList artists=DBProcessor.getArtists(ClipomatiqInside.GENRE_ID);
		int size=artists.size()>=250?250:artists.size();
		YouTubeData utube ;
		String name="3 Doors Down";
		Object[] artist;
		int id=5;
		for(int i=0;i<size;i++){
			try{
				artist =(Object[])artists.get(i);
				id=((Integer)artist[0]).intValue();
				name=""+artist[1];
				System.out.println("\n\nSearching clips for "+name);
				utube=cf.findClipsByName(name);
				DBProcessor.addClips(id, utube.getEntries());
				DBProcessor.updateArtistStatus(id);
				System.out.println("\n\nupdate status for "+name+" with id= "+id);
				System.out.println(utube.getEntries().size());
			}
			catch(Exception e){
				continue;
			}
		}
	}
	
	public YouTubeData findClipsByName(String name){
		YouTubeProcessor ytp = new YouTubeProcessor();
		artistName = name;
		
		YouTubeData utube = ytp.processGetClips(name);
		//System.out.println(utube.getTotalResults());
		utube.title=name;
		validation(utube, false);
		
		ArrayList entries = countRating(utube.getEntries());
		utube.setEntries(entries);
//		DBProcessor.addClips(124, utube.getEntries());
		validation(utube, false);
		totACount=getTotalACount(utube.getEntries());
		validation(utube, true);
//		System.out.println(utube.getEntries().size());
		return utube;
		
	}
	
	private ArrayList countRating(ArrayList entries) {
		HashMap<String, YTEntry> clips = new HashMap<String, YTEntry>();
		YTEntry  entry;
		YTEntry  entry2;
		//float totalACount = this.getTotalACount(entries);
		for(int i=0;i<entries.size();i++){
			entry=(YTEntry) entries.get(i);
			
			clips.put(entry.getId(), entry);
		}
		YouTubeData utube;
		YouTubeProcessor ytp = new YouTubeProcessor();
		int size;
		String key;
		String noDouble;
		for(int i=0;i<addWords.length;i++){
	//		entry=(YTEntry) entries.get(i);
	//		if(entry.getAverageCount()<totalACount)continue;
//			utube = ytp.processGetClips2(addWords[i].replace("#", artistName));
			utube = ytp.getRelevanceClips(addWords[i].replace("#", artistName));
			
			if(utube==null){
				continue;
				
			}
			//validation(utube);
			size=utube.getEntries().size();
			for(int a=0;a<size;a++){
				entry2 = (YTEntry)utube.getEntries().get(a); 
				key= entry2.getId();
				if(clips.containsKey(key)){
					((YTEntry)clips.get(key)).addRelevanceCount();
				}
				else{
					noDouble = check4Dublicates(entry2, new ArrayList(clips.values()));
					if(noDouble!=null){
						clips.put(entry2.getId(), entry2);
						if(noDouble.length()>0)
							((YTEntry)clips.get(noDouble)).setRelevanceCount(-10);
					}
					
				}
			}
		}
		
		return new ArrayList(clips.values());
	}
	private void validation(YouTubeData utube, boolean full) {
		// TODO Auto-generated method stub
		YTEntry entry;
		int len=utube.getEntries().size();
		String title=utube.title;
		boolean result;
		ArrayList delIndexes=new ArrayList();
		float averTemp=totACount/3;
		float averTemp10=totACount/8;
		for(int i =0;i<len;i++){
			entry=(YTEntry)utube.getEntries().get(i);
//			if(!"Music".equalsIgnoreCase(entry.getCategory()))
//				result=false;
			result = checkTitle(entry, title);
			result = result& checkDuration(entry);
			
			result= result&checkDesc(entry);
			if(full){
				if(entry.getAverageCount()>=averTemp){
					checkTitleForUpdateRating(entry,title);
				}else if(entry.getAverageCount()<averTemp10){
					if(entry.getRelevanceCount()>0){
						//System.out.println();
					}else
						entry.setRelevanceCount(-1);
				}
			
			}
				
			if(!result)delIndexes.add(entry);
		}
		for(int i =0;i<delIndexes.size();i++){
		//	utube.getEntries().remove(i);
			utube.getEntries().remove(delIndexes.get(i));
		}

	}

		

	private boolean checkDuration(YTEntry entry) {
		
		if(entry.getDuration()<111)
			return false;
		else 
			return true;
	}

	private boolean checkTitle(YTEntry entry, String title) {
		
		boolean result = true;
		String et=entry.getTitle().toLowerCase().trim();
		result = et.matches(".*(cover|tribute|fan video).*");
		if(result)
			return false;
		title=title.toLowerCase().trim();
//		result = et.matches("\\A"+title+"\\p{Blank}*-\\p{Blank}*\\p{Alnum}+?");
/*		result = et.matches("\\A"+title+"\\p{Blank}*-\\p{Blank}*\\p{Alnum}+?.*");
		if(result) {
			entry.addRelevanceCount();
//			entry.addRelevanceCount();
//			entry.addRelevanceCount();
//			entry.addRelevanceCount();
//			entry.addRelevanceCount();
		}*/
		result =et.contains(title);
		//if(!result)return result;
		return result;
	}

	private void checkTitleForUpdateRating(YTEntry entry, String title) {
		// TODO Auto-generated method stub
		boolean result = false;
		String et=entry.getTitle().toLowerCase().trim();
		title=title.toLowerCase().trim();
		title=title.replace(")", "\\)");
		title=title.replace("(", "\\(");

		String desc = entry.getContent().toLowerCase().trim();
//		result = et.matches("\\A"+title+"\\p{Blank}*-\\p{Blank}*\\p{Alnum}+?");
		result = et.matches("\\A"+title+"\\p{Blank}*-\\p{Blank}*[\\p{Alnum}\\p{Blank}\\'\\!\\?\\-]+");
		if(result) {
			result = et.matches("-.*\\(.*live.*\\).*");
			result = result & desc.matches(".* live.*(at|on|in|performance|solo|version).*");
			if(result){
				
				entry.addRelevanceCount();
			}else{
				entry.addRelevanceCount();
				entry.addRelevanceCount();
				entry.addRelevanceCount();
				entry.addRelevanceCount();
			}
		}

	}

	
	private boolean checkDesc(YTEntry entry) {
		// TODO Auto-generated method stub
		boolean result = false;
		String et=entry.getContent();
		if(et==null)return false;
		et=et.toLowerCase().trim();
		et=et.replaceAll("\\p{Cntrl}|\\p{Space}", " ");
		result = et.matches(".*( cover | tribute | fan video|slideshow).*");
	//	if(result)
	//		result=result;
		
//		result = et.matches("\\A"+title+"\\p{Blank}*-\\p{Blank}*\\p{Alnum}+?");
/*		result = et.matches("\\A"+title+"\\p{Blank}*-\\p{Blank}*\\p{Alnum}+?.*");
		if(result) {
			entry.addRelevanceCount();
//			entry.addRelevanceCount();
//			entry.addRelevanceCount();
//			entry.addRelevanceCount();
//			entry.addRelevanceCount();
		}
		result =et.contains(title);
		
		if(!result)return result;*/
		return !result;
	}

	public int getRatingForClip(String clip){
		int rating=0;
		StringBuffer comments=getCommentsForClip(clip);
		int tmpRating = 0;
		rating+= getOriginalRating(clip);
		tmpRating = commentsAnalysator(comments);
		rating+=tmpRating;
		return rating;
	}

	private int getOriginalRating(String clip) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int commentsAnalysator(StringBuffer comments) {
		// TODO Auto-generated method stub
		return 0;
	}

	private StringBuffer getCommentsForClip(String clip) {
		// TODO Auto-generated method stub
		return null;
	}

	public float getTotalACount(ArrayList clips) {
		// TODO Auto-generated method stub
		float tot=0;
		int size=clips.size();
		for(int i=0;i<size;i++){
			tot+=((YTEntry)clips.get(i)).getAverageCount();
		}
		tot/=size;

		return tot;
	}
	private String check4Dublicates(YTEntry entry2, ArrayList clips) {
		// TODO Auto-generated method stub
		int size = clips.size();
		YTEntry clip;
		String entryName= entry2.getTitle().trim().toLowerCase();
		String clipName;
		for(int i=0;i< size;i++){
			clip= (YTEntry)clips.get(i);
			entryName=entryName.replaceAll("\\p{Punct}+", " ").replaceAll("\\p{Blank}+", " ").trim();
			clipName = clip.getTitle().trim().toLowerCase().replaceAll("\\p{Punct}+", " ").replaceAll("\\p{Blank}+", " ").trim();;
			if(clipName.equals(entryName)){
				if(clip.getAverageCount()>=entry2.getAverageCount()){
					return null;
				}else{
					if(clip.getRelevanceCount()>0){
						return null;
					}
					//clips.remove(clip);
					return clip.getId();
				}
			}
			
		}
		return "";
	}

}
