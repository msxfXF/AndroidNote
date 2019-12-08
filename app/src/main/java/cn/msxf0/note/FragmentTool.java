package cn.msxf0.note;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class FragmentTool extends Fragment {
    private NestedScrollView mScrollView;
    private LinearLayout lin_ocr;

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
        lin_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView);


    }

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
                    Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(OCRError ocrError) {

                }
            });
        }
    }
}
