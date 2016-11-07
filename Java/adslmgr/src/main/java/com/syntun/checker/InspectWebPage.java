package com.syntun.checker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.syntun.crawler.CrawlerConfig;

public class InspectWebPage {

	private static HashMap<Integer, ArrayList<Map<String, String>>> hostInspectContentHs = new HashMap<Integer, ArrayList<Map<String, String>>>();
	private static InspectWebPage inspect = null;

	private InspectWebPage() {
		try {
			Connection conn = ConnectSql.getConn(
					CrawlerConfig.CHECKER_MYSQL_SERVER,
					CrawlerConfig.CHECKER_MYSQL_DB,
					CrawlerConfig.CHECKER_MYSQL_USER,
					CrawlerConfig.CHECKER_MYSQL_PASSWD
					);
			Statement stmt = conn.createStatement();
			String sql = "select * from web_error";
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rmd = rs.getMetaData();
			int columnNum = rmd.getColumnCount();
			while (rs.next()) {
				ArrayList<Map<String, String>> errContent;
				if (hostInspectContentHs.containsKey(rs.getInt("url_group"))) {
					errContent = hostInspectContentHs.get(rs
							.getInt("url_group"));
				} else { 
					errContent = new ArrayList<Map<String, String>>();
				}
				
				Map<String, String> m = new HashMap<String, String>();
				for (int i = 1; i <= columnNum; i++) {
					m.put(rmd.getColumnName(i), rs.getString(i));
				}
				errContent.add(m);
				hostInspectContentHs.put(rs.getInt("url_group"), errContent);
			}
			stmt.close();
			ConnectSql.push(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static InspectWebPage getInspectObj() {
		if (inspect == null) {
			inspect = new InspectWebPage();
		}
		return inspect;
	}

	/**
	 * 返回结果
	 * 
	 * @param ui
	 * @return
	 */
	public int inspectPage(String content,int urlGroup) {
		if (content == null) {
			return 0;
		}
		if (hostInspectContentHs.containsKey(urlGroup)) {
			ArrayList<Map<String, String>> errContent = hostInspectContentHs
					.get(urlGroup);
			int errSize = errContent.size();
			for (int i = 0; i < errSize; i++) {
				if (errContent.get(i) == null) {
					break;
				}
				if (urlGroup == 500001
						&& content.isEmpty()) {
					return 1;
				}
				if (urlGroup == 5 || urlGroup == 50
						|| urlGroup == 500) {
					if (content.replace("	", "").replace(" ", "")
							.indexOf("您访问的网页不存在") != -1) {
						return 1;
					}
				}
				if (content.indexOf(
						errContent.get(i).get("err_content_str")) != -1) {
					return Integer.parseInt(errContent.get(i).get(
							"err_content_result"));
				}
			}
		}
		return 0;
	}
	
	public int checkRedirectPage(String location){
		if(location.indexOf("//alisec") >= 0){
			return 1;
		}
		return 0;
	}
}
