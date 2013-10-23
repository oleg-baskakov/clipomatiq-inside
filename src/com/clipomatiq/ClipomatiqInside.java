package com.clipomatiq;

import java.util.ArrayList;
import java.util.HashMap;

import com.clipomatiq.daylife.DayLifeData;
import com.clipomatiq.daylife.DaylifeProcessor;
import com.clipomatiq.db.DBProcessor;
import com.clipomatiq.processor.ProcessorData;
import com.clipomatiq.wiki.WikiData;
import com.clipomatiq.wiki.WikiProcessor;


public class ClipomatiqInside {

	
	
	//private static final String scheme = "}} [[";
	//private static final String scheme = "| align=&quot;left&quot;| ";
//	private static final String scheme = "by [[";
	//private static final String scheme = "| [[";
	private static final String scheme = "*[[";
	public static final int GENRE_ID = 2;
	private static final boolean putInDb = true;
	private static final boolean isCategory = true;
	private static final boolean putGenreInDb = true;
	//private static final boolean isCategory = false;
	static String wikiSearchWord;
	private static String title;

	public ClipomatiqInside(){
		
	
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClipomatiqInside ci= new ClipomatiqInside();
		wikiSearchWord = "Category:Trip_hop_groups";
		title="Trip-hop";
		ci.wikiProcess();
	//	ci.daylifeProcess("");
	}

	public static DayLifeData daylifeProcess(String query) {
		HashMap param;
		param=new HashMap();
		//param.put(WikiProcessor.COMMAND_KEY, WikiProcessor.COMMAND_SEARCH_WORD);
		param.put(DaylifeProcessor.COMMAND_SEARCH_KEY, query+" music");
		param.put(DaylifeProcessor.COMMAND_KEY, DaylifeProcessor.COMMAND_SEARCH);
			
		///param.put(WikiProcessor., "http://ask.yahoo.com/index.xml");
	//	param.put(RSSProcessor.GET_RSS_PARAM_URL, "http://news.google.com/news?ned=us&topic=h&output=rss");
		//http://news.google.com/news?ned=us&topic=h&output=rss
		DaylifeProcessor rss=new DaylifeProcessor();
		rss.init(param);
		ProcessorData pd=rss.process();
		DayLifeData dld=(DayLifeData)pd.getData(DaylifeProcessor.DAYLIFE_DATA);
		return dld;
//		WikiData wiki = (WikiData) pd.getData(WikiProcessor.WIKI_DATA);
//		wiki.setTitle("");
	}

/**
 * <b>wikiProcess</b>
 * Data extractor from wikipedia.
 * Used to get band/genre related data 
 */
	
	private void wikiProcess() {
		HashMap<String, String> param;
		param=new HashMap<String, String>();
		//param.put(WikiProcessor.COMMAND_KEY, WikiProcessor.COMMAND_SEARCH_WORD);
		
		param.put(WikiProcessor.PARAM_SEARCH_WORD, wikiSearchWord);
		if(isCategory)
			
			param.put(WikiProcessor.COMMAND_KEY, WikiProcessor.COMMAND_GET_CATEGORY);
		else
			param.put(WikiProcessor.COMMAND_KEY, WikiProcessor.COMMAND_SEARCH_WORD);
			
		WikiProcessor rss=new WikiProcessor();
		rss.init(param);
		ProcessorData pd=rss.process();
		WikiData wiki = (WikiData) pd.getData(WikiProcessor.WIKI_DATA);
		wiki.setTitle(title);
		parse4artists(wiki);
	}


	private  void parse4artists(WikiData wiki) {
		// TODO Auto-generated method stub
		StringBuffer sb = wiki.getData();
		
		String title = wiki.getTitle();
		ArrayList<String> artists;
		if(isCategory){
			artists=new ArrayList<String>();//wiki.getCatData();
			String a;
			int size = wiki.getCatData().size();
			for(int i=0;i<size;i++){
				artists.add((""+wiki.getCatData().get(i)).replaceAll("\\(.*band\\)|\\(.*group\\)|\\(.*performer\\)|\\(.*musician\\)|\\(.*singer\\)", ""));
				
			}
		}else 	
			artists = normalize(sb);

		System.out.println(title);
		int genreId = GENRE_ID;
		if(putGenreInDb)
			genreId = DBProcessor.addGenre(wiki.getTitle(), 0);
		//for(int i=0;i<artists.size();i++){
			//System.out.println(artists.get(i));	
		if(putInDb)	
			DBProcessor.addArtists(genreId,artists);
		//}
		
		
	}
	
	
	private  void parse4Words(WikiData wiki) {
		// TODO Auto-generated method stub
		StringBuffer sb = wiki.getData();
		
		String title = wiki.getTitle();
		ArrayList artists;
		if(isCategory){
			artists=new ArrayList();//wiki.getCatData();
			String a;
			for(int i=0;i<wiki.getCatData().size();i++){
			//	artists.add((""+wiki.getCatData().get(i)).replaceAll("\\(.*band\\)|\\(.*group\\)|\\(.*performer\\)|\\(.*musician\\)|\\(.*singer\\)", ""));
				
			}
		}else {	
			artists = normalize(sb);
		}

		System.out.println(title);
		//DBProcessor.addGenre(wiki.getTitle(), 0);
		//for(int i=0;i<artists.size();i++){
			//System.out.println(artists.get(i));	
		if(putInDb)	
			DBProcessor.addArtists(GENRE_ID,artists);
		//}
		
		
	}
	
	
	private ArrayList<String> normalize(StringBuffer dat){
			int start=0;
			boolean result=true;
			int offsetSt;
			int offsetEnd=0;
			String cut;
			int offsetMid;
			ArrayList<String> data=new ArrayList<String>();

			while(result){
				offsetSt = dat.indexOf(scheme, offsetEnd);
				if(offsetSt>=0){
					//data.add(dat.substring(offsetEnd+2, offsetSt));
				//	offsetEnd = dat.indexOf(" ||", offsetSt);
					offsetEnd = dat.indexOf("]]", offsetSt);
					if(offsetEnd<0)result=false;
					else{
						cut=dat.substring(offsetSt+scheme.length(), offsetEnd);
						offsetMid=cut.indexOf("|");
						if(offsetMid<0)
							data.add(dat.substring(offsetSt+scheme.length(), offsetEnd).replaceAll("\\(.*band\\)|\\(.*group\\)|\\(.*performer\\)|\\(.*musician\\)|\\(.*singer\\)", ""));
						else 
							data.add(cut.substring(offsetMid+1).replaceAll("\\(.*band\\)|\\(.*group\\)|\\(.*performer\\)|\\(.*musician\\)|\\(.*singer\\)", ""));
					}
				}else result=false;
			}
			return data;
	}

}
