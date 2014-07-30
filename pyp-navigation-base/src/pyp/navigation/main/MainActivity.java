package pyp.navigation.main;

import pyp.navigation.R;
import pyp.navigation.association.AssociationFragment;
import pyp.navigation.association.bean.Association;
import pyp.navigation.association.detail.AssociationDetailFragment;
import pyp.navigation.home.HomeFragment;
import pyp.navigation.main.menu.LeftMenuFragment;
import pyp.navigation.main.menu.RightMenuFragment;
import pyp.navigation.map.MapFragment;
import pyp.navigation.setting.SettingFragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.animation.Interpolator;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @Title: MainActivity
 * @Description: 程序入口
 * @author qsuron
 * @date 2014-7-27
 * @email admin@qiushurong.cn
 */
public class MainActivity extends SlidingFragmentActivity implements ActionBar.TabListener{
	

	private static final boolean ACTIONBAR_LOGO = true;
	private static final boolean ACTIONBAR_SCOLL = true;
	private static final boolean ACTIONBAR_TITLE = true;
	private static final boolean ACTIONBAR_HOME_BTN = true;
	
	private CanvasTransformer mTransformer;
	private FragmentManager mFragmentManager = getSupportFragmentManager();
	
	//缓存 - 所有Fragment
	//TODO - 添加Fragment
	private final MapFragment mMapFragment = new MapFragment();
	private final HomeFragment mHomeFragment = new HomeFragment();
	private final SettingFragment mSettingFragment = new SettingFragment();
	private final AssociationFragment mAssociationFragment = new AssociationFragment();
	private final AssociationDetailFragment mAssociationDetailFragment = new AssociationDetailFragment();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_content_frame);
		setTitle("番职e家");						//设置 - 标题栏的标题
		//initActionBar();						//初始化 - ActionBar
		initAnimation();						//初始化 - 滑动动画
		initSlidingMenu(savedInstanceState);	//初始化 - 滑动菜单
		initFragment();							//初始化 - Fragment
	}


	/**
	 * 方法 initAnimation
	 * 初始化 - 滑动动画
	 */
	private void initAnimation() {
		mTransformer = new CanvasTransformer(){
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				//动画1 - Zoom
				//float scale = (float) (percentOpen*0.25 + 0.75);
				//canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
				//动画3 - silde up
				canvas.translate(0, canvas.getHeight() * (1 - interp.getInterpolation(percentOpen)));    
			}
		};
	}

	
	/**
	 * 方法 initSlidingMenu
	 * 初始化滑动菜单
	 * @param savedInstanceState
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 设置滑动菜单的视图
		setBehindContentView(R.layout.main_menu_frame);
			
		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动阴影的图像资源
		sm.setShadowDrawable(R.drawable.main_shadow);
		// 设置滑动菜单视图的宽度 //---***---
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);	
		// 设置动画 After initAnimation
		sm.setBehindCanvasTransformer(mTransformer);
		
		// 双菜单栏
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		// 左菜单栏
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftMenuFragment()).commit();
		// 右菜单栏
		sm.setSecondaryMenu(R.layout.main_menu_frame_two);
		sm.setSecondaryShadowDrawable(R.drawable.main_shadowright);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two, new RightMenuFragment()).commit();
				
	}
	
	
	/**
	 * 字段 Interpolator ： interp
	 * 动画3 - silde up
	 */
	private static Interpolator interp = new Interpolator() {  
        @Override  
        public float getInterpolation(float t) {  
            t -= 1.0f;  
            return t * t * t + 1.0f;  
        }
    }; 
    
	/**
	 * 方法 setTouchMode
	 * 方法描述
	 * @param mode int
	 * 1 : 全屏
	 * 2 : 边缘
	 * 3 : 全禁止
	 */
	public void setTouchMode(int mode){
		switch (mode) {
		case 1:
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			break;
		case 2:
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			break;
		case 3:
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			break;
		}
	}
	
	
	

	/**
	 * 重写 onBackPressed
	 * 实现返回键功能
	 */
	@Override
	public void onBackPressed() {
		//如果是在社团详情页面，就返回到社团列表页面
		if(mAssociationDetailFragment.isVisible()) {
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			ft.show(mAssociationFragment);
			ft.hide(mAssociationDetailFragment);
			ft.commit();
		} else {
			//如果菜单栏打开着
        	if(getSlidingMenu().isMenuShowing() || getSlidingMenu().isSecondaryMenuShowing()){
        		//再按一次退出，废弃了
        	}else{
        		Log.i("qsuron", "菜单栏关着");
        		getSlidingMenu().showMenu();
        		return;
        	}
			super.onBackPressed();
		}
	}
	
	
	/********************************************************
	 * 关于Fragment的所有代码，在下方！每次添加Fragment都要修改
	 ********************************************************/
	
	
	/**
	 * 方法 initFragment
	 * 初始化FragmentManager，默认显示HomeFragment，隐藏其他
	 */
	private void initFragment(){
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		//TODO - 添加Fragment
		ft.add(R.id.content_frame, mMapFragment, MapFragment.class.getName());
		ft.add(R.id.content_frame, mHomeFragment, HomeFragment.class.getName());
		ft.add(R.id.content_frame, mSettingFragment, SettingFragment.class.getName());
		ft.add(R.id.content_frame, mAssociationFragment, AssociationFragment.class.getName());
		ft.add(R.id.content_frame, mAssociationDetailFragment, AssociationDetailFragment.class.getName());
		ft.hide(mAssociationFragment);
		ft.hide(mMapFragment);
		ft.hide(mSettingFragment);
		ft.hide(mAssociationDetailFragment);
		ft.commit();
	}
	
	/**
	 * 方法 changeFragment
	 * 切换到fragment
	 * @param mFragment 要切换到的fragment的包名+类名
	 * 可以用 .class.getName()获取
	 */
	public void changeFragment(String mFragment){
		if(mFragmentManager.findFragmentByTag(mFragment).isVisible()){
			Log.i("qsuron", "已经处于要切换的页面，无需切换");
			getSlidingMenu().showContent();
			return;
		}else if(mFragmentManager.findFragmentByTag(mFragment)!=null){
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			//TODO - 添加Fragment
			ft.hide(mAssociationFragment);
			ft.hide(mMapFragment);
			ft.hide(mSettingFragment);
			ft.hide(mAssociationDetailFragment);
			ft.hide(mHomeFragment);
			ft.show(mFragmentManager.findFragmentByTag(mFragment));
			ft.commit();
			changeTouchMode(mFragment);
			getSlidingMenu().showContent();
		}else{
			getSlidingMenu().showContent();
		}
	}
	
	
	/**
	 * 方法 changeTouchMode
	 * 根据下一个fragment的类名包名，来改变菜单触摸触发方式
	 * @param mFragment 下一个fragment的类名.包名
	 */
	private void changeTouchMode(String mFragment) {
		if(mFragment.equals(AssociationFragment.class.getName())){
			setTouchMode(2);
			return;
		}
		if(mFragment.equals(MapFragment.class.getName())){
			setTouchMode(2);
			return;
		}
		setTouchMode(1);
	}



	/**
	 * 方法 showAssociationDetail
	 * 隐藏社团列表页面 - 传入社团信息 - 打开社团详情页面
	 * @param mAssociation 社团信息封装类
	 */
	public void showAssociationDetail(Association mAssociation) {	
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.hide(mAssociationFragment);
		ft.show(mAssociationDetailFragment);
		ft.commit();
		mAssociationDetailFragment.setAssociation(mAssociation);
	}
	
	
	/**
	 * 方法 hideAssociationDetail
	 * 隐藏社团详情页面 - 回到社团列表页面
	 */
	public void hideAssociationDetail() {	
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.hide(mAssociationDetailFragment);
		ft.show(mAssociationFragment);
		ft.commit();
	}


	/*****************************************************
	 *  ActionBar 相关
	 *****************************************************/
	
    
	/**
	 * 方法 initActionBar
	 * 初始化 - ActionBar
	 */
	private void initActionBar() {
		//设置 - ActionBar是否跟着滑动
		setSlidingActionBarEnabled(ACTIONBAR_SCOLL);
		//设置 - 是否显示ActionBar上的查看菜单按钮(<)
		getSupportActionBar().setDisplayHomeAsUpEnabled(ACTIONBAR_HOME_BTN);
		//设置 - 是否显示ActionBar上的LOGO
		getSupportActionBar().setDisplayUseLogoEnabled(ACTIONBAR_LOGO);
		//设置 - 是否显示ActionBar上的标题
		getSupportActionBar().setDisplayShowTitleEnabled(ACTIONBAR_TITLE);
		//设置 - 是否显示ActionBar背景
		//getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.color.main_actionbar_bgcolor));
	}
	
	
	/**
	 * 重写 onOptionsItemSelected
	 * 菜单按钮点击事件，通过点击ActionBar的Home图标按钮来打开滑动菜单
	 */
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
        	toggle();
            return false;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	

	    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	    }



		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}



		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

	    
	/*****************************************************
	 *  下面是一些 暂时没用到，可能会有用的代码段
	 *****************************************************/
	
	
	//隐藏键盘
	//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	//imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
}
