package theme.support.demo;

import android.app.Application;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.withoutActivity(this)
//                .addStrategy(new CustomSDCardLoader())          // 自定义加载策略，指定SDCard路径
                .addInflater(new SkinMaterialViewInflater())    // material design
                .addInflater(new SkinConstraintViewInflater())  // ConstraintLayout
                .addInflater(new SkinCardViewInflater())        // CardView v7
//                .addInflater(new SkinCircleImageViewInflater()) // hdodenhof/CircleImageView
//                .addInflater(new SkinFlycoTabLayoutInflater())  // H07000223/FlycoTabLayout
//                .setSkinStatusBarColorEnable(false)             // 关闭状态栏换肤
//                .setSkinWindowBackgroundEnable(false)           // 关闭windowBackground换肤
                .loadSkin();
    }
}
