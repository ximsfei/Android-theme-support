package theme.support.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import theme.support.demo.basic.WidgetActivity;
import theme.support.demo.mdtab.MaterialDesignActivity;

public class MainActivity extends BaseActivity {
    private ListView mListView;
    private Context mContext = this;
    private final String[] mItems = {"基础控件", "Material Design"}; //, "ConstraintLayout", "FlycoTabLayout", "AlertDialog"
    private final Class<?>[] mClasses = {WidgetActivity.class, MaterialDesignActivity.class};//, ConstraintLayoutActivity.class,
//            SimpleHomeActivity.class, AlertDialogActivity.class};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        mListView = (ListView) findViewById(R.id.list);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setFadingEdgeLength(0);
        mListView.setAdapter(new HomeAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, mClasses[position]);
                startActivity(intent);
            }
        });
    }

    public class HomeAdapter extends BaseAdapter {
        private DisplayMetrics mDisplayMetrics;

        public HomeAdapter() {
            mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        }

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int padding = (int) (mDisplayMetrics.density * 10);


            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.simple_list_item, null);
            tv.setText(mItems[position]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//            tv.setTextAppearance(MainActivity.this, R.style.SkinCompatTextAppearance);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(padding, padding, padding, padding);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            return tv;
        }
    }
}
