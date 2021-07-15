package com.jachs.jdbc;

public interface JdbcBehaviour {
	public void init() throws Exception;
	public void showTable() throws Exception;
	public void showColum(String tableName) throws Exception;
	public void getPrimaryKeysForTable(String tableName) throws Exception;
	public void getImportedKeysForTable(String tableName) throws Exception;
	
}
