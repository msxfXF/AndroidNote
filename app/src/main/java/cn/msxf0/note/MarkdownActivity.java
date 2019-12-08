package cn.msxf0.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import br.tiagohm.markdownview.ext.mark.Mark;

public class MarkdownActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private TextInputEditText edit_content;
    private MarkdownView markdownView;
    private ImageButton btn_b;
    private ImageButton btn_i;
    private ImageButton btn_emoji;
    private ImageButton btn_image;
    private ImageButton btn_table;
    private ImageButton btn_link;
    private ImageButton btn_share;
    private ImageButton btn_del;
    private ImageButton btn_upload;

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);
        initView();


        Intent intent = getIntent();
        intent.getStringExtra("title");
        edit_content.setText(intent.getStringExtra("content"));
        pos = intent.getIntExtra("pos", 0);


    }

    private void initView() {
        edit_content = findViewById(R.id.edit_content);
        edit_content.addTextChangedListener(MarkdownActivity.this);
        markdownView = findViewById(R.id.markdownView);
        markdownView.addStyleSheet(new Github());

        btn_b = findViewById(R.id.btn_b);
        btn_i = findViewById(R.id.btn_i);
        btn_del = findViewById(R.id.btn_del);
        btn_emoji = findViewById(R.id.btn_emoji);
        btn_image = findViewById(R.id.btn_image);
        btn_table = findViewById(R.id.btn_table);
        btn_link = findViewById(R.id.btn_link);
        btn_share = findViewById(R.id.btn_share);
        btn_upload = findViewById(R.id.btn_upload);

        btn_b.setOnClickListener(MarkdownActivity.this);
        btn_i.setOnClickListener(MarkdownActivity.this);
        btn_del.setOnClickListener(MarkdownActivity.this);
        btn_emoji.setOnClickListener(MarkdownActivity.this);
        btn_image.setOnClickListener(MarkdownActivity.this);
        btn_table.setOnClickListener(MarkdownActivity.this);
        btn_link.setOnClickListener(MarkdownActivity.this);
        btn_share.setOnClickListener(MarkdownActivity.this);
        btn_upload.setOnClickListener(MarkdownActivity.this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        markdownView.loadMarkdown(s.toString());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_share:
                //分享
                String path = new String();
                AppUtils.verifyStoragePermissions(MarkdownActivity.this);
                Bitmap bitmap = AppUtils.getViewBitmap(markdownView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    path = AppUtils.saveBitmap(bitmap, getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS));
                } else {
                    path = AppUtils.saveBitmap(bitmap, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                }


                break;

            case R.id.btn_upload:
                //保存
                Intent intent = new Intent();
                intent.putExtra("title", "");
                intent.putExtra("content", edit_content.getText().toString());
                intent.putExtra("pos", pos);
                setResult(101, intent);
                finish();
                break;

            case R.id.btn_del:
                //不保存
                setResult(100);
                finish();
                break;

            case R.id.btn_b: {
                int index1 = edit_content.getSelectionStart();
                int index2 = edit_content.getSelectionEnd() + 2;
                Editable editable = edit_content.getText();
                editable.insert(index1, "**");
                editable.insert(index2, "**");
                edit_content.setSelection(index1 + 2, index2);
                break;
            }


            case R.id.btn_i: {
                int index1 = edit_content.getSelectionStart();
                int index2 = edit_content.getSelectionEnd() + 1;
                Editable editable = edit_content.getText();
                editable.insert(index1, "*");
                editable.insert(index2, "*");
                edit_content.setSelection(index1 + 1, index2);
                break;
            }
            case R.id.btn_emoji: {
                int index1 = edit_content.getSelectionStart();
                int index2 = edit_content.getSelectionEnd() + 1;
                Editable editable = edit_content.getText();
                editable.insert(index1, ":");
                editable.insert(index2, ":");
                edit_content.setSelection(index1 + 1, index2);
                break;
            }
            case R.id.btn_image: {
                int index1 = edit_content.getSelectionStart();
                Editable editable = edit_content.getText();
                editable.insert(index1, "![avatar](https://www.baidu.com/img/baidu_jgylogo3.gif)");
                edit_content.setSelection(index1 + 2, index1 + 8);
                break;
            }
            case R.id.btn_table: {
                int index1 = edit_content.getSelectionStart();
                Editable editable = edit_content.getText();
                editable.insert(index1, "\n|列名1|列名2|列名3|\n|---|---|---|\n|表项1|:smile:|:angry:|\n|表项2|:wink:|:kissing_heart:|");
                break;
            }


        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            Toast.makeText(this, "请点击工具栏上的保存或退出按钮", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
