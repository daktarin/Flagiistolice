package eu.huras.marcin.flagiistolice;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CustoSwype extends PagerAdapter {
    private String[] number_res;
    private String[] nazwaPanstwa_res;
    private String[] stolicaPanstwa_res;
    private int[] flagi_res;

    private Context ctx;

    private LayoutInflater layoutInflater;

    Typeface tfbold;
    Typeface tfregular;


    public CustoSwype(Context c, String[] this_number_res, String[] this_nazwaPanstwa_res, String[] this_stolicaPanstwa_res, int[] this_flagi_res) {
        ctx = c;
        number_res = this_number_res;
        nazwaPanstwa_res = this_nazwaPanstwa_res;
        stolicaPanstwa_res = this_stolicaPanstwa_res;
        flagi_res = this_flagi_res;

        tfbold = Typeface.createFromAsset(c.getAssets(), "fonts/Oswald-Bold.ttf");

        tfregular = Typeface.createFromAsset(c.getAssets(), "fonts/Oswald-Regular.ttf");

    }

    @Override
    public int getCount() {

        return flagi_res.length;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.layout_swype, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_flaga_powtorki);
        TextView textView = (TextView) itemView.findViewById(R.id.text_nr_powtorki);
        TextView text_nr_pytania_powtorki = (TextView) itemView.findViewById(R.id.text_nr_pytania_powtorki);
        TextView txt_nazwa_panstwa_powtorki = (TextView) itemView.findViewById(R.id.txt_nazwa_panstwa_powtorki);
        TextView txt_nazwa_stolicy_powtorki = (TextView) itemView.findViewById(R.id.txt_nazwa_stolicy_powtorki);

        textView.setTypeface(tfbold);
        text_nr_pytania_powtorki.setTypeface(tfbold);
        txt_nazwa_panstwa_powtorki.setTypeface(tfbold);
        txt_nazwa_stolicy_powtorki.setTypeface(tfbold);

        imageView.setImageResource(flagi_res[position]);
        int temp1 = Integer.parseInt(number_res[position]) + 1;
        textView.setText(""+temp1);
        text_nr_pytania_powtorki.setText("/" + (number_res.length));
        txt_nazwa_panstwa_powtorki.setText(nazwaPanstwa_res[position]);
        txt_nazwa_stolicy_powtorki.setText(stolicaPanstwa_res[position]);
        container.addView(itemView);
        return  itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);

    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
