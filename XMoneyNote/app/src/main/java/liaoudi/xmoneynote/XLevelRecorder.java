package liaoudi.xmoneynote;

/**
 * Created by billliao on 2018/6/2.
 */

public class XLevelRecorder {

    private XObjectList current_xobject_list;
    private XObject current_xobject;
    private XObject amending_object;

    public XObject getAmending_object() {
        return amending_object;
    }

    public void setAmending_object(XObject amending_object) {
        this.amending_object = amending_object;
    }

    private static XLevelRecorder thisInstance;
    public static XLevelRecorder getInstance()
    {
        if(thisInstance == null){
            thisInstance = new XLevelRecorder();
        }
        return thisInstance;
    }
    private XLevelRecorder(){
        current_xobject = null;
        current_xobject_list = null;
    }

    public XObjectList getCurrent_xobject_list() {
        return current_xobject_list;
    }

    public void setCurrent_xobject_list(XObjectList current_xobject_list) {
        this.current_xobject_list = current_xobject_list;
    }

    public XObject getCurrent_xobject() {
        return current_xobject;
    }

    public void setCurrent_xobject(XObject current_xobject) {
        this.current_xobject = current_xobject;
    }
}
