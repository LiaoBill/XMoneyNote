package liaoudi.xmoneynote.XSearchSys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import liaoudi.xmoneynote.R;
import liaoudi.xmoneynote.XAdd;
import liaoudi.xmoneynote.XDateList;
import liaoudi.xmoneynote.XLevelRecorder;
import liaoudi.xmoneynote.XMain;
import liaoudi.xmoneynote.XObject;
import liaoudi.xmoneynote.XObjectAdapter;
import liaoudi.xmoneynote.XObjectList;

public class XSearch extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private XObjectAdapter adapter;
    private ArrayList<XObject> current_object_list;
    private ArrayList<XObject> whole_list;
    private ArrayList<String> date_embeded;
    private Spinner spinner_date;
    private EditText edittext_des;

    private String search_key_des;
    private String search_key_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xsearch);

        listView = (ListView) findViewById(R.id.xmain_list);
        listView.setOnItemClickListener(new XOnItemClickListener());
        spinner_date = (Spinner) findViewById(R.id.spinner_date);
        spinner_date.setOnItemSelectedListener(new XOnSpinnerSelectedListener());
        edittext_des = (EditText)findViewById(R.id.edittext_des);
        edittext_des.addTextChangedListener(new XOnTextChangeListener());


        current_object_list = new ArrayList<>();
        adapter = new XObjectAdapter(XSearch.this, R.layout.xobject_item, current_object_list);
        listView.setAdapter(adapter);

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
            date_items = new String[date_embeded.size()];
            for(int i =0;i!=date_embeded.size();i++){
                date_items[i] = date_embeded.get(i);
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, date_items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_date.setAdapter(adapter);
            spinner_date.setSelection(0);
        }
        else{
            clearAndJumpToMain();
        }

        //use spinner_date as key to first search
        search_key_date = date_items[0];
        search_key_des = null;
        edittext_des.setText("");
        this.searchThis();


        //print date embeded list for test
        /*
        for(int i =0;i!=date_embeded.size();i++){
            System.out.println(date_embeded.get(i));
        }
        */

        //print whole list for test
        /*
        for(int i =0;i!=whole_list.size();i++){
            System.out.println(whole_list.get(i).getDes());
        }
        */
    }

    private void searchThis(){
        //use two keys ifnot null search the whole_list and put into currentlist
        current_object_list.clear();
        for(int i =0;i!=whole_list.size();i++){
            XObject current = whole_list.get(i);
            //use search key des
            if(search_key_des == null || search_key_des.equals("")){

            } else {
                if(current.getDes().indexOf(search_key_des)>=0){

                }else{
                    continue;
                }
            }
            //use search key date
            if(current.getId().indexOf(search_key_date)>=0){
                current_object_list.add(current);
            }
        }
        adapter.notifyDataSetChanged();
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

    class XOnTextChangeListener implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!edittext_des.hasFocus()){
                return;
            }
            String des = edittext_des.getText().toString();
            if(des == null || des.equals("")){
                search_key_des = null;
                searchThis();
                return;
            }
            search_key_des = des;
            searchThis();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class XOnSpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            search_key_date = (String)spinner_date.getSelectedItem();
            searchThis();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            spinner_date.setSelection(0);
        }
    }

    class XOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    private void clearAndJumpToMain(){
        Intent intent = new Intent(XSearch.this,XMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
