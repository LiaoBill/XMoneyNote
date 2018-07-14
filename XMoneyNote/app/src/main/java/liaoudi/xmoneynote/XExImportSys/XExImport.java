package liaoudi.xmoneynote.XExImportSys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import liaoudi.xmoneynote.R;
import liaoudi.xmoneynote.XAdd;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyObjectList;
import liaoudi.xmoneynote.XDateList;
import liaoudi.xmoneynote.XMain;

public class XExImport extends AppCompatActivity {
  private ClipboardManager myClipboard;
  private Context context = this;
  private Button button_export_report;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_xex_import);

    Button button_import = (Button) findViewById(R.id.button_import);
    button_import.setOnClickListener(new XOnClickListener());
    Button button_export = (Button) findViewById(R.id.button_export);
    button_export.setOnClickListener(new XOnClickListener());
    button_export_report = (Button)findViewById(R.id.button_export_report);
    button_export_report.setOnClickListener(new XOnClickListener());


    myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
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

  private void clearAndJumpToMain(){
    Intent intent = new Intent(XExImport.this,XMain.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    //finish();
  }

  class XOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.button_import: {
          ClipData xclipdata = myClipboard.getPrimaryClip();
          ClipData.Item item = xclipdata.getItemAt(0);
          String jsonString = item.getText().toString();

          if(jsonString == null || jsonString.equals("")){
            showToastMessage("剪切板中还没有任何数据哦~");
            break;
          }

          XDateList.getInstance().recoverFromIncomingJsonString(jsonString);
          // 万一导入数据格式出错,所以选择不输出
          XDateList.getInstance().output(context);

          showToastMessage("导入成功~");

          clearAndJumpToMain();

          break;
        }
        case R.id.button_export:{
          String jsonData = XDateList.getInstance().outputToString();

          ClipData xdataclip;
          xdataclip = ClipData.newPlainText("text", jsonData);
          myClipboard.setPrimaryClip(xdataclip);

          showToastMessage("导出成功~");

          break;
        }
        case R.id.button_export_report:{
          ClipData xdataclip;
          xdataclip = ClipData.newPlainText("text", XDateList.getInstance().getReportString(""));
          myClipboard.setPrimaryClip(xdataclip);

          showToastMessage("导出成功~");
          break;
        }
      }
    }
  }
}
