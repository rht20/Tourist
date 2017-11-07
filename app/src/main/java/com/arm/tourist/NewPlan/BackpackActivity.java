package com.arm.tourist.NewPlan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.arm.tourist.R;


public class BackpackActivity extends AppCompatActivity {

    private Button SaveAndExitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpack);

        SaveAndExitBtn = findViewById(R.id.SaveBtn);

        SaveAndExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
