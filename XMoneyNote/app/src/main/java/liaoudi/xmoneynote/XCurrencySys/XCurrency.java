package liaoudi.xmoneynote.XCurrencySys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import liaoudi.xmoneynote.R;
import liaoudi.xmoneynote.XAdd;
import liaoudi.xmoneynote.XDateList;
import liaoudi.xmoneynote.XLevelRecorder;
import liaoudi.xmoneynote.XMain;
import liaoudi.xmoneynote.XObject;
import liaoudi.xmoneynote.XObjectList;

public class XCurrency extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private Button add_button;
    private Button delete_button;
    private Button update_button;
    private Button back_button;
    private EditText edittext_name;
    private EditText edittext_currency;
    private ArrayList<XCurrencyObject> current_object_list;

    private XCurrencyObject selected_object;

    private XCurrencyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcurrency);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new XOnItemClickListener());
        add_button = (Button) findViewById(R.id.button_add);
        delete_button = (Button) findViewById(R.id.button_delete);
        update_button = (Button) findViewById(R.id.button_update);
        edittext_name = (EditText) findViewById(R.id.edittext_name);
        edittext_currency = (EditText) findViewById(R.id.edittext_currency);
        back_button = (Button) findViewById(R.id.button_back);

        add_button.setOnClickListener(new XOnClickListener());
        delete_button.setOnClickListener(new XOnClickListener());
        update_button.setOnClickListener(new XOnClickListener());
        back_button.setOnClickListener(new XOnClickListener());

        XCurrencyObjectList.getInstance().read(context);
        current_object_list = XCurrencyObjectList.getInstance().getList();

        adapter = new XCurrencyAdapter(XCurrency.this, R.layout.xcurrencyobj_item, current_object_list);
        listView.setAdapter(adapter);

        selected_object = null;

    }

    class XOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_add:{
                    // check content
                    String name_incoming = edittext_name.getText().toString();
                    String currency_incoming = edittext_currency.getText().toString();

                    if(name_incoming == null || name_incoming.equals("")){

                        break;
                    }
                    if(currency_incoming == null || currency_incoming.equals("")){
                        break;
                    }
                    double currency_double;
                    try{
                        currency_double = Double.parseDouble(currency_incoming);
                    }
                    catch (Exception e){
                        break;
                    }

                    //create object to add
                    XCurrencyObject new_object = new XCurrencyObject();
                    new_object.setCurrency_name(name_incoming);
                    new_object.setCurrency(currency_double);

                    //verify already exist
                    if(XCurrencyObjectList.getInstance().checkAlreadyExists(new_object.getCurrency_name()) == true){
                        break;
                    }

                    //add object
                    XCurrencyObjectList.getInstance().add(new_object);

                    //output to disk
                    XCurrencyObjectList.getInstance().output(context);

                    //refresh the list
                    refreshListView();

                    break;
                }
                case R.id.button_delete:{
                    if(selected_object != null){
                        XCurrencyObjectList.getInstance().deleteObjectByName(selected_object.getCurrency_name());
                        selected_object = null;
                        //output to disk
                        XCurrencyObjectList.getInstance().output(context);

                        refreshListView();

                    }
                    break;
                }
                case R.id.button_update:{
                    if(selected_object == null){
                        break;
                    }
                    // check content
                    String name_incoming = edittext_name.getText().toString();
                    String currency_incoming = edittext_currency.getText().toString();

                    if(name_incoming == null || name_incoming.equals("")){

                        break;
                    }
                    if(currency_incoming == null || currency_incoming.equals("")){
                        break;
                    }
                    double currency_double;
                    try{
                        currency_double = Double.parseDouble(currency_incoming);
                    }
                    catch (Exception e){
                        break;
                    }

                    selected_object.setCurrency_name(name_incoming);
                    selected_object.setCurrency(currency_double);

                    selected_object = null;

                    //output to disk
                    XCurrencyObjectList.getInstance().output(context);

                    refreshListView();

                    break;
                }
                case R.id.button_back:{

                    clearAndJumpToMain();
                    break;
                }
            }
        }
    }

    private void clearAndJumpToMain(){
        Intent intent = new Intent(XCurrency.this,XMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void refreshListView(){
        adapter.notifyDataSetChanged();
    }

    class XOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selected_object = XCurrencyObjectList.getInstance().getObjectWithId(Integer.toString(position));
            edittext_name.setText(selected_object.getCurrency_name());
            edittext_currency.setText(selected_object.getCurrency().toString());
        }
    }
}
