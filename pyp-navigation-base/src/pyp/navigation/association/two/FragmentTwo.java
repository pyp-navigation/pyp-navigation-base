package pyp.navigation.association.two;

import pyp.navigation.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Title: FragmentTwo
 * @Description: 社团模块 - 子页面 - 2
 * @author qsuron
 * @date 2014-7-24
 * @email admin@qiushurong.cn
 */
public class FragmentTwo extends Fragment {
	private View parentView;;

	public FragmentTwo() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initViews(inflater, container);
		return parentView;
	}


	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.association_fragment_two,
				container, false);
	}

}
