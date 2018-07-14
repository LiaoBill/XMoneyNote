package liaoudi.xmoneynote;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import liaoudi.xmoneynote.XCamera.XCameraTaking;

public class XDetailAc extends AppCompatActivity {
    private EditText edittext_des;
    private EditText edittext_detail_des;
    private ImageView imageview_detail;
    private Button button_previous;
    private Button button_next;
    private Button button_photonew;
    private Button button_update;
    private Button button_cancel;

    private TextView textView_info;

    private File image_father_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdetail);

        edittext_des = (EditText) findViewById(R.id.edittext_des);
        edittext_detail_des = (EditText) findViewById(R.id.edittext_detail_des);

        imageview_detail = (ImageView) findViewById(R.id.imageview_detail);
        imageview_detail.setOnClickListener(new XOnClickListener());

        button_previous = (Button) findViewById(R.id.button_previous);
        button_previous.setOnClickListener(new XOnClickListener());
        button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new XOnClickListener());
        button_photonew = (Button) findViewById(R.id.button_photonew);
        button_photonew.setOnClickListener(new XOnClickListener());
        button_update = (Button) findViewById(R.id.button_update);
        button_update.setOnClickListener(new XOnClickListener());
        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new XOnClickListener());

        textView_info = (TextView)findViewById(R.id.textview_info);

        //get permissions
        getPermissions();

        //permission gotten, get path ready
        image_father_path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        image_father_path.mkdirs();

        // get clicked object
        XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();

        // set strings
        edittext_des.setText(current_xobject.getDes());
        edittext_detail_des.setText(current_xobject.getDetail_des());
        edittext_detail_des.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        textView_info.setText("还没有照片哦");

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
        imageIndex = 0;
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


        //set updated image urls
        current_xobject.setImage_urls(image_urls);
    }

    private void showCurrentImage(){
        XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
        ArrayList<String> image_urls = current_xobject.getImage_urls();
        String current_image_url = image_urls.get(imageIndex);
        Bitmap bitmap = BitmapFactory.decodeFile(image_father_path.toString() + "/" + current_image_url + ".jpg");
        imageview_detail.setImageBitmap(bitmap);

        //info update
        if(image_urls.size() == 0) {
            textView_info.setText("还没有照片哦");
        } else {
            textView_info.setText((imageIndex+1)+"/"+image_urls.size());
        }
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

    private int imageIndex = 0;

    class XOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageview_detail:{
                    XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();

                    //check all pictures whether be removed
                    ArrayList<String> image_urls = current_xobject.getImage_urls();
                    if(image_urls.size() == 0){
                        //nothing to check
                        return;
                    }
                    //jump to big picture viewing
                    Intent intent = new Intent();
                    intent.setClass(XDetailAc.this,XImageDetailView.class);
                    intent.putExtra("imageIndex",imageIndex);
                    XDetailAc.this.startActivity(intent);
                    break;
                }
                case R.id.button_previous: {
                    showPreviousImage();
                    break;
                }
                case R.id.button_next: {
                    showNextImage();
                    break;
                }
                case R.id.button_photonew: {
                    Intent intent = new Intent();
                    intent.setClass(XDetailAc.this,XCameraTaking.class);
                    XDetailAc.this.startActivity(intent);
                    break;
                }
                case R.id.button_update: {
                    String new_des = edittext_des.getText().toString();
                    String new_detail_des = edittext_detail_des.getText().toString();
                    if(new_des == null || new_des.equals("")){
                        showToastMessage("新的标题不能为空");
                        break;
                    }


                    XObject current_xobject = XLevelRecorder.getInstance().getCurrent_xobject();
                    current_xobject.setDes(new_des);
                    current_xobject.setDetail_des(new_detail_des);

                    //output
                    XDateList.getInstance().output(context);

                    showToastMessage("更新成功");
                    clearAndJumpToMain();
                    break;
                }
                case R.id.button_cancel: {
                    clearAndJumpToMain();
                    break;
                }
            }
        }
    }
    private Context context = this;
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

    public void getPermissions(){
        Activity thisActivity = XDetailAc.this;
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(thisActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(thisActivity,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showToastMessage("you have to allow these permissions for using this function!");
                    clearAndJumpToMain();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void clearAndJumpToMain(){
        Intent intent = new Intent(XDetailAc.this,XMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
    }

}
