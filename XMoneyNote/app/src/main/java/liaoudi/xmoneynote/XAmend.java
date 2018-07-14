package liaoudi.xmoneynote;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import liaoudi.xmoneynote.XCurrencySys.XCurrencyObjectList;

public class XAmend extends AppCompatActivity {
    private Button button_delete;
    private Button button_cancel;
    private Button button_modify;
    private EditText edittext_des;
    private EditText edittext_cost;
    //private EditText edittext_unit;
    private RadioGroup radiogroup_category;
    private TextView textView_info;
    private Context context = this;
    private Spinner spinner_unit;

    private String current_selected_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xamend);

        button_delete = (Button) findViewById(R.id.button_delete);
        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_modify = (Button) findViewById(R.id.button_modify);
        edittext_des = (EditText)findViewById(R.id.edittext_des);
        edittext_cost = (EditText)findViewById(R.id.edittext_cost);
        //edittext_unit = (EditText)findViewById(R.id.edittext_unit);
        radiogroup_category = (RadioGroup) findViewById(R.id.radiogroup_category);
        textView_info = (TextView)findViewById(R.id.textview_info);
        spinner_unit = (Spinner)findViewById(R.id.spinner_unit);

        button_delete.setOnClickListener(new XOnClickListener());
        button_cancel.setOnClickListener(new XOnClickListener());
        button_modify.setOnClickListener(new XOnClickListener());

        //prepare spinner
        XCurrencyObjectList.getInstance().read(context);
        String[] unit_items = new String[XCurrencyObjectList.getInstance().getLength()];
        for(int i =0;i!=XCurrencyObjectList.getInstance().getLength();i++){
            unit_items[i] = XCurrencyObjectList.getInstance().getList().get(i).getCurrency_name();
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, unit_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_unit.setAdapter(adapter);

        readAmendingObject();

        Intent intent = getIntent();
        current_selected_position = intent.getStringExtra("position");
    }
    private void clearAndJumpToMain(){
        Intent intent = new Intent(XAmend.this,XMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
    }
    class XOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_modify:{
                    //check valid
                    String des = edittext_des.getText().toString();
                    if(des == null || des.equals("")){
                        textView_info.setText("计费项描述不能不填~!");
                        break;
                    }
                    String cost_s = edittext_cost.getText().toString();
                    Double cost = 0.0;
                    try{
                        cost = Double.parseDouble(cost_s);
                    }
                    catch (Exception e){
                        textView_info.setText("花费金额数包含非法字符~!");
                        break;
                    }
                    String unit = (String)spinner_unit.getSelectedItem();
                    String category = "PUBLIC";
                    switch (radiogroup_category.getCheckedRadioButtonId()){
                        case R.id.category_yuanyuan_checked:{
                            category = "YUANYUAN";
                            break;
                        }
                        case R.id.category_haohao_checked:{
                            category = "HAOHAO";
                            break;
                        }
                        case R.id.category_public_checked:{
                            category = "PUBLIC";
                            break;
                        }
                    }

                    //amend object
                    XObject amending_object = XLevelRecorder.getInstance().getAmending_object();
                    amending_object.setDes(des);
                    amending_object.setCost(cost);
                    amending_object.setUnit(unit);
                    amending_object.setCategory(category);

                    //record to disk
                    XDateList.getInstance().output(context);

                    //jump back
                    clearAndJumpToMain();
                    break;
                }
                case R.id.button_cancel:{
                    clearAndJumpToMain();
                    break;
                }
                case R.id.button_delete:{
                    XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
                    XObject amending_object = XLevelRecorder.getInstance().getAmending_object();
                    if(current_xobject == null){
                        XDateList.getInstance().deleteEventById(current_selected_position);
                    }
                    else{
                        current_xobject.getChildren_list().deleteXObjectWithId(current_selected_position);
                    }

                    //record to disk
                    XDateList.getInstance().output(context);

                    //jump back
                    clearAndJumpToMain();
                    break;
                }
            }
        }
    }

    private void readAmendingObject(){
        XObject amending_object = XLevelRecorder.getInstance().getAmending_object();
        edittext_des.setText(amending_object.getDes());
        edittext_cost.setText(amending_object.getCost().toString());

        String unit_string = amending_object.getUnit();

        spinner_unit.setSelection(0);
        for(int i =0;i!= XCurrencyObjectList.getInstance().getLength();i++){
            if(XCurrencyObjectList.getInstance().getList().get(i).getCurrency_name().equals(unit_string)){
                spinner_unit.setSelection(i);
                break;
            }
        }

        String category = amending_object.getCategory();
        if(category.equals("YUANYUAN")){
            /*
            category = "YUANYUAN";
            category = "HAOHAO";
            category = "PUBLIC";
             */
            radiogroup_category.check(R.id.category_yuanyuan_checked);
        }
        else if(category.equals("HAOHAO")){
            radiogroup_category.check(R.id.category_haohao_checked);
        }
        else{
            radiogroup_category.check(R.id.category_public_checked);
        }
    }
}
