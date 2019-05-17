import java.sql.*;

public class testMain1 {

	static String url;
	static int curUserID;

	public static void showProfile(Statement stmt) {
		ResultSet rs = null;
		
		String sql = "SELECT u.User_Id, u.User_name, u.Intro, u.Partener_User_id "
				+ "FROM UserList AS u "
				+ "WHERE u.User_Id = "+curUserID+";";
		
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			System.out.println("[프로필 보기]");
			while(rs.next()) {
				System.out.println(rs.getInt("User_Id")+". "+rs.getString("User_name")+". "+rs.getString("Intro")
					+countFollowedList(stmt, curUserID)+countFollowingList(stmt, curUserID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editProfile(Statement stmt, String Intro){
		ResultSet rs = null;
		
		String sql = "UPDATE u.Intro="+Intro+" "
				+ "FROM UserList AS u "
				+ "WHERE u.User_Id = "+curUserID+";";
		
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			System.out.println("[프로필 수정]");
			while(rs.next()) {
				System.out.println(rs.getInt("User_Id")+". "+rs.getString("User_name")+". "+rs.getString("Intro")
					+countFollowedList(stmt, curUserID)+countFollowingList(stmt, curUserID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int countFollowingList(Statement stmt, int userId) {
		ResultSet rs = null;
		int followingCount = 0;
		
		String sql = "SELECT f.Followed_User_Id, u.User_name, u.Intro "
				+ "FROM followList AS f "
				+ "JOIN UserList AS u "
				+ "WHERE f.Following_User_Id = "+userId+" AND f.Followed_User_ID = u.User_Id;";
		
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			while(rs.next()) {
				followingCount++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return followingCount;
	}

	public static int countFollowedList(Statement stmt, int userId) {
		ResultSet rs = null;
		int followedCount = 0;
		
		String sql = "SELECT f.Following_User_Id, u.User_name, u.Intro "
				+ "FROM followList AS f "
				+ "JOIN UserList AS u "
				+ "WHERE f.Followed_User_Id = "+userId+" AND f.Following_User_ID = u.User_Id;";
		
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			while(rs.next()) {
				followedCount++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return followedCount;
	}
	
	// 프로필을 확인하고자 하는 유저의 아이디를 기억하고 전달하여 그 값에 맞는 follow 목록을 불러옴!
	public static void followingListPrint(Statement stmt, int userId) {
		ResultSet rs = null;
		
		String sql = "SELECT f.Followed_User_Id, u.User_name, u.Intro "
				+ "FROM followList AS f "
				+ "JOIN UserList AS u "
				+ "WHERE f.Following_User_Id = "+userId+" AND f.Followed_User_ID = u.User_Id;";
		
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			System.out.println("[팔로잉 목록]");
			while(rs.next()) {
				System.out.println(rs.getInt("Followed_User_Id")+". "+rs.getString("User_name")+". "+rs.getString("Intro"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void followedListPrint(Statement stmt, int userId) {
		ResultSet rs = null;
		
		String sql = "SELECT f.Following_User_Id, u.User_name, u.Intro "
				+ "FROM followList AS f "
				+ "JOIN UserList AS u "
				+ "WHERE f.Followed_User_Id = "+userId+" AND f.Following_User_ID = u.User_Id;";
		
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			System.out.println("[팔로워 목록]");
			while(rs.next()) {
				System.out.println(rs.getInt("Following_User_Id")+". "+rs.getString("User_name")+". "+rs.getString("Intro"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void myMovieEvaluating(Statement stmt) {
		ResultSet rs = null;
		
		String sql = "SELECT m.movie_id, m.movie_name "
				+ "FROM movie AS m "
				+ "JOIN evaluate_movie AS em "
				+ "WHERE em.eval_user_id = "+curUserID+" AND em.eval_movie_id=m.movie_id;";
		   
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			System.out.println("[평가한 영화목록]");
			while(rs.next()) {
				System.out.println(rs.getInt("movie_id")+". "+rs.getString("movie_name")+rs.getFloat("movie_star"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void myMovieWantToWatching(Statement stmt) {
		
		ResultSet rs = null;
		
		String sql = "SELECT m.movie_id, m.movie_name "
				+ "FROM movie AS m "
				+ "JOIN store_movie AS sm "
				+ "WHERE sm.kind=0 AND sm.user_id = "+curUserID+" AND sm.movie_id=m.movie_id;";
		   
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			System.out.println("[보고싶어요 영화목록]");
			while(rs.next()) {
				System.out.println(rs.getInt("movie_id")+". "+rs.getString("movie_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void myMovieDontInteresting(Statement stmt) {
		
		ResultSet rs = null;
		
		String sql = "SELECT m.movie_id, m.movie_name "
				+ "FROM movie AS m "
				+ "JOIN store_movie AS sm "
				+ "WHERE sm.kind=1 AND sm.user_id = "+curUserID+" AND sm.movie_id=m.movie_id;";
		   
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		try {
			System.out.println("[관심없어요 영화목록]");
			while(rs.next()) {
				System.out.println(rs.getInt("movie_id")+". "+rs.getString("movie_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void Delete_EvaluateMovie(Statement stmt, int movie_id) {
		String sql;
		
		sql = "DELETE FROM store_movie WHERE user_id = "+curUserID+" "
				+ " AND movie_id = "+movie_id+";"; 
		
		   try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	    conn = DriverManager.getConnection("jdbc:mysql://172.17.222.9:3306/DBclass?serverTimezone=UTC",
	    		"yc12312", "yc9639631!");//URL,UID,PWD
	    if(conn==null){
	        System.out.println("연결실패");
	    }else{
	        System.out.println("연결성공");
	        stmt = conn.createStatement();
	        
	        myMovieEvaluating(stmt);
	        myMovieWantToWatching(stmt);
	        myMovieDontInteresting(stmt);
	        //Delete_EvaluateMovie(stmt,1);
	        followedListPrint(stmt, 1);
	        followingListPrint(stmt, 1);
	        showProfile(stmt);
	        //editProfile(stmt, "great");
	        
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
