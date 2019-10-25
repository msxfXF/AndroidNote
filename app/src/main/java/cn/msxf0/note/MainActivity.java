package cn.msxf0.note;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.transition.Explode;
import android.transition.Transition;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class MainActivity extends AppCompatActivity {
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWindowFeature(); //设置窗口风格 沉浸式 输入法
        setTransition();
        setContentView(R.layout.activity_main);

        initView();

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    //case 0:
                    //    return RecyclerViewFragment.newInstance();
                    //case 1:
                    //    return RecyclerViewFragment.newInstance();
                    //case 2:
                    //    return WebViewFragment.newInstance();
                    default:
                        return RecyclerViewFragment.newInstance();
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "便签";
                    case 1:
                        return "MD笔记";
                    case 2:
                        return "工具箱";
                    case 3:
                        return "登录";
                }
                return super.getPageTitle(position);
            }
        });
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#2196F3"), getResources().getDrawable(R.drawable.b_ac,getTheme()));
                    case 1:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#FF4CAF50"), getResources().getDrawable(R.drawable.b_hulk,getTheme()));
                    case 2:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#FFFF5722"), getResources().getDrawable(R.drawable.b_iron,getTheme()));
                    case 3:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#FF9C27B0"), getResources().getDrawable(R.drawable.b_hawkeye,getTheme()));

                }
                return HeaderDesign.fromColorAndDrawable(2131034268, getResources().getDrawable(R.drawable.b_ac,getTheme()));

            }

        });


    }

    /**
     * @author XiaoFei
     * @method :setTransition
     * @description: 设置转场特效
     * @date :19-10-25 下午3:34
     */
    private void setTransition() {
        Window window = getWindow();
        Explode explode = new Explode();    //转场特效
        explode.setDuration(1200L);
        window.setEnterTransition(explode);
        window.setExitTransition(explode);
        window.setAllowEnterTransitionOverlap(false);
        window.setAllowReturnTransitionOverlap(false);
    }



    /**
     * @author: XiaoFei
     * @method: setWindowFeature
     * @description: 设置窗口沉浸模式，输入法收缩
     * @date:19-10-25 15:37
     */
    private void setWindowFeature() {
        Window window = getWindow();    //沉浸
        window.requestFeature(12);
        if (Build.VERSION.SDK_INT >= 21)
        {
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(-2147483648);
            window.setStatusBarColor(0);
        }

        window.setSoftInputMode(32);    //输入法
    }

    /**
     * @author: XiaoFei
     * @method: initView
     * @description: 绑定控件，初始化
     * @date: 19-10-25 下午3:41
     */

    @SuppressLint("RestrictedApi")
    private void initView(){
        mViewPager = findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDefaultDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }




}
