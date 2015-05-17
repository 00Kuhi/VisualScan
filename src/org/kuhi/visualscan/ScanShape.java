package org.kuhi.visualscan;

public class ScanShape {
	
	private String name = "";
	private Integer shape = new Integer(0);
	private String shapeName = "";
	
	public ScanShape(String name, Integer shape, String shapeName) {
		this.name = name;
		this.shape = shape;
		this.shapeName = shapeName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getShape() {
		return shape;
	}
	public void setShape(Integer shape) {
		this.shape = shape;
	}
	public String getShapeName() {
		return shapeName;
	}
	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}

}
