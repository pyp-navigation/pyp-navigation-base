package pyp.navigation.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class UpdateNetworkTool {
	
	/**
	* 获取网址内容
	* @param url
	* @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	* @throws Exception
	*/
	public static String getContent(String url) throws ClientProtocolException, IOException {
	    StringBuilder sb = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpParams httpParams = client.getParams();
	    //设置网络超时参数
	    HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
	    HttpResponse response = client.execute(new HttpGet(url));
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
	    	InputStream is = entity.getContent();  
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8192);
	        
	        String line = null;
	        while ((line = reader.readLine())!= null){
	        	//line = EncodingUtils.getString(line.getBytes("GB2312"), "UTF-8"); 
	            sb.append(line + "\n");
	        }
	        reader.close();
	    }
	    return sb.toString();
	} 
}
