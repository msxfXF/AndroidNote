package cn.msxf0.note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
//import com.baidu.speech.asr.SpeechConstant;
import com.bumptech.glide.Glide;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.joooonho.SelectableRoundedImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.AVUser;
import cn.leancloud.types.AVNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;


public class FragmentLogin extends Fragment {
    private NestedScrollView mScrollView;
    private Button btn_login;
    private SelectableRoundedImageView img_head;
    private MaterialDialog dialog;
    private View dialogView;
    private Button btn_getcode;
    private Button btn_regandlogin;
    private EditText edit_phone;
    private EditText edit_code;
    private TextView text_username;
    private TextView text_login;

    public FragmentLogin() {
        // Required empty public constructor
    }


    public static FragmentLogin newInstance() {
        return new FragmentLogin();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScrollView = view.findViewById(R.id.mScrollView);
        btn_login = view.findViewById(R.id.btn_login);
        img_head = view.findViewById(R.id.img_head);
        text_username = view.findViewById(R.id.text_username);
        text_login = view.findViewById(R.id.text_login);
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView);
        Glide.with(img_head).load(R.drawable.nobody).into(img_head);

        dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.dialog_login, false)
                .backgroundColor(0)
                .canceledOnTouchOutside(true)
                .build();
        dialogView = dialog.getCustomView();
        btn_getcode = dialogView.findViewById(R.id.btn_getcode);
        btn_regandlogin = dialogView.findViewById(R.id.btn_regandlogin);
        edit_code = dialogView.findViewById(R.id.edit_code);
        edit_phone = dialogView.findViewById(R.id.edit_phone);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();


            }
        });
        btn_getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(edit_phone.getText().toString());
                if (edit_phone.getText().toString().length() != 11) {
                    Toast.makeText(getContext(), "请检查手机号格式", Toast.LENGTH_SHORT).show();
                    return;
                }
                AVUser.requestLoginSmsCodeInBackground("+86" + edit_phone.getText().toString()).subscribe(new Observer<AVNull>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AVNull avNull) {
                        Toast.makeText(getContext(), "短信验证码已经发出，请等待接收", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


            }
        });
        btn_regandlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_phone.getText().toString().length() != 11) {
                    Toast.makeText(getContext(), "请检查手机号格式", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit_code.getText().toString().length() != 6) {
                    Toast.makeText(getContext(), "请检查验证码是否正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                AVUser.signUpOrLoginByMobilePhoneInBackground("+86" + edit_phone.getText().toString(), edit_code.getText().toString()).subscribe(new Observer<AVUser>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onNext(AVUser user) {
                        Toast.makeText(getContext(), "登录成功" + user.getObjectId(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        text_username.setText(user.getUsername());
                        text_login.setText("");
                        btn_login.setText("注销登录");
                        btn_login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AVUser.logOut();
                                text_login.setText("尚未登录，请先登录");
                                text_username.setText("");
                                btn_login.setText("点击登录");
                            }
                        });

                        System.out.println();
                    }

                    public void onError(Throwable throwable) {
                        // 验证码不正确
                        Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();


                    }

                    public void onComplete() {
                    }
                });
            }
        });


    }


}
