package com.ff.pp.translate.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;
	private List<String> tagList;
	private FragmentManager fragmentManager;
	
	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		fragmentManager=fm;
		this.fragmentList = fragmentList;
		tagList=new ArrayList<>();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		String name = makeFragmentName(container.getId(), getItemId(position));
		tagList.add(name);
		return super.instantiateItem(container, position);
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	public void update(int position){
		Fragment fragment=fragmentManager.findFragmentByTag(tagList.get(position));
		if (fragment==null) return;
		if (fragment instanceof FragmentRecord){
			((FragmentRecord)fragment).update();
		}
		notifyDataSetChanged();
	}

	private String makeFragmentName(int viewId, long id) {
		return "android:switcher:" + viewId + ":" + id;
	}
}
