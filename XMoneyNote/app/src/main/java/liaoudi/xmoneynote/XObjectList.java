package liaoudi.xmoneynote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class XObjectList {
  private ArrayList<XObject> list;

  public XObjectList() {
    this.list = new ArrayList<>();
  }

  private XObject father_object;

  public XObject getFather_object() {
    return father_object;
  }

  public void setList(ArrayList<XObject> list) {
    this.list = list;
  }

  public ArrayList<XObject> getList() {
    return list;
  }

  public void setFather_object(XObject father_object) {
    this.father_object = father_object;
  }

  public XObject getXObjectWithId(String id){
    //input: id is definitely valid
    //output: do or return null
    for(int i = 0;i!=this.list.size();i++){
      XObject current = this.list.get(i);
      if(current.getId().equals(id)){
        return current;
      }
    }
    return null;
  }

  public void addXObject(XObject new_object){
    //input: new_object is definitely valid
    //output: void
    this.list.add(new_object);
    new_object.setFather_list(this);
  }

  public void deleteXObjectWithId(String id){
    //input: id is definitely valid
    //output: void
    /*
    for(int i = 0;i!=this.list.size();i++){
      XObject current = this.list.get(i);
      if(current.getId().equals(id)){
        this.list.remove(i);
        break;
      }
    }
    */
    this.list.remove(Integer.parseInt(id));
  }

  public void assignXObjectWithId(String id, XObject new_object){
    for(int i = 0;i!=this.list.size();i++) {
      XObject current = this.list.get(i);
      if (current.getId().equals(id)) {
        current.assignWithoutId(new_object);
        break;
      }
    }
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

  public int getLength(){
    return this.list.size();
  }

  public void recoverFromJsonArray(JSONArray incoming_array){
    this.list.clear();
    try{
      for (int i = 0;i!=incoming_array.length();i++){
        JSONObject incoming_object = incoming_array.getJSONObject(i);
        XObject new_object = new XObject("random_id");
        new_object.recoverFromJsonObject(incoming_object);
        this.addXObject(new_object);
      }
    }
    catch (Exception e){

    }
  }
  public String getListObjectString(){
    String cons = "";
    for (int i = 0;i!=this.list.size();i++) {
      cons+="--child"+Integer.toString(i)+"\n";
      cons+=this.list.get(i).getXObjectString()+"\n";
    }
    return cons;
  }

  public String getReportString(String adder){
    String cons = "";
    for (int i = 0;i!=this.list.size();i++) {
      cons+=this.list.get(i).getReportString(adder);
    }
    return cons;
  }
}
