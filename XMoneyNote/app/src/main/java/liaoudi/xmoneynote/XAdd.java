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

public class XAdd extends AppCompatActivity {
    private EditText edittext_des;
    private EditText edittext_cost;
    //private EditText edittext_unit;
    private Spinner spinner_unit;
    private RadioGroup radiogroup_category;
    private TextView textView_info;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xadd);



        edittext_des = (EditText)findViewById(R.id.edittext_des);
        edittext_cost = (EditText)findViewById(R.id.edittext_cost);
        //edittext_unit = (EditText)findViewById(R.id.edittext_unit);
        spinner_unit = (Spinner)findViewById(R.id.spinner_unit);
        radiogroup_category = (RadioGroup) findViewById(R.id.radiogroup_category);
        textView_info = (TextView)findViewById(R.id.textview_info);

        Button button_add = (Button) findViewById(R.id.button_add);
        Button button_reset = (Button) findViewById(R.id.button_reset);

        button_add.setOnClickListener(new XOnClickListener());
        button_reset.setOnClickListener(new XOnClickListener());

        //prepare spinner
        XCurrencyObjectList.getInstance().read(context);
        String[] unit_items = new String[XCurrencyObjectList.getInstance().getLength()];
        for(int i =0;i!=XCurrencyObjectList.getInstance().getLength();i++){
            unit_items[i] = XCurrencyObjectList.getInstance().getList().get(i).getCurrency_name();
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, unit_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_unit.setAdapter(adapter);

        initAllValue();

    }

    private void initAllValue(){
        edittext_des.setText("");
        edittext_cost.setText("");
    }

    private void clearAndJumpToMain(){
        Intent intent = new Intent(XAdd.this,XMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
    }

    class XOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_add:{
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

                    //build xobject

                    XObject current_adding_object = new XObject("temp_id");
                    current_adding_object.setDes(des);
                    current_adding_object.setCost(cost);
                    current_adding_object.setUnit(unit);
                    current_adding_object.setCategory(category);

                    //add to current xobjectfatherlist
                    XObject recorded_current_object = XLevelRecorder.getInstance().getCurrent_xobject();
                    if(recorded_current_object == null){
                        //set id
                        current_adding_object.setId(XDateGiver.getInstance().getCurrentTimeAsId());
                        //add
                        XDateList.getInstance().addEvent(current_adding_object);
                    }
                    else {
                        //set id
                        current_adding_object.setId(XDateGiver.getInstance().getCurrentTimeAsId());
                        //add
                        recorded_current_object.addChildren(current_adding_object);
                    }

                    //record to disk
                    XDateList.getInstance().output(context);

                    //jump back
                    clearAndJumpToMain();
                    break;
                }
                case R.id.button_reset:{
                    edittext_des.setText("");
                    edittext_cost.setText("");
                    break;
                }
            }
        }
    }
}
