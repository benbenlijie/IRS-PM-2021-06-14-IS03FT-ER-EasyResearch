package com.IRS.optaplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PaperDAOimpl implements PaperDAO {

	public List<Paper> findAll() throws SQLException {
	    List<Paper> list = new ArrayList<Paper>();
	    Connection connection = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommendation","root","");
	        pst = connection.prepareCall("select *  from paper ");
	        rs = pst.executeQuery();

	        while (rs.next()) {
	            Paper paper = new Paper();
	            paper.setTitle(rs.getString("title"));
	            paper.setAuthors(rs.getString("authors"));
	            paper.setDate(rs.getString("date"));
	            paper.setKeyword(rs.getString("keyword"));
	            paper.setCategory(rs.getString("category"));
	            paper.setPaperid(rs.getString("paperid"));
	            paper.setSource(rs.getString("source"));
	            list.add(paper);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        connection.close();
	        pst.close();
	        rs.close();

	    }
	    return list;
	}
	
}
