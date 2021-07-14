package com.jachs.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

/***
 * 
 * @author zhanchaohan
 *
 */
public class Mtest {
	JdbcUtill ju = new JdbcUtill();

	//初始化
	@Before
	public void init() {
		try {
			ju.init();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	//获取全部表
	@Test
	public void testShowTable() {
		try {
			ju.showTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//获取表全部字段
	@Test
	public void testShowColum() {
		try {
			ju.showColum("studentcard");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//获取主键
	@Test
	public void testPrimaryKeys() {
		try {
			ju.getPrimaryKeysForTable("studentcard");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//获取外键
	@Test
	public void testImportedKeys() {
		try {
			ju.getImportedKeysForTable("studentcard");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
