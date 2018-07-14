package liaoudi.xmoneynote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class XObjectAdapter extends ArrayAdapter{
  private final int resourceId;

  public XObjectAdapter(@NonNull Context context, int resource, @NonNull List objects) {
    super(context, resource, objects);
    resourceId = resource;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    XObject x_object = (XObject) getItem(position);
    View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
//    ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
//    TextView fruitName = (TextView) view.findViewById(R.id.fruit_name);
//    fruitImage.setImageResource(fruit.getImageId());
//    fruitName.setText(fruit.getName());
    TextView xobject_id = (TextView) view.findViewById(R.id.xobject_id);
    TextView xobject_des = (TextView) view.findViewById(R.id.xobject_des);
    TextView xobject_cost = (TextView) view.findViewById(R.id.xobject_cost);
    TextView xobject_unit = (TextView) view.findViewById(R.id.xobject_unit);
    TextView xobject_category = (TextView) view.findViewById(R.id.xobject_category);
    TextView xobject_children_count = (TextView) view.findViewById(R.id.xobject_children_count);
    xobject_id.setText(x_object.getId());
    xobject_des.setText(x_object.getDes());
    xobject_cost.setText(x_object.getCost().toString());
    xobject_unit.setText(x_object.getUnit());
    xobject_children_count.setText("子项共:"+Integer.toString(x_object.getChildren_list().getLength())+"个");
    if(x_object.getCategory().equals("HAOHAO")){
      xobject_category.setText("昊昊家付");
    }
    else if(x_object.getCategory().equals("YUANYUAN")){
      xobject_category.setText("远远家付");
    }
    else{
      xobject_category.setText("共同承担");
    }
    return view;
  }


}
