package com.clipomatiq.youtube;

import java.util.ArrayList;

public class YouTubeData {

	public YouTubeData() {
		entries = new ArrayList();
		// TODO Auto-generated constructor stub
	}
	
	public String title;
	private int totalResults;
	private float totalACnt;
	private int startIndex;
	private int itemsPerPage;
	private ArrayList entries;
	
	public int getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	public ArrayList getEntries() {
		return entries;
	}
	public void setEntries(ArrayList entries) {
		this.entries = entries;
		this.totalResults=entries.size();
	}

	public void addEntry(YTEntry entry) {
		if(this.entries == null)this.entries = new ArrayList();
		this.entries.add(entry);
	}
	public float getTotalACnt() {
		return totalACnt;
	}
	public void setTotalACnt(float totalACnt) {
		this.totalACnt = totalACnt;
	}


}

