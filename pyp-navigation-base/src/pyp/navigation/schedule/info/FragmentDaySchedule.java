package pyp.navigation.schedule.info;

import pyp.navigation.R;
import pyp.navigation.schedule.bean.Day;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 课程表模块中展示一天的课程
 * 
 * @Title: FragmentDaySchedule
 * @Description: viewPage里中的一项，用listView展示数据
 * @author 作者名字chenkaihua
 * @date 2014年7月28日
 * @email 作者邮箱954822984@qq.com
 */
public class FragmentDaySchedule extends Fragment {
	private View parentView;;
	private Day mDay;
	private ListView listView; 

	 public FragmentDaySchedule() {		
		super();

	}

	public void setDay(Day day) {
		this.mDay = day;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initViews(inflater, container);
		return parentView;
	}

	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.schedule_info_day_fragment,
				container, false);
	}

}
