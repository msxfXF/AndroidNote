package cn.msxf0.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.transition.Explode;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private FragmentNote fragmentNote;
    private FragmentMarkdown fragmentMarkdown;
    public static SpeechRecognizer mIatDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        initLeanCloud();
        setWindowFeature(); //设置窗口风格 沉浸式 输入法
        setTransition();
        setContentView(R.layout.activity_main);
        initView();


        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5d81c995");

        mIatDialog = SpeechRecognizer.createRecognizer(this, new InitListener() {
            @Override
            public void onInit(int i) {

            }
        });


        mIatDialog.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mIatDialog.setParameter(SpeechConstant.SUBJECT, null);
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "plain");
        //此处engineType为“cloud”
        mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        //设置语音输入语言，zh_cn为简体中文
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, "1");


        OCR.getInstance(MainActivity.this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "ok" + token, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
                System.out.println("fail");
            }
        }, getApplicationContext());


    }


    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    private void initLeanCloud() {
//        AVOSCloud.initialize(this,"YhmvLJOcWoFs1jcFsBkFfbWa-gzGzoHsz", "RTvYqUdJI3eKSpzaGm0HPaht","");
        AVOSCloud.initialize(this, "YhmvLJOcWoFs1jcFsBkFfbWa-gzGzoHsz", "RTvYqUdJI3eKSpzaGm0HPaht", "https://notes.msxf.fun");
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
//        AVUser.loginByEmail("1263413089@qq.com", "123456").subscribe(new Observer<AVUser>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(AVUser avUser) {
//                Toast.makeText(MainActivity.this, "登录成功" + avUser.getUsername(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(MainActivity.this, "登录失败" + e.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    /**
     * @author XiaoFei
     * @method :setTransition
     *  * @description: 设置转场特效
     *  * @date :19-10-25 下午3:34
     *  
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
     *  * @description: 设置窗口沉浸模式，输入法收缩
     *  * @date:19-10-25 15:37
     *  
     */
    private void setWindowFeature() {
        Window window = getWindow();    //沉浸
        window.requestFeature(12);
        if (Build.VERSION.SDK_INT >= 21) {
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
     *  * @description: 绑定控件，初始化
     *  * @date: 19-10-25 下午3:41
     *  
     */

    @SuppressLint("RestrictedApi")
    private void initView() {
        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bar);
        mViewPager = findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDefaultDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mViewPager.getViewPager().getCurrentItem()) {
                    case 0:
                        fragmentNote.addItem();
                        break;
                    case 1:
                        fragmentMarkdown.addItem();
                        break;
                    default:
                        try {
                            AppUtils.goAppShop(MainActivity.this);
                        } catch (Exception e) {
//                            Snackbar.make(mViewPager, "没有找到应用市场", Snackbar.LENGTH_INDEFINITE).show();
                        }

                }
            }
        });
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                System.out.println(item.getItemId());
                switch (item.getItemId()) {
                    case R.id.app_bar_1:
                        mViewPager.getViewPager().setCurrentItem(0, true);
                        break;
                    case R.id.app_bar_2:
                        mViewPager.getViewPager().setCurrentItem(1, true);
                        break;
                    case R.id.app_bar_3:
                        mViewPager.getViewPager().setCurrentItem(2, true);
                        break;
                    case R.id.app_bar_4:
                        mViewPager.getViewPager().setCurrentItem(3, true);
                        break;
                }
                return false;
            }
        });
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    case 0:
                        fragmentNote = FragmentNote.newInstance();
                        return fragmentNote;

                    case 1:
                        fragmentMarkdown = FragmentMarkdown.newInstance();
                        return fragmentMarkdown;
                    case 2:
                        return FragmentTool.newInstance();
                    default:
                        return FragmentLogin.newInstance();
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
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
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#2196F3"), getResources().getDrawable(R.drawable.b_ac, getTheme()));
                    case 1:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#FF4CAF50"), getResources().getDrawable(R.drawable.b_hulk, getTheme()));
                    case 2:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#FFFF5722"), getResources().getDrawable(R.drawable.b_iron, getTheme()));
                    case 3:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#FF9C27B0"), getResources().getDrawable(R.drawable.b_hawkeye, getTheme()));

                }
                return HeaderDesign.fromColorAndDrawable(2131034268, getResources().getDrawable(R.drawable.b_ac, getTheme()));

            }

        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(position);
                if (position == 2 || position == 3) {
//                    fab.hide();
                    fab.setImageResource(R.drawable.star);
                } else {
                    fab.show();
                    fab.setImageResource(R.drawable.add);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
