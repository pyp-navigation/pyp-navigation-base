package pyp.navigation.association.service;

import java.util.Comparator;

import pyp.navigation.association.bean.Association;

/**
 * @Title: PinyinComparator
 * @Description: 社团模块-社团列表-按首字母排序器
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class PinyinComparator implements Comparator<Association> {

	public int compare(Association o1, Association o2) {
		if (o1.getKey().equals("@") || o2.getKey().equals("#")) {
			return -1;
		} else if (o1.getKey().equals("#") || o2.getKey().equals("@")) {
			return 1;
		} else {
			return o1.getKey().compareTo(o2.getKey());
		}
	}

}
