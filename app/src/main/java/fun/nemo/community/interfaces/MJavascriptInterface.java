package fun.nemo.community.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fun.nemo.community.ImagePreviewActivity;

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 10:02$
 */
public class MJavascriptInterface {
    private Activity context;

    public MJavascriptInterface(Activity context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img, String[] array) {
        ArrayList<String> list = addImages(array);
        //查看当前点击图片的索引
        int i = list.indexOf(img);
        //跳转显示图片页面
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putStringArrayListExtra("imageUrls", list);
        intent.putExtra("curImg", img);
        intent.putExtra("index", i);
        context.startActivity(intent);
    }

    //去重复的图片路径
    private ArrayList<String> addImages(String[] array) {
        ArrayList<String> list = new ArrayList<>();
        Set set = new HashSet();
        for (String cd : array) {
            if (set.add(cd)) {
                list.add(cd);
            }
        }
        return list;
    }
}
