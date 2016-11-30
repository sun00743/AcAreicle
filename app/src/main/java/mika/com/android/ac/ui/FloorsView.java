/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package mika.com.android.ac.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FloorsView extends LinearLayout {
	private Drawable mBorder;
    private int mMaxNum;
	@Override
	public boolean isDuplicateParentStateEnabled() {
	    return true;
	}
	public FloorsView(Context context) {
		this(context, null);
	}

	public FloorsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		setOrientation(VERTICAL);
		mMaxNum = getResources().getInteger(mika.com.android.ac.R.integer.max_floors_w);
//		setBackgroundResource(R.drawable.item_background);
	}

	public void setQuoteList(List<View> quoteList) {
		if(quoteList == null || quoteList.isEmpty()) {
			removeAllViewsInLayout();
			return;
		}
//		int spacing = DensityUtil.dip2px(getContext(), 0);
		int j = 0;
		for(int i=quoteList.size()-1;i>=0;i--){
			LinearLayout.LayoutParams params = generateDefaultLayoutParams();
//			int k = spacing * i;
//			if(quoteList.size()>mMaxNum+2 && i>mMaxNum){
//			    k = spacing*mMaxNum;
//			}
//			params.leftMargin = k;
//			params.rightMargin = k;
//			params.topMargin = j==0?k:0;
			View v = quoteList.get(i);
			TextView floor = (TextView) v.findViewById(mika.com.android.ac.R.id.quote_item_floor);
			floor.setText(String.valueOf(j+1));
			addViewInLayout(v, j++,params);
		}
	}

	public void setFloorBorder(Drawable border) {
		this.mBorder = border;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		/*
	    if(!isPressed()){
    		final int i = getChildCount();
    		if(this.mBorder == null){
    		    // stroke border if above v17
    		    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
    		        this.mBorder = getResources().getDrawable(Theme.isNightMode() ? R.drawable.floors_border_dark : R.drawable.floors_border);
    		    // else using .9 png
    		    else
    		        this.mBorder = getResources().getDrawable(Theme.isNightMode() ? R.drawable.comment_floor_bg_dark_2 : R.drawable.comment_floor_bg_2);
    		}
    		if ((this.mBorder != null) && (i > 0))
    			for (int j = i - 1; j >=0; j--) {
    				View child = getChildAt(j);
    				this.mBorder.setBounds(child.getLeft(), child.getLeft(),
                            child.getRight(), child.getBottom());
    				// draw background color only once
    				if (j == i - 1 ) {
    				    int border = DensityUtil.dip2px(getContext(), 1);
    				    Rect bounds = mBorder.copyBounds();
    				    ColorDrawable drawable = new ColorDrawable(Theme.isNightMode()? 0xFF545454 : 0xFFFFFEEE);
    				    drawable.setBounds(bounds.left + border, bounds.top + border, bounds.right - border, bounds.bottom - border);
    				    drawable.draw(canvas);
    				}
    				this.mBorder.draw(canvas);
    			}
	    }
		*/
		super.dispatchDraw(canvas);
	}
}
