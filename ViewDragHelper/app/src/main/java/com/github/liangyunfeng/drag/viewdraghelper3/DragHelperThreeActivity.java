package com.github.liangyunfeng.drag.viewdraghelper3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.liangyunfeng.drag.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yunfeng.l on 2018/7/2.
 */

public class DragHelperThreeActivity extends AppCompatActivity {

    private String[] data = {
            "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango",
            "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango",
            "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango",
            "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango"
    };

    ListView mListView;
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_helper_three);

        mListView = (ListView) findViewById(R.id.lv);
        mGridView = (GridView) findViewById(R.id.gv_img);

        mListView.setAdapter(new ArrayAdapter<String>(
                DragHelperThreeActivity.this, R.layout.list_item, data));

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 100; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("imageView", R.mipmap.ic_launcher);//添加图像资源的ID
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
                lstImageItem,//数据来源
                R.layout.grid_item,//night_item的XML实现
                //动态数组与ImageItem对应的子项
                new String[]{"imageView"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.iv});
        //添加并且显示
        mGridView.setAdapter(saImageItems);
    }
}
