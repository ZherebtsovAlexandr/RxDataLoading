package mansonheart.com.rxdataloading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zherebtsov Alexandr on 08.01.2016.
 */
public final class ModelAdapter extends BaseAdapter {


    private final LayoutInflater inflater;

    private List<Model> items = new ArrayList<>();

    public ModelAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setItems(List<Model> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<Model> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Model getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.model_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_data);
            holder.container = (LinearLayout) convertView.findViewById(R.id.container);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Model item = getItem(position);
        CharSequence title = item.toString();
        holder.title.setText(title);
        return convertView;
    }

    public static class ViewHolder {
        TextView title;
        LinearLayout container;
    }
}
