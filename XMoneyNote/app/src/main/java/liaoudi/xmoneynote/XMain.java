package liaoudi.xmoneynote;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import liaoudi.xmoneynote.XCurrencySys.XCurrency;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyObjectList;
import liaoudi.xmoneynote.XExImportSys.XExImport;
import liaoudi.xmoneynote.XMoneyExhangeCalSys.XExchangeMoney;
import liaoudi.xmoneynote.XSearchSys.XSearch;
import liaoudi.xmoneynote.XStatisticSys.XStatistic;

public class XMain extends AppCompatActivity {

    private Context context = this;
    private XObjectAdapter adapter;
    private TextView textview_current_title;
    private ListView listView;
    private ArrayList<XObject> current_object_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmain);

        Button add_button = (Button) findViewById(R.id.button_add);
        add_button.setOnClickListener(new XOnClickListener());
//        Button back_button = (Button) findViewById(R.id.button_back);
//        back_button.setOnClickListener(new XOnClickListener());
        Button currency_button = (Button) findViewById(R.id.button_currency);
        currency_button.setOnClickListener(new XOnClickListener());
        Button exchange_money_button = (Button) findViewById(R.id.button_money_exchange);
        exchange_money_button.setOnClickListener(new XOnClickListener());
        Button search_button = (Button) findViewById(R.id.button_search);
        search_button.setOnClickListener(new XOnClickListener());
        Button statistic_button = (Button) findViewById(R.id.button_statistic);
        statistic_button.setOnClickListener(new XOnClickListener());
        Button button_eximport = (Button) findViewById(R.id.button_eximport);
        button_eximport.setOnClickListener(new XOnClickListener());

        textview_current_title = (TextView)findViewById(R.id.textview_current_title);
        textview_current_title.setOnClickListener(new XOnClickListener());

        listView = (ListView) findViewById(R.id.xmain_list);
        listView.setOnItemClickListener(new XOnItemClickListener());
        listView.setOnItemLongClickListener(new XOnItemLongClickListener());

        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });



        // addTestData();
        //XDateList.getInstance().read(context);

        if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
            XDateList.getInstance().read(context);
            current_object_list = XDateList.getInstance().getList();
            refreshListView();
        }
        else {
            current_object_list = XLevelRecorder.getInstance().getCurrent_xobject_list().getList();
            refreshListView();
        }

        //set current title
        if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
            textview_current_title.setText("日期列表 ▼" + XDateList.getInstance().getLength());
        }
        else{
            XObject current_xobject_textview = XLevelRecorder.getInstance().getCurrent_xobject();
            textview_current_title.setText(current_xobject_textview.getDes()+" ▼" + current_xobject_textview.getChildren_list().getLength());
        }

        // jsonStringFyTest();
        // System.out.println("oncreate");

        //read currency
        XCurrencyObjectList.getInstance().read(context);

        back_status = 0;
    }

    private void refreshListView(){
        adapter = new XObjectAdapter(XMain.this, R.layout.xobject_item, current_object_list);
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);
    }

    class XOnItemLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(XCurrencyObjectList.getInstance().getLength() == 0){
                showToastMessage("请至少有一种货币汇率~");
                return true;
            }
            XObject amending_object = current_object_list.get(position);
            XLevelRecorder.getInstance().setAmending_object(amending_object);
            Intent intent = new Intent();
            intent.setClass(XMain.this,XAmend.class);
            intent.putExtra("position",Integer.toString(position));
            XMain.this.startActivity(intent);
            return true;
        }
    }


    class XOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            XObject selected_object = current_object_list.get(position);
            XLevelRecorder.getInstance().setCurrent_xobject(selected_object);

            XObjectList selected_objects_children = selected_object.getChildren_list();
            XLevelRecorder.getInstance().setCurrent_xobject_list(selected_objects_children);

            current_object_list = selected_objects_children.getList();

            refreshListView();

            //set current title
            if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
                textview_current_title.setText("日期列表 ▼" + XDateList.getInstance().getLength());
            }
            else{
                XObject current_xobject_textview = XLevelRecorder.getInstance().getCurrent_xobject();
                textview_current_title.setText(current_xobject_textview.getDes()+" ▼" + current_xobject_textview.getChildren_list().getLength());
            }

            System.out.println(selected_object.getXObjectString());
        }
    }

    private int back_status = 0;

    @Override
    protected void onResume() {
        super.onResume();
        back_status = 0;
    }

    @Override
    public void onBackPressed() {
        if(XLevelRecorder.getInstance().getCurrent_xobject() == null && back_status == 1){
            back_status = 2;
            showToastMessage("再按一次退出应用");
            return;
        }
        if(XLevelRecorder.getInstance().getCurrent_xobject() == null && back_status == 2){
            //exit app
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return;
        }
        if(XLevelRecorder.getInstance().getCurrent_xobject() == null && back_status == 0){
            back_status = 1;
            buttonBackPressed();
            return;
        }
        if(XLevelRecorder.getInstance().getCurrent_xobject() != null){
            back_status = 0;
            buttonBackPressed();
            return;
        }
    }

    private Toast toast;

    private void showToastMessage(String message){
        if(toast != null){
          toast.cancel();
        }

        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void buttonBackPressed(){
        if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
            showToastMessage("已经没有上一级啦~");
            return;
        }
        if (XLevelRecorder.getInstance().getCurrent_xobject().getFather_list() == null){
            current_object_list = XDateList.getInstance().getList();
            XLevelRecorder.getInstance().setCurrent_xobject(null);
            XLevelRecorder.getInstance().setCurrent_xobject_list(null);
        }
        else{
            current_object_list = XLevelRecorder.getInstance().getCurrent_xobject().getFather_list().getList();
            XLevelRecorder.getInstance().setCurrent_xobject_list(XLevelRecorder.getInstance().getCurrent_xobject().getFather_list());
            XLevelRecorder.getInstance().setCurrent_xobject(XLevelRecorder.getInstance().getCurrent_xobject().getFather_list().getFather_object());
        }
        refreshListView();

        //set current title
        if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
            textview_current_title.setText("日期列表 ▼" + XDateList.getInstance().getLength());
        }
        else{
            XObject current_xobject_textview = XLevelRecorder.getInstance().getCurrent_xobject();
            textview_current_title.setText(current_xobject_textview.getDes()+" ▼" + current_xobject_textview.getChildren_list().getLength());
        }
    }

    class XOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.textview_current_title:{
                    //get current object info
                    if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
                        //textview_current_title.setText("日期列表 ▼");
                        //do nothing
                    }
                    else{
                        //textview_current_title.setText(XLevelRecorder.getInstance().getCurrent_xobject().getDes()+" ▼");
                        //open detail activity
                        Intent intent = new Intent();
                        intent.setClass(XMain.this,XDetailAc.class);
                        XMain.this.startActivity(intent);
                    }
                    break;
                }
                case R.id.button_add:{
                    if(XCurrencyObjectList.getInstance().getLength() == 0){
                        showToastMessage("请至少有一种货币汇率~");
                        break;
                    }
                    Intent intent = new Intent();
                    intent.setClass(XMain.this,XAdd.class);
                    XMain.this.startActivity(intent);
                    break;
                }
                case R.id.button_back:{
//                    if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
//                        showToastMessage("已经没有上一级啦~");
//                        break;
//                    }
//                    if (XLevelRecorder.getInstance().getCurrent_xobject().getFather_list() == null){
//                        current_object_list = XDateList.getInstance().getList();
//                        XLevelRecorder.getInstance().setCurrent_xobject(null);
//                        XLevelRecorder.getInstance().setCurrent_xobject_list(null);
//                    }
//                    else{
//                        current_object_list = XLevelRecorder.getInstance().getCurrent_xobject().getFather_list().getList();
//                        XLevelRecorder.getInstance().setCurrent_xobject_list(XLevelRecorder.getInstance().getCurrent_xobject().getFather_list());
//                        XLevelRecorder.getInstance().setCurrent_xobject(XLevelRecorder.getInstance().getCurrent_xobject().getFather_list().getFather_object());
//                    }
//                    refreshListView();
//
//                    //set current title
//                    if(XLevelRecorder.getInstance().getCurrent_xobject() == null){
//                        textview_current_title.setText("日期列表 ▼");
//                    }
//                    else{
//                        textview_current_title.setText(XLevelRecorder.getInstance().getCurrent_xobject().getDes()+" ▼");
//                    }
                    break;
                }
                case R.id.button_currency:{
                    Intent intent = new Intent();
                    intent.setClass(XMain.this,XCurrency.class);
                    XMain.this.startActivity(intent);
                    break;
                }
                case R.id.button_money_exchange:{
                    if(XCurrencyObjectList.getInstance().getLength() == 0){
                        showToastMessage("请至少有一种货币汇率~");
                        break;
                    }
                    Intent intent = new Intent();
                    intent.setClass(XMain.this,XExchangeMoney.class);
                    XMain.this.startActivity(intent);
                    break;
                }
                case R.id.button_search:{

                    if(XDateList.getInstance().getLength() == 0){
                        showToastMessage("请至少有一条记录~");
                        break;
                    }
                    if(XCurrencyObjectList.getInstance().getLength() == 0){
                        showToastMessage("请至少有一种货币汇率~");
                        break;
                    }
                    Intent intent = new Intent();
                    intent.setClass(XMain.this,XSearch.class);
                    XMain.this.startActivity(intent);
                    break;
                }
                case R.id.button_statistic:{

                    if(XDateList.getInstance().getLength() == 0){
                        showToastMessage("请至少有一条记录~");
                        break;
                    }
                    if(XCurrencyObjectList.getInstance().getLength() == 0){
                        showToastMessage("请至少有一种货币汇率~");
                        break;
                    }
                    Intent intent = new Intent();
                    intent.setClass(XMain.this,XStatistic.class);
                    XMain.this.startActivity(intent);
                    break;
                }
                case R.id.button_eximport:{
                    Intent intent = new Intent();
                    intent.setClass(XMain.this,XExImport.class);
                    XMain.this.startActivity(intent);
                    break;
                }
            }
        }
    }

    private void addTestData(){
        XObject newone = new XObject("1");
        newone.setDes("早饭");
        newone.setUnit("12");
        newone.setCost(12.00);
        newone.setCategory("11");

        XObject newone2 = new XObject("2");
        newone2.setDes("参观葡萄酒庄");
        newone2.setUnit("122");
        newone2.setCost(12.002);
        newone2.setCategory("112");

        XObject newone3 = new XObject("3");
        newone3.setDes("晚饭");
        newone3.setUnit("123");
        newone3.setCost(12.003);
        newone3.setCategory("113");

        XObject newone4 = new XObject("4");
        newone4.setDes("饭后红酒");
        newone4.setUnit("124");
        newone4.setCost(12.04);
        newone4.setCategory("114");

        newone2.addChildren(newone3);
        newone3.addChildren(newone4);


        XDateList.getInstance().addEvent(newone);
        XDateList.getInstance().addEvent(newone2);
    }

    private void jsonStringFyTest(){
        /*
        String jsonfy_cons = XDateList.getInstance().jsonStringFy();
        System.out.println(jsonfy_cons);
        XDateList.getInstance().clearList();
        XDateList.getInstance().recoverFromIncomingJsonString(jsonfy_cons);

        ArrayList<XObject> list = XDateList.getInstance().getList();
        for(int i =0;i!=list.size();i++){
            System.out.println(list.get(i).getXObjectString());
        }
        */
        String jsonfy_cons = XDateList.getInstance().jsonStringFy();
        System.out.println(jsonfy_cons);
        XDateList.getInstance().output(context);
        XDateList.getInstance().clearList();
        XDateList.getInstance().read(context);
        ArrayList<XObject> list = XDateList.getInstance().getList();
        for(int i =0;i!=list.size();i++){
            System.out.println(list.get(i).getXObjectString());
        }


    }

}
