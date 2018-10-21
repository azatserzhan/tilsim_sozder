package kz.tilsimsozder.style;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kz.tilsimsozder.R;
/**
 * Created by azatserzhanov on 13.12.15.
 */
public class CustomListAdapter extends ArrayAdapter {

    public Activity mContext;
    private int id;
    private String[] items;
    Typeface tb,tl,tr;

    public CustomListAdapter(Activity context, int textViewResourceId, String[] list) {
        super(context, textViewResourceId, list);
        id = textViewResourceId;
        mContext = context;
        items = list;

        //tr = Typeface.createFromAsset(context.getAssets(), "font/kz_r.ttf");
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        if (items[position] != null) {
            text.setText(Html.fromHtml(items[position]));
            text.setTypeface(tl);
        }

        ImageView imageView = (ImageView) mView.findViewById(R.id.imageView15);
        imageView.setImageResource(R.drawable.circle);
        text.setTextColor(Color.parseColor("#ffffff"));

        return mView;
    }

}