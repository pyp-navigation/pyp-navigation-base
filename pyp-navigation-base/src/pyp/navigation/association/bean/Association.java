package pyp.navigation.association.bean;

/**
 * @Title: Association
 * @Description: xml文件对应的bean ： Demo{key,name,id}
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class Association {
	private String key; // 社团主键
	private String name; // 社团名称
	private String id; // 社团ID
	private String icon; // 社团logo的资源文件名

	public Association() {
		super();
	}

	public Association(String key, String name, String id) {
		super();
		this.key = key;
		this.name = name;
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Key:" + this.key + " name:" + this.name + " id:" + this.id
				+ "\n";
	}

}
