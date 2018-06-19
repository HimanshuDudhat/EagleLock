package cn.jcyh.eaglelock.function.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.ui.fragment.SendPwdItemFragment;
import cn.jcyh.eaglelock.util.Util;

public class SendPwdPagerAdapter extends FragmentPagerAdapter {
    private LockKey mLockKey;
    private String[] mTitles;

    public SendPwdPagerAdapter(FragmentManager fm, LockKey lockKey) {
        super(fm);
        mLockKey = lockKey;
        mTitles = Util.getApp().getResources().getStringArray(R.array.send_pwd_type);
    }

    @Override
    public Fragment getItem(int position) {
        SendPwdItemFragment sendPwdItemFragment = new SendPwdItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.LOCK_KEY, mLockKey);
        bundle.putInt(Constant.POSITION, position);
        sendPwdItemFragment.setArguments(bundle);
        return sendPwdItemFragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
