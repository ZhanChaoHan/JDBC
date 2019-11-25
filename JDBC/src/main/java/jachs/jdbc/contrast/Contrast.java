package jachs.jdbc.contrast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jachs.jdbc.App;
import jachs.jdbc.contrast.entity.TabeleEntity;

public class Contrast {
	private Connection conn;
	private Connection conn_contrast;

	private Map<String, Set<TabeleEntity>> fieldMap = new HashMap<>();
	private Map<String, Set<TabeleEntity>> fieldcontrastMap = new HashMap<>();

	private BufferedWriter bw;
	@Before
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.util.Properties pro = new Properties();
			pro.load(App.class.getResourceAsStream("application.properties"));
			conn = DriverManager.getConnection(pro.getProperty("url"), pro.getProperty("user"),
					pro.getProperty("password"));
			conn_contrast = DriverManager.getConnection(pro.getProperty("contrast_url"),
					pro.getProperty("contrast_user"), pro.getProperty("contrast_password"));
			bw=new BufferedWriter(new FileWriter(pro.getProperty("printPath")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void destroy() {
		try {
			conn.close();
			conn_contrast.close();
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			Set<TabeleEntity> teArr;
			while (tables.next()) {
				ResultSet field = connData.getColumns(null, "%", tables.getString("TABLE_NAME"), "%");
				teArr = new HashSet<TabeleEntity>();
				while (field.next()) {
					teArr.add(new TabeleEntity(field.getString("COLUMN_NAME"), field.getString("TYPE_NAME"),
							field.getInt("COLUMN_SIZE")));
				}
				fieldMap.put(tables.getString("TABLE_NAME"), teArr);
			}

			ResultSet conn_contrast_tables = conn_contrastData.getTables(catalog(), schemaPattern(), tableNamePattern(),
					types());
			Set<TabeleEntity> contrast_teArr;
			while (conn_contrast_tables.next()) {
				ResultSet fieldContrast = conn_contrastData.getColumns(null, "%",
						conn_contrast_tables.getString("TABLE_NAME"), "%");
				contrast_teArr = new HashSet<>();
				while (fieldContrast.next()) {
					contrast_teArr.add(new TabeleEntity(fieldContrast.getString("COLUMN_NAME"),
							fieldContrast.getString("TYPE_NAME"), fieldContrast.getInt("COLUMN_SIZE")));
				}
				fieldcontrastMap.put(conn_contrast_tables.getString("TABLE_NAME"), contrast_teArr);
			}
			for (Entry<String, Set<TabeleEntity>> tabeleEntity : fieldMap.entrySet()) {
				if (fieldcontrastMap.get(tabeleEntity.getKey()) != null) {
					Set<TabeleEntity> tf = fieldcontrastMap.get(tabeleEntity.getKey());
					Set<TabeleEntity> t = fieldMap.get(tabeleEntity.getKey());
					for (TabeleEntity te : tf) {
						boolean finde = false;
						for (TabeleEntity tee : t) {
							if (te.getFieldName().equals(tee.getFieldName())) {
								finde = true;
								if (tee.getFieldName().equals(te.getFieldName())) {
//									if (tee.getFielSize() != te.getFielSize()) {
//										bw.write(tabeleEntity.getKey() + "表字段大小" + te.getFieldName() + "\n");
//									}
									if (!tee.getFielType().equals(te.getFielType())) {
										bw.write(tabeleEntity.getKey() + "表字段类型变化" + te.getFielType() + "\n");
									}
								}
							}
						}
						if (!finde) {
							bw.write(tabeleEntity.getKey() + "表字段新增" + te.getFieldName() + "\n");
						}
					}
				} else {
					bw.write(tabeleEntity.getKey() + "表新增\n");
				}
			}
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
