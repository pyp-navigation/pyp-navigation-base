package pyp.navigation.association.three;

import pyp.navigation.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Title: FragmentThree
 * @Description: 社团模块 - 子页面 - 3
 * @author qsuron
 * @date 2014-7-24
 * @email admin@qiushurong.cn
 */
public class FragmentThree extends Fragment {
	private View parentView;;

	public FragmentThree() {
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
