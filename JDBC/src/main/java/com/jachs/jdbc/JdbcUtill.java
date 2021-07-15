package com.jachs.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/***
 * 
 * @author zhanchaohan
 *
 */
public class JdbcUtill implements JdbcBehaviour {
	private Connection connection;

	public void init() throws Exception {
		Properties properties = new Properties();
		properties.load(JdbcUtill.class.getResourceAsStream("/connection.properties"));

		Class.forName(properties.getProperty("driver"));

		connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
				properties.getProperty("password"));

		System.out.println(connection.isClosed());
	}

	private Map<String, String> printResultSet(ResultSet tables) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();

		while (tables.next()) {
			// 列的个数
			int columnCount = tables.getMetaData().getColumnCount();
			// set key list
			List<String> colNamesList = new ArrayList<String>();
			for (int i = 1; i <= columnCount; i++) { // 获取列名称
				String columnName = tables.getMetaData().getColumnName(i);
				colNamesList.add(columnName);
			}
			System.out.println(colNamesList);
			// print key and values
			for (String cKey : colNamesList) {
				System.out.println(cKey + "\t\t" + tables.getString(cKey));
				map.put(cKey, tables.getString(cKey));
			}
			System.out.println("--------------------------------------------------------");
		}
		return map;
	}

	/**
	 * 获取全部表信息
	 * 
	 * @throws SQLException
	 */
	public void showTable() throws Exception {
		DatabaseMetaData metaData = connection.getMetaData();

		ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
		printResultSet(tables);
	}

	/***
	 * 获取表全部字段信息
	 * 
	 * @param tableName
	 * @throws SQLException
	 */
	public void showColum(String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();

		ResultSet colRet = metaData.getColumns(null, "%", tableName, "%");

		printResultSet(colRet);
	}

	/***
	 * 获取表主键
	 * 
	 * @param tableName
	 * @throws SQLException
	 */
	public void getPrimaryKeysForTable(String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);

		printResultSet(primaryKeyResultSet);
	}

	/***
	 * 获取表外键
	 * 
	 * @param tableName
	 * @throws SQLException
	 */
	public void getImportedKeysForTable(String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet primaryKeyResultSet = metaData.getImportedKeys(null, null, tableName);

		printResultSet(primaryKeyResultSet);
	}

}
