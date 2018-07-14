package liaoudi.xmoneynote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import liaoudi.xmoneynote.XCurrencySys.XCurrencyObject;
import liaoudi.xmoneynote.XCurrencySys.XCurrencyObjectList;

public class XObject {
  private String id;
  private String des;
  private Double cost;
  private String unit;
  private String category;
  private ArrayList<String> image_urls;
  private String detail_des;

  private XObjectList father_list;

  //picture
  private XObjectList children_list;

  // Constructors
  public XObject(String id) {
    this.id = id;
    this.children_list = new XObjectList();
    this.children_list.setFather_object(this);
    image_urls = new ArrayList<String>();
    detail_des="";
  }

  // Getters and Setters

  public ArrayList<String> getImage_urls() {
    return image_urls;
  }

  public void setImage_urls(ArrayList<String> image_urls) {
    this.image_urls = image_urls;
  }

  public String getDetail_des() {
    return detail_des;
  }

  public void setDetail_des(String detail_des) {
    this.detail_des = detail_des;
  }

  public XObjectList getFather_list() {
    return father_list;
  }

  public void setFather_list(XObjectList father_list) {
    this.father_list = father_list;
  }

  public XObjectList getChildren_list() {
    return children_list;
  }

  private void setChildren_list(XObjectList children_list) {
    this.children_list = children_list;
    this.children_list.setFather_object(this);
  }

  public String getId() {
    return id;
  }

  public String getDes() {
    return des;
  }

  public Double getCost() {
    return cost;
  }

  public String getUnit() {
    return unit;
  }

  public String getCategory() {
    return category;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setDes(String des) {
    this.des = des;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void assignWithoutId(XObject new_object){
    this.setDes(new_object.getDes());
    this.setCost(new_object.getCost());
    this.setUnit(new_object.getUnit());
    this.setCategory(new_object.getCategory());
  }

  public String jsonStringFy(){
    String cons;
    try{
      JSONObject jsonfy_object = this.jsonFy();
      if (jsonfy_object == null){
        cons = "[ERROR OCCURED WHEN JSONFY]";
      } else {
        cons = jsonfy_object.toString(1);
      }
    }
    catch (Exception e){
      cons = "[ERROR OCCURED WHEN JSONSTRINGFY]";
    }
    return cons;
  }

  public JSONArray imageUrlsJsonFy(){
    JSONArray jsonArray = new JSONArray();

    for(int i = 0;i!=this.image_urls.size();i++) {
      String current_string = this.image_urls.get(i);
      jsonArray.put(current_string);
    }

    return jsonArray;
  }

  public JSONObject jsonFy(){
    JSONObject jsonobject = new JSONObject();
    try{
      jsonobject.put("id",this.getId());
      jsonobject.put("des",this.getDes());
      jsonobject.put("cost",this.getCost().toString());
      jsonobject.put("unit",this.getUnit());
      jsonobject.put("category",this.getCategory());
      if(this.getChildren_list().getLength() == 0){
        jsonobject.put("children_list",new JSONArray());
      } else {
        jsonobject.put("children_list",this.getChildren_list().jsonFy());
      }
      //put imageurls
      if(this.getImage_urls().size() == 0){
        jsonobject.put("image_urls",new JSONArray());
      } else {
        jsonobject.put("image_urls",imageUrlsJsonFy());
      }
      //put detail_des
      jsonobject.put("detail_des",this.getDetail_des());
      return jsonobject;
    }
    catch (Exception e){
      return null;
    }
  }

  public static Double roundDoubleWith2Prece(Double input){
    return (double) Math.round(input*100)/100;
  }

  public void addChildren(XObject child){
    this.children_list.addXObject(child);
  }

  public void recoverImageUrlsFromJsonArray(JSONArray incoming_array){
    this.image_urls.clear();
    try{
      for (int i = 0;i!=incoming_array.length();i++){
        String current_string = incoming_array.getString(i);
        this.image_urls.add(current_string);
      }
    }
    catch (Exception e){

    }
  }

  public void recoverFromJsonObject(JSONObject incoming_object){
    try{
      this.setId(incoming_object.getString("id"));
      this.setDes(incoming_object.getString("des"));
      this.setCost(Double.parseDouble(incoming_object.getString("cost")));
      this.setUnit(incoming_object.getString("unit"));
      this.setCategory(incoming_object.getString("category"));
      JSONArray incoming_array = incoming_object.getJSONArray("children_list");
      if(incoming_array == null || incoming_array.length() == 0){

      } else {
        this.children_list.recoverFromJsonArray(incoming_array);
      }
      JSONArray image_url_array = incoming_object.getJSONArray("image_urls");
      if(image_url_array == null || image_url_array.length() == 0){

      } else {
        recoverImageUrlsFromJsonArray(image_url_array);
      }
      this.setDetail_des(incoming_object.getString("detail_des"));
    }
    catch (Exception e){
      this.setId("ERROR");
      System.out.println(e);
    }
  }

  public String getXObjectString(){
    String cons = "";
    cons+=this.getId()+"|";
    cons+=this.getDes()+"|";
    cons+=this.getCost()+"|";
    cons+=this.getUnit()+"|";
    cons+=this.getCategory()+"|";
    if(this.getChildren_list().getLength()!=0){
      cons+=this.getChildren_list().getListObjectString();
    }
    if(this.image_urls.size()!=0) {
      for (int i = 0; i != image_urls.size() - 1; i++) {
        cons += image_urls.get(i) + ",";
      }
      cons += image_urls.get(image_urls.size() - 1) + "|";
    }
    cons+=this.getDetail_des();
    return cons;
  }

  public String getReportString(String adder){
    String cons = "";
    cons += adder + "|<" + this.getDes() + ">\n";

    // caculate cost
    Double current_cost = this.getCost();
    String current_unit = this.getUnit();
    Double currency = 0.0;
    ArrayList<XCurrencyObject> currency_list = XCurrencyObjectList.getInstance().getList();
    for (int j = 0; j != currency_list.size(); j++) {
      if (currency_list.get(j).getCurrency_name().equals(current_unit)) {
        currency = currency_list.get(j).getCurrency();
        break;
      }
    }
    if (currency == 0.0) {
      // not find currency
      cons += adder + "|花 " + this.getCost() + " " + this.getUnit() + " (汇率信息缺失无折合数值)\n";
    }
    else {
      Double current_cons = current_cost / currency;
      current_cons = XObject.roundDoubleWith2Prece(current_cons);
      cons += adder + "|折合花 " + current_cons.toString() + " 人民币, ";
    }
    //category
    if(this.getCategory().equals("HAOHAO")){
      cons += "昊昊.\n";
    }
    else if(this.getCategory().equals("YUANYUAN")){
      cons += "远远.\n";
    }
    else{
      cons += "公共.\n";
    }
    // children
    cons += this.getChildren_list().getReportString(adder + "|--");
    return cons;
  }
}
