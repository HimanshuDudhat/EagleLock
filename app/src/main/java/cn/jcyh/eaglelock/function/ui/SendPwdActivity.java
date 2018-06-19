package cn.jcyh.eaglelock.function.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.adapter.SendPwdPagerAdapter;
import cn.jcyh.eaglelock.util.SizeUtil;

//发送密码
public class SendPwdActivity extends BaseActivity {
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.tl_title)
    TabLayout tLtitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_pwd;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        LockKey lockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        vpContent.setOffscreenPageLimit(5);
        vpContent.setCurrentItem(0);
        vpContent.setAdapter(new SendPwdPagerAdapter(getSupportFragmentManager(), lockKey));
        tLtitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        tLtitle.setupWithViewPager(vpContent);
        setIndicator(tLtitle);
    }


    @OnClick(R.id.ibtn_return)
    public void onClick() {
        finish();
    }

    private void setIndicator(TabLayout tabs) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        Field tabTextView=null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert tabStrip != null;
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int left;
        int right;
        assert ll_tab != null;
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            left =0;
            right = SizeUtil.dp2px(18);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout
                    .LayoutParams.MATCH_PARENT, 1);
            if (i == ll_tab.getChildCount() - 1) {
                right =0;
            }
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            params.rightMargin = right;
            params.leftMargin = left;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
