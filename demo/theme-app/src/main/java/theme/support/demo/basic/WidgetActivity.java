package theme.support.demo.basic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import theme.support.demo.BaseActivity;
import theme.support.demo.R;
import theme.support.demo.TabFragmentPagerAdapter;
import theme.support.demo.basic.fragment.BasicWidgetFragment;
import theme.support.demo.basic.fragment.SupportWidgetFragment;

public class WidgetActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_widget);
        initToolbar();
        configFragments();
    }

    private void configFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(new BasicWidgetFragment());
        list.add(new SupportWidgetFragment());
//        list.add(new TFragment());
//        list.add(new LastFragment());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), list));
        List<String> listTitle = new ArrayList<>();
        listTitle.add("Basic Widget");
        listTitle.add("Support Widget");
//        listTitle.add("List");
//        listTitle.add("第三方库控件");
        TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list, listTitle);
        viewPager.setAdapter(tabFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
