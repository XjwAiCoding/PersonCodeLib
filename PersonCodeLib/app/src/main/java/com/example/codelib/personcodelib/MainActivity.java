package com.example.codelib.personcodelib;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.codelib.personcodelib.ui.RippleButton.RippleCompat;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testRippleButton();
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
}
