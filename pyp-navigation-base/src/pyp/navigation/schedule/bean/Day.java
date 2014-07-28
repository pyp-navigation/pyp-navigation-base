package pyp.navigation.schedule.bean;

import java.util.Calendar;

import pyp.navigation.schedule.service.DayService;

public class Day {

	private int year;
	private int month;
	private int dayCount;
	private Week_enum week;

	/**
	 * 通过年月日构造Day对象 构造方法
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param dayCount
	 *            日
	 */

	public Day(int year, int month, int dayCount) {
		this(year, month, dayCount, DayService.getWeek(year, month, dayCount));
	}

	public Day(Day day) {

		this(day.getYear(), day.getMonth(), day.getDayCount(), day.getWeek());

	}

	public Day(int year, int month, int dayCount, Week_enum week) {
		this.year = year;
		this.month = month;
		this.dayCount = dayCount;
		this.week = week;

	}

	/**
	 * 
	 * 方法 parseCalendar 将Day对象转换成Calendar对象
	 * 
	 * @return Calendar 转换后的Calendar对象
	 */
	public Calendar parseCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, dayCount);
		return calendar;
	}

	public Day() {

	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDayCount() {
		return dayCount;
	}

	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}

	public Week_enum getWeek() {
		return week;
	}

	public void setWeek(Week_enum week) {
		this.week = week;
	}

}
