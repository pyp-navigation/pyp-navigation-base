package pyp.navigation.map.bean;

/**
 * @Title: Marker
 * @Description: 存储Marker的信息
 * @author 张伟杰
 * @date 2014-7-24
 * @email 531724220@qq.com
 */
public class Marker {

	/**
	 * 字段 int ： key
	 * TODO 用于区分Marker的类型
	 * key=1, 运行场
	 * key=2, 宿舍
	 * key=3, 教学楼
	 * key=4, 公交
	 * key=5, 生活类(超市，食堂，外卖)
	 * key=6, 厕所
	 */
	public int key;
	
	public double[] position;
	public String positionName = "";
	public String info = "";
	
	public Marker() {}

	public Marker( int key, double[] position, String positionName, String info) {
		this.key = key;
		this.position = position;
		this.positionName = positionName;
		this.info = info;
	}
	
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public double[] getPosition() {
		return position;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}
	
	public String getPositionName() {
		return positionName;
	}
	
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
}
