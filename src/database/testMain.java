package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class testMain {

	static String url;
	static int curUserID;

	public static void myMovieWatching(Statement stmt) {

		ResultSet rs = null;

		String sql = "SELECT m.movie_id, m.movie_name " + "FROM movie AS m " + "JOIN store_movie AS sm "
				+ "WHERE sm.kind=2 AND sm.user_id = " + curUserID + " AND sm.movie_id=m.movie_id;";

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("[보는 중 영화목록]");
			while (rs.next()) {
				System.out.println(rs.getInt("movie_id") + ". " + rs.getString("movie_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void Delete_StoreMovie(Statement stmt, int movie_id) {
		String sql;

		// set kind in program
		int kind = 2;

		sql = "DELETE FROM store_movie WHERE user_id = " + curUserID + " " + "AND kind = " + kind + " AND movie_id = "
				+ movie_id + ";";

		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void MyCollectionPrint(Statement stmt) {
		ResultSet rs = null;

		String sql = "SELECT collection_id, collection_name " + "FROM collection " + "WHERE user_id = " + curUserID
				+ ";";
		// is deleted 조건

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("[내가 만든 컬렉션]");
			while (rs.next()) {
				System.out.println(rs.getInt("collection_id") + ". " + rs.getString("collection_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LikeCollectionPrint(Statement stmt) {
		ResultSet rs = null;

		String sql = "SELECT c.collection_id, c.collection_name " + "FROM collection AS c "
				+ "JOIN collectionLike AS cl " + "WHERE cl.user_id = " + curUserID
				+ " AND c.collection_id = cl.collection_id;";
		// is deleted 조건

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("[좋아요한 컬렉션]");
			while (rs.next()) {
				System.out.println(rs.getInt("collection_id") + ". " + rs.getString("collection_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// index 초기화 - 삭제한 경우에 사용
	public static void RearrangeIndex(Statement stmt, String tableName, String indexName) {
		String sql, sql2, sql3;
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
		ResultSet rs2 = null;

		String sql1, sql2;

		sql1 = "SELECT comment_id, comment_content " + "FROM collectionComments " + "WHERE user_id = " + curUserID
				+ ";";
		// is deleted 조건

		sql2 = "SELECT comment_id, comment_content " + "FROM movieComments " + "WHERE user_id = " + curUserID + ";";
		// is deleted 조건

		try {
			rs1 = stmt.executeQuery(sql1);

			System.out.println("[내가 작성한 코멘트(영화)]");
			while (rs1.next()) {
				System.out.println(rs1.getInt("comment_id") + ". " + rs1.getString("comment_content"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			rs2 = stmt.executeQuery(sql2);

			System.out.println("[내가 작성한 코멘트(컬렉)]");
			while (rs2.next()) {
				System.out.println(rs2.getInt("comment_id") + ". " + rs2.getString("comment_content"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LikeMovieCommentPrint(Statement stmt) {
		ResultSet rs = null;

		String sql;

		sql = "select movie.movie_name,movieComments.comment_id,movieComments.comment_content from "
				+ "	movieCommentLike inner join movie on movie.movie_id = movieCommentLike.movie_id "
				+ "    inner join movieComments on movieCommentLike.commnet_id = movieComments.comment_id "
				+ "WHERE movieCommentLike.user_id = " + curUserID + ";";
		// is deleted 조건

		try {
			rs = stmt.executeQuery(sql);

			System.out.println("[내가 좋아한 코멘트(영화)]");
			while (rs.next()) {
				System.out.println(rs.getInt("comment_id") + ". " + rs.getString("comment_content") + " - "
						+ rs.getString("movie_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void LikeCollectionCommentPrint(Statement stmt) {
		ResultSet rs = null;

		String sql;

		sql = "select collection.collection_name, collectionComments.comment_id, collectionComments.comment_content from "
				+ "	collectionCommentLike inner join collection on collection.collection_id = collectionCommentLike.collection_id "
				+ "    inner join collectionComments on collectionComments.comment_id = collectionCommentLike.comment_id "
				+ "WHERE collectionCommentLike.user_id = " + curUserID + ";";
		// is deleted 조건

		try {
			rs = stmt.executeQuery(sql);

			System.out.println("[내가 좋아한 코멘트(컬렉)]");
			while (rs.next()) {
				System.out.println(rs.getInt("comment_id") + ". " + rs.getString("comment_content") + " - "
						+ rs.getString("collection_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void starDistribution(Statement stmt) {
		ResultSet rs = null;
		int total = 0, count = 0;
		int temp;

		int[] star = new int[5];

		String sql = "SELECT * " + "FROM evaluate_movie " + "WHERE eval_user_id = " + curUserID + ";";

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("[내 별점 분포]");
			while (rs.next()) {
				temp = rs.getInt("star_point");
				switch (temp) {
				case 1:
					star[0] = ++star[0];
					break;
				case 2:
					star[1] = ++star[1];
					break;
				case 3:
					star[2] = ++star[2];
					break;
				case 4:
					star[3] = ++star[3];
					break;
				case 5:
					star[4] = ++star[4];
					break;
				}
				total += temp;
				count++;
			}

			for (int i = 1; i < 6; i++) {
				System.out.print(i + ": ");

				for (int j = 0; j < (int) ((float) star[i - 1] / (total / count) * 20); j++) {
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

		int total = 0, count = 0;

		String sql = "SELECT m.movie_runningTime " + "FROM movie AS m " + "JOIN evaluate_movie AS em "
				+ "WHERE em.eval_user_id = " + curUserID + " AND em.eval_movie_id = m.movie_id;";

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				total += rs.getInt("movie_runningTime");
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("[평가한 영화 수] : " + count + "회");
		System.out.println("[영화 감상 시간] : 약 " + Math.round((float) total / 60) + "시간");
	}

	public static void myfavMovieinfo(Statement stmt) {
		ResultSet rs = null;

		int total = 0, count = 0;

		String sql = "SELECT m.movie_runningTime " + "FROM movie AS m " + "JOIN evaluate_movie AS em "
				+ "WHERE em.eval_user_id = " + curUserID + " AND em.eval_movie_id = m.movie_id;";

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				total += rs.getInt("movie_runningTime");
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("[평가한 영화 수] : " + count + "회");
		System.out.println("[영화 감상 시간] : 약 " + Math.round((float) total / 60) + "시간");
	}

	/* 영화 리스트 구현하기 - default로 실행 */
	public static void movie_list(Statement stmt) {
		ResultSet rs = null;

		String sql = "select * from movie";

		try {
			rs = stmt.executeQuery(sql);

			System.out.print("id");
			System.out.print("\t");
			System.out.print("name");
			System.out.print("\t");
			System.out.print("year");
			System.out.print("\t");
			System.out.print("date");
			System.out.print("\t");
			System.out.print("country");
			System.out.print("\t");
			System.out.print("description");
			System.out.print("\t");
			System.out.print("runningTime");
			System.out.print("\t");
			System.out.print("title");
			System.out.print("\t");
			System.out.print("play");
			System.out.print("\n");
			System.out.println("────────────────────────────────────────────────────────────────────────");

			while (rs.next()) {
				System.out.print(rs.getInt("movie_id"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_name"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_year"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_date"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_country"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_description"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_runningTime"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_title"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_play"));
				System.out.print("\n");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* 영화 목록을 카테고리 (장르별)로 만들기 - tag 이름으로 받았을 때 */
	public static void movie_list_category(Statement stmt, String tag) {
		ResultSet rs = null;
		String sql = "select tag_id from tag where tag_name = '" + tag + "';";
		int index = 0;

		/* 태그이름으로 tag_id 찾기 */
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				index = rs.getInt("tag_id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		/* tag_id가 들어간 movie_id 찾기 */
		sql = "select movie_id_for from tag_genre where tag_id1 = '" + index + "' OR tag_id2 = '" + index
				+ "' OR tag_id3 = '" + index + "' OR tag_id4 = '" + index + "' OR tag_id5 = '" + index + "';";

		List<Object> array = new ArrayList<Object>();
		try {
			rs = stmt.executeQuery(sql);

			while (rs.next()) {

				/* movie_id를 array에 저장하기 */
				array.add(rs.getInt("movie_id_for"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.print("id");
		System.out.print("\t");
		System.out.print("name");
		System.out.print("\t");
		System.out.print("year");
		System.out.print("\t");
		System.out.print("date");
		System.out.print("\t");
		System.out.print("country");
		System.out.print("\t");
		System.out.print("description");
		System.out.print("\t");
		System.out.print("runningTime");
		System.out.print("\t");
		System.out.print("title");
		System.out.print("\t");
		System.out.print("available to play");
		System.out.print("\n");
		System.out.println(
				"──────────────────────────────────────────────────────────────────────────────────────────────────────────────");

		for (int i = 0; i < array.size(); i++) {
			choose_movie(stmt, Integer.parseInt(array.get(i).toString()), 0);
		}

	}

	/* 영화 선택하기 id로 받았을 때 */
	public static void choose_movie(Statement stmt, int id, int title) {
		String table = "movie";
		String sql = "select * from " + table + " where movie_id = " + id + ";";
		try {
			ResultSet rs = stmt.executeQuery(sql);

			if (title != 0) {
				System.out.print("id");
				System.out.print("\t");
				System.out.print("name");
				System.out.print("\t");
				System.out.print("year");
				System.out.print("\t");
				System.out.print("date");
				System.out.print("\t");
				System.out.print("country");
				System.out.print("\t");
				System.out.print("description");
				System.out.print("\t");
				System.out.print("runningTime");
				System.out.print("\t");
				System.out.print("title");
				System.out.print("\t");
				System.out.print("play");
				System.out.print("\n");
				System.out.println(
						"─────────────────────────────────────────────────────────────────────────────────────────────────────────────");
			}
			while (rs.next()) {
				System.out.print(rs.getInt("movie_id"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_name"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_year"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_date"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_country"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_description"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_runningTime"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_title"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_play"));
				System.out.print("\n");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* 검색 - 영화 선택하기 name으로 받았을 때 */
	public static void choose_movie(Statement stmt, String name) {
		String table = "movie";
		String sql = "select * from " + table + " where movie_name = '" + name + "';";

		try {
			ResultSet rs = stmt.executeQuery(sql);

			System.out.print("id");
			System.out.print("\t");
			System.out.print("name");
			System.out.print("\t");
			System.out.print("year");
			System.out.print("\t");
			System.out.print("date");
			System.out.print("\t");
			System.out.print("country");
			System.out.print("\t");
			System.out.print("description");
			System.out.print("\t");
			System.out.print("runningTime");
			System.out.print("\t");
			System.out.print("title");
			System.out.print("\t");
			System.out.print("play");
			System.out.print("\n");
			System.out.println("─────────────────────────────────────────────────────────────"
					+ "─────────────────────────────────────────");

			while (rs.next()) {
				System.out.print(rs.getInt("movie_id"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_name"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_year"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_date"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_country"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_description"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_runningTime"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_title"));
				System.out.print("\t");
				System.out.print(rs.getString("movie_play"));
				System.out.print("\n");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* 영화 평점 주기 */
	public static void rate_movie(Statement stmt, int user_id, int movie_id) {
		ResultSet rs = null;
		Scanner a = new Scanner(System.in);

		int exists = 1;

		/* 영화 평가한 있는지 체크 */
		String sql = "SELECT eval_movie_id FROM evaluate_movie WHERE EXISTS (SELECT eval_user_id "
				+ "FROM evaluate_movie WHERE eval_user_id = " + user_id + " AND eval_movie_id = " + movie_id + " );";

		try {
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				if (rs.getInt("eval_movie_id") > 0)
					exists = 0;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.printf("몇 점을 주시겠습니까?");
		float rate = a.nextFloat();

		switch (exists) {
		case 0:
			/* 평가한 적이 있을 때 */
			sql = "update evaluate_movie set star_point = " + rate + " where eval_user_id = '" + user_id + "' and "
					+ "eval_movie_id = " + movie_id + ";";
			try {
				stmt.executeUpdate(sql);
				System.out.println("별점이 수정되었습니다.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		case 1:
			/* 평가한 적이 없을 때 */
			sql = "insert evaluate_movie set eval_user_id = " + user_id + ", eval_movie_id = " + movie_id
					+ ", star_point = " + rate + ";";

			try {
				stmt.executeUpdate(sql);
				System.out.println("별점을 저장하였습니다.");

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}

	}

	/* 상영 중인 영화 골라서 박스 오피스 보여주기 */
	public static void boxOffice(Statement stmt) {
		ResultSet rs = null;
		List<Object> array = new ArrayList<Object>();

		/* 상영 중 영화 예매량 순으로 arraylist에 추가 */
		String sql = "SELECT movie_id FROM theater_now ORDER BY movie_total DESC";

		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				array.add(rs.getInt("movie_id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.print("id");
		System.out.print("\t");
		System.out.print("name");
		System.out.print("\t");
		System.out.print("year");
		System.out.print("\t");
		System.out.print("date");
		System.out.print("\t");
		System.out.print("country");
		System.out.print("\t");
		System.out.print("description");
		System.out.print("\t");
		System.out.print("runningTime");
		System.out.print("\t");
		System.out.print("title");
		System.out.print("\t");
		System.out.print("available to play");
		System.out.print("\n");
		System.out.println(
				"──────────────────────────────────────────────────────────────────────────────────────────────────────────────");

		for (int i = 0; i < array.size(); i++) {
			choose_movie(stmt, Integer.parseInt(array.get(i).toString()), 0);
		}

	}

	/* 영화가 담긴 collection 보여주기 */
	public static void relatedCollection_list(Statement stmt, int id) // id means movie_id
	{

		List<Object> array = new ArrayList<Object>();

		/* movie_id가 포함된 collection_id 찾기 */
		ResultSet rs = null;
		String sql = "select collection_id from collectionMovie where movie_id = " + id + ";";

		try {
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				array.add(rs.getInt("collection_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/* collection 정보 출력 */
		for (int i = 0; i < array.size(); i++) {
			choose_collection(stmt, Integer.parseInt(array.get(i).toString()));
		}

	}

	/* collection 정보 출력 */
	public static void choose_collection(Statement stmt, int id)// id means collection_id
	{
		String sql = "select collection_id, collection_name, collectionuser_id, collection_description"
				+ " from collection where collection_id = " + id + ";";
		try {
			ResultSet rs = stmt.executeQuery(sql);

			System.out.print("id");
			System.out.print("\t");
			System.out.print("name");
			System.out.print("\t");
			System.out.print("user_id");
			System.out.print("\t");
			System.out.print("description");
			System.out.print("\n");
			System.out.println(
					"─────────────────────────────────────────────────────────────────────────────────────────────────────────────");

			while (rs.next()) {
				System.out.print(rs.getInt("collection_id"));
				System.out.print("\t");
				System.out.print(rs.getString("collection_name"));
				System.out.print("\t");
				System.out.print(rs.getString("collectionuser_id"));
				System.out.print("\t");
				System.out.print(rs.getString("collection_description"));
				System.out.print("\n");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/* movie comment 출력 */
	public static void movie_comment(Statement stmt, int id) // id means movie_id
	{
		String sql = "select * from movieComments where movie_id = " + id
				+ " AND is_deleted = 0 ORDER BY COMMENT_DATE ASC;";

		try {
			ResultSet rs = stmt.executeQuery(sql);

			System.out.print("USER");
			System.out.print("\t\t");
			System.out.print("comment");
			System.out.print("\n");
			System.out.println(
					"─────────────────────────────────────────────────────────────────────────────────────────────────────────────");

			while (rs.next()) {
				System.out.print(rs.getInt("user_id"));
				System.out.print("\t");
				if (rs.getInt("parent_comment_id") > 0)
					System.out.print("-> ");
				System.out.print(rs.getString("comment_content"));
				System.out.print("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* comment 좋아요/신고/신고취소 */
	public static void like_comment(Statement stmt, int movie_id, int comment_id, int User_id) {
		String sql;
		Scanner s = new Scanner(System.in);

		System.out.println("1.좋아요 2.신고");
		int which = s.nextInt();
		int exists = 1;

		switch (which) {

		case 1:

			/* 좋아요 체크 */
			sql = "SELECT comment_id FROM movieCommentLike WHERE EXISTS (SELECT * "
					+ "FROM movieCommentLike WHERE movie_id = " + movie_id + " AND comment_id = " + comment_id
					+ " AND User_id = " + User_id + " );";

			try {

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					if (rs.getInt("comment_id") > 0)
						exists = 0;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (exists == 0) // 좋아요 함 -> 좋아요 취소
			{
				sql = "DELETE FROM movieCommentLike WHERE movie_id = " + movie_id + " AND comment_id = " + comment_id
						+ " AND User_id = " + User_id + ";";
				try {
					stmt.executeUpdate(sql);
					System.out.println("좋아요 취소!");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			else // 좋아요 아직 안함 -> 좋아요
			{

				sql = "INSERT movieCommentLike SET movie_id = " + movie_id + ", comment_id = " + comment_id
						+ ", User_id = " + User_id + ";";

				try {
					stmt.executeUpdate(sql);
					System.out.println("좋아요!\n");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			break;

		case 2:

			System.out.println("1.부적절한 표현 신고  2.스포일러 신고");
			int report = s.nextInt();

			exists = 1;

			/* 신고 체크 */
			sql = "SELECT Comment_id FROM reportedComments WHERE EXISTS (SELECT * "
					+ "FROM reportedComments WHERE MovieOrCollection = 0 AND Comment_id = " + comment_id
					+ " AND User_id = " + User_id + " );";
			try {

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					if (rs.getInt("Comment_id") > 0)
						exists = 0;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (exists == 0) // 신고 취소하기
			{
				sql = "DELTE FROM reportedComments WHERE MovieOrComments = 0 AND Comment_id = " + comment_id
						+ "AND User_id = " + User_id + ";";
				try {
					stmt.executeUpdate(sql);
					System.out.println("신고 취소했습니다!");
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				sql = "INSERT reportedComments SET MovieOrCollection = 0, Comment_id = " + comment_id + ", User_id = "
						+ User_id + ", Report_Reasons = " + report + ";";
				System.out.println(sql);
				try {
					stmt.executeUpdate(sql);
					System.out.println("신고 완료했습니다!");
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			break;

		}

	}

	public static void main(String[] args) {

		Connection conn = null;

		Statement stmt = null;

		PreparedStatement pstmt = null;

		ResultSet rs = null;

		// 현재 user 지정
		curUserID = 1;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); // JDBC 드라이버 로드

			conn = DriverManager.getConnection("jdbc:mysql://172.17.159.36:3306/DBclass?serverTimezone=UTC", "yc12312",
					"yc9639631!");// URL,UID,PWD

			if (conn == null) {

				System.out.println("연결실패");

			} else {

				System.out.println("연결성공\n");

				stmt = conn.createStatement();

//              myMovieWatching(stmt);

//              Delete_StoreMovie(stmt,2);

//              MyCollectionPrint(stmt);

//              LikeCollectionPrint(stmt);

//              RearrangeIndex(stmt,"collection","collection_id");

//              MyCommentPrint(stmt);

//              starDistribution(stmt);

//              myMovieinfo(stmt);

//              LikeMovieCommentPrint(stmt);

//              LikeCollectionCommentPrint(stmt);

//			    movie_list(stmt); //ok
//				movie_list_category(stmt, "happy");

//				choose_movie(stmt, 3, 1);
//				choose_movie(stmt, "LoveAffair");

//				rate_movie(stmt, 21000261, 4);

//				boxOffice(stmt);

//				relatedCollection_list(stmt, 9);

//				movie_comment(stmt, 2);

				like_comment(stmt, 2, 2, 21000261);

			}

		} catch (ClassNotFoundException ce) {

			ce.printStackTrace();

		} catch (SQLException se) {

			se.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try { // 연결 해제(한정돼 있으므로)

				if (rs != null) {
					rs.close();
				}

				if (pstmt != null) {
					pstmt.close();
				}

				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (SQLException se2) {

				se2.printStackTrace();

			}

		}

	}

}
