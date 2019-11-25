package jachs.jdbc.contrast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jachs.jdbc.App;
import jachs.jdbc.contrast.entity.TabeleEntity;

public class Contrast {
	private Connection conn;
	private Connection conn_contrast;

	private Map<String, List<TabeleEntity>>fieldMap=new HashMap<>();
	private Map<String, List<TabeleEntity>>fieldcontrastMap=new HashMap<>();
	
	@Before
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.util.Properties pro = new Properties();
			pro.load(App.class.getResourceAsStream("application.properties"));
			conn = DriverManager.getConnection(pro.getProperty("url"), pro.getProperty("user"),
					pro.getProperty("password"));
			conn_contrast = DriverManager.getConnection(pro.getProperty("contrast_url"), pro.getProperty("contrast_user"),
					pro.getProperty("contrast_password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void destroy() {
		try {
			conn.close();
			conn_contrast.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		try {
			DatabaseMetaData connData = conn.getMetaData();
			DatabaseMetaData conn_contrastData = conn_contrast.getMetaData();

			ResultSet tables = connData.getTables(catalog(), schemaPattern(), tableNamePattern(), types());
			List<TabeleEntity>teArr;
			while(tables.next()) {
				ResultSet field = connData.getColumns(null,"%", tables.getString("TABLE_NAME"),"%"); 
				teArr=new ArrayList<TabeleEntity>();
				while(field.next()) {
					teArr.add(new TabeleEntity(field.getString("COLUMN_NAME"), field.getString("TYPE_NAME"), field.getInt("COLUMN_SIZE")));
				}
				fieldMap.put(tables.getString("TABLE_NAME"), teArr);
			}
			
			ResultSet conn_contrast_tables = conn_contrastData.getTables(catalog(), schemaPattern(), tableNamePattern(), types());
			List<TabeleEntity>contrast_teArr;
			while(conn_contrast_tables.next()) {
				ResultSet fieldContrast = conn_contrastData.getColumns(null,"%", conn_contrast_tables.getString("TABLE_NAME"),"%"); 
				contrast_teArr=new ArrayList<>();
				while(fieldContrast.next()) {
					contrast_teArr.add(new TabeleEntity(fieldContrast.getString("COLUMN_NAME"), fieldContrast.getString("TYPE_NAME"), fieldContrast.getInt("COLUMN_SIZE")));
				}
				fieldcontrastMap.put(conn_contrast_tables.getString("TABLE_NAME"), contrast_teArr);
			}
			BufferedWriter bw=new BufferedWriter(new FileWriter("e:\\a.txt"));
			for (Entry<String, List<TabeleEntity>> tabeleEntity : fieldMap.entrySet()) {
				if(fieldcontrastMap.get(tabeleEntity.getKey())!=null) {
					List<TabeleEntity> tf=fieldcontrastMap.get(tabeleEntity.getKey());
					List<TabeleEntity> t=fieldMap.get(tabeleEntity.getKey());
					for (TabeleEntity te : tf) {
						boolean finde=false;//默认未找到当前表字段
						for (TabeleEntity tet : t) {
							if(te.getFieldName().equals(tet.getFieldName())) {
								finde=true;
							}
							if(te.getFielSize()!=tet.getFielSize()) {
								bw.write(tabeleEntity.getKey()+"表大小"+te.getFieldName()+"字段\n");
							}
							if(!te.getFielType().equals(te.getFielType())){
								bw.write(tabeleEntity.getKey()+"表类型"+te.getFieldName()+"字段\n");
							}
						}
						if(!finde) {
							bw.write(tabeleEntity.getKey()+"表新增"+te.getFieldName()+"字段\n");
						}
					}
				}else {
					bw.write(tabeleEntity.getKey()+"表新增\n");
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static String catalog() {
		return null;
	}

	protected static String tableNamePattern() {
		return "%";
	}

	protected static String[] types() {
		return new String[] { "TABLE", "VIEW" };
	}

	private static String schemaPattern() {
		return null;
	}
}
