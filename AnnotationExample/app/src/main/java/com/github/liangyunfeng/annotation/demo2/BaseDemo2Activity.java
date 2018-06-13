package com.github.liangyunfeng.annotation.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.liangyunfeng.annotation.R;
import com.github.liangyunfeng.module_api.TAHelper2;
import com.guthub.liangyunfeng.module_annotation.FindId;
import com.guthub.liangyunfeng.module_annotation.OnClick;

@FindId(R.layout.activity_base_demo2)
public class BaseDemo2Activity extends AppCompatActivity {

    @FindId(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base_demo2);
        TAHelper2.inject(this);
        if(tv != null) {
            tv.setText("hahaha,how are you?");
        }
    }

    @OnClick({R.id.btn1})
    void clickTest(View view){
        switch (view.getId()){
            case R.id.btn1:
                tv.setText("This is a BindOnClick test.");
                Toast.makeText(BaseDemo2Activity.this,"test click",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
