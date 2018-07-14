package liaoudi.xmoneynote;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class XDateList {
  private static XDateList thisInstance;
  public static XDateList getInstance()
  {
    if(thisInstance == null){
      thisInstance = new XDateList();
    }
    return thisInstance;
  }
  private ArrayList<XObject> list;
  private XDateList(){
    list = new ArrayList<>();
  }

  public void addEvent(XObject new_event){
    this.list.add(new_event);
    new_event.setFather_list(null);
  }

  public ArrayList<XObject> getList() {
    return list;
  }

  public int getLength(){
    return list.size();
  }

  public void deleteEventById(String id){
    /*
    for(int i =0;i!=this.list.size();i++){
      XObject current = this.list.get(i);
      if(current.getId().equals(id)){
        this.list.remove(i);
        break;
      }
    }
    */
    this.list.remove(Integer.parseInt(id));
  }

  public void clearList(){
    this.list.clear();
  }

  public String jsonStringFy(){
    JSONArray jsonArray = this.jsonFy();
    try{
      return jsonArray.toString(1);
    }catch (Exception e){
      return null;
    }
  }

  public JSONArray jsonFy(){
    JSONArray jsonArray = new JSONArray();

    for(int i = 0;i!=this.list.size();i++) {
      XObject current = this.list.get(i);
      jsonArray.put(current.jsonFy());
    }

    return jsonArray;
  }

  public void recoverFromJsonArray(JSONArray incoming_array){
    this.list.clear();
    try{
      for (int i = 0;i!=incoming_array.length();i++){
        JSONObject incoming_object = incoming_array.getJSONObject(i);
        XObject new_object = new XObject("random_id");
        new_object.recoverFromJsonObject(incoming_object);
        this.addEvent(new_object);
      }
    }
    catch (Exception e){

    }
  }

  public void recoverFromIncomingJsonString(String json_string){
    try{
      JSONArray incoming_array = new JSONArray(json_string);
      this.recoverFromJsonArray(incoming_array);
    }
    catch (Exception e){

    }
  }

  public String outputToString(){
    return this.jsonStringFy();
  }

  public boolean output(Context context){
    FileOutputStream fileOutputStream =null;
    PrintWriter printWriter = null;
    try {
      fileOutputStream = context.openFileOutput("object_saver.txt",Context.MODE_PRIVATE);
      printWriter = new PrintWriter(fileOutputStream);
      printWriter.println(this.jsonStringFy());
      //System.out.println(this.jsonStringFy());
      printWriter.close();
      fileOutputStream.close();
      return true;
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      try{
        if(printWriter!=null){
          printWriter.close();
        }
        if(fileOutputStream!=null){
          fileOutputStream.close();
        }
      }
      catch(Exception e){

      }
    }
    return false;
  }

  public boolean read(Context context){
    FileInputStream inputStream = null;
    Scanner scanner = null;
    try {
      inputStream = context.openFileInput("object_saver.txt");
      scanner = new Scanner(inputStream);
      String line = null;
      String json_string = "";
      while(scanner.hasNextLine()){
        line = scanner.nextLine();
        json_string+=line;
      }
      this.recoverFromIncomingJsonString(json_string);
      scanner.close();
      inputStream.close();
      return true;
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      try{
        if(scanner!=null){
          scanner.close();
        }
        if(inputStream!=null){
          inputStream.close();
        }
      }
      catch(Exception e){

      }
    }
    return false;
  }

  public String getReportString(String adder){
    String cons = "";
    for (int i = 0;i!=this.list.size();i++) {
      cons+=this.list.get(i).getReportString(adder);
    }
    return cons;
  }
}
