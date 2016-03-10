package mansonheart.com.rxdataloading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Zherebtsov Alexandr on 08.03.2016.
 */
public class Remote {

    private final int DELAY_IN_MS = 2000;
    private final Local local;

    public Remote(Local local) {
        this.local = local;
    }

    public Observable<Model> getDataById(final int id) {
        //return Observable.error(new Exception("Just for test"));
        return Observable
                .just(new Model(id, String.format("Item with id=%s was updated from remote", id)))
                .delay(DELAY_IN_MS, TimeUnit.MILLISECONDS)
                .doOnNext(new Action1<Model>() {
                    @Override
                    public void call(Model model) {
                        local.changeData(id, model.toString());
                    }
                });
    }

    public Observable<List<Model>> getDataByOffset(final int offset) {
        final int id = offset;
        List<Model> models = new ArrayList<>();
        models.add(new Model(id, String.format("Item with id=%s was updated from remote", id)));
        return Observable
                .just(models)
                .delay(DELAY_IN_MS, TimeUnit.MILLISECONDS)
                .doOnNext(new Action1<List<Model>>() {
                    @Override
                    public void call(List<Model> models) {
                        for (Model model : models) {
                            local.changeData(model.getId(), model.toString());
                        }
                    }
                });
    }
}
