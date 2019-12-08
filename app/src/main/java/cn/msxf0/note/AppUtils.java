package cn.msxf0.note;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import br.tiagohm.markdownview.MarkdownView;

/**
 *  * @author XiaoFei
 *  * @SchoolNum:B17070714 
 *  * @description: 
 *  * @date :19-12-7 上午10:51
 *  
 */
public class AppUtils {
    public static void goAppShop(Context context) throws Exception {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Bitmap getViewBitmap(MarkdownView markdownView) {
        int height = (int) (markdownView.getContentHeight() * markdownView.getScale());
        int width = markdownView.getWidth();
        int pH = markdownView.getHeight();
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bm);
        int top = height;
        while (top > 0) {
            if (top < pH) {
                top = 0;
            } else {
                top -= pH;
            }
            canvas.save();
            canvas.clipRect(0, top, width, top + pH);
            markdownView.scrollTo(0, top);
            markdownView.draw(canvas);
            canvas.restore();
        }
        return bm;
    }

    public static String saveBitmap(Bitmap bm, File p) {

        File f = new File(p, String.valueOf(System.currentTimeMillis()) + ".png");

        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
