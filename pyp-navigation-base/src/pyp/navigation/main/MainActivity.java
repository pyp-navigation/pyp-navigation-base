package pyp.navigation.main;

import pyp.navigation.R;
import pyp.navigation.association.AssociationFragment;
import pyp.navigation.association.bean.Association;
import pyp.navigation.association.detail.AssociationDetailFragment;
import pyp.navigation.home.HomeFragment;
import pyp.navigation.map.MapFragment;
import pyp.navigation.setting.SettingFragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Interpolator;
import android.widget.Toast;


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
public class MainActivity extends SlidingFragmentActivity {
	
	private long close_waittime = 2000;								//关闭程序确认延迟时间(毫秒)
	private long close_touchtime = 0;
	
	private CanvasTransformer mTransformer;
	private FragmentManager mFragmentManager = getSupportFragmentManager();
	
    
	
	//缓存所有Fragment
	//TODO - 添加Fragment
	private final MapFragment mMapFragment = new MapFragment();
	private final HomeFragment mHomeFragment = new HomeFragment();
	private final SettingFragment mSettingFragment = new SettingFragment();
	private final AssociationFragment mAssociationFragment = new AssociationFragment();
	private final AssociationDetailFragment mAssociationDetailFragment = new AssociationDetailFragment();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置标题栏的标题
		setTitle("番职e家");

		//设置ActionBar是否跟着滑动
		setSlidingActionBarEnabled(false);

		//设置是否显示ActionBar上的查看菜单按钮(<)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//设置主界面视图
		setContentView(R.layout.main_content_frame);	

		//初始化滑动动画
		initAnimation();
		
		//初始化滑动菜单
		initSlidingMenu(savedInstanceState);
		
		//初始化Fragment
		initFragment();
		
	}


    
	/**
	 * 方法 initAnimation
	 * 初始化滑动动画
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
	 * 初始化滑动菜单
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
	
	
	
	//动画3 - silde up
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
	@SuppressWarnings("unused")
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
	 * 菜单按钮点击事件，通过点击ActionBar的Home图标按钮来打开滑动菜单
	 */
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;		
		}
		return super.onOptionsItemSelected(item);
	}

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
//        		Log.i("qsuron", "菜单栏打开着");
//        		long currentTime = System.currentTimeMillis();
//    			if ((currentTime - close_touchtime) >= close_waittime) {
//    				Toast.makeText(this, "再按一次退出程序?", Toast.LENGTH_SHORT).show();
//    				close_touchtime = currentTime;
//    			} else {
//    				Log.i("qsuron", "返回键-退出程序 finish()");
//    				finish();
//    			}
        	}else{
        		Log.i("qsuron", "菜单栏关着");
        		getSlidingMenu().showMenu();
        		return;
        	}
			super.onBackPressed();
		}
	}
	
	/********************************************************
	 * 关于Fragment的所有代码，在下方！每加一个Fragment都要修改
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
	
	
	
	//隐藏键盘
	//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	//imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
		
}
