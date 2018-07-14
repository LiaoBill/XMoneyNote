package liaoudi.xmoneynote.XCurrencySys;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by billliao on 2018/6/2.
 */

public class XCurrencyObjectList {
    private ArrayList<XCurrencyObject> list;
    private static XCurrencyObjectList thisInstance;
    public static XCurrencyObjectList getInstance()
    {
        if(thisInstance == null){
            thisInstance = new XCurrencyObjectList();
        }
        return thisInstance;
    }
    private XCurrencyObjectList(){
        list = new ArrayList<>();
    }

    public void add(XCurrencyObject newone){
        this.list.add(newone);
    }

    public void deleteWithIndex(String index){
        int index_int = Integer.parseInt(index);
        this.list.remove(index_int);
    }

    public XCurrencyObject getObjectWithId(String id){
        int index_int = Integer.parseInt(id);
        return this.list.get(index_int);
    }

    public void deleteObjectByName(String name){
        for(int i =0;i!=this.list.size();i++){
            XCurrencyObject currentObject = this.list.get(i);
            if(currentObject.getCurrency_name().equals(name)){
                this.list.remove(i);
                break;
            }
        }
    }

    public boolean checkAlreadyExists(String name){
        for(int i =0;i!=this.list.size();i++){
            XCurrencyObject currentObject = this.list.get(i);
            if(currentObject.getCurrency_name().equals(name)){
                return true;
            }
        }
        return false;
    }

    public int getLength(){
        return this.list.size();
    }

    public ArrayList<XCurrencyObject> getList() {
        return list;
    }

    public void setList(ArrayList<XCurrencyObject> list) {
        this.list = list;
    }

    public boolean output(Context context){
        FileOutputStream fileOutputStream =null;
        PrintWriter printWriter = null;
        try {
            fileOutputStream = context.openFileOutput("currency_saver.txt",Context.MODE_PRIVATE);
            printWriter = new PrintWriter(fileOutputStream);
            for(int i =0;i!=this.list.size();i++){
                printWriter.println(this.list.get(i).getString());
            }
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
        this.list.clear();
        FileInputStream inputStream = null;
        Scanner scanner = null;
        try {
            inputStream = context.openFileInput("currency_saver.txt");
            scanner = new Scanner(inputStream);
            String line = null;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                XCurrencyObject new_object = new XCurrencyObject();
                new_object.recoverFromString(line);
                this.add(new_object);
            }
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
}
