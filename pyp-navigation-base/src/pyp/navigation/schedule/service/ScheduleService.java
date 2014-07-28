package pyp.navigation.schedule.service;

import pyp.navigation.schedule.ScheduleFragment;
import android.widget.TextView;

public class ScheduleService {

	private ScheduleFragment mFragment;
	private int mMonth;
	private int mWeek;

	/**
	 * 得到当前周数
	 * 
	 * @return int 当前周数
	 */
	public int getWeek() {
		return mWeek;
	}

	/**
	 * 得到当前月份 方法 getMonth
	 * 
	 * @return int 当前月份
	 */
	public int getMonth() {
		return mMonth;
	}

	public ScheduleService(ScheduleFragment fragment) {
		mFragment = fragment;

	}

	/**
	 * 改变当前月份 
	 * 
	 * @param month
	 *            void
	 */
	public void changeMonth(int month) {
		mMonth = month;
		TextView tv = mFragment.getMonthView();
		tv.setText(month + "");

	}
	/**
	 * 改变当前周数，
	 * 方法 changeWeek
	 * 调用后，界面会发生相应变化(暂未实现)
	 * @param week
	 * void
	 */
	public void changeWeek(int week) {
		mWeek = week;
		TextView tv = mFragment.getWeekView();
		tv.setText(week + "");
	}

	/**
	 * 改变月份和周数
	 * 
	 * @param newMonth
	 *            新月份
	 * @param newWeek
	 *            新周数 void
	 */
	public void changeMonthAndWeek(int newMonth, int newWeek) {
		this.changeMonth(newMonth);
		this.changeWeek(newWeek);
	}

}
