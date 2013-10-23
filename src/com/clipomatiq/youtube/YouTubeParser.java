package com.clipomatiq.youtube;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;



public class YouTubeParser implements ContentHandler {

	String curTag;
	String oldVal;
	private YouTubeData utube;
	private boolean isEntry;
	private boolean hasThumb;
	int a=0;
	private YTEntry entry;
	
	
	public YouTubeData getYouTubeData() {
		return utube;
	}


	public YouTubeParser() {
		utube = new YouTubeData();
		curTag="";
		isEntry=false;
		hasThumb = false;
		oldVal="";
	}





	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}


	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
		String tagName=name;
		String attrName;
		
		if("".equals(tagName)) tagName=name;
	//	tagStack.push(curTag);
		curTag=tagName;
		
		if("entry".equalsIgnoreCase(tagName)){
			entry=new YTEntry();
			isEntry=true;
		}
		else
		if("yt:duration".equalsIgnoreCase(tagName)){
			entry.setDuration(Integer.parseInt(atts.getValue("seconds")));

		}else if("media:thumbnail".equalsIgnoreCase(tagName)){
			entry.setThumb(atts.getValue("url"));
			hasThumb =true;
		}else if("yt:statistics".equalsIgnoreCase(tagName)){
			entry.setViewCount(Long.parseLong(atts.getValue("viewCount")));
			if(atts.getValue("favoriteCount")!=null)
				entry.setFavCount(Long.parseLong(atts.getValue("favoriteCount")));
		}else if("gd:rating".equalsIgnoreCase(tagName)){
			entry.setNumRatings(Integer.parseInt(atts.getValue("numRaters")));
			entry.setAverageRating(Float.parseFloat(atts.getValue("average")));
		}else if("gd:feedLink".equalsIgnoreCase(tagName)){
			entry.setCommentsLink(atts.getValue("href"));
		}else if("media:player".equalsIgnoreCase(tagName)){
			entry.setMediaUrl(atts.getValue("url"));
		}else if("category".equalsIgnoreCase(tagName)){
			String scheme=""+atts.getValue("scheme");
			if(scheme.contains("categories.cat")){
				entry.setCategory(""+atts.getValue("term"));
			}

		} 
		

	}

	public void characters(char[] ch, int start, int length)
	throws SAXException {
		
		String val=oldVal+new String(ch,start,length);
		oldVal=val;
		if(isEntry){
			parseEntry(val);
			return;
		}
		
		if ("openSearch:itemsPerPage".equalsIgnoreCase(curTag)) {
			utube.setItemsPerPage(Integer.parseInt(val));
		} else if ("openSearch:startIndex".equalsIgnoreCase(curTag)) {
			utube.setStartIndex(Integer.parseInt(val));
		} else if ("openSearch:totalResults".equalsIgnoreCase(curTag)) {
			utube.setTotalResults(Integer.parseInt(val));
		}
		
	}

	private void parseEntry(String val) {

//		NamedNodeMap attr = it.getAttributes();
		String name;
		//System.out.println("size=" + size);
			if ("title".equalsIgnoreCase(curTag)) {
				entry.setTitle(val);
			} else if ("content".equalsIgnoreCase(curTag)) {
				entry.setContent(val);
			} else if ("media:keywords".equalsIgnoreCase(curTag)) {
				entry.setKeywoords(val);
			} else if ("published".equalsIgnoreCase(curTag)) {
				entry.setPublished(val);
			} else if ("id".equalsIgnoreCase(curTag)) {
				entry.setId(val);
			} else if ("media:category".equalsIgnoreCase(curTag)) {
				entry.setCategory(val);

			}

	}
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		oldVal="";
		String tagName=name;
		if("".equals(tagName)) tagName=name;
		
		if("entry".equalsIgnoreCase(tagName)){
			boolean result;
			if(!"Music".equalsIgnoreCase(entry.getCategory()))
				result=false;
			else
				result= check4Dublicates(entry);
			
			if(result)
				utube.addEntry(entry);
			isEntry=false;
			hasThumb= false;
			System.out.println("add new entry"+a++);
		}else curTag="";
	}

	
	private boolean check4Dublicates(YTEntry entry2) {
		// TODO Auto-generated method stub
		int size = utube.getEntries().size();
		YTEntry clip;
		String entryName= entry2.getTitle().trim().toLowerCase();
		String clipName;
		for(int i=0;i< size;i++){
			clip= (YTEntry)utube.getEntries().get(i);
			entryName=entryName.replaceAll("\\p{Punct}+", " ").replaceAll("\\p{Blank}+", " ").trim();
			clipName = clip.getTitle().trim().toLowerCase().replaceAll("\\p{Punct}+", " ").replaceAll("\\p{Blank}+", " ").trim();;
			if(clipName.equals(entryName)){
				if(clip.getAverageCount()>=entry2.getAverageCount()){
					return false;
				}else{
					utube.getEntries().remove(clip);
					return true;
				}
			}
			
		}
		return true;
	}


	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}




}
