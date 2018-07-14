package liaoudi.xmoneynote.XMoneyExhangeCalSys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import liaoudi.xmoneynote.R;
import liaoudi.xmoneynote.XCurrencySys.XCurrency;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyAdapter;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyObject;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyObjectList;
import liaoudi.xmoneynote.XMain;
import liaoudi.xmoneynote.XObject;

public class XExchangeMoney extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private ArrayList<XCurrencyObject> current_object_list;
    private XCurrencyAdapter adapter;

    private TextView textview_name;
    private TextView textview_currency;

    private XCurrencyObject selected_object;

    private EditText editext_ch_yuan;
    private EditText edittext_other_cost;

    private Button back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xexchange_money);

        XCurrencyObjectList.getInstance().read(context);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new XOnItemClickListener());
        back_button = (Button) findViewById(R.id.button_back);
        back_button.setOnClickListener(new XOnClickListener());
        textview_name = (TextView)findViewById(R.id.edittext_name);
        textview_currency = (TextView)findViewById(R.id.edittext_currency);
        editext_ch_yuan = (EditText)findViewById(R.id.edittext_ch_yuan);
        edittext_other_cost = (EditText)findViewById(R.id.edittext_other_cost);


        edittext_other_cost.addTextChangedListener(new XTextWatcherA());
        editext_ch_yuan.addTextChangedListener(new XTextWatcherB());

        current_object_list = XCurrencyObjectList.getInstance().getList();

        adapter = new XCurrencyAdapter(XExchangeMoney.this, R.layout.xcurrencyobj_item, current_object_list);

        listView.setAdapter(adapter);

    }

    class XTextWatcherA implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(edittext_other_cost.hasFocus() == false){
                return;
            }
            String other_cost_string = edittext_other_cost.getText().toString();
            if(other_cost_string == null || other_cost_string.equals("")){
                return;
            }
            Double other_cost_double;
            try{
                other_cost_double = Double.parseDouble(other_cost_string);
            }
            catch (Exception e){
                return;
            }

            // change here
            if(selected_object == null) {
                return;
            }
            Double answer = other_cost_double/selected_object.getCurrency();
            answer = XObject.roundDoubleWith2Prece(answer);
            editext_ch_yuan.setText(answer.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class XTextWatcherB implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(editext_ch_yuan.hasFocus() == false){
                return;
            }
            String ch_yuan_string = editext_ch_yuan.getText().toString();
            if(ch_yuan_string == null || ch_yuan_string.equals("")){
                return;
            }
            Double ch_yuan_double;
            try{
                ch_yuan_double = Double.parseDouble(ch_yuan_string);
            }
            catch (Exception e){
                return;
            }

            // change here
            if(selected_object == null) {
                return;
            }
            Double answer = ch_yuan_double*selected_object.getCurrency();
            answer = XObject.roundDoubleWith2Prece(answer);
            edittext_other_cost.setText(answer.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class XOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_back: {
                    clearAndJumpToMain();
                    break;
                }
            }
        }
    }

    private void clearAndJumpToMain(){
        Intent intent = new Intent(XExchangeMoney.this,XMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    class XOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selected_object = XCurrencyObjectList.getInstance().getObjectWithId(Integer.toString(position));
            textview_name.setText(selected_object.getCurrency_name());
            textview_currency.setText(selected_object.getCurrency().toString());

            String other_cost_string = edittext_other_cost.getText().toString();
            if(other_cost_string == null || other_cost_string.equals("")){
                return;
            }
            Double other_cost_double;
            try{
                other_cost_double = Double.parseDouble(other_cost_string);
            }
            catch (Exception e){
                return;
            }

            // change here
            if(selected_object == null) {
                return;
            }
            Double answer = other_cost_double/selected_object.getCurrency();
            answer = XObject.roundDoubleWith2Prece(answer);
            editext_ch_yuan.setText(answer.toString());
        }
    }
}
