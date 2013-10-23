package com.clipomatiq.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.clipomatiq.Genre;
import com.clipomatiq.youtube.YTEntry;
import com.mysql.jdbc.PreparedStatement;

public class DBProcessor {
 
	
	public static HashMap<String,Integer> clipsNum=new HashMap<String, Integer>();

	
	public static void addArtists(int genre, ArrayList artists) {
		//int genreId = addGenre(genre, 0);
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql="insert into artists(name, description, genre_id) VALUES (?, null,?)";
		int artistId;
		try {
			String artist;
			ps = (PreparedStatement) DBConnector.getInstance().getConnection().prepareStatement(sql);

			for(int i=0;i<artists.size();i++){
				artist=(String) artists.get(i);
				//artist=artist.replace("'", "''");
				if(artist==null)continue;
				artist=artist.trim();
				artistId=DBProcessor.getArtistByName(artist, genre);
				if(artistId!=-1) continue;
				ps.setString(1, artist);
				ps.setInt(2, genre);

				ps.executeUpdate();
				System.out.println("Add a new artist: "+artists.get(i));	
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		
	}

	public static void addClips(int artistId, ArrayList clips) {
		//int genreId = addGenre(genre, 0);
		String sql = "INSERT INTO clips " +
		"( artist_id, title, content, youtube_id, " +
		"average_cnt, media_url, average_rating, comments_url, " +
		"view_cnt, favorite_cnt, thumb_url, duration, keywords, published, status, relevance_count)" +
		" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			YTEntry clip;
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
			for(int i=0;i<clips.size();i++){
				try {
					
				clip=(YTEntry) clips.get(i);
				//artist=artist.replace("'", "''");
				//if(artist==null)continue;
				System.out.println("Try to add  new clip: "+clip.getTitle()+" # "+i+" with id="+clip.getId());	

			ps.setInt(1, artistId);
			ps.setString(2, clip.getTitle());
			ps.setString(3, clip.getContent());

			ps.setString(4, clip.getId());
			ps.setFloat(5, clip.getAverageCount());
			ps.setString(6, clip.getMediaUrl());			
			ps.setFloat(7, clip.getAverageRating());
			ps.setString(8, clip.getCommentsLink());			
			ps.setLong(9, clip.getViewCount());
			ps.setLong(10, clip.getFavCount());
			ps.setString(11, clip.getThumb());			
			ps.setInt(12, clip.getDuration());
			ps.setString(13, clip.getKeywoords());
			ps.setDate(14, new Date(clip.getDateFrom()));
			ps.setInt(15, 0);
			ps.setInt(16, clip.getRelevanceCount());
			ps.executeUpdate();
			
				} catch (Exception e) {
					e.printStackTrace();
					
				}

			}
			
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		
	}


	
	public static int removeClip(int clipId) {
		//int genreId = addGenre(genre, 0);
		String sql = "delete from clips " +
		"where" +
		" id=? ";
		ResultSet rs = null;
		PreparedStatement ps = null;
		int result=0;
		try {
			YTEntry clip;
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);

			ps.setInt(1, clipId);
			//ps.setString(2, clip.getTitle());
			result=ps.executeUpdate(); 
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return result;
	}

	public static int removeClips(String utubeId) {
		//int genreId = addGenre(genre, 0);
		String sql = "delete from clips " +
		"where" +
		" youtube_id=? ";
		ResultSet rs = null;
		PreparedStatement ps = null;
		int result=0;
		try {
			YTEntry clip;
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);

			ps.setString(1, utubeId);
			result=ps.executeUpdate(); 
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return result;
	}
	
	
	public static ArrayList getClips(int artistId, boolean orderByACnt, int start) {
		//int genreId = addGenre(genre, 0);
//		String sql = "select " +
//		"id, title, content, youtube_id, " +
//		"average_cnt, media_url, average_rating, comments_url, " +
//		"view_cnt, favorite_cnt, thumb_url, duration, keywords, status, published, relevance_count" +
//		" FROM" +
//		"	clips" +
//		" WHERE" +
//		"	artist_id=? and relevance_count>0 " +
//		"ORDER by  relevance_count desc, "+(orderByACnt?"average_cnt desc":"view_cnt desc")+ " LIMIT 100,"+start;
		String sql = "select " +
		"c.id, c.title, c.content, c.youtube_id, " +
		"c.average_cnt, c.media_url, c.average_rating, c.comments_url, " +
		"c.view_cnt, c.favorite_cnt, c.thumb_url, c.duration,   c.relevance_count,a.name" +
		" FROM" +
		"	clips c, artists a, genries g" +
		" WHERE" +
		"	c.artist_id=a.id and a.genre_id=g.id   and relevance_count>0 and g.id=? " +
		"ORDER by  relevance_count desc, "+(orderByACnt?"a.name desc":"view_cnt desc")+ " LIMIT "+start+", 30";


		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList clips= new ArrayList();
		try {
			YTEntry clip;
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, artistId);
			rs = ps.executeQuery();
			while(rs.next()){
				clip = new YTEntry();
				clip.setAverageCount(rs.getFloat(5));
				clip.setAverageRating(rs.getFloat(7));
				clip.setCommentsLink(rs.getString(8));
				clip.setContent(rs.getString(3));
				clip.setDuration(rs.getInt(12));
				clip.setFavCount(rs.getLong(10));
				clip.setId(rs.getString(4));
				clip.setUid(rs.getString(1));
				//clip.setKeywoords(rs.getString(13));
				clip.setMediaUrl(rs.getString(6));
				clip.setThumb(rs.getString(11));
				clip.setTitle(rs.getString(2));
				clip.setViewCount(rs.getLong(9), true);
				clip.setArtistName(rs.getString(14));
				clip.setRelevanceCount(rs.getInt(13));
				clips.add(clip);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return clips;
	}
	
	
	public static ArrayList getArtists(int genreId) {
		//int genreId = addGenre(genre, 0);
		String sql = "select " +
		"id,name " +
		" FROM" +
		"	artists" +
		" WHERE" +
		"	genre_id=? and status=0 " ;//+
		//"ORDER by  relevance_count desc, "+(orderByACnt?"average_cnt desc":"view_cnt desc")+ " LIMIT 100";
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList clips= new ArrayList();
		try {
			Object[] artist;
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, genreId);
			rs = ps.executeQuery();
			while(rs.next()){
				artist = new Object[2];
				artist[0]=rs.getInt(1);
				artist[1]=rs.getString(2);
				clips.add(artist);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return clips;
	}
	
	public static int getArtistByName(String name, int genreId) {
		//int genreId = addGenre(genre, 0);
		String sql = "select " +
		"artists.id " +
		" FROM" +
		"	artists" +
		" WHERE" +
		"	artists.name=? and artists.genre_id=? " ;//+
		//"ORDER by  relevance_count desc, "+(orderByACnt?"average_cnt desc":"view_cnt desc")+ " LIMIT 100";
		ResultSet rs = null;
		PreparedStatement ps = null;
		int artistId=-1;

		try {
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
			ps.setString(1, name);
			ps.setInt(2, genreId);
			rs = ps.executeQuery();
			if(rs.next()){
				artistId=rs.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return artistId;
	}
	
	public static String[] getGenreByName(String genreId) {
		//int genreId = addGenre(genre, 0);
		String[] genre=new String[2];
		String sql = "select " +
		"name, description " +
		" FROM" +
		"	genries" +
		" WHERE" +
		"	id=? " ;//+
		//"ORDER by  relevance_count desc, "+(orderByACnt?"average_cnt desc":"view_cnt desc")+ " LIMIT 100";
		ResultSet rs = null;
		PreparedStatement ps = null;
		int artistId=-1;

		try {
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
			ps.setString(1, genreId);
			rs = ps.executeQuery();
			if(rs.next()){
				genre[0]=rs.getString(1);
				genre[1]=rs.getString(2);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return genre;
	}
	public static void updateArtistStatus( int artistId) {

		Statement ps = null;
		try {
			ps = DBConnector.getInstance().getConnection().createStatement();
			ps.executeUpdate(
					"update artists set status=1 where id=" + artistId + "; ");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}

	}
	
	public static void updateGenreRating( String genreId) {

		Statement ps = null;
		try {
			ps = DBConnector.getInstance().getConnection().createStatement();
			ps.executeUpdate(
					"update genries set req_count=req_count+1 where id=" + genreId + "; ");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}

	}
	public static int updateClipRating( String clipId,int rating) {

		Statement ps = null;
		int result=-1;
		try {
			ps = DBConnector.getInstance().getConnection().createStatement();
			result = ps.executeUpdate(
					"update clips set relevance_count="+rating+" where id=" + clipId + "; ");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return result;
	}
	
	public static int addGenre(String genre, int parentId) {
		ResultSet rs = null;
		Statement ps = null;
		try {
			ps = DBConnector.getInstance().getConnection().createStatement();
			ps.executeUpdate(
					"insert into genries(name, description, parent_id) VALUES ('"
							+ genre + "', null," + parentId + ") ",
					Statement.RETURN_GENERATED_KEYS);
			rs = ps.getGeneratedKeys();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt(1);
			} else {
				// throw an exception from here
			}
			rs.close();
			rs = null;
			System.out.println("Key returned from add Genre:" + id);
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return -1;
	}
	
	
	public static int getNumRecsInGenre(String genreId){
		
		if(genreId.length()==0)return 200000;
		String sql  = "select count(c.id)" +
		" FROM" +
		"	clips c, artists a" +
		" where a.genre_id=? and c.artist_id=a.id and c.relevance_count>=1;" ;//+
		//"ORDER by  relevance_count desc, "+(orderByACnt?"average_cnt desc":"view_cnt desc")+ " LIMIT 100";
		ResultSet rs = null;
		PreparedStatement ps = null;
		Genre genre;
		ArrayList clips= new ArrayList();
		try {
			
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
			ps.setString(1, genreId);
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return 0;
		
	}

	
	public static ArrayList getGenries(){
		String sql  = "select " +
		"id, name, req_count " +
		" FROM" +
		"	genries" +
		" ORDER by name" ;//+
		//"ORDER by  relevance_count desc, "+(orderByACnt?"average_cnt desc":"view_cnt desc")+ " LIMIT 100";
		ResultSet rs = null;
		PreparedStatement ps = null;
		Genre genre;
		ArrayList clips= new ArrayList();
		try {
			
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			while(rs.next()){
				genre = new Genre();
				genre.id=rs.getInt(1);
				genre.name=rs.getString(2);
				genre.reqCount=rs.getInt(3);
				clips.add(genre);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return clips;
	}
	
	public static int[] getMinMaxRate4Genre(){
		String sql  = "select " +
		"max(req_count), min(req_count) " +
		" FROM" +
		"	genries" ;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Genre genre;
		int result[]=new int[2];
		try {
			
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			if(rs.next()){
				result[0]=rs.getInt(1);
				result[1]=rs.getInt(2);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return result;
	}
	
	public static int getTotalClips(){
		String sql  = "select " +
		"count(id) " +
		" FROM" +
		"	clips " +
		" where" +
		" 	relevance_count>=4" ;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Genre genre;
		int result=0;
		try {
			
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			if(rs.next()){
				result=rs.getInt(1);

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		return result;
	}
	
	public static YTEntry getClip(ArrayList viewedClips, int viewedArtistId,String genreId) {
/*
		"	c.id, c.title, c.content, c.youtube_id, " +
		"	c.average_cnt, c.media_url, c.average_rating, c.comments_url, " +
		"	c.view_cnt, c.favorite_cnt, c.thumb_url, c.duration, c.keywords," +
		"	c.status, c.published, c.relevance_count, c.artist_id " +
*/
		String sql = 
			"select  " +
			"	c.title, c.youtube_id, " +
			"	 " +
			"	 " +
			"	 c.relevance_count, c.artist_id, c.media_url " +
			" from " +
			"	clips as c, artists as a, genries as g" +
			" where " +
			"	c.relevance_count>=?" +
			" and" +
			"	c.youtube_id not in (?) " +
			" and c.artist_id=a.id " +
			" and a.id<>? " +
			" and a.genre_id=g.id" +
//			" and ? in (g.id,g.parent_id)" +
			" and ? = g.id" +
			" order by " +
			"	rand()" +
			" limit" +
			"	1;";
		
		String sql2="select mod( rand()*100, max(c.relevance_count))  from clips as c, artists as a, genries as g " +
				"where c.artist_id=a.id and a.genre_id=g.id and ? in (g.id,g.parent_id)";
		ResultSet rs = null;
		PreparedStatement ps = null;
	//	ArrayList clips= new ArrayList();
		String result="";
		YTEntry clip=null;
		try {
			ps=(PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql2);
			ps.setString(1, genreId);
			rs = ps.executeQuery();
			int rnd=0;
			if(rs.next()){
				rnd = rs.getInt(1);
			}
			if(rnd<0)rnd=0;
			ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
			int size = viewedClips.size();
			String filteredClips="";
			for(int i=0; i<size;i++){
				if(i>0)filteredClips+=",";
				filteredClips+="'"+viewedClips.get(i)+"'";
			}
			ps.setInt(1, rnd>2?rnd:3);

			ps.setString(2, filteredClips);
			ps.setInt(3, viewedArtistId);
			ps.setString(4, genreId);
			rs = ps.executeQuery();
			while(rs.next()){
				//result=rs.getString(1);
				clip = new YTEntry();
				//clip.setAverageCount(rs.getFloat(5));
				//clip.setAverageRating(rs.getFloat(7));
				//clip.setCommentsLink(rs.getString(8));
				//clip.setContent(rs.getString(3));
				//clip.setDuration(rs.getInt(12));
				//clip.setFavCount(rs.getLong(10));
				clip.setId(rs.getString(2));
				//clip.setKeywoords(rs.getString(13));
				clip.setMediaUrl(rs.getString(5));
				//clip.setThumb(rs.getString(11));
				clip.setTitle(rs.getString(1));
				//clip.setViewCount(rs.getLong(9), true);
				//clip.setDateFrom(rs.getDate(15).getTime());
				clip.setRelevanceCount(rs.getInt(3));
				clip.artistId=rs.getInt(4);
				//clips.add(clip);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					// ignore
				}
			}
		}
		
		
		return clip;
	}


	public static ArrayList<YTEntry> getRndClips(int count) {
		return getRndClips(new ArrayList(),0,"1",count);
	}

public static ArrayList<YTEntry> getRndClips1(ArrayList viewedClips, int viewedArtistId,String genreId,int count) {
	/*
			"	c.id, c.title, c.content, c.youtube_id, " +
			"	c.average_cnt, c.media_url, c.average_rating, c.comments_url, " +
			"	c.view_cnt, c.favorite_cnt, c.thumb_url, c.duration, c.keywords," +
			"	c.status, c.published, c.relevance_count, c.artist_id " +
	
			String sql = 
				"select  " +
				"	c.title, c.youtube_id, " +
				"	 c.relevance_count, c.artist_id, " +
				"	 c.media_url, c.thumb_url" +
				" from " +
				"	clips as c, artists as a, genries as g" +
				" where " +
				"	c.relevance_count>=?" +
				" and" +
				"	c.youtube_id not in (?) " +
				" and c.artist_id=a.id " +
				//" and a.id<>? " +
				" and a.genre_id=g.id" +
//				" and ? in (g.id,g.parent_id)" +
				(genreId==""?" ":" and ? = g.id") +
				" order by " +
				"	rand()" +
				" limit " +count;
				//"	6;";
*/
			
			int rnd;
			int cnt;
			
			if(clipsNum.get(genreId)==null){
				cnt = getNumRecsInGenre(genreId);
				clipsNum.put(genreId, cnt);
			}else
				cnt=clipsNum.get(genreId);
			
			
			
			String sql = 
				"select  " +
				"	c.title, c.youtube_id, " +
				"	 c.relevance_count, c.artist_id, " +
				"	 c.media_url, c.thumb_url" +
				" from " +
				"	clips as c, artists as a" +
				" where " +
				"	c.relevance_count>=1" +
				(genreId==""?" ":" and ? = a.genre_id") +
//				"	c.youtube_id not in (?) " +
				" and c.artist_id=a.id " +
				" limit ?, 1;";
				//"	6;";

			
			
			String sql2="select mod( rand()*100, max(c.relevance_count))  from clips as c, artists as a, genries as g " +
					"where c.artist_id=a.id and a.genre_id=g.id and ? in (g.id,g.parent_id)";
			ResultSet rs = null;
			PreparedStatement ps = null;
		//	ArrayList clips= new ArrayList();
			String result="";
			YTEntry clip=null;
			ArrayList<YTEntry>clips=new ArrayList<YTEntry>();
			try {
				ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
//				int size = viewedClips.size();
//				String filteredClips="";
//				for(int i=0; i<size;i++){
//					if(i>0)filteredClips+=",";
//					filteredClips+="'"+viewedClips.get(i)+"'";
//				}
//				ps.setString(2, filteredClips);
//				ps.setInt(3, viewedArtistId);
				int rndPos=1;
				if(genreId!=""){
					ps.setString(1, genreId);
					rndPos=2;
				}
				for(int k=0;k<count;k++){
			
					rnd=(int) Math.round(Math.random()*cnt);
					ps.setInt(rndPos, rnd);
				rs = ps.executeQuery();
				if(rs.next()){
					//result=rs.getString(1);
					clip = new YTEntry();
					//clip.setAverageCount(rs.getFloat(5));
					//clip.setAverageRating(rs.getFloat(7));
					//clip.setCommentsLink(rs.getString(8));
					//clip.setContent(rs.getString(3));
					//clip.setDuration(rs.getInt(12));
					//clip.setFavCount(rs.getLong(10));
					clip.setId(rs.getString(2));
					//clip.setKeywoords(rs.getString(13));
					clip.setMediaUrl(rs.getString(5));
					clip.setThumb(rs.getString(6));
					clip.setTitle(rs.getString(1));
					//clip.setViewCount(rs.getLong(9), true);
					//clip.setDateFrom(rs.getDate(15).getTime());
					clip.setRelevanceCount(rs.getInt(3));
					clip.artistId=rs.getInt(4);
					clips.add(clip);
				}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException ex) {
						// ignore
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException ex) {
						// ignore
					}
				}
			}
			
			
			return clips;
		}




public static ArrayList<YTEntry> getRndClips(ArrayList viewedClips, int viewedArtistId,String genreId,int count) {
	/*
			"	c.id, c.title, c.content, c.youtube_id, " +
			"	c.average_cnt, c.media_url, c.average_rating, c.comments_url, " +
			"	c.view_cnt, c.favorite_cnt, c.thumb_url, c.duration, c.keywords," +
			"	c.status, c.published, c.relevance_count, c.artist_id " +
	
			String sql = 
				"select  " +
				"	c.title, c.youtube_id, " +
				"	 c.relevance_count, c.artist_id, " +
				"	 c.media_url, c.thumb_url" +
				" from " +
				"	clips as c, artists as a, genries as g" +
				" where " +
				"	c.relevance_count>=?" +
				" and" +
				"	c.youtube_id not in (?) " +
				" and c.artist_id=a.id " +
				//" and a.id<>? " +
				" and a.genre_id=g.id" +
//				" and ? in (g.id,g.parent_id)" +
				(genreId==""?" ":" and ? = g.id") +
				" order by " +
				"	rand()" +
				" limit " +count;
				//"	6;";
*/
			
			
			int cnt;

			
			String sql = 
				"select  " +
				"	c.title, c.youtube_id, " +
				"	 c.relevance_count, c.artist_id, " +
				"	 c.media_url, c.thumb_url" +
				" from " +
				"	clips as c, artists as a" +
				" where " +
				"	c.relevance_count>=2" +
//				" and" +
//				"	c.youtube_id not in (?) " +
				" and c.artist_id=a.id " +
				//" and a.id<>? " +
//				" and a.genre_id=g.id" +
//				" and ? in (g.id,g.parent_id)" +
				(genreId==""?" ":" and ? = a.genre_id") +
				" order by " +
				"	rand()" +
				" limit " +count;//+", 1;";
				//"	6;";

			
			
			String sql2="select mod( rand()*100, max(c.relevance_count))  from clips as c, artists as a " +
					"where c.artist_id=a.id and a.genre_id=?";
			ResultSet rs = null;
			PreparedStatement ps = null;
		//	ArrayList clips= new ArrayList();
			String result="";
			YTEntry clip=null;
			ArrayList<YTEntry>clips=new ArrayList<YTEntry>();
			try {
				ps=(PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql2);
				ps.setString(1, genreId);
				rs = ps.executeQuery();
				int rnd=0;
				if(rs.next()){
					rnd = rs.getInt(1);
				}
				if(rnd<0)rnd=0;
				ps = (PreparedStatement)DBConnector.getInstance().getConnection().prepareStatement(sql);
				int size = viewedClips.size();
				String filteredClips="";
				for(int i=0; i<size;i++){
					if(i>0)filteredClips+=",";
					filteredClips+="'"+viewedClips.get(i)+"'";
				}
				//ps.setInt(1, rnd>3?rnd:4);
				//ps.setString(2, filteredClips);
				//ps.setInt(3, viewedArtistId);
				if(genreId!="")
					ps.setString(1, genreId);
				rs = ps.executeQuery();
				while(rs.next()){
					//result=rs.getString(1);
					clip = new YTEntry();
					//clip.setAverageCount(rs.getFloat(5));
					//clip.setAverageRating(rs.getFloat(7));
					//clip.setCommentsLink(rs.getString(8));
					//clip.setContent(rs.getString(3));
					//clip.setDuration(rs.getInt(12));
					//clip.setFavCount(rs.getLong(10));
					clip.setId(rs.getString(2));
					//clip.setKeywoords(rs.getString(13));
					clip.setMediaUrl(rs.getString(5));
					clip.setThumb(rs.getString(6));
					clip.setTitle(rs.getString(1));
					//clip.setViewCount(rs.getLong(9), true);
					//clip.setDateFrom(rs.getDate(15).getTime());
					clip.setRelevanceCount(rs.getInt(3));
					clip.artistId=rs.getInt(4);
					clips.add(clip);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException ex) {
						// ignore
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException ex) {
						// ignore
					}
				}
			}
			
			
			return clips;
		}



}
