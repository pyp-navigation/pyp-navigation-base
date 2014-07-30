package pyp.navigation.main.menu;

import pyp.navigation.R;
import pyp.navigation.home.HomeFragment;
import pyp.navigation.main.MainActivity;
import pyp.navigation.setting.SettingFragment;
import pyp.navigation.update.UpdateManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RightMenuFragment extends ListFragment {

	private MainActivity parentActivity;
	private UpdateManager update;
	
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
		
		String[] menu_right_items = getResources().getStringArray(R.array.menu_right_item);
		
		ArrayAdapter<String> menu_right_adapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, menu_right_items);
		
		setListAdapter(menu_right_adapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		switch (position) {
		case 0:
			Log.i("qsuron", "设置");
			parentActivity.changeFragment(SettingFragment.class.getName());
			break;
		case 1:
			Log.i("qsuron", "检查新版本");
			parentActivity.check4update();
			break;
		default:
			Log.i("qsuron", "default");
		}
	}
	

}
