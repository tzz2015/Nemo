package fun.nemo.community;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import fun.nemo.community.utils.Constants;

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/15$ 17:48$
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initUm();
    }

    private void initUm() {
        //初始化组件化基础库, 所有友盟业务SDK都必须调用此初始化接口。
        UMConfigure.init(this, Constants.UM_APP_KEY, "Rom", UMConfigure.DEVICE_TYPE_PHONE, "");
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }


}
