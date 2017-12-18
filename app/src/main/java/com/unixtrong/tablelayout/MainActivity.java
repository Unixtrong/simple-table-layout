package com.unixtrong.tablelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] mon = "语 数 外 数 理 化 生 体".split(" ");
        String[] tue = "体 语 数 外 数 理 化 生".split(" ");
        String[] wed = "生 体 语 数 外 数 理 化".split(" ");
        String[] thu = "化 生 体 语 数 外 数 理".split(" ");
        String[] fri = "理 化 生 体 语 数 外 数".split(" ");
        String[] sat = "数 理 化 生 体 语 数 外".split(" ");
        String[] Sun = "外 数 理 化 生 体 语 数".split(" ");

        List<SampleObject> dataList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String header = "第" + (i + 1) + "节";
            SampleObject obj = new SampleObject(header);
            obj.value1 = mon[i];
            obj.value2 = tue[i];
            obj.value3 = wed[i];
            obj.value4 = thu[i];
            obj.value5 = fri[i];
            obj.value6 = sat[i];
            obj.value7 = Sun[i];
            dataList.add(obj);
        }

        SimpleTableLayout tableLayout = (SimpleTableLayout) findViewById(R.id.stl);
        LinkedHashMap<String, LinkedHashMap<String, ?>> map = new LinkedHashMap<>();
        for (SampleObject bean : dataList) {
            map.put(bean.header, toMap(bean));
        }
        tableLayout.setFirstCell("节次 \\ 星期");
        tableLayout.setDataList(map);
    }

    private LinkedHashMap<String, String> toMap(SampleObject bean) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("星期一", bean.value1);
        map.put("星期二", bean.value2);
        map.put("星期三", bean.value3);
        map.put("星期四", bean.value4);
        map.put("星期五", bean.value5);
        map.put("星期六", bean.value6);
        map.put("星期日", bean.value7);
        return map;
    }
}
