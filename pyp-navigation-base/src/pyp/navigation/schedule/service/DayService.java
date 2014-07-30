package pyp.navigation.schedule.service;

import java.util.Calendar;

import pyp.navigation.schedule.bean.Day;
import pyp.navigation.schedule.bean.Week_enum;

public class DayService {

	public static Day getDay(int year, int month, int dayCount) {
		Day day = new Day();
		day.setYear(year);
		day.setMonth(month);
		day.setDayCount(dayCount);
		day.setWeek(getWeek(year, month, dayCount));
		return day;
	}

	public static Week_enum getWeek(int year, int month, int dayCount) {

		Calendar calendar = Calendar.getInstance();// 获得一个日历
		calendar.set(year, month - 1, dayCount);// 设置当前时间,月份是从0月开始计算
		int number = calendar.get(Calendar.DAY_OF_WEEK);// 星期表示1-7，是从星期日开始，
		Week_enum[] week = Week_enum.values();// 枚举转换成数组
		return week[number - 1];

	}

	/**
	 * (还未实现 )得到提前指定天数后的Day对象 方法 beforeDays
	 * 
	 * @param oldDay
	 *            对照的Day对象
	 * @param days
	 *            提前的天数
	 * @return Day
	 */
	public static Day beforeDays(Day oldDay, int days) {
		if (days == 0)
			return oldDay;

		if (days < 0)
			return null;

		Day day = new Day(oldDay);

		int newDayCount = oldDay.getDayCount() - days;
		// 减去后日期合法，修改日期返回
		if (newDayCount > 0) {
			day.setDayCount(newDayCount);
			day.setWeek(getWeek(day.getYear(), day.getMonth(),
					day.getDayCount()));

			return day;
		}
		int newMonth = day.getMonth() - 1;

		if (newMonth >= 0) {

		}

		return null;
	}

	public static Day getCurrentDay() {
		Calendar c = Calendar.getInstance();
		Day day = new Day();
		// 取得系统日期:
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int dayCount = c.get(Calendar.DAY_OF_MONTH);
		day.setYear(year);
		day.setMonth(month);
		day.setDayCount(dayCount);

		int number = c.get(Calendar.DAY_OF_WEEK);// 星期表示1-7，是从星期日开始，
		Week_enum[] week = Week_enum.values();// 枚举转换成数组
		day.setWeek(week[number - 1]);

		// 取得系统时间：

		return day;
	}
}
