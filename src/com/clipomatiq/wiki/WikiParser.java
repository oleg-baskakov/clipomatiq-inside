package com.clipomatiq.wiki;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;



public class WikiParser implements ContentHandler {

	String curTag;
	private WikiData wiki;

	
	
	public WikiParser() {
		wiki = new WikiData();
	}


	public void endDocument() throws SAXException {

	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
//		String tagName=name;
		String tagName=localName;
		String attrName;
		if("".equals(tagName)) tagName=name;
		
		if("page".equalsIgnoreCase(tagName)){
			int size=atts.getLength();
			for(int i=0;i<size;i++){
				attrName=atts.getQName(i);
				if(attrName!=null&&attrName.length()>0){
					if ("pageid".equalsIgnoreCase(name)) {
						wiki= new WikiData();
						wiki.setId(atts.getValue(i));
					} else if ("title".equalsIgnoreCase(name)) {
						wiki.setTitle(atts.getValue(i));
					}
				}
			}
		}else if("categorymembers".equalsIgnoreCase(tagName)){
			wiki= new WikiData();
			
		}else if("cm".equalsIgnoreCase(tagName)){
			int size=atts.getLength();
			for(int i=0;i<size;i++){
				attrName=atts.getQName(i);
				if(attrName!=null&&attrName.length()>0){
					if ("pageid".equalsIgnoreCase(attrName)) {
						
					}else if ("title".equalsIgnoreCase(attrName)) {
						wiki.addCatData(atts.getValue(i));
					}
				}
			}

		}else 
			curTag=tagName;

	}

	public void characters(char[] ch, int start, int length)
	throws SAXException {

		String val=new String(ch,start,length);
		if ("rev".equalsIgnoreCase(curTag)) {
			wiki.addData(val);
		}

	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		String tagName=name;
		if("".equals(tagName)) tagName=localName;
		
		//if("item".equalsIgnoreCase(tagName)){
		//}else 
			curTag="";

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


	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {

	}


	public WikiData getWiki() {
		return wiki;
	}

}
