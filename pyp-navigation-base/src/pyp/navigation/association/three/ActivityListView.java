package pyp.navigation.association.three;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Date;

import pyp.navigation.R;

public class ActivityListView extends ListView {
	private static final int DONE = 3;
	private static final int LOADING = 4;
	private static final int PULL_To_REFRESH = 1;
	private static final int RATIO = 5;
	private static final int REFRESHING = 2;
	private static final int RELEASE_To_REFRESH = 0;
	private static final String TAG = "listview";
	private RotateAnimation animation;
	private ImageView arrowImageView;
	private int firstItemIndex;
	private int firstItemTop;
	private int headContentHeight;
	private int headContentWidth;
	private LayoutInflater inflater;
	private boolean isBack;
	private boolean isRecored;
	private boolean isRefreshable;
	private TextView lastUpdatedTextView;
	private LinearLayout line;
	private ProgressBar progressBar;
	private RotateAnimation reverseAnimation;
	private int startY;
	private int state;
	private TextView tipsTextview;

	public ActivityListView(Context paramContext) {
		super(paramContext);
		init(paramContext);
	}

	public ActivityListView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}


	private void init(Context paramContext) {
		setCacheColorHint(paramContext.getResources().getColor(R.color.transparent));
		this.inflater = LayoutInflater.from(paramContext);
		this.arrowImageView.setMinimumWidth(70);
		this.arrowImageView.setMinimumHeight(50);
		this.line = new LinearLayout(getContext());
		AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(-1, -2);
		this.line.setLayoutParams(localLayoutParams);
		this.line.setOrientation(1);
		addHeaderView(this.line, null, false);
		this.animation = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
		this.animation.setInterpolator(new LinearInterpolator());
		this.animation.setDuration(250L);
		this.animation.setFillAfter(true);
		this.reverseAnimation = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1,0.5F);
		this.reverseAnimation.setInterpolator(new LinearInterpolator());
		this.reverseAnimation.setDuration(200L);
		this.reverseAnimation.setFillAfter(true);
		this.state = 3;
		this.isRefreshable = false;
	}

	private void measureView(View paramView) {
		ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
		if (localLayoutParams == null)
			localLayoutParams = new ViewGroup.LayoutParams(-1, -2);
		int i = ViewGroup.getChildMeasureSpec(0, 0, localLayoutParams.width);
		int j = localLayoutParams.height;
		if (j > 0)
		for (int k = View.MeasureSpec.makeMeasureSpec(j, 1073741824);; 
		k = View.MeasureSpec.makeMeasureSpec(0, 0)) {
			paramView.measure(i, k);
			return;
		}
	}


	public LinearLayout getLine() {
		return this.line;
	}

	public void onScroll(AbsListView paramAbsListView, int paramInt1,
			int paramInt2, int paramInt3) {
		this.firstItemIndex = paramInt1;
	}

	public void setAdapter(BaseAdapter paramBaseAdapter) {
		this.lastUpdatedTextView.setText("上次刷新:" + new Date().toLocaleString());
		super.setAdapter(paramBaseAdapter);
	}

	public void setLine(LinearLayout paramLinearLayout) {
		this.line = paramLinearLayout;
	}

}

/*
 * Location: F:\S-学术性\A-Android\工具\反编译\工程\社团助手\classes_dex2jar.jar Qualified
 * Name: com.example.xiaopangsoc.ui.MyListView JD-Core Version: 0.6.2
 */