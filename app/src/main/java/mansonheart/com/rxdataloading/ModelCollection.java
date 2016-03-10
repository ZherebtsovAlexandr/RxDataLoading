package mansonheart.com.rxdataloading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zherebtsov Alexandr on 08.03.2016.
 */
public class ModelCollection {

    private List<Model> models = new ArrayList<>();

    public ModelCollection() {
        models.add(new Model(0, "Item 0"));
        models.add(new Model(1, "Item 1"));
        models.add(new Model(2, "Item 2"));
        models.add(new Model(3, "Item 3"));
        models.add(new Model(4, "Item 4"));
        models.add(new Model(5, "Item 5"));
        models.add(new Model(6, "Item 6"));
    }

    public Model getModelById(int id) {
        Model needle = null;
        for (Model model : models) {
            if (model.getId() == id) {
                needle = model;
                return needle;
            }
        }
        return needle;
    }

    public List<Model> getModelByIds(List<Integer> ids) {
        List<Model> needles = new ArrayList<>();
        for (Model model : models) {
            if (ids.contains(model.getId())) {
                needles.add(model);
            }
        }
        return needles;
    }

    public List<Model> getModels() {
        return models;
    }

}
