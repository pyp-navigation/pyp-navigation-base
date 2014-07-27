package pyp.navigation.home;

import pyp.navigation.R;
import pyp.navigation.main.MainActivity;
import pyp.navigation.setting.SettingFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

/**
 * @Title: HomeFragment
 * @Description: 主页模块页面
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class HomeFragment extends Fragment {

	private MainActivity parentActivity;
	private View parentView;
	private Button btn_open_menu;
	private GifView gif_loading;
	private Button btn_main_titlebar_left_menu; // 界面做左菜单按钮
	private Button btn_main_titlebar_right_menu; // 界面做右菜单按钮

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initViews(inflater, container);
		initListensers();
		return parentView;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = (MainActivity) activity;
	}


	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.home, container, false);
		
		gif_loading = (GifView) parentView.findViewById(R.id.home_imageView);
		gif_loading.setGifImage(R.drawable.home_loading);
		// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
		gif_loading.setGifImageType(GifImageType.COVER);
		//gif_loading.setShowDimension(170, 160);
		btn_open_menu = (Button) parentView.findViewById(R.id.home_btn_open_menu);
		// 菜单栏左右按钮
		btn_main_titlebar_left_menu = (Button) parentView.findViewById(R.id.home_titlebar_left_menu);
		btn_main_titlebar_right_menu = (Button) parentView.findViewById(R.id.home_titlebar_right_menu);
	}

	public void initListensers() {
		btn_open_menu.setOnClickListener(btn_open_menu_listener);
		btn_main_titlebar_left_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(getActivity(), "左按钮", Toast.LENGTH_SHORT).show();
					}
				});
		btn_main_titlebar_right_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(getActivity(), "右按钮", Toast.LENGTH_SHORT).show();
					}
				});
	}

	/**
	 * 按钮(打开菜单栏)监听器
	 */
	View.OnClickListener btn_open_menu_listener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			parentActivity.changeFragment(SettingFragment.class.getName());
		}
	};
	

}