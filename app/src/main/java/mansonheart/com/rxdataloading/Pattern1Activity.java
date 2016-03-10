package mansonheart.com.rxdataloading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Pattern1Activity extends AppCompatActivity {

    private final String LOGCAT_TAG = "Pattern1Activity";

    @Bind(R.id.tv_data)
    TextView tvData;

    private Local local;
    private Remote remote;
    private int id;

    public static Intent getLaunchIntent(final Context context) {
        Intent intent = new Intent(context, Pattern1Activity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern1);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        Observable<Model> localData = local.getDataById(this.id);
        Observable<Model> remoteData = remote.getDataById(this.id)
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Model>>() {
                    @Override
                    public Observable<? extends Model> call(Throwable throwable) {
                        return local.getDataById(id);
                    }
                }).ignoreElements();

        Observable<Model> result = Observable.merge(localData, remoteData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        result.subscribe(new Observer<Model>() {
            @Override
            public void onCompleted() {
                Log.d(LOGCAT_TAG, "OnCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGCAT_TAG, "Error: " + e);
            }

            @Override
            public void onNext(Model model) {
                Log.d(LOGCAT_TAG, "Model: " + model.toString());
                tvData.setText(model.toString());
            }
        });
    }

    @OnClick(R.id.btn_change_data)
    public void onBtnChangeDataClick() {
        local.changeData(this.id, String.format("Item with id=%s was updated manually", this.id));
    }

    private void init() {
        this.id = 1;
        this.local = new Local();
        this.remote = new Remote(local);
    }

}
