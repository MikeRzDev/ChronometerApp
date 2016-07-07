package co.kogi.chronometerapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.kogi.chronometerapp.R;
import co.kogi.chronometerapp.model.Lap;

/**
 * Created by user on 1/07/2016.
 */
public class LapAdapter extends BaseAdapter {
    private List<Lap> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public LapAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Lap getItem(int position) {
        return objects.get(objects.size()-1-position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_lap, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag(),position);
        return convertView;
    }

    private void initializeViews(Lap object, ViewHolder holder, int position) {

        holder.textViewLapNo.setText("Lap "+(objects.size()-1-position));
        holder.textViewDays.setText(object.getDays());
        holder.textViewHours.setText(object.getHours());
        holder.textViewMins.setText(object.getMins());
        holder.textViewSecs.setText(object.getSeconds());
        holder.textViewMillis.setText(object.getMillis());
    }

    public void setItems(List<Lap> lapList){
        this.objects = lapList;
        notifyDataSetChanged();
    }

    public void clearItems(){
        objects.clear();
        notifyDataSetChanged();
    }

    public List<Lap> getItems(){
        return objects;
    }

    public void addItem(Lap lap){
        this.objects.add(lap);
        notifyDataSetChanged();
    }

    protected class ViewHolder {
        private TextView textViewLapNo;
        private TextView textViewMillis;
        private TextView textViewSecs;
        private TextView textViewMins;
        private TextView textViewHours;
        private TextView textViewDays;


        public ViewHolder(View view) {
            textViewLapNo = (TextView) view.findViewById(R.id.textView_lapNo);
            textViewMillis = (TextView) view.findViewById(R.id.textView_millis);
            textViewSecs = (TextView) view.findViewById(R.id.textView_secs);
            textViewMins = (TextView) view.findViewById(R.id.textView_mins);
            textViewHours = (TextView) view.findViewById(R.id.textView_hours);
            textViewDays = (TextView) view.findViewById(R.id.textView_days);

        }
    }

}
