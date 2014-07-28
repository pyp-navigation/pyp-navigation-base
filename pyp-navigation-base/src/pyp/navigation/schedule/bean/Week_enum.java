package pyp.navigation.schedule.bean;

/**
 * 星期的枚举
 * 
 * @Title: Week_enum
 * @Description: 排序从周日到周六,索引从0到6
 * @author 作者名字chenkaihua
 * @date 2014年7月28日
 * @email 作者邮箱954822984@qq.com
 */
public enum Week_enum {
	/* 周日 */Sunday("周日", "日", 6)

	/* 周一 */, Monday("周一", "一", 0),
	/* 周二 */Tuesday("周二", "二", 1)

	/* 星期三 */, Wednesday("周三", "三", 2)

	/* 星期四 */, Thursday("周四", "四", 3)

	/* 星期五 */, Friday("周五", "五", 4)

	/* 星期六 */, Saturday("周六", "六", 5);

	private String chinesName;
	private String chinnesSimpleName;
	private int index;

	private Week_enum(String chinesName, String chinesSimpleNmae, int index) {
		this.chinesName = chinesName;
		this.index = index;
		this.chinnesSimpleName = chinesSimpleNmae;
	}

	/**
	 * 
	 * 方法 getChinnesName 得到中文称呼 例如 "周一"、"周日"
	 * 
	 * @return String
	 */
	public String getChinnesName() {
		return this.chinesName;
	}
	/**
	 * 
	 * 方法 getIndex
	 * 得到当前枚举的索引
	 * @return
	 * int
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 
	 * 方法 getSimpleChinnesName 得到中文称呼 例如 "一"代表周一、"日"代表周日
	 * 
	 * @return String
	 */
	public String getSimpleChinnesName() {
		return this.chinnesSimpleName;
	}

}
