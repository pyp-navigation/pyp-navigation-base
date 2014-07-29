package pyp.navigation.main.menu;

import java.util.ArrayList;
import java.util.HashMap;

import pyp.navigation.R;
import pyp.navigation.association.AssociationFragment;
import pyp.navigation.home.HomeFragment;
import pyp.navigation.main.MainActivity;
import pyp.navigation.map.MapFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title: LeftMenuFragment
 * @Description: 左菜单栏 - 仿天天动听菜单
 * @author qsuron
 * @date 2014-7-28
 * @email admin@qiushurong.cn
 */
public class LeftMenuFragment extends Fragment implements OnItemClickListener, OnClickListener{
	private View parentView;
	private MainActivity parentActivity;
	private GridView mGridView;
	private ImageView mImageView_avatar;
	private TextView mTextView_exit;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.main_slidingmenu, null);
		mGridView = (GridView) parentView.findViewById(R.id.main_slidingmenu_gridview);
		mImageView_avatar = (ImageView) parentView.findViewById(R.id.main_slidingmenu_head_avatarframe_avatar);
		mTextView_exit = (TextView) parentView.findViewById(R.id.main_slidingmenu_bottom_exit);
		return parentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		/*****************
		 * 初始化菜单按钮 *
		 *****************/
		
		
		//从配置文件读取菜单按钮
		String[] menu_left_item_icon = getResources().getStringArray(R.array.menu_left_item_icon);
		String[] menu_left_item_title = getResources().getStringArray(R.array.menu_left_item_title);
		
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		
		if(menu_left_item_title.length!=menu_left_item_icon.length)
			Log.i("qsuron", "配置文件出错 - Array.xml");
		
		//遍历数组，放到list<map<String,Object>>中
		for(int i=0;i<menu_left_item_title.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>(); 
            int resId = getResources().getIdentifier(menu_left_item_icon[i], "drawable", "pyp.navigation");
            map.put("icon", resId);						//添加图像资源的ID 
            map.put("title", menu_left_item_title[i]);	//按序号做ItemText
            list.add(map);
        }
		
		//用适配器为GridView设置内容
		SimpleAdapter sa= new SimpleAdapter(
				parentActivity,list,
				R.layout.main_slidingmenu_gridview_item,
				new String[]{"icon","title"},
				new int[]{
						R.id.main_slidingmenu_gridview_item_image,
						R.id.main_slidingmenu_gridview_item_text});
        mGridView.setAdapter(sa);
        
        /**************************
		 * 初始化所有按钮的监听事件 *
		 **************************/
        
        mGridView.setOnItemClickListener(this);
        
        mImageView_avatar.setOnClickListener(this);
        
		mTextView_exit.setOnClickListener(this);
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//具体序号要参考 array.xml 中设置的顺序
		switch (position) {
		case 0:
			Log.i("qsuron", "主页");
			parentActivity.changeFragment(HomeFragment.class.getName());
			break;
		case 1:
			Log.i("qsuron", "社团");
			parentActivity.changeFragment(AssociationFragment.class.getName());
			break;
		case 2:
			Log.i("qsuron", "地图");
			parentActivity.changeFragment(MapFragment.class.getName());
			break;
		case 3:
			Log.i("qsuron", "课程表");
			Toast.makeText(this.getActivity(), "课程表开发中", Toast.LENGTH_SHORT).show();
			break;
		default:
			Log.i("qsuron", "技术人员不要命开发中...");
			Toast.makeText(this.getActivity(), "技术人员不要命开发中...", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.main_slidingmenu_head_avatarframe_avatar:
			Log.i("qsuron", "登录-技术人员不要命开发中...");
			Toast.makeText(this.getActivity(), "登录-技术人员不要命开发中...", Toast.LENGTH_SHORT).show();
			break;
		case R.id.main_slidingmenu_bottom_exit:
			Log.i("qsuron", "退出程序");
			parentActivity.finish();
		default:
			Log.i("qsuron", "不存在此监听事件");
		}
	}
}


