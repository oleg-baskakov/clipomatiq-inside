package com.clipomatiq.daylife;

import java.util.ArrayList;

public class DayLifeData {

	
	public String title;

	
	
	private ArrayList entries;
	
	public DayLifeData() {
		entries = new ArrayList();
	}

	public ArrayList getEntries() {
		return entries;
	}

	public void setEntries(ArrayList entries) {
		this.entries = entries;
	}

	public void addEntry(DaylifeArticle entry) {
		if(this.entries == null)this.entries = new ArrayList();
		this.entries.add(entry);
	}

}
