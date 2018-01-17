package me.bakumon.ugank;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePal;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * App
 *
 * @author bakumon https://bakumon.me
 * @date 2016/12/8 17:18
 */
public class App extends Application {
    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        initThemeManage();
        initConfigManage();
        initBGASwipeBackManager();
        initLeakCanary();
        initLitePal();
    }

    /**
     * 初始化主题色
     */
    private void initThemeManage() {
        ThemeManage.INSTANCE.initColorPrimary(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 初始化配置管理器
     */
    private void initConfigManage() {
        ConfigManage.INSTANCE.initConfig(this);
    }

    /**
     * 初始化滑动返回
     */
    private void initBGASwipeBackManager() {
        /*
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null);
    }

    /**
     * 初始化 LeakCanary
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 初始化 LitePal
     */
    private void initLitePal() {
        LitePal.initialize(this);
    }
}
