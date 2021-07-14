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
	JdbcUtill ju=new JdbcUtill();
	
	@Before
	public void init(){
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
	
	@Test
	public void Ztest1() {
		try {
			ju.showTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
