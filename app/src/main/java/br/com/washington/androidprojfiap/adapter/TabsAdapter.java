package br.com.washington.androidprojfiap.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.fragments.DogsFragment;

/**
 * Created by washington on 09/09/2017.
 */

public class TabsAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.small);
        } else if (position == 1) {
            return context.getString(R.string.medium);
        }
        return context.getString(R.string.large);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = DogsFragment.newInstance(R.string.small);
        } else if (position == 1) {
            f = DogsFragment.newInstance(R.string.medium);
        } else {
            f = DogsFragment.newInstance(R.string.large);
        }
        return f;
    }
}
