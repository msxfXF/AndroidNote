package cn.msxf0.note;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import core.mini.AutoCheck;
//import core.recog.MyRecognizer;
//import core.recog.listener.IRecogListener;
//import core.recog.listener.MessageStatusRecogListener;

import static android.app.Activity.RESULT_OK;


public class FragmentTool extends Fragment {
    private NestedScrollView mScrollView;
    private LinearLayout lin_ocr;
    private LinearLayout lin_nls;
    private MaterialDialog dialog;
    private View dialogView;
    private TextView dialog_title;
    private Button dialog_btn_y;
    private EditText dialog_content;

    //    private MyRecognizer myRecognizer;
    public FragmentTool() {
        // Required empty public constructor
    }


    public static FragmentTool newInstance() {
        return new FragmentTool();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tool, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScrollView = view.findViewById(R.id.mScrollView);
        lin_ocr = view.findViewById(R.id.lin_ocr);
        lin_nls = view.findViewById(R.id.lin_nls);


        dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.dialog_nls, false)
                .backgroundColor(0)
                .canceledOnTouchOutside(true)
                .build();
        dialogView = dialog.getCustomView();
        dialog_title = dialogView.findViewById(R.id.dialog_title);
        dialog_btn_y = dialogView.findViewById(R.id.dialog_btn_y);
        dialog_content = dialogView.findViewById(R.id.dialog_edit);
        lin_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_title.setText("OCR文字识别");

                PictureSelector.create(FragmentTool.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .minSelectNum(1)
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .previewImage(true)
                        .isCamera(true)
                        .compress(true)
                        .synOrAsy(false)
                        .forResult(103);
                dialog.show();

            }
        });
        lin_nls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_title.setText("语音识别");
                dialog.show();
                MainActivity.mIatDialog.startListening(new RecognizerListener() {
                    @Override
                    public void onVolumeChanged(int i, byte[] bytes) {

                    }

                    @Override
                    public void onBeginOfSpeech() {
                        dialog_content.setText("");
                        Toast.makeText(getContext(), "语音识别开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEndOfSpeech() {
                        Toast.makeText(getContext(), "语音识别结束", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        dialog_content.append(recognizerResult.getResultString());
                    }

                    @Override
                    public void onError(SpeechError speechError) {

                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                });
            }
        });
        dialog_btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //获取剪贴板管理器
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", dialog_content.getText().toString());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(getContext(), "复制成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "复制出错", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView);

//        IRecogListener listener = new MessageStatusRecogListener(new Handler());
//        myRecognizer = new MyRecognizer(getContext(), listener);

    }

//    private void start() {
//        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
//        final Map<String, Object> params = new HashMap<String, Object>();
//        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
//        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印
//        Log.i("aaaaaaaaaaaaa", "设置的start输入参数：" + params);
//        // 复制此段可以自动检测常规错误
//        (new AutoCheck(getContext(), new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
////                        txtLog.append(message + "\n");
//                        ; // 可以用下面一行替代，在logcat中查看代码
//                        Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//        }, false)).checkAsr(params);
//
//        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
//        // DEMO集成步骤2.2 开始识别
//        myRecognizer.start(params);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 103 && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

            GeneralBasicParams generalBasicParams = new GeneralBasicParams();
            generalBasicParams.setDetectDirection(true);
            generalBasicParams.setImageFile(new File(((LocalMedia) selectList.get(0)).getCompressPath()));
            OCR.getInstance(getContext()).recognizeGeneralBasic(generalBasicParams, new OnResultListener<GeneralResult>() {

                @Override
                public void onResult(GeneralResult result) {
                    StringBuffer sb = new StringBuffer();
                    for (WordSimple wordSimple : result.getWordList()) {
                        // wordSimple不包含位置信息
                        WordSimple word = wordSimple;
                        sb.append(word.getWords());
                        sb.append("\n");
                    }

                    System.out.println(result.getJsonRes());
                    System.out.println(sb);
                    dialog_content.setText(sb.toString());
                    Toast.makeText(getContext(), "OCR文字识别结束", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(OCRError ocrError) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
//        myRecognizer.release();
        super.onDestroy();
    }
}
