package mansonheart.com.rxdataloading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Pattern2Activity extends AppCompatActivity {

    private final String LOGCAT_TAG = "Pattern2Activity";

    ModelAdapter modelAdapter;

    @Bind(R.id.list)
    ListView listView;

    private Local local;
    private Remote remote;

    private int offset;

    public static Intent getLaunchIntent(final Context context) {
        Intent intent = new Intent(context, Pattern2Activity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern2);
        ButterKnife.bind(this);

        modelAdapter = new ModelAdapter(this);
        listView.setAdapter(modelAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        final Observable<Integer> offsetObservable = Observable.just(0, 1, 2, 3, 4);

        final Observable<List<Model>> localData = local.getAllData().take(1);

        Observable<List<Model>> remoteData = offsetObservable
                .concatMap(new Func1<Integer, Observable<List<Model>>>() {
                    @Override
                    public Observable<List<Model>> call(final Integer offset) {
                        Log.d(LOGCAT_TAG, "Offset: " + offset);
                        return remote.getDataByOffset(offset);
                    }
                })
                .map(new Func1<List<Model>, List<Integer>>() {
                    @Override
                    public List<Integer> call(List<Model> models) {
                        List<Integer> ids = new ArrayList<Integer>();
                        for (Model model : models) {
                            ids.add(model.getId());
                        }
                        Log.d(LOGCAT_TAG, "Ids: " + ids.get(0));
                        return ids;
                    }
                })
                .flatMap(new Func1<List<Integer>, Observable<List<Model>>>() {
                    @Override
                    public Observable<List<Model>> call(List<Integer> ids) {
                        return local.getDataById(ids);
                    }
                });


        Observable<List<Model>> result = Observable.concat(localData, remoteData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        result.subscribe(new Observer<List<Model>>() {
            @Override
            public void onCompleted() {
                Log.d(LOGCAT_TAG, "OnCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGCAT_TAG, "Error: " + e);
            }

            @Override
            public void onNext(List<Model> models) {
                Log.d(LOGCAT_TAG, "OnNext size: " + models.size());
                if (offset == 1) {
                    modelAdapter.clear();
                }

                offset++;
                modelAdapter.addItems(models);
            }
        });
    }

    @OnClick(R.id.btn_change_data)
    public void onBtnChangeDataClick() {
        local.changeData(4, "Data was updated manually");
    }

    private void init() {
        local = new Local();
        remote = new Remote(local);
    }

}
