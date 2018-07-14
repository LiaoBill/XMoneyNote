package liaoudi.xmoneynote.XStatisticSys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import liaoudi.xmoneynote.R;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyObject;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyObjectList;
import liaoudi.xmoneynote.XDateList;
import liaoudi.xmoneynote.XMain;
import liaoudi.xmoneynote.XObject;
import liaoudi.xmoneynote.XObjectList;

public class XStatistic extends AppCompatActivity {
    private Button button_haohao;
    private Button button_yuanyuan;
    private Button button_all;
    private ArrayList<XObject> whole_list;
    private Context context = this;

    private TextView textview_cons;
    private Spinner spinner_date;
    private ArrayList<String> date_embeded;
    private String search_key_date;

    private TextView textview_cons_showing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xstatistic);

        button_haohao = (Button) findViewById(R.id.button_haohao);
        button_yuanyuan = (Button) findViewById(R.id.button_yuanyuan);
        button_all = (Button) findViewById(R.id.button_all);
        textview_cons = (TextView)findViewById(R.id.textview_cons);
        spinner_date = (Spinner) findViewById(R.id.spinner_date);
        spinner_date.setOnItemSelectedListener(new XOnSpinnerSelectedListener());
        textview_cons_showing = (TextView) findViewById(R.id.textview_cons_showing);

        button_haohao.setOnClickListener(new XOnClickListener());
        button_yuanyuan.setOnClickListener(new XOnClickListener());
        button_all.setOnClickListener(new XOnClickListener());

        //assemble the whole list
        whole_list = new ArrayList<>();
        XDateList.getInstance().read(context);
        ArrayList<XObject> date_list = XDateList.getInstance().getList();
        for(int i =0;i!=date_list.size();i++){
            XObject current = date_list.get(i);
            suckObject(current);
        }

        //fetch date embeded
        date_embeded = new ArrayList<>();
        for(int i =0;i!=whole_list.size();i++){
            XObject current = whole_list.get(i);
            String[] date_strings = current.getId().split("\\n");
            String date = date_strings[0];
            boolean flag = true;
            for(int j =0;j!=date_embeded.size();j++){
                if(date_embeded.get(j).equals(date)){
                    flag = false;
                    break;
                }
            }
            if(flag){
                date_embeded.add(date);
            }
        }

        // make spinner_date
        String[] date_items = null;
        if(date_embeded.size() != 0){
            date_items = new String[date_embeded.size()+1];
            for(int i =0;i!=date_embeded.size();i++){
                date_items[i] = date_embeded.get(i);
            }
            date_items[date_embeded.size()] = "ALL";
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, date_items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_date.setAdapter(adapter);
            spinner_date.setSelection(0);
        }
        else{
            clearAndJumpToMain();
        }

        search_key_date = date_items[0];

        //read currency
        XCurrencyObjectList.getInstance().read(context);

        currentSelectedButton = null;


    }

    private void clearAndJumpToMain(){
        Intent intent = new Intent(XStatistic.this,XMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void suckObject(XObject input_object){
        whole_list.add(input_object);
        this.suckObjectList(input_object.getChildren_list());
    }

    private void suckObjectList(XObjectList input_list){
        for(int i =0;i!=input_list.getLength();i++){
            this.suckObject(input_list.getList().get(i));
        }
    }

    private String currentSelectedButton;

    class XOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_haohao:{
                    Double cons = calcuAllWithCategory("HAOHAO");
                    textview_cons.setText(cons.toString()+" RMB");
                    currentSelectedButton = "HAOHAO";
                    textview_cons_showing.setText("统计结果 (昊昊家)");
                    break;
                }
                case R.id.button_yuanyuan:{
                    Double cons = calcuAllWithCategory("YUANYUAN");
                    textview_cons.setText(cons.toString()+" RMB");
                    currentSelectedButton = "YUANYUAN";
                    textview_cons_showing.setText("统计结果 (远远家)");
                    break;
                }
                case R.id.button_all:{
                    Double cons = calcuAllWithCategory("PUBLIC");
                    textview_cons.setText(cons.toString()+" RMB");
                    currentSelectedButton = "PUBLIC";
                    textview_cons_showing.setText("统计结果 (公共部分)");
                    break;
                }
            }
        }
    }
    class XOnSpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            search_key_date = (String)spinner_date.getSelectedItem();
            if(currentSelectedButton == null){
                return;
            }
            if (currentSelectedButton.equals("HAOHAO")){
                Double cons = calcuAllWithCategory("HAOHAO");
                textview_cons.setText(cons.toString()+" RMB");
                textview_cons_showing.setText("统计结果 (昊昊家)");
            }
            else if(currentSelectedButton.equals("YUANYUAN")){
                Double cons = calcuAllWithCategory("YUANYUAN");
                textview_cons.setText(cons.toString()+" RMB");
                textview_cons_showing.setText("统计结果 (远远家)");
            }
            else{
                Double cons = calcuAllWithCategory("PUBLIC");
                textview_cons.setText(cons.toString()+" RMB");
                textview_cons_showing.setText("统计结果 (公共部分)");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            spinner_date.setSelection(0);
        }
    }

    private Double calcuAllWithCategory(String category){
//        category = "YUANYUAN";
//        category = "HAOHAO";
//        category = "PUBLIC";
        Double cons = 0.0;
        for(int i =0;i!=whole_list.size();i++){
            XObject current = whole_list.get(i);
            if(!search_key_date.equals("ALL") && current.getId().indexOf(search_key_date)<0){
                continue;
            }
            if(current.getCategory().equals(category)){
                Double current_cost = current.getCost();
                String current_unit = current.getUnit();
                Double currency = 0.0;
                ArrayList<XCurrencyObject> currency_list = XCurrencyObjectList.getInstance().getList();
                for(int j =0;j!=currency_list.size();j++){
                    if(currency_list.get(j).getCurrency_name().equals(current_unit)){
                        currency = currency_list.get(j).getCurrency();
                        break;
                    }
                }
                if(currency == 0.0){
                    continue;
                }
                Double current_cons = current_cost/currency;
                current_cons = XObject.roundDoubleWith2Prece(current_cons);
                cons += current_cons;
            }
        }
        cons = XObject.roundDoubleWith2Prece(cons);
        return cons;
    }
}


