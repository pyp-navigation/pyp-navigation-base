package pyp.navigation.map;


import java.util.ArrayList;
import java.util.List;

import pyp.navigation.R;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOvelray;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * @Title: BaiduMapActivity
 * @Description: 百度地图
 * @author 张伟杰
 * @date 2014-7-27
 * @email 531724220@qq.com
 */
public class BaiduMapActivity extends Activity implements 
OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
BaiduMap.OnMapClickListener,OnGetBusLineSearchResultListener,
OnGetRoutePlanResultListener {
	
	@SuppressWarnings("unused")
	private static final String LTAG = BaiduMapActivity.class.getSimpleName();
	private MapView mMapView;
	private BaiduMap mBaiduMap;


	private Context local_context;
	private ImageView map_location_baiduMapBtn;
	private ImageView map_baidu_layyer;
	private Button baidu_nearBtn;
	private Button baidu_routeBtn;
	private Button baidu_naviBtn;
	private Button baidu_busBtn;
	
	/**
	 * 字段 int ： HIDESTYLELAYER
	 * TODO  等于1的时候显示地图类型按钮，反之则隐藏。
	 */
	private int HIDESTYLELAYER = 1;
	private Button satellite_layerBtn;
	private Button commen_layerBtn;
	private CheckBox traffic_layerCheck;
	
	
	
	
	/**
	 * 字段 int ： HIDENEAR
	 * TODO  等于1的时候显示地图的附近按钮，反之则隐藏。
	 */
	private int HIDENEAR = 1;
	private TextView textview1;
	private EditText baidu_poi_city;
	private TextView textview2;
	private EditText baidu_poi_target;
	private Button baidu_poi_startBtn;
	private Button baidu_poi_nextGroupBtn;
	

	/**
	 * 字段 int ： HIDEROUTE
	 * TODO  等于1的时候显示地图的附近按钮，反之则隐藏。
	 */
	private int HIDEROUTE = 1;
	private TextView textView3;
	private EditText baidu_route_start;
	private TextView textView4;
	private EditText baidu_route_end;
	private Button baidu_route_driveCar;
	private Button baidu_route_byBus;
	private Button baidu_route_walk;

	/**
	 * 字段 int ：SHARE_UI_ID
	 * TODO  等于1的时候显示地图的附近按钮，2表示公交。
	 */
	private int SHARE_UI_ID = 0;
	
	//poi变量（bus）
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	//搜索关键字输入窗口
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;
	
	
	 /*//浏览路线节点相关
    Button mBtnPre = null;//上一个节点
    Button mBtnNext = null;//下一个节点
*/    int nodeIndex = -2;//节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    private TextView popupText = null;//泡泡view
    private View viewCache = null;
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

	private List<String> busLineIDList = null;
	private int busLineIndex = 0;
	private BusLineResult route_bus = null;// 保存驾车/步行路线数据的变量，供浏览节点时使用
	// 搜索相关
	private PoiSearch mSearch_bus = null; // 搜索模块，也可去掉地图模块独立使用
	private BusLineSearch mBusLineSearch = null;
    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initViews();
		initListensers();
	}

	
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	public void initViews() {
		
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this.getApplicationContext());
		setContentView(R.layout.map_main_baidumap);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

        
		local_context = this;
		
		//隐藏地图类型
		this.satellite_layerBtn = (Button) findViewById(R.id.satellite_layerBtn);
		this.commen_layerBtn = (Button) findViewById(R.id.commen_layerBtn);
		this.traffic_layerCheck = (CheckBox) findViewById(R.id.traffic_layerBtn);
		this.satellite_layerBtn.setVisibility(View.GONE);
		this.commen_layerBtn.setVisibility(View.GONE);
		this.traffic_layerCheck.setVisibility(View.GONE);
		
		//隐藏POI(附近)
		this.textview1 = (TextView) findViewById(R.id.textView1);
		this.baidu_poi_city = (EditText) findViewById(R.id.baidu_poi_city);
		this.textview2 = (TextView) findViewById(R.id.textView2);
		this.baidu_poi_target = (EditText) findViewById(R.id.baidu_poi_target);
		this.baidu_poi_startBtn = (Button) findViewById(R.id.baidu_poi_start);
		this.baidu_poi_nextGroupBtn = (Button) findViewById(R.id.baidu_poi_nextGroup);
		this.textview1.setVisibility(View.GONE);
		this.baidu_poi_city.setVisibility(View.GONE);
		this.textview2.setVisibility(View.GONE);
		this.baidu_poi_target.setVisibility(View.GONE);
		this.baidu_poi_startBtn.setVisibility(View.GONE);
		this.baidu_poi_nextGroupBtn.setVisibility(View.GONE);
		
		//隐藏route(路线区域)
		this.textView3 = (TextView) findViewById(R.id.textView3);
		this.baidu_route_start = (EditText) findViewById(R.id.baidu_route_start);
		this.textView4 = (TextView) findViewById(R.id.textView4);
		this.baidu_route_end = (EditText) findViewById(R.id.baidu_route_end);
		this.baidu_route_driveCar = (Button) findViewById(R.id.baidu_route_driveCar);
		this.baidu_route_byBus = (Button) findViewById(R.id.baidu_route_byBus);
		this.baidu_route_walk = (Button) findViewById(R.id.baidu_route_walk);
		this.textView3.setVisibility(View.GONE);
		this.baidu_route_start.setVisibility(View.GONE);
		this.textView4.setVisibility(View.GONE);
		this.baidu_route_end.setVisibility(View.GONE);
		this.baidu_route_driveCar.setVisibility(View.GONE);
		this.baidu_route_byBus.setVisibility(View.GONE);
		this.baidu_route_walk.setVisibility(View.GONE);
		
		
		//POI初始化
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		


        //地图点击事件处理
        mBaiduMap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);


        //bus初始化
		mSearch_bus = PoiSearch.newInstance();
		mSearch_bus.setOnGetPoiSearchResultListener(this);
		mBusLineSearch = BusLineSearch.newInstance();
		mBusLineSearch.setOnGetBusLineSearchResultListener(this);
		busLineIDList = new ArrayList<String>();

		//地图切换按钮
		this.map_location_baiduMapBtn = (ImageView) findViewById(R.id.map_location_baiduMap);
		
		//地图类型切换按钮
		this.map_baidu_layyer = (ImageView) findViewById(R.id.map_baidumap_layyer);
		
		//附近切换按钮(poi)
		this.baidu_nearBtn = (Button) findViewById(R.id.baidu_near);
		
		//路线切换按钮(route)
		this.baidu_routeBtn = (Button) findViewById(R.id.baidu_route);

		//公交切换按钮(bus)
		this.baidu_busBtn = (Button) findViewById(R.id.baidu_bus);
		
		//导航功能(navi)
		this.baidu_naviBtn = (Button) findViewById(R.id.baidu_navi);
	}

	public void initListensers() {
		this.map_location_baiduMapBtn.setOnClickListener(map_location_baiduMapOnListensers);
		
		this.map_baidu_layyer.setOnClickListener(map_baidu_layyerOnListensers);
		this.commen_layerBtn.setOnClickListener(commen_layerBtnOnListensers);
		this.satellite_layerBtn.setOnClickListener(satellite_layerBtnOnListensers);
		this.traffic_layerCheck.setOnCheckedChangeListener(traffic_layerCheckChangeListensers);
		
		this.baidu_nearBtn.setOnClickListener(baidu_nearOnListensers);
		this.baidu_poi_startBtn.setOnClickListener(baidu_poi_startBtnOnListensers);
		this.baidu_poi_nextGroupBtn.setOnClickListener(baidu_poi_nextGroupBtnOnListensers);
		
		this.baidu_routeBtn.setOnClickListener(baidu_routeBtnOnListensers);
		this.baidu_route_driveCar.setOnClickListener(baidu_route_driveCarOnListensers);
		this.baidu_route_byBus.setOnClickListener(baidu_route_byBusOnListensers);
		this.baidu_route_walk.setOnClickListener(baidu_route_walkOnListensers);
		
		this.baidu_busBtn.setOnClickListener(baidu_findBtnOnListensers);
		
		this.baidu_naviBtn.setOnClickListener(baidu_naviBtnOnListensers);
	}

	
	OnClickListener baidu_naviBtnOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			Toast.makeText(getApplicationContext(), "正在不要命开发中！！！", Toast.LENGTH_LONG).show();
		}
	};
	

	OnClickListener baidu_findBtnOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			if(HIDESTYLELAYER == 1) {
				textview1.setVisibility(View.VISIBLE);
				baidu_poi_city.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				baidu_poi_target.setVisibility(View.VISIBLE);
				baidu_poi_startBtn.setVisibility(View.VISIBLE);
				baidu_poi_nextGroupBtn.setVisibility(View.VISIBLE);
				HIDESTYLELAYER = 0;
				SHARE_UI_ID = 2;
			} else {
				textview1.setVisibility(View.GONE);
				baidu_poi_city.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
				baidu_poi_target.setVisibility(View.GONE);
				baidu_poi_startBtn.setVisibility(View.GONE);
				baidu_poi_nextGroupBtn.setVisibility(View.GONE);
				HIDESTYLELAYER = 1;
				SHARE_UI_ID = 0;
			}
		}
	};
	
	OnClickListener baidu_route_driveCarOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			SearchButtonProcess_forRoute(view);
		}
	};

	OnClickListener baidu_route_byBusOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			SearchButtonProcess_forRoute(view);
		}
	};

	OnClickListener baidu_route_walkOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			SearchButtonProcess_forRoute(view);
		}
	};

	OnClickListener baidu_routeBtnOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {

			if(HIDEROUTE == 1) {
				textView3.setVisibility(View.VISIBLE);
				baidu_route_start.setVisibility(View.VISIBLE);
				textView4.setVisibility(View.VISIBLE);
				baidu_route_end.setVisibility(View.VISIBLE);
				baidu_route_driveCar.setVisibility(View.VISIBLE);
				baidu_route_byBus.setVisibility(View.VISIBLE);
				baidu_route_walk.setVisibility(View.VISIBLE);
				HIDEROUTE = 0;
			} else {
				textView3.setVisibility(View.GONE);
				baidu_route_start.setVisibility(View.GONE);
				textView4.setVisibility(View.GONE);
				baidu_route_end.setVisibility(View.GONE);
				baidu_route_driveCar.setVisibility(View.GONE);
				baidu_route_byBus.setVisibility(View.GONE);
				baidu_route_walk.setVisibility(View.GONE);
				HIDEROUTE = 1;
			}
		}
	};

	/**
	 * 方法 searchButtonProcess
	 * 方法描述：poi搜索按功能是调用，供baidu_poi_nextGroupBtnOnListensers，baidu_poi_startBtnOnListensers使用
	 * @param v
	 * void
	 */
	public void searchButtonProcess(View v) {

		EditText editCity = (EditText) findViewById(R.id.baidu_poi_city);
		EditText editSearchKey = (EditText) findViewById(R.id.baidu_poi_target);
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(editCity.getText().toString())
				.keyword(editSearchKey.getText().toString())
				.pageNum(load_Index));
	}

	OnClickListener baidu_poi_nextGroupBtnOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			if(SHARE_UI_ID == 1) {
				load_Index++;
				searchButtonProcess(null);
			} else if (SHARE_UI_ID == 2){
				//Toast.makeText(getApplicationContext(), "bus next button!!!", Toast.LENGTH_LONG).show();
				load_Index++;
				searchButtonProcess_forBus(view);
			}
		}
	};

	OnClickListener baidu_poi_startBtnOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			if(SHARE_UI_ID == 1) {
				searchButtonProcess(view);
			} else if (SHARE_UI_ID == 2){
				//Toast.makeText(getApplicationContext(), "start button!!!", Toast.LENGTH_LONG).show();
				searchButtonProcess_forBus(view);
			}
		}
	};

	OnClickListener map_location_baiduMapOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			((Activity) local_context).finish();
		}
	};
	
	OnClickListener commen_layerBtnOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);   // 普通地图
		}
	};

	
	OnClickListener satellite_layerBtnOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View view) {
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);  // 卫星地图
		}
	};

	
	OnCheckedChangeListener traffic_layerCheckChangeListensers = new OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged(CompoundButton view, boolean arg1) {

			mBaiduMap.setTrafficEnabled(((CheckBox) view).isChecked());		//交通路况
		}
	};


	
	/**
	 * 字段 OnClickListener ： map_baidu_layyerOnListensers
	 * TODO 隐藏和显示更换地图类型功能中的输入区域
	 */
	OnClickListener map_baidu_layyerOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View arg0) {
			if(HIDENEAR == 1) {
				satellite_layerBtn.setVisibility(View.VISIBLE);
				commen_layerBtn.setVisibility(View.VISIBLE);
				traffic_layerCheck.setVisibility(View.VISIBLE);
				HIDENEAR = 0;
			} else {
				satellite_layerBtn.setVisibility(View.GONE);
				commen_layerBtn.setVisibility(View.GONE);
				traffic_layerCheck.setVisibility(View.GONE);
				HIDENEAR = 1;
			}
		}
	};
	
	
	/**
	 * 字段 OnClickListener ： baidu_nearOnListensers
	 * TODO 隐藏和显示附近功能中的输入区域
	 */
	OnClickListener baidu_nearOnListensers = new OnClickListener()
	{
		@Override
		public void onClick(View arg0) {
			if(HIDESTYLELAYER == 1) {
				textview1.setVisibility(View.VISIBLE);
				baidu_poi_city.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				baidu_poi_target.setVisibility(View.VISIBLE);
				baidu_poi_startBtn.setVisibility(View.VISIBLE);
				baidu_poi_nextGroupBtn.setVisibility(View.VISIBLE);
				HIDESTYLELAYER = 0;
				SHARE_UI_ID = 1;
			} else {
				textview1.setVisibility(View.GONE);
				baidu_poi_city.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
				baidu_poi_target.setVisibility(View.GONE);
				baidu_poi_startBtn.setVisibility(View.GONE);
				baidu_poi_nextGroupBtn.setVisibility(View.GONE);
				HIDESTYLELAYER = 1;
				SHARE_UI_ID = 0;
			}
		}
	};
	
	
	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
	}


	
	
	/**
	 *以下到下一个注释方法为poi功能的代码，是实现OnGetPoiSearchResultListener, OnGetSuggestionResultListener这两个接口
	 *
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if(this.SHARE_UI_ID == 1) {
			if (result == null
					|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				mBaiduMap.clear();
				PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
				mBaiduMap.setOnMarkerClickListener(overlay);
				overlay.setData(result);
				overlay.addToMap();
				overlay.zoomToSpan();
				return;
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

				// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
				String strInfo = "在";
				for (CityInfo cityInfo : result.getSuggestCityList()) {
					strInfo += cityInfo.city;
					strInfo += ",";
				}
				strInfo += "找到结果";
				Toast.makeText(BaiduMapActivity.this, strInfo, Toast.LENGTH_LONG)
						.show();
			}
		} else if(this.SHARE_UI_ID == 2) {

			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_LONG).show();
				return;
			}
			// 遍历所有poi，找到类型为公交线路的poi
			busLineIDList.clear();
			for (PoiInfo poi : result.getAllPoi()) {
				if (poi.type == PoiInfo.POITYPE.BUS_LINE
						|| poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
					busLineIDList.add(poi.uid);
				}
			}
			SearchNextBusline(null);
			route = null;
		}
		
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(BaiduMapActivity.this, "成功，查看详情页面", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			}
			return true;
		}
	}
	

	
	/**
	 *以下到下一个注释方法为route功能的代码，是实现BaiduMap.OnMapClickListener,OnGetRoutePlanResultListener这两个接口
	 *
	 */
	
    /**
     * 发起路线规划搜索示例
     *
     * @param v
     */
    public void SearchButtonProcess_forRoute(View v) {
        //重置浏览节点的路线数据
        route = null;/*
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);*/
        mBaiduMap.clear();
        // 处理搜索按钮响应
        EditText editSt = (EditText) findViewById(R.id.baidu_route_start);
        EditText editEn = (EditText) findViewById(R.id.baidu_route_end);
        //设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", editSt.getText().toString());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", editEn.getText().toString());

        // 实际使用中请对起点终点城市进行正确的设定
        if (v.getId() == R.id.baidu_route_driveCar) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        } else if (v.getId() == R.id.baidu_route_byBus) {
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .city("北京")
                    .to(enNode));
        } else if (v.getId() == R.id.baidu_route_walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }
    }

    /**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {
        if (nodeIndex < -1 || route == null ||
                route.getAllStep() == null
                || nodeIndex > route.getAllStep().size()) {
            return;
        }
        
        //获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        //移动节点至中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));

    }

    
    
    
    /**
     * 切换路线图标，刷新地图使其生效
     * 注意： 起终点图标使用中心对齐.
     */
    public void changeRouteIcon(View v) {
        if (routeOverlay == null) {
            return;
        }
        if (useDefaultIcon) {
            ((Button) v).setText("自定义起终点图标");
            Toast.makeText(this,
                    "将使用系统起终点图标",
                    Toast.LENGTH_SHORT).show();

        } else {
            ((Button) v).setText("系统起终点图标");
            Toast.makeText(this,
                    "将使用自定义起终点图标",
                    Toast.LENGTH_SHORT).show();

        }
        useDefaultIcon = !useDefaultIcon;
        routeOverlay.removeFromMap();
        routeOverlay.addToMap();
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            DrivingRouteOvelray overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOvelray {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
    	return false;
    }
    
    
    
    
    
    
	/**
	 *以下到下一个注释方法为route功能的代码，是实现OnGetBusLineSearchResultListener接口
	 *
	 */
    
    /**
	 * 发起检索
	 * 
	 * @param v
	 */
	public void searchButtonProcess_forBus(View v) {
		busLineIDList.clear();
		busLineIndex = 0;
		EditText editCity = (EditText) findViewById(R.id.baidu_poi_city);
		EditText editSearchKey = (EditText) findViewById(R.id.baidu_poi_target);
		// 发起poi检索，从得到所有poi中找到公交线路类型的poi，再使用该poi的uid进行公交详情搜索
		mSearch_bus.searchInCity((new PoiCitySearchOption()).city(
				editCity.getText().toString()).keyword(
				editSearchKey.getText().toString()));
	}

	public void SearchNextBusline(View v) {
		if (busLineIndex >= busLineIDList.size()) {
			busLineIndex = 0;
		}
		if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
				&& busLineIDList.size() > 0) {
			mBusLineSearch.searchBusLine((new BusLineSearchOption()
					.city(((EditText) findViewById(R.id.baidu_poi_city)).getText()
							.toString()).uid(busLineIDList.get(busLineIndex))));

			busLineIndex++;
		}

	}


	@Override
	public void onGetBusLineResult(BusLineResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		route_bus = result;
		nodeIndex = -1;
		BusLineOverlay overlay = new BusLineOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(result);
		overlay.addToMap();
		overlay.zoomToSpan();
		baidu_poi_startBtn.setVisibility(View.VISIBLE);
		baidu_poi_nextGroupBtn.setVisibility(View.VISIBLE);
		Toast.makeText(BaiduMapActivity.this, result.getBusLineName(),
				Toast.LENGTH_SHORT).show();
	}
    
}
