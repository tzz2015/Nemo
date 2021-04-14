package fun.nemo.community.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 19:06$
 */
public class DownImageUtil {
    /**
     * 下载到本地
     *
     * @param context 上下文
     * @param url     网络图
     */
    private void saveImgToLocal(Context context, String url) {
        //如果是网络图片，抠图的结果，需要先保存到本地
        Glide.with(context)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        saveToAlbum(context, resource);
                        return false;
                    }
                })
                .preload();
    }

    /**
     * 保存到相册中
     *
     * @param context  上下文
     * @param resource 网络图保存到本地的缓存文件路径
     */
    private void saveToAlbum(Context context, File resource) {
        Random random = new Random();
        String imageFileName = "JPEG_" + "down" + random.nextInt(10) + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES) + "nemo");
        File file = new File(storageDir, imageFileName);
        boolean isCopySuccess = copy(resource, file);
        if (isCopySuccess) {
            //发送广播通知
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
            Toast.makeText(context, "图片保存到相册成功", Toast.LENGTH_LONG).show();
            resource.delete();
        } else {
            Toast.makeText(context, "图片保存到相册失败", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean copy(File source, File dest) {
        boolean result = false;

        if (source == null || dest == null) {
            return false;
        }

        if (!source.exists()) {
            return false;
        }

        if (!dest.exists()) {
            try {
                dest.createNewFile();
            } catch (Exception e) {
                dest.delete();
                e.printStackTrace();
            }
        }

        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
