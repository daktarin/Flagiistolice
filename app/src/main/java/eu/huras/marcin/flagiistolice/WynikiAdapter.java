package eu.huras.marcin.flagiistolice;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin on 2017-01-26.
 */

public class WynikiAdapter extends ArrayAdapter {

    Typeface tfregular;



    List list = new ArrayList();

    public WynikiAdapter(Context context, int resource) {
        super(context, resource);
        tfregular = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Regular.ttf");

    }

    static class DataHandler {

        TextView txtId;
        TextView txtObszar;
        TextView txtPoprawne;

    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);

    }

    @Override
    public int getCount() {
        return this.list.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;



        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row, parent, false);
            handler = new DataHandler();
            handler.txtId = (TextView) row.findViewById(R.id.tv_twoje_wyniki_nr);
            handler.txtObszar = (TextView) row.findViewById(R.id.tv_twoje_wyniki_obszar);
            handler.txtPoprawne = (TextView) row.findViewById(R.id.tv_twoje_wyniki_poprawne);

            row.setTag(handler);



        } else {

            handler = (DataHandler) row.getTag();
        }

        WynikiDataProvider wynikiDataProvider;
        wynikiDataProvider = (WynikiDataProvider) this.getItem(position);
        handler.txtId.setText(wynikiDataProvider.getId_res());
        handler.txtObszar.setText(wynikiDataProvider.getObszar_res());
        handler.txtPoprawne.setText(wynikiDataProvider.getPoprawne_res());

        TextView txtId = (TextView) row.findViewById(R.id.tv_twoje_wyniki_nr);
        TextView txtObszar = (TextView) row.findViewById(R.id.tv_twoje_wyniki_obszar);
        TextView txtPoprawne = (TextView) row.findViewById(R.id.tv_twoje_wyniki_poprawne);

        txtId.setTypeface(tfregular);
        txtObszar.setTypeface(tfregular);
        txtPoprawne.setTypeface(tfregular);

        return row;

    }
}
