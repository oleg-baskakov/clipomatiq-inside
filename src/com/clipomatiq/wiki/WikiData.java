package com.clipomatiq.wiki;

import java.util.ArrayList;

import com.clipomatiq.processor.ProcessorData;

public class WikiData  {

	private StringBuffer data;
	private String id;
	private String title;
	private ArrayList catData;

	public ArrayList getCatData() {
		return catData;
	}


	public void setCatData(ArrayList catData) {
		this.catData = catData;
	}

	public void addCatData(Object data) {
		if(this.catData==null) catData = new ArrayList();
		catData.add(data);
	}


	public WikiData() {
		// TODO Auto-generated constructor stub
		
	}


	public StringBuffer getData() {
		return data;
	}


	public void setData(StringBuffer data) {
		this.data = data;
	}
	public void addData(String dat) {
		if(data==null)this.data = new StringBuffer(dat);
		this.data.append(dat);
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	


}
