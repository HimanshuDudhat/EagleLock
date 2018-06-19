package cn.jcyh.eaglelock.base.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jogger on 2017/5/2.
 * 主页grid布局分割线
 */

public class MainGridItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;

    public MainGridItemDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position % spanCount == 0) {
            outRect.right = spacing;
        }
        outRect.top = spacing;
    }
}
