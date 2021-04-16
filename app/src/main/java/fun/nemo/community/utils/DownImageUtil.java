package fun.nemo.community.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tencent.smtt.utils.Md5Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Random;

import static fun.nemo.community.utils.FileUtils.copy;

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 19:06$
 */
public class DownImageUtil {
    public interface DownCallback {
        void success(String url);

        void fail(String url);
    }

    public static void saveImgToLocal(Context context, String url, boolean isAddToAlbum) {
        saveImgToLocal(context, url, isAddToAlbum, null);
    }

    /**
     * 下载到本地
     *
     * @param context 上下文
     * @param url     网络图
     */
    public static void saveImgToLocal(Context context, String url, boolean isAddToAlbum, DownCallback callback) {
        if (isExistLocalFile(url)) {
            if (isAddToAlbum) {
                //发送广播通知
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + getSavePathName(url))));
                Toast.makeText(context, "图片保存到相册成功", Toast.LENGTH_LONG).show();
            }
            return;
        }
        //如果是网络图片，抠图的结果，需要先保存到本地
        Glide.with(context)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        if (isAddToAlbum)
                            Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                        if (callback != null) callback.fail(url);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        saveToAlbum(context, resource, url, isAddToAlbum);
                        if (callback != null) callback.success(url);
                        return false;
                    }
                })
                .preload();
    }

    /**
     * 保存到相册中
     *
     * @param context      上下文
     * @param resource     网络图保存到本地的缓存文件路径
     * @param url
     * @param isAddToAlbum 是否保存到相册
     */
    private static void saveToAlbum(Context context, File resource, String url, boolean isAddToAlbum) {
        try {
            File saveFile = new File(getSavePathName(url));
            boolean isCopySuccess = copy(resource, saveFile);
            if (!isAddToAlbum) {
                return;
            }
            if (isCopySuccess) {
                //发送广播通知
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + saveFile.getAbsolutePath())));
                Toast.makeText(context, "图片保存到相册成功", Toast.LENGTH_LONG).show();
                resource.delete();
            } else {
                Toast.makeText(context, "图片保存到相册失败", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DownImageUtil", Objects.requireNonNull(e.getMessage()));
        }

    }


    /**
     * 根据Url获取本地存储路径
     *
     * @param url
     * @return
     */
    public static String getSavePathName(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String imageFileName = "JPEG_" + "down" + Md5Utils.getMD5(url) + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES) + "/nemo");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File file = new File(storageDir, imageFileName);
        return file.getAbsolutePath();
    }

    /**
     * 当前url 是否存在本地图片
     *
     * @param url
     * @return
     */
    public static boolean isExistLocalFile(String url) {
        String path = getSavePathName(url);
        return FileUtils.isFileExists(path);
    }

}
