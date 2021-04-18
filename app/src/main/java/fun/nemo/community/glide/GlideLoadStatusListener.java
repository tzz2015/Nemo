package fun.nemo.community.glide;

/**
 * 　　* @Description:
 * 　　* @author 刘宇飞
 * 　　* @date  2021/4/19 0:16
 *
 */
public interface GlideLoadStatusListener extends ProgressListener {
    void onLoadFailed();
    void onLoadSuccess();
}
