package theme.support.demo;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import skin.support.SkinCompatManager;
import skin.support.utils.SkinPreference;

public class BaseActivity extends AppCompatActivity {
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Subtitle");
        toolbar.setNavigationIcon(R.drawable.ic_settings_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(BaseActivity.this, SettingsActivity.class));
                if (SkinPreference.getInstance().getSkinName().equals("")) {
                    SkinCompatManager.getInstance().loadSkin("night.theme", null, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                } else {
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
            case R.id.clear_all:
                SkinCompatManager.getInstance().restoreDefaultTheme();
                return true;
            case R.id.build:
            case R.id.build_in:
                SkinCompatManager.getInstance().loadSkin("white", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                return true;
            case R.id.plug:
            case R.id.plug_in:
                SkinCompatManager.getInstance().loadSkin("night.theme", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                return true;
        }
        return false;
    }
}
