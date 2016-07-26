package com.gobble.gobble_up;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


class FragStatePagerAdapter extends FragmentStatePagerAdapter {



    private ArrayList<categoryBean> list = new ArrayList<>();
    String id , name;
    categoryBean item;
    int count;


    FragStatePagerAdapter(FragmentManager fm, ArrayList<categoryBean> list, int count) {
        super(fm);
        this.list = list;
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {

        item = list.get(position);

        return SubCatFragment.newInstance(position+1 , String.valueOf(item.getId()));
    }

    @Override
    public int getCount() {
        return count;
    }

    public String getPagetitle(int position)
    {
        return list.get(position).getName();
    }


}
