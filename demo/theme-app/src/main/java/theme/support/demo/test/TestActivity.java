package theme.support.demo.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import theme.support.demo.BaseActivity;
import theme.support.demo.R;

public class TestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initToolbar();
    }
}
