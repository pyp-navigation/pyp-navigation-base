package pyp.navigation.association;

import java.util.ArrayList;
import java.util.List;

import pyp.navigation.R;
import pyp.navigation.association.one.FragmentOne_List;
import pyp.navigation.association.three.FragmentThree;
import pyp.navigation.association.two.FragmentTwo;
import pyp.navigation.main.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Title: AssociationFragment
 * @Description: 社团模块
 * @author qsuron
 * @date 2014-7-22
 * @email admin@qiushurong.cn
 */
public class AssociationFragment extends Fragment implements OnPageChangeListener {

	private MainActivity parentActivity;
	private View parentView = null;

	// 选项卡序号
	public static final int FRAGMENT_ONE = 0;
	public static final int FRAGMENT_TWO = 1;
	public static final int FRAGMENT_THREE = 2;

	public static final String EXTRA_TAB = "tab";
	public static final String EXTRA_QUIT = "extra.quit";

	protected int mCurrentTab = 0; // 当前tab
	protected int mLastTab = -1; // 下一个tab
	protected ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>(); // 存放选项卡信息的列表
	protected MyViewPageAdapter myAdapter = null; // 显示区域适配器
	protected ViewPager mPager; // 显示区域
	protected TitleIndicator mIndicator; // 选项卡控件

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initViews(inflater, container);
		return parentView;
	}


	public void initViews(LayoutInflater inflater, ViewGroup container) {
		// 这段代码是为了解决BUG(重新打开fragment数据丢失)
		// 通过parentView判断之前是否打开过
//		if (parentView != null) {
//			// Log.i("qsuron", "test-null");
//			// 如果打开过，就获取父视图
//			ViewGroup parent = (ViewGroup) parentView.getParent();
//			// 如果获取到的父视图不为空，就移除该fragment
//			if (parent != null) {
//				// Log.i("qsuron", "test-null-null");
//				parent.removeView(parentView);
//			}
//			// 返回
//			return;
//	}

		
		// 如果之前没打开过，那么照常进行
		parentView = inflater.inflate(R.layout.association_main, container,
				false);

		if (mLastTab < 0)
			mCurrentTab = supplyTabs(mTabs);

		myAdapter = new MyViewPageAdapter(parentActivity,
				parentActivity.getSupportFragmentManager(), mTabs);

		Log.i("qsuron", "myAdapter - " + myAdapter.getCount());

		mPager = (ViewPager) parentView.findViewById(R.id.pager);
		mPager.setAdapter(myAdapter);
		mPager.setOnPageChangeListener(this);
		mPager.setOffscreenPageLimit(mTabs.size());

		Log.i("qsuron", "mTab.size() = " + mTabs.size());

		mIndicator = (TitleIndicator) parentView
				.findViewById(R.id.pagerindicator);
		mIndicator.init(mCurrentTab, mTabs, mPager);

		mPager.setCurrentItem(mCurrentTab);
		mLastTab = mCurrentTab;

		// 设置viewpager内部页面之间的间距
		mPager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.page_margin_width));
		// 设置viewpager内部页面间距的drawable
		mPager.setPageMarginDrawable(R.color.page_viewer_margin_color);
	}


	/**
	 * 返回layout id
	 * 
	 * @return layout id
	 */
	protected int getMainViewResId() {
		return R.layout.association_main;
	}

	/**
	 * 方法 addTabInfo 添加一个选项卡
	 * 
	 * @param tab
	 *            void
	 */
	public void addTabInfo(TabInfo tab) {
		mTabs.add(tab);
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * 方法 addTabInfos 从列表添加选项卡
	 * 
	 * @param tabs
	 *            fragment列表
	 */
	public void addTabInfos(ArrayList<TabInfo> tabs) {
		mTabs.addAll(tabs);
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * 方法 getFragmentById 通过ID获取对应的选项卡对象
	 * 
	 * @param tabId
	 *            选项卡id
	 * @return TabInfo 选项卡
	 */
	protected TabInfo getFragmentById(int tabId) {
		if (mTabs == null)
			return null;
		for (int index = 0, count = mTabs.size(); index < count; index++) {
			TabInfo tab = mTabs.get(index);
			if (tab.getId() == tabId) {
				return tab;
			}
		}
		return null;
	}

	/**
	 * 方法 navigate 跳转到任意选项卡
	 * 
	 * @param tabId
	 *            选项卡下标
	 */
	public void navigate(int tabId) {
		for (int index = 0, count = mTabs.size(); index < count; index++) {
			if (mTabs.get(index).getId() == tabId) {
				mPager.setCurrentItem(index);
			}
		}
	}

	/**
	 * 方法 getIndicator 取得选项卡对象
	 * 
	 * @return TitleIndicator 选项卡对象
	 */
	public TitleIndicator getIndicator() {
		return mIndicator;
	}

	/**
	 * 方法 supplyTabs 初始化内容列表
	 * 
	 * @param tabs
	 *            fragment列表
	 * @return int 默认进入后显示的首页
	 */
	protected int supplyTabs(List<TabInfo> tabs) {
		Log.i("qsuron", "supplyTabs");
		tabs.add(new TabInfo(FRAGMENT_ONE,
				getString(R.string.test_fragment_one), FragmentOne_List.class));
		tabs.add(new TabInfo(FRAGMENT_TWO,
				getString(R.string.test_fragment_two), FragmentTwo.class));
		tabs.add(new TabInfo(FRAGMENT_THREE,
				getString(R.string.test_fragment_three), true,
				FragmentThree.class));
		return FRAGMENT_TWO;
	}

	/**
	 * 重写 onPageScrolled
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int,
	 *      float, int)
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		mIndicator.onScrolled((mPager.getWidth() + mPager.getPageMargin())
				* position + positionOffsetPixels);
	}

	/**
	 * 重写 onPageSelected
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
	 */
	@Override
	public void onPageSelected(int position) {
		mIndicator.onSwitched(position);
		mCurrentTab = position;
	}

	/**
	 * 重写 onPageScrollStateChanged
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			mLastTab = mCurrentTab;
		}
	}

}