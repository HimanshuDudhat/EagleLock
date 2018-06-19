package cn.jcyh.eaglelock.function.ui;

import android.animation.ObjectAnimator;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.function.adapter.GuideAdpapter;
import cn.jcyh.eaglelock.util.SPUtil;
import cn.jcyh.eaglelock.util.SizeUtil;

//引导页
public class GuideActivity extends BaseActivity {
    @BindView(R.id.ll_dot_container)
    LinearLayout llDotContainer;
    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    private int mPointDis;
    @BindView(R.id.iv_dot_pressed)
    ImageView ivDotPressed;
    @BindView(R.id.tv_start)
    TextView tvStart;
    private int[] mImgResId = new int[]{
            R.drawable.photo1,
            R.drawable.photo2,
            R.drawable.photo3
    };

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        SPUtil.getInstance().put(Constant.IS_FIRST_INTO, false);
        List<ImageView> imgList = new ArrayList<>();
        for (int i = 0; i < mImgResId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImgResId[i]);
            imgList.add(imageView);
            //添加默认小圆点
            ImageView pointDefault = new ImageView(this);
            pointDefault.setImageResource(R.mipmap.page_n);
            //初始化布局参数，通过LayoutParams设置自适应宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                //第二个圆开始设置左边距
                params.leftMargin = SizeUtil.dp2px(12);
            }
            //设置布局参数
            pointDefault.setLayoutParams(params);
            llDotContainer.addView(pointDefault);
        }
        GuideAdpapter adpapter = new GuideAdpapter();
        adpapter.loadData(imgList);
        vpGuide.setAdapter(adpapter);
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滚动过程中
                // 小圆点的移动距离=移动百分比*两个圆点的间距
                // 更新点距离
                int dis = (int) (mPointDis * positionOffset) + position * mPointDis;// 因为移动完一个界面后，百分比会归0，所以要加上移动过的单位position*mPointDis
//                //获取小圆点的布局属性，更新左边距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDotPressed.getLayoutParams();
                params.leftMargin = dis;// 修改左边距
//                // 重新设置布局参数
                ivDotPressed.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImgResId.length - 1) {
                    tvStart.setVisibility(View.VISIBLE);
                    ObjectAnimator
                            .ofFloat(tvStart, "alpha", 0, 1)
                            .setDuration(500)
                            .start();
                } else {
                    ObjectAnimator
                            .ofFloat(tvStart, "alpha", 1, 0)
                            .setDuration(500)
                            .start();
                    tvStart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        ivDotPressed.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivDotPressed.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointDis = llDotContainer.getChildAt(1).getLeft() - llDotContainer.getChildAt(0).getLeft();
            }
        });
    }

    @OnClick(R.id.tv_start)
    public void onClick() {
        startNewActivity(LoginActivity.class);
        finish();
    }
}
