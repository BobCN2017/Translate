package com.ff.pp.translate;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.ff.pp.translate.fragment.FragmentMain;
import com.ff.pp.translate.fragment.FragmentRecord;
import com.ff.pp.translate.fragment.MyFragmentPagerAdapter;
import com.ff.pp.translate.view.ThreePositionToolbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ArrayList<Fragment> fragmentList;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawerLayout();
        initViewPager();

    }

    private void initToolbar() {
        ThreePositionToolbar toolbar= (ThreePositionToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("小翻");
        toolbar.setCenterIcon(R.mipmap.ic_launcher_round);
        toolbar.setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    private void initDrawerLayout() {
        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.design_navigation_view);

        //左侧侧滑菜单设置默认选项及监听；
        navigationView.setCheckedItem(R.id.item_1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.item_1:


                        break;
                    case R.id.item_2:


                        break;
                    case R.id.item_3:


                        break;
                    case  R.id.item_4:

                        break;
                    case  R.id.item_5:

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
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
                                                getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

}
