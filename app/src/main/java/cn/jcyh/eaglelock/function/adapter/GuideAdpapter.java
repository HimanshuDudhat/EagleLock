package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by jogger on 2017/3/3.
 * 导航页
 */

public class GuideAdpapter extends PagerAdapter {
    private List<ImageView> mImgList;

    public void loadData(List<ImageView> imgList) {
        mImgList = imgList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @NonNull
    @Override
    public ImageView instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mImgList.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mImgList.get(position));
    }
}
