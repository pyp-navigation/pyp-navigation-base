package pyp.navigation.association.one;

import java.util.List;

import pyp.navigation.R;
import pyp.navigation.association.bean.Association;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * @Title: MyAdapter
 * @Description: 社团模块-社团列表适配器
 * @author qsuron
 * @date 2014-7-25
 * @email admin@qiushurong.cn
 */
public class MyListAdapter extends BaseAdapter implements SectionIndexer {

	/**
	 * 字段 List<Association> ： list 用于存放所有读取到的社团信息
	 */
	private List<Association> list = null;
	private Context mContext;

	// private Class draw = R.drawable.class;
	// private Field field;

	/**
	 * 构造方法
	 * 
	 * @param mContext
	 *            上下文
	 * @param list
	 *            社团信息列表
	 */
	public MyListAdapter(Context mContext, List<Association> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.association_fragment_one_item, null);
			viewHolder.tvIcon = (ImageView) view
					.findViewById(R.id.association_fragment_one_icon);
			viewHolder.tvTitle = (TextView) view
					.findViewById(R.id.association_fragment_one_title);
			viewHolder.tvLetter = (TextView) view
					.findViewById(R.id.association_fragment_one_catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Association mDemo = list.get(position);
		if (position == 0) {
			// 设置社团首字母
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mDemo.getKey());
		} else {
			String lastCatalog = list.get(position - 1).getKey();
			if (mDemo.getKey().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mDemo.getKey());
			}
		}

		// 设置社团标题
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		String pic = this.list.get(position).getIcon();

		// 设置LOGO - 文件名 - 资源ID

		// 方式1：反射
		// 通过反射机制，把icon文件名，转化成资源id，设置到对应的社团imageView中
		//
		// try {
		// field = draw.getDeclaredField(pic);
		// resId = field.getInt(pic);
		// } catch (Exception e) {
		// e.printStackTrace();
		// resId = R.drawable.association_logo_0;
		// }

		// 方式2：getIdentifier
		int resId = mContext.getResources().getIdentifier(pic, "drawable",
				"pyp.navigation.main");
		// 找不到资源就设置为默认logo
		if (resId == 0)
			resId = R.drawable.association_logo_0;
		// 设置为社团Logo图像资源
		viewHolder.tvIcon.setImageResource(resId);

		return view;

	}

	final static class ViewHolder {
		ImageView tvIcon;
		TextView tvTitle;
		TextView tvLetter;
	}

	public Object[] getSections() {
		return null;
	}

	public int getSectionForPosition(int position) {
		return 0;
	}

	public int getPositionForSection(int section) {
		Association mDemo;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				mDemo = (Association) list.get(i);
				l = mDemo.getKey();
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i + 1;
				}
			}
		}
		mDemo = null;
		l = null;
		return -1;
	}

}