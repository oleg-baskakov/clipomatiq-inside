package com.clipomatiq.wiki;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.clipomatiq.processor.*;

public class WikiProcessor extends WebConnector implements Processor{

	public static final String WIKI_DATA = "wikiData";

	private String url;
	private final static String LANG_DEFAULT = "en";
	private final static String URL_SERVICE = ".wikipedia.org/w/api.php";
	public final static String COMMAND_SEARCH_WORD = "word_search";
	public final static String COMMAND_GET_CATEGORY = "get_category";
	private static final String COMMAND_GET_LANG = "get_langs";
	public final static String COMMAND_KEY = "_command";

	public final static String PARAM_SEARCH_WORD = "search_word";
//http://en.wikipedia.org/w/api.php?action=query&list=categorymembers&cmlimit=500&cmtitle=Category:Trip%20hop%20groups
//	http://ru.wikipedia.org/w/api.php?action=query&prop=revisions&titles=������ �����&rvprop=content
	public final static String PARAM_GET_ACTION = "action";
	public final static String PARAM_GET_PROP = "prop";
	public final static String PARAM_GET_TITLES = "titles";
	public final static String PARAM_GET_RVPROP = "rvprop";
	public final static String PARAM_GET_FORMAT = "format";
	public final static String PARAM_GET_LIST = "list";
	public final static String PARAM_GET_CMLIMIT = "cmlimit";
	public final static String PARAM_GET_CMTITLE = "cmtitle";

	public final static String PARAM_VAL_ACTION_QUERY = "query";
	public final static String PARAM_VAL_LIST_CATEGORY = "categorymembers";
	public final static String PARAM_VAL_PROP_REV = "revisions";
	public final static String PARAM_VAL_RVPROP_CONTENT = "content";
	public final static String PARAM_VAL_PROP_XML = "xml";

	private static final Object PARAM_SEARCH_LANG = "search_lang";
	// private final static String PARAM_CITY_SEARCH_INPUT="ptrigger2";
	private HashMap params;
	private WikiData wiki;
	private int currentCmd;


	public WikiProcessor() {
		// TODO Auto-generated constructor stub
		super();
		params = new HashMap();
		url = "";
	}

	public ProcessorData process() {
		// TODO Auto-generated method stub
		String resp = connect(url, params);
		//
		//System.out.println(resp);
		wiki= new WikiData();
		//wiki.addData(resp);
		parseWiki(resp);
		ProcessorData pData=new ProcessorData();
		pData.addData(WIKI_DATA, wiki);
		pData.setProcName("WikiService");
		 System.out.println(resp);
		return pData;
	}


	private void parseWiki(String source) {
		
		WikiParser parser=new WikiParser();
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
			
			xmlReader.setContentHandler(parser);
			ByteArrayInputStream str = new ByteArrayInputStream(source.getBytes("UTF-8"));
			InputSource is=new InputSource();
			is.setByteStream(str);
			
			xmlReader.parse(is);
			wiki=parser.getWiki();
		} catch (Throwable t) {
		t.printStackTrace();
		}

	}


	
	
	
	public HashMap getParam() {
		return params;
	}

	public String getURL() {
		// TODO Auto-generated method stub
		return url;
	}

	public void init(HashMap param) {
		super.init();
		if (param == null || param.size() == 0) {
			return;
		}
		String command = "" + param.get(this.COMMAND_KEY);
		if (command == null)
			return;
		if (COMMAND_SEARCH_WORD.equalsIgnoreCase(command)) {
			url="http://";
			params.put(PARAM_GET_TITLES, param.get(PARAM_SEARCH_WORD));
			params.put(this.PARAM_METHOD_KEY, this.PARAM_METHOD_VAL_GET);
			String lang=(String) param.get(PARAM_SEARCH_LANG);
			if(lang!=null&&lang.trim().length()>0){
				url+= lang;
			}else 
				url+=LANG_DEFAULT;
			url += this.URL_SERVICE;
			params.put(this.PARAM_GET_PROP, this.PARAM_VAL_PROP_REV);
			params.put(this.PARAM_GET_RVPROP, this.PARAM_VAL_RVPROP_CONTENT);
			params.put(this.PARAM_GET_ACTION, this.PARAM_VAL_ACTION_QUERY);
			params.put(this.PARAM_GET_FORMAT, this.PARAM_VAL_PROP_XML);
			
			//currentCmd = this.COMMAND_SEARCH_WORD;
		} else if (COMMAND_GET_LANG.equalsIgnoreCase(command)) {
			//currentCmd = this.CMD_TYPE_WEATHER;

		}else if(COMMAND_GET_CATEGORY.equalsIgnoreCase(command)){
			url="http://";
			params.put(PARAM_GET_CMTITLE, param.get(PARAM_SEARCH_WORD));
			params.put(this.PARAM_METHOD_KEY, this.PARAM_METHOD_VAL_GET);
			String lang=(String) param.get(PARAM_SEARCH_LANG);
			if(lang!=null&&lang.trim().length()>0){
				url+= lang;
			}else 
				url+=LANG_DEFAULT;
			url += this.URL_SERVICE;
			params.put(this.PARAM_GET_LIST, this.PARAM_VAL_LIST_CATEGORY);
			params.put(this.PARAM_GET_CMLIMIT, "500");
			params.put(this.PARAM_GET_ACTION, this.PARAM_VAL_ACTION_QUERY);
			params.put(this.PARAM_GET_FORMAT, this.PARAM_VAL_PROP_XML);
			
		}

	}

}
