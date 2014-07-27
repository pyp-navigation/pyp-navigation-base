package pyp.navigation.association.one;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pyp.navigation.R;
import pyp.navigation.association.bean.Association;
import pyp.navigation.association.service.PinyinComparator;
import pyp.navigation.association.service.ReadXmlByPullService;
import pyp.navigation.main.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

/**
 * @Title: AssociationFragment
 * @Description: 社团模块主界面
 * @author qsuron
 * @date 2014-7-25
 * @email admin@qiushurong.cn
 */
public class FragmentOne_List extends Fragment {

	/**
	 * @Title: 接口 OnAssociationListener
	 * @Description: 用于回调Activity，实现fragment之间的通信
	 * @author qsuron
	 * @date 2014-7-20
	 * @email admin@qiushurong.cn
	 */
	public interface OnAssociationListener {
		public void onNewFragment(int id, String name);
	}

	/**
	 * 字段 List<Association> ： list 存放社团信息的列表
	 */
	private List<Association> list = new ArrayList<Association>();
	private View parentView;
	private ListView mListView;
	private SideBar indexBar;
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private View head;
	private MainActivity parentActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			parentActivity = (MainActivity) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initData();
		initViews(inflater, container);
		initListensers();
		return parentView;
	}

	public void initData() {
		// 读取XML取得社团列表：Assocaition{key,name,key}
		InputStream inputStream = FragmentOne_List.class.getClassLoader()
				.getResourceAsStream("AllAssociation.xml");
		try {
			list = ReadXmlByPullService.ReadXmlByPull(inputStream);
		} catch (Exception e) {
			list.add(new Association("A", "读取XML异常", "null"));
		}
		// 根据a-z进行排序
		Collections.sort(list, new PinyinComparator());
	}

	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.association_fragment_one,
				container, false);
		parentActivity = (MainActivity) this.getActivity();
		// 启动activity时不自动弹出软键盘
		parentActivity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		indexBar = (SideBar) parentView
				.findViewById(R.id.association_main_sideBar);
		mDialogText = (TextView) LayoutInflater.from(parentActivity).inflate(
				R.layout.association_fragment_one_toast, null);
		mDialogText.setVisibility(View.INVISIBLE);
		head = LayoutInflater.from(parentActivity).inflate(
				R.layout.association_fragment_one_search, null);
		mListView = (ListView) parentView
				.findViewById(R.id.association_main_list);
		mListView.addHeaderView(head);
		mWindowManager = (WindowManager) parentActivity
				.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(mDialogText, getLayoutParams());
		indexBar.setTextView(mDialogText);
		// 为listView设置内容适配类
		mListView.setAdapter(new MyListAdapter(parentActivity, list));
		// 设置SideBar的ListView内容实现点击a-z中任意一个进行定位
		indexBar.setListView(mListView);
		// 隐藏滚动条
		mListView.setVerticalScrollBarEnabled(true);
	}

	public void initListensers() {
		mListView.setOnItemClickListener(mListViewListener);
	}

	/**
	 * 方法 getLayoutParams 获取布局参数
	 * 
	 * @return WindowManager.LayoutParams
	 */
	private WindowManager.LayoutParams getLayoutParams() {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		return lp;
	}

	/**
	 * 字段 OnItemClickListener ： mListViewListener mListView监听器
	 */
	OnItemClickListener mListViewListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String association_ID = list.get(position - 1).getId();
			if (association_ID == null || association_ID.equals("")
					|| view.equals(head)) {
				// Log.i("qsuron", "!!!!!!!! getId() = null");
				return;
			}
			// Log.i("qsuron", "onNewFragment : "+id);
			
			// 切换到详情页面
			parentActivity.showAssociationDetail(list.get(position - 1));
		}
	};
}
