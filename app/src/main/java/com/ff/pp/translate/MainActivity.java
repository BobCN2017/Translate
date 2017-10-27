package com.ff.pp.translate;


import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.ff.pp.translate.fragment.FragmentMain;
import com.ff.pp.translate.fragment.FragmentRecord;
import com.ff.pp.translate.fragment.MyFragmentPagerAdapter;
import com.ff.pp.translate.utils.HttpUtil;
import com.ff.pp.translate.utils.T;
import com.ff.pp.translate.view.ThreePositionToolbar;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int RECORD_AUDIO = 1;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ArrayList<Fragment> fragmentList;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mPagerAdapter;
    private ThreePositionToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);

        initToolbar();
        initDrawerLayout();
        initViewPager();
        applyPermission();
        HttpUtil.TestNewWork(this);
    }

    private void initToolbar() {
        mToolbar = (ThreePositionToolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("小翻");
        mToolbar.setCenterIcon(R.mipmap.ic_launcher_round);
        mToolbar.setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_list);

        //左侧侧滑菜单设置默认选项及监听；
        navigationView.setCheckedItem(R.id.item_1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.item_1:


                        break;
                    case R.id.item_2:


                        break;
                    case R.id.item_3:


                        break;
                    case R.id.item_4:

                        break;
                    case R.id.item_5:

                        break;
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void initViewPager() {

        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentMain());
        fragmentList.add(new FragmentRecord());

        mViewPager = (ViewPager) findViewById(R.id.viewPager_container);
        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager()
                , fragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    mToolbar.setTitle("小翻");
                }else if (position==1){
                    mToolbar.setTitle("记录");
                }
            }
        });

    }

    public void updateFragment(int position) {
        mPagerAdapter.update(position);
    }

    private void applyPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    T.showTips(getString(R.string.recording_accepted));
                } else {
                    T.showTips(getString(R.string.recording_disagreed));
                }
                break;
        }
    }
}
