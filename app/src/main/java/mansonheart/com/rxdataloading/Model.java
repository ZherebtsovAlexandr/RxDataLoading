package mansonheart.com.rxdataloading;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zherebtsov Alexandr on 08.03.2016.
 */
public class Model {

    public static final String ID = "id";
    public static final String DATA = "data";

    private int id;
    private String data;

    private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

    public Model(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        notifyListeners(this,
                DATA,
                this.data,
                this.data = data);

    }

    private void notifyListeners(Object object, String property, String oldValue, String newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    @Override
    public String toString() {
        return this.data;
    }

}
