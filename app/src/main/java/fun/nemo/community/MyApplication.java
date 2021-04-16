package fun.nemo.community;

import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.HashMap;

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/15$ 17:48$
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initTbsSettings();
    }

    private void initTbsSettings() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.e("app", "onDownloadFinish -->下载X5内核完成：" + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.e("app", "onInstallFinish -->安装X5内核进度：" + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.e("app", "onDownloadProgress -->下载X5内核进度：" + i);
            }
        });

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，true表x5内核加载成功，否则表加载失败，会自动切换到系统内核。
                Log.e("app", " 内核加载 " + arg0);
                Toast.makeText(MyApplication.this,"启用x5内核："+arg0,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }
}
