package com.example.codelib.personcodelib;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.codelib.personcodelib.ui.RippleButton.RippleCompat;
import com.example.codelib.personcodelib.ui.ToggleButton.SlideSwitch;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testRippleButton();
        testSlideSwitchButton();
    }

    private void testRippleButton() {
        Button testBtn = (Button)findViewById(R.id.test_ripple_btn);
        //RippleCompat.apply(testBtn);
        RippleCompat.apply(testBtn,0x7000ff00);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void testSlideSwitchButton(){
        SlideSwitch slideBtn = findViewById(R.id.toggle_btn);
        slideBtn.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                Log.e(TAG,"isOpen");
            }

            @Override
            public void close() {
                Log.e(TAG,"isClose");
            }
        });
    }
}
