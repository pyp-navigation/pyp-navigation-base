package pyp.navigation.schedule.info;

import java.util.List;

import pyp.navigation.schedule.bean.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CursorListAdapter extends BaseAdapter {
	private List<Cursor> mCursors;

	public CursorListAdapter(List<Cursor> cursors) {
		this.mCursors = cursors;
	}

	@Override
	public int getCount() {

		return mCursors.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
