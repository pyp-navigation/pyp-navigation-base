package pyp.navigation.setting;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import pyp.navigation.R;
import pyp.navigation.main.MainActivity;
import pyp.navigation.main.RightMenuFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingFragment extends Fragment {
	private View parentView;
	private MainActivity parentActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.setting, container, false);
		if (getActivity() instanceof MainActivity) 
			parentActivity = (MainActivity) getActivity();
		initView(parentView);
		return parentView;
	}
	
	
	/**
	 * 初始化组件
	 */
	private void initView(View parentView) {
		// 设置滑动菜单的位置（左边、右边或者左右两边都有）
		RadioGroup mode = (RadioGroup) parentView.findViewById(R.id.mode);
		// 默认勾选双栏
		mode.check(R.id.left_right);
		mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				SlidingMenu sm = parentActivity.getSlidingMenu();
				switch (checkedId) {
				case R.id.left:
					sm.setMode(SlidingMenu.LEFT);
					sm.setShadowDrawable(R.drawable.main_shadow);
					break;
				case R.id.right:
					sm.setMode(SlidingMenu.RIGHT);
					sm.setShadowDrawable(R.drawable.main_shadowright);
					break;
				case R.id.left_right:
					sm.setMode(SlidingMenu.LEFT_RIGHT);
					sm.setSecondaryMenu(R.layout.main_menu_frame_two);					
					parentActivity.getSupportFragmentManager().beginTransaction()
					 						   .replace(R.id.menu_frame_two,
					 						new RightMenuFragment()).commit();
					sm.setSecondaryShadowDrawable(R.drawable.main_shadowright);
					sm.setShadowDrawable(R.drawable.main_shadow);
				}
			}
		});

		// 设置触摸的模式（全屏触摸滑动、边缘触摸滑动或者触摸不能滑动）
		RadioGroup touchAbove = (RadioGroup) parentView.findViewById(R.id.touch_above);
		touchAbove.check(R.id.touch_above_full);
		touchAbove.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.touch_above_full:
					parentActivity.setTouchMode(1);
					break;
				case R.id.touch_above_margin:
					parentActivity.setTouchMode(2);
					break;
				case R.id.touch_above_none:
					parentActivity.setTouchMode(3);
					break;
				}
			}
		});

		// 设置滑动菜单滑动时缩放的效果（值越大效果越明显）
		SeekBar scrollScale = (SeekBar) parentView.findViewById(R.id.scroll_scale);
		scrollScale.setMax(1000);
		scrollScale.setProgress(333);
		scrollScale.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				parentActivity.getSlidingMenu().setBehindScrollScale((float) seekBar.getProgress() / seekBar.getMax());
			}
		});

		// 设置滑动菜单时下方视图的宽度（值越大宽度越大）
		SeekBar behindWidth = (SeekBar) parentView.findViewById(R.id.behind_width);
		behindWidth.setMax(1000);
		behindWidth.setProgress(750);
		behindWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				float percent = (float) seekBar.getProgress() / seekBar.getMax();
				parentActivity.getSlidingMenu().setBehindWidth((int) (percent * parentActivity.getSlidingMenu().getWidth()));
				parentActivity.getSlidingMenu().requestLayout();
			}
		});

		// 设置滑动菜单滑动时的阴影效果（值越大效果越明显）
		CheckBox shadowEnabled = (CheckBox) parentView.findViewById(R.id.shadow_enabled);
		shadowEnabled.setChecked(true);
		shadowEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked)
							parentActivity.getSlidingMenu().setShadowDrawable(parentActivity.getSlidingMenu().getMode() == SlidingMenu.LEFT ? R.drawable.main_shadow: R.drawable.main_shadowright);
						else
							parentActivity.getSlidingMenu().setShadowDrawable(null);
					}
				});
		SeekBar shadowWidth = (SeekBar) parentView.findViewById(R.id.shadow_width);
		shadowWidth.setMax(1000);
		shadowWidth.setProgress(75);
		shadowWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				float percent = (float) seekBar.getProgress() / (float) seekBar.getMax();
				int width = (int) (percent * (float) parentActivity.getSlidingMenu().getWidth());
				
				parentActivity.getSlidingMenu().setShadowWidth(width);
				parentActivity.getSlidingMenu().invalidate();
			}
		});

		// 设置滑动菜单滑动时渐入渐出的效果（值越大效果越明显）
		CheckBox fadeEnabled = (CheckBox) parentView.findViewById(R.id.fade_enabled);
		fadeEnabled.setChecked(true);
		fadeEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						parentActivity.getSlidingMenu().setFadeEnabled(isChecked);
					}
				});
		SeekBar fadeDeg = (SeekBar) parentView.findViewById(R.id.fade_degree);
		fadeDeg.setMax(1000);
		fadeDeg.setProgress(666);
		fadeDeg.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				parentActivity.getSlidingMenu().setFadeDegree(
						(float) seekBar.getProgress() / seekBar.getMax());
			}
		});
		
				
	}
}
