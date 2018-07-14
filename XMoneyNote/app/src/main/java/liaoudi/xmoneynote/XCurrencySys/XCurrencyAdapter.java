package liaoudi.xmoneynote.XCurrencySys;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import liaoudi.xmoneynote.R;
import liaoudi.xmoneynote.XObject;

/**
 * Created by billliao on 2018/6/2.
 */

public class XCurrencyAdapter extends ArrayAdapter {
    private final int resourceId;

    public XCurrencyAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        XCurrencyObject x_currobj = (XCurrencyObject) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView textview_name = (TextView)view.findViewById(R.id.xcurrencyobj_name);
        TextView textview_currency = (TextView)view.findViewById(R.id.xcurrencyobj_currency);
        textview_name.setText(x_currobj.getCurrency_name());
        textview_currency.setText(Double.toString(x_currobj.getCurrency()));
        return view;
    }

}
