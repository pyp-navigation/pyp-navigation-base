package pyp.navigation.schedule.bean;

/**
 * 课程类，课程表模块的 bean
 * 
 * @Title: Cursor
 * @Description: 封装课程的名字、教师名字、上课地点、一天中这们课程从第几节开始到第几节结束、在整个学期中从第几周到第几周结束
 * @author 作者名字chenkaihua
 * @date 2014年7月28日
 * @email 作者邮箱954822984@qq.com
 */
public class Cursor {
	private String mName;
	private String mTeacherName;
	private String mLocation;
	private int mStartClassOnDay;
	private int mEndClassOnDay;
	private int mStartClassWeek;
	private int mEndClassWeek;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getTeacherName() {
		return mTeacherName;
	}

	public void setTeacherName(String teacherName) {
		this.mTeacherName = teacherName;
	}

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String location) {
		this.mLocation = location;
	}

	public int getStartClassOnDay() {
		return mStartClassOnDay;
	}

	public void setStartClassOnDay(int startClassOnDay) {
		this.mStartClassOnDay = startClassOnDay;
	}

	public int getEndClassOnDay() {
		return mEndClassOnDay;
	}

	public void setEndClassOnDay(int endClassOnDay) {
		this.mEndClassOnDay = endClassOnDay;
	}

	public int getStartClassWeek() {
		return mStartClassWeek;
	}

	public void setStartClassWeek(int startClassWeek) {
		this.mStartClassWeek = startClassWeek;
	}

	public int getmEndClassWeek() {
		return mEndClassWeek;
	}

	public void setmEndClassWeek(int mEndClassWeek) {
		this.mEndClassWeek = mEndClassWeek;
	}

}
