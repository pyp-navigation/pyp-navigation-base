package pyp.navigation.main.menu;

import pyp.navigation.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SlidmenuAvatarFrameView extends RelativeLayout
{
  private ImageView mImageView;

  public SlidmenuAvatarFrameView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(paramContext).inflate(R.layout.main_slidingmenu_head, this);
  }

  public ImageView getAvatarView()
  {
    return this.mImageView;
  }

}