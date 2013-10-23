package com.clipomatiq.daylife;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;



public class DaylifeParser implements ContentHandler {

	String curTag;
	String oldVal;
	private DayLifeData dlData;
	private boolean isArticle;
	private boolean isSource;
	private boolean hasThumb;
	int a=0;
	private DaylifeArticle entry;


	public DaylifeParser() {
		dlData = new DayLifeData();
		curTag="";
		isArticle=false;
		hasThumb = false;
		isSource=false;
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
		
		if("article".equalsIgnoreCase(tagName)){
			entry=new DaylifeArticle();
			isArticle=true;
		}
		else
		if("source".equalsIgnoreCase(tagName)){
			isSource=true;
		}

		 
		

	}

	public void characters(char[] ch, int start, int length)
	{
		try{
		String val=oldVal+new String(ch,start,length);
		oldVal=val;
		if(isSource){
			if ("name".equalsIgnoreCase(curTag)) {
				entry.setSourceName(val);
			}
		}
		else if(isArticle){
			parseEntry(val);
			return;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void parseEntry(String val) {

//		NamedNodeMap attr = it.getAttributes();
		String name;
		//System.out.println("size=" + size);
			if ("headline".equalsIgnoreCase(curTag)) {
				entry.setHeadline(val);
			} else if ("timestamp".equalsIgnoreCase(curTag)) {
				entry.setTimestamp(val);
			} else if ("excerpt".equalsIgnoreCase(curTag)) {
				entry.setExcerpt(val);
			} else if ("daylife_url".equalsIgnoreCase(curTag)) {
				entry.setDaylife_url(val);
			} else if ("article_id".equalsIgnoreCase(curTag)) {
				entry.setArticle_id(val);

			}

	}
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		oldVal="";
		String tagName=name;
		if("".equals(tagName)) tagName=name;
		
		if("article".equalsIgnoreCase(tagName)){
			dlData.addEntry(entry);
			isArticle=false;
			System.out.println("add new entry"+a++);
		}else if("source".equalsIgnoreCase(tagName)){
			isSource=false;
		}
		else curTag="";
	}

	
	


	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		
	}





	public DayLifeData getDLD() {
		return dlData;
	}




}
