package eu.huras.marcin.flagiistolice;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin on 2017-01-10.
 */

public class ZegarkiAdapter extends ArrayAdapter {


    List list = new ArrayList();
    Typeface tf;
    Typeface tfregular;



    public ZegarkiAdapter(Context context, int resource) {

        super(context, resource);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Bold.ttf");

        tfregular = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Regular.ttf");

    }

    static class DataHandler {

        ImageView imgZegarek;
        TextView txtNazwa;
        TextView txtOcena;


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

       // Toast.makeText(MainActivity.this, "TU = " + jezyk, Toast.LENGTH_LONG).show();

        View row;
        row = convertView;
        DataHandler handler;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_row, parent, false);
            handler = new DataHandler();
            handler.imgZegarek = (ImageView) row.findViewById(R.id.imageView);
            handler.txtNazwa = (TextView) row.findViewById(R.id.textViewNazwa);
            handler.txtOcena = (TextView) row.findViewById(R.id.textViewOcena);


            row.setTag(handler);
        } else {

            handler = (DataHandler) row.getTag();

        }

        ZegarkiDataProvider zegarkiDataProvider;
        zegarkiDataProvider = (ZegarkiDataProvider) this.getItem(position);
        handler.imgZegarek.setImageResource(zegarkiDataProvider.getMovie_res());
        handler.txtNazwa.setText(zegarkiDataProvider.getKraj_lista() + " " + zegarkiDataProvider.getZegarki_nazwa());
        handler.txtOcena.setText(zegarkiDataProvider.getStolica_lista() + " " + zegarkiDataProvider.getZegarki_ocena());

        TextView textViewNazwa1 = (TextView) row.findViewById(R.id.textViewNazwa1);
        TextView textViewOcena1 = (TextView) row.findViewById(R.id.textViewOcena1);

        TextView tv = (TextView) row.findViewById(R.id.textViewNazwa);
        TextView tv1 = (TextView) row.findViewById(R.id.textViewOcena);

        tv.setTypeface(tf);
        tv1.setTypeface(tf);
        textViewNazwa1.setTypeface(tfregular);
        textViewOcena1.setTypeface(tfregular);

        return row;


    }
}

