package pyp.navigation.association.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import pyp.navigation.association.bean.Association;
import android.util.Xml;

/**
 * @Title: ReadXmlByPullService
 * @Description: 使用pull解析xml - AllAssociation.xml
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class ReadXmlByPullService {

	public static List<Association> ReadXmlByPull(InputStream inputStream)
			throws Exception {
		List<Association> list = null;

		// android给我们提供了xml 用来得到xmlpull解析器
		XmlPullParser xmlpull = Xml.newPullParser();

		// 将输入流传入 设定编码方式
		xmlpull.setInput(inputStream, "utf-8");

		/*
		 * pull读到xml后 返回数字 读取到xml的声明返回数字0 START_DOCUMENT; 读取到xml的结束返回数字1
		 * END_DOCUMENT ; 读取到xml的开始标签返回数字2 START_TAG 读取到xml的结束标签返回数字3 END_TAG
		 * 读取到xml的文本返回数字4 TEXT
		 */

		int eventCode = xmlpull.getEventType();

		// 只要这个事件返回的不是1 我们就一直读取xml文件
		Association association = null;
		while (eventCode != XmlPullParser.END_DOCUMENT) {

			switch (eventCode) {
			case XmlPullParser.START_DOCUMENT: {
				list = new ArrayList<Association>();
				break;
			}

			case XmlPullParser.START_TAG: {
				if ("association".equals(xmlpull.getName())) {
					association = new Association();
					String id = xmlpull.getAttributeValue(0);
					association.setId(id);
					association.setIcon("association_logo_" + id);
				} else if (association != null) {
					if ("key".equals(xmlpull.getName())) {
						association.setKey(xmlpull.nextText());
					} else if ("name".equals(xmlpull.getName())) {
						association.setName(xmlpull.nextText());
					}
				}
				break;
			}

			case XmlPullParser.END_TAG: {
				if ("association".equals(xmlpull.getName())
						&& association != null) {
					list.add(association);
					association = null;
				}
				break;
			}
			}
			// 没有结束xml文件就推到下个进行解析
			eventCode = xmlpull.next();
		}
		return list;
	}

}
