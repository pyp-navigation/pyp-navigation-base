package pyp.navigation.main;

import pyp.navigation.R;
import pyp.navigation.association.AssociationFragment;
import pyp.navigation.home.HomeFragment;
import pyp.navigation.map.MapFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LeftMenuFragment extends ListFragment {

	private MainActivity parentActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] menu_left_items = getResources().getStringArray(R.array.menu_left_item);
		
		ArrayAdapter<String> menu_left_adapter = new ArrayAdapter<String>(getActivity(), 
				R.layout.main_menu_list_item, android.R.id.text1, menu_left_items);
		
		setListAdapter(menu_left_adapter);
		
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
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
		case 4:
			Log.i("qsuron", "退出程序");
			parentActivity.finish();
			break;
		default:
			Log.i("qsuron", "default");
		}
	}


}
