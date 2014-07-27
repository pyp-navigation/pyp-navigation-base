package pyp.navigation.association.detail;

import pyp.navigation.R;
import pyp.navigation.association.bean.Association;
import pyp.navigation.main.MainActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title: AssociationDetailFragment
 * @Description: 社团模块 - 社团信息详情
 * @author qsuron
 * @date 2014-7-26
 * @email admin@qiushurong.cn
 */
public class AssociationDetailFragment extends Fragment {

	private MainActivity parentActivity;
	private View parentView;;
	private Button btn_main_titlebar_left_menu; 	// 界面左菜单按钮
	private Button btn_main_titlebar_right_menu; 	// 界面右菜单按钮
	private TextView titlebar_title; 				// 界面ActionBar中央文本

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initViews(inflater, container);
		initListensers();
		return parentView;
	}

	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.association_info_layout_main,
				container, false);
		// 菜单栏左右按钮
		btn_main_titlebar_left_menu = (Button) parentView
				.findViewById(R.id.association_info_menu_left);
		btn_main_titlebar_right_menu = (Button) parentView
				.findViewById(R.id.association_info_menu_right);
		// 菜单栏中央文本
		titlebar_title = (TextView) parentView
				.findViewById(R.id.association_info_title);
	}

	public void initListensers() {
		btn_main_titlebar_left_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//隐藏社团详情页面 - 回到社团列表页面
						parentActivity.hideAssociationDetail();
					}
				});
		btn_main_titlebar_right_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(parentActivity, "收藏", Toast.LENGTH_SHORT).show();
					}
				});
	}

	public void setAssociation(Association mAssociation) {
		titlebar_title.setText(mAssociation.getName());
		Toast.makeText(parentActivity, "社团ID：" + mAssociation.getId(),Toast.LENGTH_SHORT).show();
	}

}