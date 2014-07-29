package pyp.navigation.main.menu;

public class SlidmenuButtonView {
	private String title;
	private int resId;
	
	public SlidmenuButtonView(String title, int resId) {
		this.resId = resId;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
}
