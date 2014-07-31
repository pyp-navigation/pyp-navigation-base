package pyp.navigation.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pyp.navigation.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @Title: UpdateManager
 * @Description: 类的描述
 * @author qsuron
 * @date 2014-7-29
 * @email admin@qiushurong.cn
 */
public class UpdateManager {
	private static final String TAG = "qsuron";
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	
	private int appVersion_local; 			// 客户端版本号
	private int appVersion_server;  		// 服务器版本号
	private String appVersionName_local;  	// 客户端版本名
	private String appVersionName_server;  	// 服务器版本名
	private int length_byte;				// 文件大小—字节
	private String length_mb; 				// 文件大小-兆

	
	private Context mContext;
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	private AlertDialog installDialog;
	private ProgressBar mProgress;
	private int progress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;

	// 提示语
	private String updateMsg = "检测到新版本  1.0 -> 4.0 ( 4.7 MB )";

	// 服务器配置文件
	private String VersionUri = "http://www.qiushurong.cn/pyp/update.json";

	// 默认安装包url
	private String apkUrl = "http://www.qiushurong.cn/pyp/pyp-navigation-base.apk";

	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/";

	/* 文件保存名 */
	private static final String saveFileName = savePath + "pyp-navigation-base.apk";


	/**
	 * 构造方法
	 * @param context Activity
	 */
	public UpdateManager(Context context) {
		this.mContext = context;
	}
	
	// 外部接口让主Activity调用 - 检查是否有更新
	public void checkUpdateInfo() {
		// 获取服务器 json 内容
		String jsonversion;
		try{
			jsonversion = UpdateNetworkTool.getContent(VersionUri);
			Log.d(TAG, "ver json:" + jsonversion);
			JSONArray array = new JSONArray(jsonversion);
			// 解析 json ，获取版本号
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				appVersion_server = Integer.parseInt(obj.getString("verCode"));
				appVersionName_server = obj.getString("verName");
				apkUrl = obj.getString("apkUrl");
				length_byte = Integer.parseInt(obj.getString("length"));
			}
		} catch (ClientProtocolException e) {
			Log.d(TAG, "checkUpdateInfo-ClientProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "checkUpdateInfo-IOException");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d(TAG, "checkUpdateInfo-JSONException");
			e.printStackTrace();
		}

		// 获取当前的版本号和版本名s
		appVersion_local = getVersionCode();
		appVersionName_local = getVersionName();
		length_mb = getMB(length_byte);
		updateMsg = "番职e家 : "+appVersionName_local+" -> "+appVersionName_server+"  ( "+length_mb+" )";
		
		Log.d(TAG, "JSON:apkUrl:"+apkUrl);
		Log.d(TAG, "JSON:appVersion_local:"+appVersion_local);
		Log.d(TAG, "JSON:appVersion_server:"+appVersion_server);
		Log.d(TAG, "JSON:appVersionName_local:"+appVersionName_local);
		Log.d(TAG, "JSON:appVersionName_server:"+appVersionName_server);
		Log.d(TAG, "JSON:length_mb:"+length_mb);
		Log.d(TAG, "JSON:length_byte:"+length_byte);
		Log.d(TAG, "appVersion_local:appVersion_server = "+appVersion_local+":"+appVersion_server);
		Log.d(TAG, "提示信息："+updateMsg);
		
		
		// 如果 本地版本号 低于 服务器版本号
		if (appVersion_local != 0 && appVersion_local<appVersion_server) {
			showNoticeDialog();
		}else{
			Toast.makeText(this.mContext, "恭喜，当前已是最新版本:" + getVersionName()+".", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 方法 showNoticeDialog
	 * 显示提醒更新版本对话框
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("检测到新版本：");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("极速下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 方法 showDownloadDialog
	 * 显示安装包下载对话框
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新 - " + length_mb);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_download, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

		downloadApk();
	}

	/**
	 * 方法 downloadApk
	 * 新建线程 - 下载apk
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/**
	 * 字段 Runnable ： mdownApkRunnable
	 * 下载APK线程
	 */
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					// 更新进度
					progress = (int) (((float) count / length_byte) * 100);
					Log.i(TAG,"进度="+progress);
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					// 下载完成
					if (numread <= 0) {
						//通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				//下载完成，隐藏下载对话框，显示安装对话框
				downloadDialog.dismiss();
				showInstallDialog();
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 方法 showInstallDialog
	 * 显示安装对话框
	 */
	private void showInstallDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件下载 (100%) - 安装中...");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_install, null);
		builder.setView(v);
		builder.setNegativeButton("重新安装", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				installApk();
			}
		});
		installDialog = builder.create();
		installDialog.setCanceledOnTouchOutside(false);
		installDialog.show();
		
		downLoadThread = new Thread(mInstallApkRunnable);
		downLoadThread.start();
	}


	/**
	 * 字段 Runnable ： mdownApkRunnable
	 * 下载APK线程
	 */
	private Runnable mInstallApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			installApk();
		}
	};
	
	
	/**
	 * 方法 installApk
	 * 安装APK程序
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	
	/**
	 * 方法 getVersionCode
	 * 获取客户端版本号
	 * @return int 版本号，如 1 ，用于程序发行判断
	 */
	private int getVersionCode() {
		int appVersionCode = 0;
		PackageManager manager = this.mContext.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.mContext.getPackageName(), 0);
			appVersionCode = info.versionCode;
			//Log.d(TAG, "getVersionCode:"+appVersionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersionCode;
	}
	
	
	/**
	 * 方法 getVersionName
	 * 获取客户端版本名
	 * @return String 返回版本名，如 1.0 ，给用户看的
	 */
	private String getVersionName() {
		String appVersionName = "1.0";
		PackageManager manager = this.mContext.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.mContext.getPackageName(), 0);
			appVersionName = info.versionName;
			//Log.d(TAG, "getVersionCode:"+appVersionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersionName;
	}
	
	/**
	 * 方法 getMB
	 * 换算 - 字节 byte -> 兆 MB
	 * @param length 文件长度 - byte
	 * @return String 文件长度 - MB
	 */
	private String getMB(int length){
		float mb = length/1024.0f/1024.0f;
		return String.format("%.2f",mb)+"MB";
	}

}


/****************** JSON格式 ************************
[
    {
        "appname": "番职e家",
        "apkname": "pyp-navigation-base.apk",
        "apkUrl": "http://www.qiushurong.cn/pyp/pyp-navigation-base.apk",
        "verCode": "4",
        "verName": "4.0",
        "length": "5006099"
    }
]
 ***************************************************/
