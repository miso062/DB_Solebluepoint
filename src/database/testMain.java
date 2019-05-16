package database;

import java.sql.*;

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
		
		System.out.println("[내가 좋아한 코멘트(컬렉)]");
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
	
	int[] star = new int[5];
	
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
			 switch(temp) {
			 case 1 :
				 star[0] = ++star[0];
				break;
			 case 2 :
				 star[1] = ++star[1];
				break;
			 case 3 :
				 star[2] = ++star[2];
				break;
			 case 4 :
				 star[3] = ++star[3];
				break;
			 case 5 :
				 star[4] = ++star[4];
				break;
			 }
			 total += temp;
			 count++;
		}
		
		
		for(int i=1;i<6;i++) {
			System.out.print(i+": ");

			for(int j=0; j<(int)((float)star[i-1]/(total/count)*20);j++) {
				System.out.print("*");
			}
			System.out.println();
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

public static void myfavMovieinfo(Statement stmt) {
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

public static void main(String[] args) {

	Connection conn = null;

    Statement stmt = null;

    PreparedStatement pstmt = null;

    ResultSet rs = null;
    
    //현재 user 지정
    curUserID = 1;

    try{

    Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); // JDBC 드라이버 로드

        conn = DriverManager.getConnection("jdbc:mysql://172.17.222.109:3306/DBclass", "yc12312", "yc9639631!");//URL,UID,PWD        

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
