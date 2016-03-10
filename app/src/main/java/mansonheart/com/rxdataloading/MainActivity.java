package mansonheart.com.rxdataloading;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @OnClick(R.id.btn_pattern1)
    public void onClickBtnPattern1() {
        startActivity(Pattern1Activity.getLaunchIntent(this));
    }

    @OnClick(R.id.btn_pattern2)
    public void onClickBtnPattern2() {
        startActivity(Pattern2Activity.getLaunchIntent(this));
    }
}
