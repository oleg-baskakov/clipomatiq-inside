package com.clipomatiq.youtube;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YTEntry {

	private String id;
	private String uid;
	private String mediaUrl;
	private String published;
	private String title;
	private String content;
	private String keywoords;
	private int duration;
	private String category;
	private String thumb;
	private long viewCount;
	private long favCount;
	private int numRatings;
	private float averageRating;
	private String commentsLink;
	private long dateFrom;
	private float averageCount;
	private int relevanceCount;
	public int artistId;
	public String artistName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(id!=null &&id.length()>0){
			//if(id.startsWith("http://gdata.youtube.com/feeds/api/videos/")){
				id=id.replace("http://gdata.youtube.com/feeds/api/videos/", "");
				id=id.replace("/api/videos/", "");
				
			//}
				if(id.length()>15)
					this.id = id;
			this.id = id;
		}
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
		try {
			if(published.length()<11){
				dateFrom=0;
				
				return;
				
			}
			String tmpDate= published.substring(0, 10);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date date = format.parse(tmpDate);
			dateFrom=date.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKeywoords() {
		return keywoords;
	}

	public void setKeywoords(String keywoords) {
		this.keywoords = keywoords;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb.replace("0.jpg", "");
	}

	public long getViewCount() {
		return viewCount;
	}

	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
		long period = (System.currentTimeMillis()-dateFrom)/60000;
		averageCount=(float)viewCount/period;
		System.out.println(this.getTitle()+" was viewed "+viewCount+" since "+new Date(this.dateFrom)+". It's average "+averageCount+" per min");
	}
	public void setViewCount(long viewCount, boolean db) {
		this.viewCount = viewCount;
	}

	public long getFavCount() {
		return favCount;
	}

	public void setFavCount(long favCount) {
		this.favCount = favCount;
	}

	public int getNumRatings() {
		return numRatings;
	}

	public void setNumRatings(int numRatings) {
		this.numRatings = numRatings;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public String getCommentsLink() {
		return commentsLink;
	}

	public void setCommentsLink(String commentsLink) {
		this.commentsLink = commentsLink;
	}

	public YTEntry() {
		// TODO Auto-generated constructor stub
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public float getAverageCount() {
		return averageCount;
	}

	public void setAverageCount(float averageCount) {
		this.averageCount = averageCount;
	}

	public long getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(long dateFrom) {
		this.dateFrom = dateFrom;
	}

	public int getRelevanceCount() {
		return relevanceCount;
	}

	public void addRelevanceCount() {
		this.relevanceCount++;
	}


	public void setRelevanceCount(int relevanceCount) {
		this.relevanceCount = relevanceCount;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	

}
