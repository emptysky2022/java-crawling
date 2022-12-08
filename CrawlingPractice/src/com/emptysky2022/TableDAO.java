package com.emptysky2022;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private static TableDAO dao;
	private final String url = "Your Database URL in here";
	private final String user = "Your Username in here";
	private final String pwd = "Your Password in here";
	
	private TableDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url,user,pwd);
			System.out.println("---------- DriverManager ----------" + conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void disConnection() {
		try {
			if(pstmt != null)pstmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static TableDAO newInstance() {
		if(dao == null) dao = new TableDAO();
		return dao;
	}
	
	public void gameOfMonthInsert(TableVO vo) {
		//1. DB 연결
		getConnection();
		
		//2. sql 작성
		String sql = "insert into crawlingTBL values (null, ?,?,?,?,?,?,?,?,?)";
		
		try {
			//3. sql 전달
			pstmt = conn.prepareStatement(sql);
			
			//4. prepareStatememt에 vo에게 전달받은 값을 할당
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getSection());
			pstmt.setString(3, vo.getGenre());
			pstmt.setString(4, vo.getRating());
			pstmt.setString(5, vo.getAwardDate());
			pstmt.setString(6, vo.getReleaseDate());
			pstmt.setString(7, vo.getCompany());
			pstmt.setString(8, vo.getComAddr());
			pstmt.setString(9, vo.getInfo());
			
			//5. 전송된 값을 테이블에 적용, 업데이트
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
