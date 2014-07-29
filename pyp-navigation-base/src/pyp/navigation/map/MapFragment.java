package pyp.navigation.map;

import java.util.ArrayList;

import pyp.navigation.R;
import pyp.navigation.main.MainActivity;
import pyp.navigation.map.bean.Marker;
import android.app.Activity;
import android.content.Context;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qozix.tileview.TileView;

/**
 * @Title: MapFragment
 * @Description: 地图模块入口
 * @author 张伟杰
 * @date 2014-7-17
 * @email 531724220@qq.com
 */
/**
 * @Title: MapFragment
 * @Description: 
 * @author 张伟杰
 * @date 2014-7-24
 * @email 531724220@qq.com
 */
public class MapFragment extends Fragment {
	//test 
	private MainActivity parentActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.parentActivity = (MainActivity) activity;
	}



	/**
	 * 字段 int ： DISABLE_DIRECTION
	 *  0 : 隐藏搜索栏
	 *  1 : 显示搜索栏
	 */
	private int CLICK_COUNTR_SEARCH = 0;
	
	
    private View parentView;
    private TileView tileView;
    private FrameLayout tilesViewlayout;
	private ArrayList<ImageView> storeIsShowMarker = new ArrayList<ImageView>();
	private MapCallout storeIsShowCallout = null;
	
    
    private ImageView map_searchBtn; 
    private ImageView map_placeBtn;
    private ImageView map_busBtn;
    private ImageView map_wcBtn;
    private ImageView map_messBtn;
    private ImageView map_teachFloorBtn;
    private ImageView map_locationBtn;
    
    private RelativeLayout relativeLayout; 
    private View belowheaderline;
    private View belowhelpmefindline;
    
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews(inflater, container);
        initListensers();
        return parentView;
    }


	public void initViews(LayoutInflater inflater, ViewGroup container) {
		
	 	//初始化地图主界面
		parentView = inflater.inflate(R.layout.map_main, container, false);
		
		//初始化静态地图显示的FrameLayout
		tilesViewlayout = (FrameLayout) parentView.findViewById(R.id.map_tilesView);
	    this.tileView = new TileView(this.getActivity());
	    this.tileView.setSize(4352, 3176);
	    this.tileView.addDetailLevel(1.0F, "tiles/1000_%col%_%row%.jpg", "downsamples/map.jpg");
	    this.tileView.addDetailLevel(0.5F, "tiles/500_%col%_%row%.jpg", "downsamples/map.jpg");
	    this.tileView.addDetailLevel(0.25F, "tiles/250_%col%_%row%.jpg", "downsamples/map.jpg");
	    this.tileView.addDetailLevel(0.125F, "tiles/125_%col%_%row%.jpg", "downsamples/map.jpg");
	    this.tileView.setScale(0.25D);
	    tilesViewlayout.addView(this.tileView);

	    //初始化按钮
		this.map_searchBtn = (ImageView) parentView.findViewById(R.id.map_main_searchIcon);
		this.map_busBtn = (ImageView) parentView.findViewById(R.id.map_main_bus);
		this.map_messBtn = (ImageView) parentView.findViewById(R.id.map_main_mess);
		this.map_placeBtn = (ImageView) parentView.findViewById(R.id.imap_main_place);
		this.map_wcBtn = (ImageView) parentView.findViewById(R.id.map_main_WC);
		this.map_teachFloorBtn = (ImageView) parentView.findViewById(R.id.map_main_teachFloor);
		this.map_locationBtn = (ImageView) parentView.findViewById(R.id.map_location_staticMap);

	    
		//隐藏搜索栏
		relativeLayout = (android.widget.RelativeLayout) parentView.findViewById(R.id.map_main_searchholder);
		this.belowheaderline = parentView.findViewById(R.id.map_main_belowheaderline);
		this.belowhelpmefindline = parentView.findViewById(R.id.map_main_belowheaderline);
		this.belowheaderline.setVisibility(View.GONE); 
		this.relativeLayout.setVisibility(View.GONE); 
		this.belowhelpmefindline.setVisibility(View.GONE); 
		
		
		/**静态地图标注初始化**/
		tileView.setMarkerAnchorPoints( -0.5f, -1.0f );
		tileView.setScaleLimits( 0, 2 );
		tileView.setTransitionsEnabled( false );
		// 给相对定位提供海图图角坐标public void defineRelativeBounds(double left, double top, double right, double bottom)
		tileView.defineRelativeBounds( 42.379676, -71.094919, 42.346550, -71.040280 );
		// 获取默认点的样式，通过普通点的实例也具有同样的效果
		Paint paint = tileView.getPathPaint();
		paint.setShadowLayer( 4, 2, 2, 0x66000000 );
		paint.setPathEffect( new CornerPathEffect( 5 ) );
	}

	/**
	 * 方法 addMarker
	 * 方法描述: 添加Marker到地图中
	 * @param key  marker的分类
	 * @param viewID  对应marker分类的定位图标
	 * void
	 */
	public void addMarker(int key, int viewID) {
	
		//清空已经显示的marker
		while(!storeIsShowMarker.isEmpty()) {
			this.tileView.removeMarker(storeIsShowMarker.remove(0));
		}
		
		//清除当前打开的callout
		if(this.storeIsShowCallout != null) {
			this.tileView.removeCallout(this.storeIsShowCallout);
			storeIsShowCallout = null;
		}
		
		// 给所有的点添加markers,给经过的坐标设置一个setTag, 当点击marker的时候将会打开一个callout.
		for ( Marker markersinfo : markersinfos ) {
			
			ImageView markerImageView = new ImageView(this.getActivity());
			if(markersinfo.getKey() == key) {
				markerImageView.setTag( markersinfo.getPosition() );    	// 保存中心坐标和callout的位置
				markerImageView.setImageResource( viewID );                 // 给marker一个标准的图标 - 在这些 指示点下方和中心, 我们可以用到合适的锚点
				markerImageView.setOnClickListener( markerClickListener );  // 为了在指定区域的tap上显示自定义的信息 ，要添加一个OnClickListener,在点击的时候时候进行信息设置
				
				storeIsShowMarker.add(markerImageView);                     //添加到已经显示的marker窗口中
				
				// 将mark添加到树视图中
				tileView.addMarker( markerImageView, markersinfo.getPosition()[0], markersinfo.getPosition()[1] );
				
			}

		}
		
	}

	/**
	 * 方法 searchMarkerInfo
	 * 方法描述: 在MarkerInfos中查找是否有与传入的x,y相同的点坐标的坐标信息。
	 * @param x
	 * @param y
	 * @return 如果有相同的坐标则返回当前坐标，反之返回NULL
	 * Marker
	 */
	public Marker searchMarkerInfo(double x, double y) {
		for ( int i=0; i<markersinfos.size(); i++) {
			if(markersinfos.get(i).getPosition()[0] == x && markersinfos.get(i).getPosition()[1] == y){ 
				return markersinfos.get(i);
			}
		}
		return null;
	}
	
	public void initListensers() {
		this.map_searchBtn.setOnClickListener(searchBtn_OnClickListener);
		this.map_busBtn.setOnClickListener(busBtn_OnClickListener);
		this.map_messBtn.setOnClickListener(messBtn_OnClickListener);
		this.map_placeBtn.setOnClickListener(placeBtn_OnClickListener);
		this.map_teachFloorBtn.setOnClickListener(teachFloorBtn_OnClickListener);
		this.map_wcBtn.setOnClickListener(wcBtn_OnClickListener);
		this.map_locationBtn.setOnClickListener(locationBtn_OnClickListener);
	}
	
	
	
	OnClickListener searchBtn_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(CLICK_COUNTR_SEARCH == 0) {
				belowheaderline.setVisibility(View.VISIBLE); 
				relativeLayout.setVisibility(View.VISIBLE); 
				belowhelpmefindline.setVisibility(View.VISIBLE); 
				CLICK_COUNTR_SEARCH = 1;
			} else {
				belowheaderline.setVisibility(View.GONE); 
				relativeLayout.setVisibility(View.GONE); 
				belowhelpmefindline.setVisibility(View.GONE); 
				CLICK_COUNTR_SEARCH = 0;
			}
		}
		
	};
	
	OnClickListener busBtn_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//	Toast.makeText(getActivity(), "公交按钮", Toast.LENGTH_SHORT).show();
			addMarker(4, R.drawable.map_marker_orange);
		}
		
	};
	
	OnClickListener messBtn_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//	Toast.makeText(getActivity(), "食堂按钮", Toast.LENGTH_SHORT).show();
			addMarker(5, R.drawable.map_marker_green);
		}
		
	};
	
	OnClickListener placeBtn_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//	Toast.makeText(getActivity(), "地点按钮", Toast.LENGTH_SHORT).show();
			addMarker(1, R.drawable.map_marker_lightblue);
		}
		
	};
	
	OnClickListener teachFloorBtn_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//	Toast.makeText(getActivity(), "教学楼按钮", Toast.LENGTH_SHORT).show();
			addMarker(3, R.drawable.map_marker_blue);
		}
		
	};
	
	OnClickListener wcBtn_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		   //  Toast.makeText(getActivity(), "厕所按钮", Toast.LENGTH_SHORT).show();
			addMarker(6, R.drawable.map_marker_purple);
		}
		
	};


	
	OnClickListener locationBtn_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "打开百度地图！！！", Toast.LENGTH_LONG).show();
		}
		
	};

	private View.OnClickListener markerClickListener = new View.OnClickListener() {

		@Override
		public void onClick( View view ) {
			// 保存marker's tag的坐标
			double[] position = (double[]) view.getTag();
			// 移到屏幕中间的坐标
			tileView.slideToAndCenter( position[0], position[1] );
			
			Marker isexist = searchMarkerInfo( position[0], position[1]);
			
			if(isexist != null) {
				// 创建一个简单的callout
				MapCallout callout = new MapCallout( view.getContext(), isexist );
				// 在相同的位置上将callout添加到视图树中，当marker被点击的时候调用它
				tileView.addCallout( callout, position[0], position[1], -0.5f, -1.0f );
				// 为callout设置动画效果
				callout.transitionIn();
				
				//保存当前打开的callout
				storeIsShowCallout = callout;
			} else {
				Toast.makeText(getActivity(), "位置：" + position[0] + ":" + position[1] + "，不存在信息！！！", Toast.LENGTH_LONG).show();
			}
		}
	};
	

	/**
	 * 字段 ArrayList<double[]> ： points  
	 * TODO 储存标注点的信息
	 */
	private ArrayList<Marker> markersinfos = new ArrayList<Marker>();
	{
		//X增大时，水平向左移动，反之则向右移动。Y增大时，垂直你上移动，反之则向下移动。
		
		/**
		 * 运行场,key=1
		 */
		this.markersinfos.add(new Marker(1, new double[] { 42.36370, -71.064900 }, "东区蓝球场", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(1, new double[] { 42.36080, -71.065000 }, "学生活动中心", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(1, new double[] { 42.36700, -71.066100 }, "游泳池", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(1, new double[] { 42.36700, -71.071200 } , "风雨操场", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(1, new double[] { 42.36750, -71.073500 }, "田径运动场", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(1, new double[] { 42.36322, -71.049200 }, "体育馆", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(1, new double[] { 42.35675, -71.078500 }, "网球场", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(1, new double[] { 42.35545, -71.063350 }, "罗马广场", "当然是学校的地点啦！！！"));
	
		
		/**
		 * 宿舍,key=2
		 */
		this.markersinfos.add(new Marker(2, new double[] { 42.35660, -71.093220 }, "西3宿舍", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.35580, -71.089200 }, "西4宿舍", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.35810, -71.091320 }, "西5A宿舍", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.35710, -71.091820 }, "西5B宿舍", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36180, -71.062700 }, "5号宿舍楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36595, -71.066200 }, "6号宿舍楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36675, -71.065100 } ,"7号宿舍楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36380, -71.069020 }, "集萃楼宿舍楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36145, -71.069820 }, "结绮楼宿舍楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.35910, -71.065750 }, "何厚铧宿舍楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36010, -71.064750 }, "陈瑞球宿舍楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36390, -71.085720 }, "青年旅馆", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(2, new double[] { 42.36150, -71.082920 }, "教师宿舍", "当然是学校的地点啦！！！"));
		
		
		/**
		 * 教学楼,key=3
		 */
		this.markersinfos.add(new Marker(3, new double[] { 42.35475, -71.078500 }, "7号教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35575, -71.082500 }, "7B号教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35275, -71.079000 }, "7C号教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35155, -71.079600 }, "7D号教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35075, -71.063000 }, "4号楼教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35075, -71.061000 }, "5号楼教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35435, -71.060000 }, "6号楼，6001~6***教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35215, -71.060300 }, "6号楼，6***~6***教学楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35770, -71.069050 }, "艺术设计大楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35405, -71.073300 }, "陶吧", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35185, -71.063190 }, "1号楼行政楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35185, -71.065800 }, "2号楼行政楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35085, -71.065900 }, "3号楼行政楼", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35495, -71.075500 }, "警卫处", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(3, new double[] { 42.35305, -71.070000 }, "图书馆", "当然是学校的地点啦！！！"));
		
		/**
		 * 公交,key=4
		 */
		this.markersinfos.add(new Marker(4, new double[] { 42.37340, -71.073000 }, "西门公交站(宝墨园方向)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(4, new double[] { 42.37400, -71.075600 }, "西门公交站(市桥方向)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(4, new double[] { 42.36975, -71.045200 }, "东门隧道", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(4, new double[] { 42.36965, -71.043100 }, "东门公交站(宝墨园方向)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(4, new double[] { 42.37100, -71.043100 }, "东门公交站(市桥方向)", "你口渴了吗？？？"));	
		
		
		/**
		 * 生活类(超市，食堂，外卖)key=5
		 */
		this.markersinfos.add(new Marker(5, new double[] { 42.36020, -71.092520 } , "西区饭堂", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.37550, -71.071500 }, "西门宵夜", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.35970, -71.093450 } , "西区饭堂饮料", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.36250, -71.066520 }, "东区学生饭堂", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.36210, -71.066900 }, "东区 饭堂饮料(右上)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.36270, -71.066000 }, "东区 饭堂饮料(左下)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.36146, -71.063550 }, "东区超市", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.35440, -71.060000 }, "6号楼自动贩卖机", "你口渴了吗？？？"));
		this.markersinfos.add(new Marker(5, new double[] { 42.35230, -71.065400 }, "2号楼自动贩卖机", "你口渴了吗？？？"));
		this.markersinfos.add(new Marker(5, new double[] { 42.35580, -71.089520 }, "西区超市", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.35480, -71.089720 }, "理发店", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.35500, -71.089920 }, "西区水票寄售点", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(5, new double[] { 42.36040, -71.063700 }, "东区校医", "当然是学校的地点啦！！！"));
		
		/**
		 * 厕所,key=6
		 */
		this.markersinfos.add(new Marker(6, new double[] { 42.36230, -71.048400 }, "体育馆厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.36740, -71.070400 }, "风雨操场厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.36210, -71.066900 }, "东区 饭堂厕所(右上)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.36270, -71.066000 }, "东区 饭堂厕所(左下)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35750, -71.07050 }, "艺术设计大楼厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35290, -71.058950 }, "6号楼6***~6***厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35435, -71.060000 }, "6号楼6001~6***厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35070, -71.058900 }, "6号楼厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35125, -71.061700 }, "5号楼厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35065, -71.062900 }, "4号楼厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35175, -71.062750 }, "1号楼厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35176, -71.066100 }, "2号楼厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35226, -71.071000 }, "图书馆厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35240, -71.078400 }, "7C馆厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35096, -71.080200 }, "7D馆厕所", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35580, -71.080700 }, "7B馆厕所(架空层)", "当然是学校的地点啦！！！"));
		this.markersinfos.add(new Marker(6, new double[] { 42.35540, -71.083400 }, "7B馆厕所", "当然是学校的地点啦！！！"));
	}
}

