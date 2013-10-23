package com.clipomatiq.youtube;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.HashMap;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.clipomatiq.processor.*;

public class YouTubeProcessor extends WebConnector{


	private static final String _10 = "10";
	private static final String SEARCH_CATEGORY = "search_category";
	private static final String SPECIFIC = "specific";
	private static final String SEARCH_CATEGORY_TYPE = "search_category_type";
	private String url;
	private final static String URL_SERVICE = "http://gdata.youtube.com/feeds/api/videos/-/";
	private final static String URL_SERVICE_2 = "http://gdata.youtube.com/feeds/api/videos";
	public final static String MAX_RESULT = "max-results";
	public final static String VQ = "vq";
	private static final String ORDERBY = "orderby=";
	public final static String START_INDEX = "start-index=";
	private static final String MAX_RESULT_50 = "50";
	private static final String MAX_RESULT_20 = "20";
	private static final String MAX_RESULT_40 = "40";
	private static final String ORDER_BY_VIEW_COUNT = "viewCount";

	// private final static String PARAM_CITY_SEARCH_INPUT="ptrigger2";
	private HashMap params;
	private YouTubeData utube;
	private int currentCmd;


	public YouTubeProcessor() {
		// TODO Auto-generated constructor stub
		super();
		params = new HashMap();

		url = "";
		init();
	}

	public YouTubeData processGetClips(String name) {
		// TODO Auto-generated method stub
		name= name.trim().replaceAll("\\p{Punct}+|\\p{Blank}+", "/").toLowerCase();
		name= name.replaceAll("/+", "/");
		
		//String localUrl=this.URL_SERVICE+name+ "/?"+this.MAX_RESULT+MAX_RESULT_50+"&"+this.ORDERBY+ORDER_BY_VIEW_COUNT;
		if(!name.endsWith("/"))name+="/";
		String localUrl=this.URL_SERVICE+name+ "Music/";
		url=localUrl;//+"&"+this.START_INDEX+"10";
		params.put(this.PARAM_METHOD_KEY, this.PARAM_METHOD_VAL_GET);

		params.put(this.ORDERBY, ORDER_BY_VIEW_COUNT);
		params.put(this.MAX_RESULT, MAX_RESULT_50);
		String resp = connect(url, params);
		//
		System.out.println(url);
		//System.out.println(resp);
		//wiki= new WikiData();
		//wiki.addData(resp);
		YouTubeData utube = parseYouTube(resp);
		// System.out.println(resp);
		return utube;
	}


	public YouTubeData getRelevanceClips2(String name) {
		// TODO Auto-generated method stub

		name= name.replaceAll("\\p{Punct}+|\\p{Blank}+", "/").toLowerCase();
		//String localUrl=this.URL_SERVICE+name+ "/?"+this.MAX_RESULT+MAX_RESULT_50+"&"+this.ORDERBY+ORDER_BY_VIEW_COUNT;
		if(!name.endsWith("/"))name+="/";
		String localUrl=this.URL_SERVICE+name+ "Music/";
		url=localUrl;//+"&"+this.START_INDEX+"10";
		params.put(this.PARAM_METHOD_KEY, this.PARAM_METHOD_VAL_GET);

		params.put(this.ORDERBY, ORDER_BY_VIEW_COUNT);
		params.put(this.MAX_RESULT, MAX_RESULT_20);
		String resp = connect(url, params);
		//
		System.out.println(url);
		//System.out.println(resp);
		//wiki= new WikiData();
		//wiki.addData(resp);
		YouTubeData utube = parseYouTube(resp);
		// System.out.println(resp);
		return utube;
	}

	
	public YouTubeData getRelevanceClips(String name) {
		// TODO Auto-generated method stub
		//name= name.replaceAll("\\p{Punct}|\\p{Blank}", "/");
		//String localUrl=this.URL_SERVICE+name+ "/?"+this.MAX_RESULT+MAX_RESULT_50+"&"+this.ORDERBY+ORDER_BY_VIEW_COUNT;
		String localUrl=this.URL_SERVICE_2;
		url=localUrl;//+"&"+this.START_INDEX+"10";

		params.put(this.PARAM_METHOD_KEY, this.PARAM_METHOD_VAL_GET);
		params.put(this.VQ, name);
		params.put(this.MAX_RESULT, MAX_RESULT_20);
		params.put(SEARCH_CATEGORY_TYPE, SPECIFIC);
		params.put(SEARCH_CATEGORY, _10);
		//params.put(this.ORDERBY, ORDER_BY_VIEW_COUNT);
		System.out.println(URLEncoder.encode(name));
		//params.put(this.MAX_RESULT, MAX_RESULT_50);
		String resp = connect(url, params);
		//
		System.out.println(url);
		//System.out.println(resp);
		//wiki= new WikiData();
		//wiki.addData(resp);
		YouTubeData utube = parseYouTube(resp);
		// System.out.println(resp);
		return utube;
	}

	private YouTubeData parseYouTube(String source) {
		
		YouTubeParser parser=new YouTubeParser();
		YouTubeData utube=null;
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
			
			xmlReader.setContentHandler(parser);
			ByteArrayInputStream str = new ByteArrayInputStream(source.getBytes("UTF-8"));
			InputSource is=new InputSource();
			is.setByteStream(str);
			
			xmlReader.parse(is);
			utube=parser.getYouTubeData();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return utube;
	}

	
	public HashMap getParam() {
		return params;
	}

	public String getURL() {
		// TODO Auto-generated method stub
		return url;
	}



}
