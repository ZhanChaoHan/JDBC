package jachs.jdbc.contrast.entity;

public class TabeleEntity {
	private String fieldName;
	private String fielType;
	private long fielSize;
	
	public TabeleEntity() {
		super();
	}
	public TabeleEntity(String fieldName, String fielType, long fielSize) {
		super();
		this.fieldName = fieldName;
		this.fielType = fielType;
		this.fielSize = fielSize;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFielType() {
		return fielType;
	}
	public void setFielType(String fielType) {
		this.fielType = fielType;
	}
	public long getFielSize() {
		return fielSize;
	}
	public void setFielSize(long fielSize) {
		this.fielSize = fielSize;
	}
	
}
