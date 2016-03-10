package mansonheart.com.rxdataloading;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Zherebtsov Alexandr on 08.03.2016.
 */
public class Local  {

    private final int DELAY_IN_MS = 100;

    private ModelCollection modelCollection;

    public Local() {
        modelCollection = new ModelCollection();
    }

    public Observable<Model> getDataById(final int id) {
        return Observable.create(new Observable.OnSubscribe<Model>() {
            @Override
            public void call(final Subscriber<? super Model> subscriber) {
                final Model model = modelCollection.getModelById(id);
                model.addChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                        subscriber.onNext(model);
                    }
                });
                subscriber.onNext(model);
            }
        }).delay(DELAY_IN_MS, TimeUnit.MILLISECONDS);
    }

    public Observable<List<Model>> getDataById(final List<Integer> ids) {
        return Observable.create(new Observable.OnSubscribe<List<Model>>() {
            @Override
            public void call(final Subscriber<? super List<Model>> subscriber) {
                final List<Model> models = modelCollection.getModelByIds(ids);
                subscriber.onNext(models);
            }
        }).delay(DELAY_IN_MS, TimeUnit.MILLISECONDS);
    }

    public Observable<List<Model>> getAllData() {
        return Observable.create(new Observable.OnSubscribe<List<Model>>() {
            @Override
            public void call(final Subscriber<? super List<Model>> subscriber) {
                final List<Model> models = modelCollection.getModels();
                subscriber.onNext(models);
            }
        }).delay(DELAY_IN_MS, TimeUnit.MILLISECONDS);
    }

    public void changeData(int id, String data) {
        Model model = this.modelCollection.getModelById(id);
        if (model != null) {
            model.setData(data);
        }
    }

}
