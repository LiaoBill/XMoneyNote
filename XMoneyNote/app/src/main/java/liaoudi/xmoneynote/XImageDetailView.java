package liaoudi.xmoneynote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class XImageDetailView extends AppCompatActivity {
    private ImageView imageview_big;
    private Button button_previous;
    private Button button_next;
    private Button button_delete;
    private TextView textView_info;

    private Context context = this;

    private File image_father_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ximage_detail_view);

        imageview_big = (ImageView) findViewById(R.id.imageview_big);

        button_previous = (Button) findViewById(R.id.button_previous);
        button_previous.setOnClickListener(new XOnClickListener());
        button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new XOnClickListener());
        button_delete = (Button) findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new XOnClickListener());

        textView_info = (TextView)findViewById(R.id.textview_info);

        //permission gotten, get path ready
        image_father_path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        image_father_path.mkdirs();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // get clicked object
        XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();

        //check all pictures whether be removed
        ArrayList<String> image_urls = current_xobject.getImage_urls();
        if(image_urls.size() == 0){
            //nothing to check
        }
        else{
            checkAllImages();
        }

        //show pictures
        imageIndex = getIntent().getIntExtra("imageIndex",-1);
        System.out.println("imageIndex get~~~~~~~~~~~~~~~~~~~"+imageIndex);
        image_urls = current_xobject.getImage_urls();
        if(image_urls.size() == 0){
            //nothing to show

            //switch p and n buttons off
            button_previous.setVisibility(View.INVISIBLE);
            button_next.setVisibility(View.INVISIBLE);
        }
        else{
            button_previous.setVisibility(View.VISIBLE);
            button_next.setVisibility(View.VISIBLE);
            showCurrentImage();
        }
    }

    private void checkAllImages(){
        System.out.println("checking~~~~~~~~~~~~~~~~~~~");
        XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
        ArrayList<String> image_urls = current_xobject.getImage_urls();

        for(int i=0;i!=image_urls.size();i++){
            String current_url = image_urls.get(i);
            File check_file = new File(image_father_path.toString() + "/" + current_url + ".jpg");
//          Bitmap bitmap = BitmapFactory.decodeFile(image_father_path.toString() + "/" + current_url + ".jpg");
            if(!check_file.exists()){
                imageIndex = 0;
                image_urls.remove(i);
                if(image_urls.size() == 0){
                    break;
                } else {
                    if(i == image_urls.size()){
                        break;
                    }
                    else{
                        i--;
                    }
                }
            }

        }

        if(image_urls.size() == 0){
            clearAndJumpToMain();
        }

        //set updated image urls
        current_xobject.setImage_urls(image_urls);
    }

    private void clearAndJumpToMain(){
        Intent intent = new Intent(XImageDetailView.this,XDetailAc.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
    }

    private int imageIndex;

    private void showCurrentImage(){
        XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
        ArrayList<String> image_urls = current_xobject.getImage_urls();
        String current_image_url = image_urls.get(imageIndex);
        Bitmap bitmap = BitmapFactory.decodeFile(image_father_path.toString() + "/" + current_image_url + ".jpg");
        imageview_big.setImageBitmap(bitmap);

        //update info
        textView_info.setText((imageIndex+1)+"/"+image_urls.size());
    }

    private void showNextImage(){
        XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
        ArrayList<String> image_urls = current_xobject.getImage_urls();

        int whole_length = image_urls.size();
        if(imageIndex == whole_length-1){
            imageIndex = 0;
        } else {
            imageIndex++;
        }

        showCurrentImage();
    }

    private void showPreviousImage(){
        XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
        ArrayList<String> image_urls = current_xobject.getImage_urls();

        int whole_length = image_urls.size();
        if(imageIndex == 0){
            imageIndex = whole_length - 1;
        } else {
            imageIndex--;
        }

        showCurrentImage();
    }

    class XOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_previous: {
                    showPreviousImage();
                    break;
                }
                case R.id.button_next: {
                    showNextImage();
                    break;
                }
                case R.id.button_delete:{
                    XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
                    ArrayList<String> image_urls = current_xobject.getImage_urls();
                    String current_url = image_urls.get(imageIndex);
                    File check_file = new File(image_father_path.toString() + "/" + current_url + ".jpg");
                    check_file.delete();
                    image_urls.remove(imageIndex);

                    if(image_urls.size() == 0){
                        //output
                        XDateList.getInstance().output(context);
                        clearAndJumpToMain();
                        break;
                    }

                    if(imageIndex == image_urls.size()){
                        imageIndex--;
                    }

                    showCurrentImage();

                    //output
                    XDateList.getInstance().output(context);
                    break;
                }
            }
        }
    }
}
