package liaoudi.xmoneynote.XCamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import liaoudi.xmoneynote.R;
import liaoudi.xmoneynote.XDateList;

public class XCameraTaking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcamera_taking);

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }
}
