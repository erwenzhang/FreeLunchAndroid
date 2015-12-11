package com.application.Kevin_Wenwen.FreeLunch;

/**
 * Created by xiangkundai on 12/10/15.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class CListView2 extends ListActivity {

    static final ArrayList<HashMap<String,String>> list =
            new ArrayList<HashMap<String,String>>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clistview);
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.crowview,
                new String[] {"rank","model","company"},
                new int[] {R.id.text1,R.id.text2, R.id.text3}
        );
        populateList();
        setListAdapter(adapter);
    }

    private void populateList() {
        HashMap map = new HashMap();
        map.put("rank", "1");
        map.put("model", "Samsung Galaxy Nexus");
        map.put("company", "Samsung");
        list.add(map);
        map = new HashMap();
        map.put("rank", "2");
        map.put("model", "Samsung Epic Touch 4G");
        map.put("company", "Samsung");
        list.add(map);
        map = new HashMap();
        map.put("rank", "3");
        map.put("model", "HTC Evo 3D");
        map.put("company", "HTC");
        list.add(map);
        map = new HashMap();
        map.put("rank", "4");
        map.put("model", "T-Mobile MyTouch 4G Slide");
        map.put("company", "HTC");
        list.add(map);
        map = new HashMap();
        map.put("rank", "5");
        map.put("model", "Samsung Galaxy S II");
        map.put("company", "Samsung");
        list.add(map);
        map = new HashMap();
        map.put("rank", "6");
        map.put("model", "Apple iPhone 4S 16GB");
        map.put("company", "Apple");
        list.add(map);
        map = new HashMap();
        map.put("rank", "7");
        map.put("model", "Motorola Droid Razr");
        map.put("company", "Motorola");
        list.add(map);
        map = new HashMap();
        map.put("rank", "8");
        map.put("model", "Motorola Droid Bionic");
        map.put("company", "Motorola");
        list.add(map);
        map = new HashMap();
        map.put("rank", "9");
        map.put("model", "Samsung Focus S");
        map.put("company", "Samsung ");
        list.add(map);
        map = new HashMap();
        map.put("rank", "10");
        map.put("model", "Samsung Focus Flash");
        map.put("company", "Samsung");
        list.add(map);
    }
}
