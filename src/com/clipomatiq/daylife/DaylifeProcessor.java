package com.clipomatiq.daylife;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.clipomatiq.processor.Processor;
import com.clipomatiq.processor.ProcessorData;
import com.clipomatiq.processor.WebConnector;




public class DaylifeProcessor extends WebConnector implements Processor{

	public static final String DAYLIFE_DATA = "daylifeData";
//	private static final String TAG_CITY = "city";
	private String url;// ="http://weather.yahooapis.com/forecastrss";
//	private final static String LANG_DEFAULT = "en";
	private final static String URL_SERVICE = "http://freeapi.daylife.com/xmlrest/publicapi/4.3/";
	public final static String COMMAND_SEARCH = "search";
//	private static final String COMMAND_GET_LANG = "get_langs";
	public final static String COMMAND_KEY = "_command";
	public final static String PARAM_SEARCH = "search_getRelatedArticles";
	public final static String ACCESS_KEY = "2cbb48350282d4ce1b6b47812bda3209";
	public final static String SECRET_KEY = "32a11c70d6d6f9253813c7ff111cf191";
	
//	http://ru.wikipedia.org/w/api.php?action=query&prop=revisions&titles=������ �����&rvprop=content
	public final static String PARAM_GET_ACCESSKEY = "accesskey";
	public final static String PARAM_GET_SIGNATURE = "signature";

	public final static String PARAM_GET_QUERY = "query";
	public static final String COMMAND_SEARCH_KEY = "search_key";

	// private final static String PARAM_CITY_SEARCH_INPUT="ptrigger2";
	private HashMap params;
	//private WikiData wiki;
	private int currentCmd;
	private DayLifeData dld;

	public DaylifeProcessor() {
		// TODO Auto-generated constructor stub
		super();
		params = new HashMap();
		url = "";
	}

	public ProcessorData process() {
		// TODO Auto-generated method stub
		String resp = connect(url, params);
		//System.out.println(resp);
		parseResp(resp);
		ProcessorData pData=new ProcessorData();
		pData.addData(this.DAYLIFE_DATA, dld);
		pData.setProcName("DaylifeService");
		 System.out.println(resp);
		return pData;
	}


	private void parseResp(String source) {
		
		DaylifeParser parser=new DaylifeParser();
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
			xmlReader.setContentHandler(parser);
			ByteArrayInputStream str = new ByteArrayInputStream(source.getBytes("utf8"));
			InputSource is=new InputSource();
			is.setByteStream(str);
			xmlReader.parse(is);
			
		} catch (Throwable t) {
		t.printStackTrace();
		}finally{
		 dld=parser.getDLD();
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
		if (COMMAND_SEARCH.equalsIgnoreCase(command)) {
			url=URL_SERVICE+PARAM_SEARCH;
			String searchValue=""+param.get(COMMAND_SEARCH_KEY);
			String hash=null;
			try {
				hash = generateMD5(searchValue);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params.put(PARAM_GET_ACCESSKEY, ACCESS_KEY);
			params.put(PARAM_GET_SIGNATURE, hash);
			params.put(PARAM_GET_QUERY, searchValue);
			params.put(this.PARAM_METHOD_KEY, this.PARAM_METHOD_VAL_GET);
			
			//currentCmd = this.COMMAND_SEARCH_WORD;
		}

	}
	

	
	
	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
        	int halfbyte = (data[i] >>> 4) & 0x0F;
        	int two_halfs = 0;
        	do {
	        	if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	            	buf.append((char) ('a' + (halfbyte - 10)));
	        	halfbyte = data[i] & 0x0F;
        	} while(two_halfs++ < 1);
        }
        return buf.toString();
    }
 
	public String generateMD5(String text)
	throws NoSuchAlgorithmException, UnsupportedEncodingException  {
		MessageDigest md;
		String data2=ACCESS_KEY+SECRET_KEY+text;

		md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(data2.getBytes("iso-8859-1"), 0, data2.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	public DayLifeData getDld() {
		return dld;
	}


}
