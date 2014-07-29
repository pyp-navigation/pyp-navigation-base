package pyp.navigation.main.menu;


import pyp.navigation.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;

public class SlidmenuGridView extends LinearLayout {
  private GridView c;

  public SlidmenuGridView(Context paramContext)
  {
    super(paramContext);
  }

  public SlidmenuGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    LayoutInflater.from(paramContext).inflate(R.layout.main_slidingmenu_gridview, this);
    this.c = ((GridView)findViewById(R.id.main_slidingmenu_gridview));
  }

  public GridView getGridView()
  {
    return this.c;
  }

}