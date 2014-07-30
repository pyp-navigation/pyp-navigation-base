package pyp.navigation.schedule;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * @Title: MyAdapter
 * @Description: 社团模块 - 主界面 - 内容页ViewPage的适配器
 * @author qsuron
 * @date 2014-7-22
 * @email admin@qiushurong.cn
 */
public class MyViewPageAdapter extends FragmentPagerAdapter {
	ArrayList<TabInfo> tabs = null;
	Context context = null;

	public MyViewPageAdapter(Context context, FragmentManager fm,
			ArrayList<TabInfo> tabs) {
		super(fm);
		this.tabs = tabs;
		this.context = context;
	}

	@Override
	public Fragment getItem(int pos) {
		Fragment fragment = null;
		if (tabs != null && pos < tabs.size()) {
			TabInfo tab = tabs.get(pos);
			if (tab == null)
				return null;
			fragment = tab.createFragment();
		}
		return fragment;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		if (tabs != null && tabs.size() > 0)
			return tabs.size();
		return 0;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TabInfo tab = tabs.get(position);
		// Log.i("qsuron","this - "+this);
		// Log.i("qsuron","container - "+container);
		// Log.i("qsuron","position - "+position);
		Fragment fragment = (Fragment) super.instantiateItem(container,
				position);

		tab.fragment = fragment;
		return fragment;
	}
}