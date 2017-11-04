package com.unixtrong.tablelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SampleObject sample = new SampleObject();
        sample.header1 = "danyun";

        Field[] fields = SampleObject.class.getFields();
        Utils.debug("getFields: " + Arrays.toString(fields));
        Field[] declaredFields = SampleObject.class.getDeclaredFields();
        Utils.debug("getDeclaredFields: " + Arrays.toString(declaredFields));
        for (Field field : declaredFields) {
            try {
                Object o = field.get(sample);
                Utils.debug(String.format("%s: %s", field.getName(), o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        List<SampleObject> dataList = new ArrayList<SampleObject>() {{
            for (int i = 1; i <= 20; i++) {
                add(new SampleObject(
                        "Col 1, Row " + i,
                        "Col 2, Row " + i + " - multi lines",
                        "Col 3, Row " + i,
                        "Col 4, Row " + i,
                        "Col 5, Row " + i,
                        "Col 6, Row " + i,
                        "Col 7, Row " + i,
                        "Col 8, Row " + i,
                        "Col 9, Row " + i,
                        "Col A, Row " + i,
                        "Col B, Row " + i,
                        "Col C, Row " + i
                ));
            }
        }};

        SimpleTableLayout tableLayout = (SimpleTableLayout) findViewById(R.id.stl);
        LinkedHashMap<String, LinkedHashMap<String, ?>> map = new LinkedHashMap<>();
        for (SampleObject bean : dataList) {
            map.put(bean.header1, toMap(bean));
        }
        tableLayout.setFirstCell("Unixtrong");
        tableLayout.setDataList(map);
    }

    private LinkedHashMap<String, String> toMap(SampleObject bean) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("2", bean.header2);
        map.put("3", bean.header3);
        return map;
    }
}
