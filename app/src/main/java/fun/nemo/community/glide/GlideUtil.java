package fun.nemo.community.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * 　　* @Description:
 * 　　* @author 刘宇飞
 * 　　* @date  2021/4/19 0:17
 */
public class GlideUtil {

    public static void load(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView,GlideLoadStatusListener listener) {
        ProgressInterceptor.addListener(url, progress -> {
            if(listener!=null){
                listener.onProgress(progress);
            }
        });
        GlideApp.with(context).load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if(listener!=null){
                            listener.onLoadFailed();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if(listener!=null){
                            listener.onLoadSuccess();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }


}
