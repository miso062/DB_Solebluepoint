package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class testMain {

static String url;
static int curUserID;

public static void myMovieWatching(Statement stmt) {
	
	ResultSet rs = null;
	
	String sql = "SELECT m.movie_id, m.movie_name "
			+ "FROM movie AS m "
			+ "JOIN store_movie AS sm "
			+ "WHERE sm.kind=2 AND sm.user_id = "+curUserID+" AND sm.movie_id=m.movie_id;";
	   
	try {
		rs = stmt.executeQuery(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	try {
		System.out.println("[보는 중 영화목록]");
		while(rs.next()) {
			System.out.println(rs.getInt("movie_id")+". "+rs.getString("movie_name"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
}

public static void Delete_StoreMovie(Statement stmt, int movie_id) {
	String sql;
	
	//set kind in program
	int kind = 2;
	
	sql = "DELETE FROM store_movie WHERE user_id = "+curUserID+" "
			+ "AND kind = " + kind + 
			" AND movie_id = "+movie_id+";"; 
	
	   try {
		stmt.executeUpdate(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
}

public static void MyCollectionPrint(Statement stmt) {
	ResultSet rs = null;
	
	String sql = "SELECT collection_id, collection_name "
			+ "FROM collection "
			+ "WHERE user_id = " + curUserID +";";
	//is deleted 조건
	   
	try {
		rs = stmt.executeQuery(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	try {
		System.out.println("[내가 만든 컬렉션]");
		while(rs.next()) {
			System.out.println(rs.getInt("collection_id")+". "+rs.getString("collection_name"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void LikeCollectionPrint(Statement stmt) {
ResultSet rs = null;
	
	String sql = "SELECT c.collection_id, c.collection_name "
			+ "FROM collection AS c "
			+ "JOIN collectionLike AS cl "
			+ "WHERE cl.user_id = "+curUserID+" AND c.collection_id = cl.collection_id;";
	//is deleted 조건
	
	try {
		rs = stmt.executeQuery(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	try {
		System.out.println("[좋아요한 컬렉션]");
		while(rs.next()) {
			System.out.println(rs.getInt("collection_id")+". "+rs.getString("collection_name"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

//index 초기화 - 삭제한 경우에 사용 
public static void RearrangeIndex(Statement stmt,String tableName, String indexName) {
	String sql,sql2,sql3;
	sql = "ALTER TABLE collection DROP COLUMN collection_id;";
			 
	sql2 = "ALTER TABLE collection AUTO_INCREMENT=1;";

	sql3 = "ALTER TABLE collection ADD COLUMN collection_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;";
	
	try {
		stmt.executeUpdate(sql);
		stmt.executeUpdate(sql2);
		stmt.executeUpdate(sql3);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
}

public static void MyCommentPrint(Statement stmt) {
	ResultSet rs1 = null;
	ResultSet rs2= null;
	
	String sql1,sql2;
	
	sql1 = "SELECT comment_id, comment_content "
			+ "FROM collectionComments "
			+ "WHERE user_id = " + curUserID +";";
	//is deleted 조건
	
	sql2 = "SELECT comment_id, comment_content "
			+ "FROM movieComments "
			+ "WHERE user_id = " + curUserID +";";
	//is deleted 조건
	   
	try {
		rs1 = stmt.executeQuery(sql1);
		
		System.out.println("[내가 작성한 코멘트(영화)]");
		while(rs1.next()) {
			System.out.println(rs1.getInt("comment_id")+". "+rs1.getString("comment_content"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	try {
		rs2 = stmt.executeQuery(sql2);
		
		System.out.println("[내가 작성한 코멘트(컬렉)]");
		while(rs2.next()) {
			System.out.println(rs2.getInt("comment_id")+". "+rs2.getString("comment_content"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void LikeMovieCommentPrint(Statement stmt) {
	ResultSet rs = null;
	
	String sql;
	
	sql = "select movie.movie_name,movieComments.comment_id,movieComments.comment_content from " + 
			"	movieCommentLike inner join movie on movie.movie_id = movieCommentLike.movie_id " + 
			"    inner join movieComments on movieCommentLike.commnet_id = movieComments.comment_id "
			+ "WHERE movieCommentLike.user_id = " + curUserID +";";
	//is deleted 조건

	   
	try {
		rs = stmt.executeQuery(sql);
		
		System.out.println("[내가 좋아한 코멘트(영화)]");
		while(rs.next()) {
			System.out.println(rs.getInt("comment_id")+". "+rs.getString("comment_content")
			+" - "+rs.getString("movie_name"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

public static void LikeCollectionCommentPrint(Statement stmt) {
	ResultSet rs = null;
	
	String sql;
	
	sql = "select collection.collection_name, collectionComments.comment_id, collectionComments.comment_content from " + 
			"	collectionCommentLike inner join collection on collection.collection_id = collectionCommentLike.collection_id " + 
			"    inner join collectionComments on collectionComments.comment_id = collectionCommentLike.comment_id "
			+ "WHERE collectionCommentLike.user_id = " + curUserID +";";
	//is deleted 조건

	   
	try {
		rs = stmt.executeQuery(sql);
		
		System.out.println("[내가 좋아한 코멘트(컬렉션)]");
		while(rs.next()) {
			System.out.println(rs.getInt("comment_id")+". "+rs.getString("comment_content")
			+" - "+rs.getString("collection_name"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

public static void starDistribution(Statement stmt) {
	ResultSet rs = null;
	int total =0, count = 0;
	int temp;
	float init = (float) 0.5;
	
	int[] star = new int[10];
	
	String sql = "SELECT * "
			+ "FROM evaluate_movie "
			+ "WHERE eval_user_id = " + curUserID +";";
	   
	try {
		rs = stmt.executeQuery(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	try {
		System.out.println("[내 별점 분포]");
		while(rs.next()) {
			 temp = rs.getInt("star_point");
			 
			 if(temp==0.5) {
				 star[0] = ++star[0];
			 }else if(temp==1) {
				 star[1] = ++star[1];
			 }else if(temp==1.5) {
				 star[2] = ++star[2];
			 }else if(temp==2) {
				 star[3] = ++star[3];
			 }else if(temp==2.5) {
				 star[4] = ++star[4];
			 }else if(temp==3) {
				 star[5] = ++star[5];
			 }else if(temp==3.5) {
				 star[6] = ++star[6];
			 }else if(temp==4) {
				 star[7] = ++star[7];
			 }else if(temp==4.5) {
				 star[8] = ++star[8];
			 }else if(temp==5) {
				 star[9] = ++star[9];
			 }
			 
			 total += temp;
			 count++;
		}
		
		
		for(int i=1;i<11;i++) {
			System.out.print(init+": ");

			for(int j=0; j<(int)((float)star[i-1]/(total/count)*20);j++) {
				System.out.print("*");
			}
			System.out.println();
			init+=0.5;
		}
	
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void myMovieinfo(Statement stmt) {
	ResultSet rs = null;
	
	int total =0, count=0;
	
	String sql = "SELECT m.movie_runningTime "
			+ "FROM movie AS m "
			+ "JOIN evaluate_movie AS em "
			+ "WHERE em.eval_user_id = " + curUserID +" AND em.eval_movie_id = m.movie_id;";
	   
	try {
		rs = stmt.executeQuery(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		while(rs.next()) {
			 total += rs.getInt("movie_runningTime");
			 count++;
			 }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println("[평가한 영화 수] : "+count+"회");
	System.out.println("[영화 감상 시간] : 약 " + Math.round((float)total/60) + "시간");
}

public static void myfavMovieActorDirector(Statement stmt) {
	//선호 배우, 감독
	ResultSet rs = null;
	
	List<String> actor_list = new ArrayList<>();
	List<String> dir_list = new ArrayList<>();
	
	List<String> fav_actor_list = new ArrayList<>();
	List<String> fav_dir_list = new ArrayList<>();
	
	String sql = "select person_list.person_name,A.cast_personality from (select cast_list.cast_person_id, cast_list.cast_personality "
			+ "from evaluate_movie " + 
			" inner join cast_list on cast_list.cast_movie_id = evaluate_movie.eval_movie_id" + 
			" where evaluate_movie.star_point > 3.5 and evaluate_movie.eval_user_id = "+ curUserID +") as A" + 
			" inner join person_list on person_list.person_Id = A.cast_person_id;";
	   
	try {
		rs = stmt.executeQuery(sql);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	try {
		while(rs.next()) {
			//인물 성향에 따라 분류 
				if(rs.getInt("cast_personality") == 0){
					actor_list.add(rs.getString("person_name"));
				}
				else {
					dir_list.add(rs.getString("person_name"));
				}
			 }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Random rand = new Random();
	
	if(actor_list.size()/2>0) {
		System.out.println("[선호배우]");
		for(int i=0; i<actor_list.size()/2;i++) {
			int ran = rand.nextInt(actor_list.size());
			
			if(!fav_actor_list.contains(actor_list.get(ran))){
				fav_actor_list.add(actor_list.get(ran));
			}
		}
		for(int i=0; i<fav_actor_list.size();i++) {
			System.out.println(fav_actor_list.get(i));
		}
	}	
	else {
		System.out.println("[선호배우]");
		System.out.println("더 많은 영화 평가가 필요합니다.");
	}
	
	if(dir_list.size()/2>0) {
		System.out.println("[선호감독]");
		for(int i=0; i<dir_list.size()/2;i++) {
			int ran = rand.nextInt(dir_list.size());
		
			if(!fav_dir_list.contains(dir_list.get(ran))){
				fav_dir_list.add(dir_list.get(ran));
			};
		}
		for(int i=0; i<fav_dir_list.size();i++) {
			System.out.println(fav_dir_list.get(i));
		}
	}	
	else {
		System.out.println("[선호감독]");
		System.out.println("더 많은 영화 평가가 필요합니다.");
	}
	
}

public static void myfavMovieTagGenre(Statement stmt) {
	//장르,tag
	
	ResultSet rs_genre = null;
	ResultSet rs_tag = null;
	
	List<String> genre_list = new ArrayList<>();
	List<String> tag_list = new ArrayList<>();
	
	List<String> fav_genre_list = new ArrayList<>();
	List<String> fav_tag_list = new ArrayList<>();
	
	String sql_genre = "select " + 
			"gernre_list.gernre_name " + 
			"from (select tag_genre.* from evaluate_movie " + 
			"inner join tag_genre on evaluate_movie.eval_movie_id = tag_genre.movie_id_for " + 
			"where evaluate_movie.star_point > 3.5 and evaluate_movie.eval_user_id = 0) as A " + 
			"inner join gernre_list on "
			+ "gernre_list.gernre_id = A.genre_id1 or gernre_list.gernre_id = A.genre_id2 or "
			+ "gernre_list.gernre_id = A.genre_id3 or gernre_list.gernre_id = A.genre_id4 or "
			+ "gernre_list.gernre_id = A.genre_id5;" ;
	
	String sql_tag = "select tag.tag_name " + 
			"from (select tag_genre.* from evaluate_movie " + 
			"inner join tag_genre on evaluate_movie.eval_movie_id = tag_genre.movie_id_for " + 
			"where evaluate_movie.star_point > 3.5 and evaluate_movie.eval_user_id = 0) as A " + 
			"inner join tag on tag.tag_id = A.tag_id1 or tag.tag_id = A.tag_id2 "
			+ "or tag.tag_id = A.tag_id3 or tag.tag_id = A.tag_id4 or tag.tag_id = A.tag_id5;";
	
	try {
		rs_genre = stmt.executeQuery(sql_genre);
		
		while(rs_genre.next()) {
			String temp = rs_genre.getString("gernre_name");
				genre_list.add(temp);
			 }
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	try {
		rs_tag = stmt.executeQuery(sql_tag);
		
		while(rs_tag.next()) {
			String temp = rs_tag.getString("tag_name");
				tag_list.add(temp);
		 }
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	Random rand = new Random();
	
	if(genre_list.size()/2>0) {
		System.out.println("[선호장르]");
		for(int i=0; i<genre_list.size()/2;i++) {
			int ran = rand.nextInt(genre_list.size());
			
			if(!fav_genre_list.contains(genre_list.get(ran))){
				fav_genre_list.add(genre_list.get(ran));
			};
		}
		for(int i=0; i<fav_genre_list.size();i++) {
			System.out.println(fav_genre_list.get(i));
		}
	}	
	else {
		System.out.println("[선호장르]");
		System.out.println("더 많은 영화 평가가 필요합니다.");
	}
	
	if(tag_list.size()/2>0) {
		System.out.println("[선호 tag]");
		for(int i=0; i<tag_list.size()/2;i++) {
			int ran = rand.nextInt(tag_list.size());
			if(!fav_tag_list.contains(tag_list.get(ran))){
				fav_tag_list.add(tag_list.get(ran));
			};
		}
		for(int i=0; i<fav_tag_list.size();i++) {
			System.out.println(fav_tag_list.get(i));
		}
	}	
	else {
		System.out.println("[선호 tag]");
		System.out.println("더 많은 영화 평가가 필요합니다.");
	}
	
}

public static void myfavMovieCountry(Statement stmt) {
	//국가
	
	ResultSet rs_country = null;
	
	List<String> country_list = new ArrayList<>();

	List<String> fav_country_list = new ArrayList<>();
	
	String sql = "select movie.movie_country from evaluate_movie" + 
			"    inner join movie on evaluate_movie.eval_movie_id = movie.movie_id" + 
			"    where evaluate_movie.star_point > 3.5 and evaluate_movie.eval_user_id = 0;";
	
	try {
		rs_country = stmt.executeQuery(sql);
		
		while(rs_country.next()) {
			String temp = rs_country.getString("movie_country");
			country_list.add(temp);
			 }
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	Random rand = new Random();
	
	if(country_list.size()/2>0) {
		System.out.println("[선호국가]");
		for(int i=0; i<country_list.size()/2;i++) {
			int ran = rand.nextInt(country_list.size());
			
			if(!fav_country_list.contains(country_list.get(ran))){
				fav_country_list.add(country_list.get(ran));
			};
		}
		for(int i=0; i<fav_country_list.size();i++) {
			System.out.println(fav_country_list.get(i));
		}
	}	
	else {
		System.out.println("[선호국가]");
		System.out.println("더 많은 영화 평가가 필요합니다.");
	}
	
}

public static void main(String[] args) {

	Connection conn = null;

    Statement stmt = null;

    PreparedStatement pstmt = null;

    ResultSet rs = null;
    
    //현재 user 지정
    curUserID = 0;

    try{

    Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); // JDBC 드라이버 로드

        conn = DriverManager.getConnection("jdbc:mysql://141.223.207.26/DBclass", "yc12312", "yc9639631!");//URL,UID,PWD        

        if(conn==null){

            System.out.println("연결실패");

        }else{

//            System.out.println("연결성공");
            
            stmt = conn.createStatement();
            
//            myMovieWatching(stmt);
          
//            Delete_StoreMovie(stmt,2);
            
//            MyCollectionPrint(stmt);
            
//            LikeCollectionPrint(stmt);
            
//            RearrangeIndex(stmt,"collection","collection_id");
            
//            MyCommentPrint(stmt);
            
//            starDistribution(stmt);
            
//            myMovieinfo(stmt);
            
//            LikeMovieCommentPrint(stmt);
            
//            LikeCollectionCommentPrint(stmt);
            
//            myfavMovieActorDirector(stmt);
            
//            myfavMovieTagGenre(stmt);
            
//            myfavMovieCountry(stmt);
            }

        }catch(ClassNotFoundException ce){

        ce.printStackTrace();            

    }catch(SQLException se){

        se.printStackTrace();    

    }catch(Exception e){

        e.printStackTrace();

    }finally{

        try{ // 연결 해제(한정돼 있으므로)

            if(rs!=null){        rs.close();            }

            if(pstmt!=null){    pstmt.close();        }

            if(stmt!=null){    stmt.close();        }

            if(conn!=null){    conn.close();        }

        }catch(SQLException se2){

            se2.printStackTrace();

            }

        }

    }

}
