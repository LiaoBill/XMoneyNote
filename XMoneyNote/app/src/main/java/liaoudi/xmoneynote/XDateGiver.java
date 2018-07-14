package liaoudi.xmoneynote;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by billliao on 2018/6/2.
 */

public class XDateGiver {
    private static XDateGiver thisInstance;
    public static XDateGiver getInstance()
    {
        if(thisInstance == null){
            thisInstance = new XDateGiver();
        }
        return thisInstance;
    }
    private XDateGiver(){

    }

    public String getCurrentTimeAsId(){
        String nowtime_id = "";
        nowtime_id+= Calendar.getInstance().get(Calendar.MONTH)+ 1 +"/";
        nowtime_id+=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"\n";
        nowtime_id+=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":";
        nowtime_id+=Calendar.getInstance().get(Calendar.MINUTE);
        return nowtime_id;
    }

    public String getCurrentTimeAsImageName(){
        String nowtime_id = "";
        nowtime_id+= Calendar.getInstance().get(Calendar.MONTH)+ 1 +"_";
        nowtime_id+=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"_";
        nowtime_id+=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+"_";
        nowtime_id+=Calendar.getInstance().get(Calendar.MINUTE)+"_";
        nowtime_id+=Calendar.getInstance().get(Calendar.SECOND);
        return nowtime_id;
    }
}
