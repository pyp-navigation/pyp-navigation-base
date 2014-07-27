package pyp.navigation.map;

import pyp.navigation.map.bean.Marker;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Title: SampleCallout
 * @Description: 这是一个简单的View用于显示callouts,这是旧版本的可能存在很多错误，但是和起来方便，好看。
 * @author 张伟杰
 * @date 2014-7-23
 * @email 531724220@qq.com
 */
public class MapCallout extends RelativeLayout {

	/**
	 *他们弃用了setBackgroundDrawable，正是如此, 才重命名新方法(setBackground)但是这个新方法不能在低版本的SDKs中工作，
	 *并且使用老方法(setBackgroundDrawable)会有弃用警告。期望不要在项目移至低版本的sdk时，每次调用fork conditionals设定可拉的视图。
	 *特此警告。
	 */
	@SuppressWarnings("deprecation")
	public MapCallout( Context context, Marker marker) {

		super( context );

		LinearLayout bubble = new LinearLayout( context );
		bubble.setOrientation( LinearLayout.HORIZONTAL );
		int[] colors = { 0xE6888888, 0xFF000000 };
		GradientDrawable drawable = new GradientDrawable( GradientDrawable.Orientation.TOP_BOTTOM, colors );
		drawable.setCornerRadius( 6 );
		drawable.setStroke( 2, 0xDD000000 );
		bubble.setBackgroundDrawable( drawable );
		bubble.setId( 1 );
		bubble.setGravity( Gravity.CENTER_VERTICAL );
		bubble.setPadding( 10, 10, 10, 10 );
		LayoutParams bubbleLayout = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		addView( bubble, bubbleLayout );

		Nub nub = new Nub( context );
		LayoutParams nubLayout = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		nubLayout.addRule( RelativeLayout.BELOW, bubble.getId() );
		nubLayout.addRule( RelativeLayout.CENTER_IN_PARENT );
		addView( nub, nubLayout );

		LinearLayout labels = new LinearLayout( context );
		labels.setGravity( Gravity.CENTER_VERTICAL );
		labels.setOrientation( LinearLayout.VERTICAL );
		LinearLayout.LayoutParams labelLayout = new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		labelLayout.setMargins( 12, 0, 12, 0 );
		bubble.addView( labels, labelLayout );

		//设置callout中的标题内容
		TextView titleView = new TextView( getContext() );
		titleView.setTextColor( 0xFFFFFFFF );
		titleView.setTextSize( 15 );
		titleView.setMaxWidth( 250 );
		titleView.setTypeface( Typeface.SANS_SERIF, Typeface.BOLD );
		titleView.setText( marker.getPositionName());
		labels.addView( titleView );

		//设置callout的子标题内容
		TextView subTitleView = new TextView( getContext() );
		subTitleView.setTextColor( 0xFFFFFFFF );
		subTitleView.setTextSize( 12 );
		subTitleView.setTypeface( Typeface.SANS_SERIF );
		subTitleView.setText( marker.getInfo() );
		labels.addView( subTitleView );

	}

	/**
	 * 方法 transitionIn
	 * 方法描述:对callout的动画效果
	 *    android中提供了4中动画：   
	 *		 AlphaAnimation 透明度动画效果   
	 *		 ScaleAnimation 缩放动画效果   
	 *		 TranslateAnimation 位移动画效果   
	 *		 RotateAnimation 旋转动画效果  
	 * void
	 */
	public void transitionIn() {

		// 设置缩放动画效果
		ScaleAnimation scaleAnimation = new ScaleAnimation( 0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f );
		scaleAnimation.setInterpolator( new OvershootInterpolator( 1.2f ) );
		scaleAnimation.setDuration( 250 );

		// 设置透明度动画效果
		AlphaAnimation alphaAnimation = new AlphaAnimation( 0, 1f );
		alphaAnimation.setDuration( 200 );

		//　AnimationSet提供了一个把多个动画组合成一个组合的机制
		AnimationSet animationSet = new AnimationSet( false );

		animationSet.addAnimation( scaleAnimation );
		animationSet.addAnimation( alphaAnimation );

		startAnimation( animationSet );

	}

	private static class Nub extends View {

		private Paint paint = new Paint();
		private Path path = new Path();

		public Nub( Context context ) {

			super( context );

			paint.setStyle( Paint.Style.FILL );
			paint.setColor( 0xFF000000 );
			paint.setAntiAlias( true );

			path.lineTo( 20, 0 );
			path.lineTo( 10, 15 );
			path.close();

		}

		@Override
		protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
			setMeasuredDimension( 20, 15 );
		}

		@Override
		public void onDraw( Canvas canvas ) {
			canvas.drawPath( path, paint );
			super.onDraw( canvas );
		}
	}

}
