package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    private final static String SP_NAME = "sharedPref";
    private final static String KEY_LARGE_TEXT = "largeText";

    private SharedPreferences sharedPref;
    private String largeText;
    private List<Map<String, String>> listContent = new ArrayList<>();
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        largeText = sharedPref.getString(KEY_LARGE_TEXT, null);

        if (largeText == null) {
            largeText = getString(R.string.large_text);
            sharedPref
                    .edit()
                    .putString(KEY_LARGE_TEXT, largeText)
                    .apply();
        }

        listContent = prepareContent(largeText);

        ListView listView = findViewById(R.id.list);

        final BaseAdapter listContentAdapter = createAdapter(listContent);

        listView.setAdapter(listContentAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listContent.remove(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });

/*        swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // Будет вызван, когда пользователь потянет список вниз
            @Override
            public void onRefresh() {
                listContent = prepareContent(sharedPref.getString(KEY_LARGE_TEXT, null));
                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });*/
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> listContent) {
        SimpleAdapter adapter = new SimpleAdapter(this, listContent, R.layout.list_item,
                new String[]{"paragraph", "number"}, new int[]{R.id.paragraph, R.id.number});
        return adapter;
    }

    @NonNull
    private List<Map<String, String>> prepareContent(String largeText) {
        String[] sourceStrings = largeText.split("\n\n");

        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < sourceStrings.length; i++) {
            Map<String, String> curValue = new HashMap<>();
            curValue.put("paragraph", sourceStrings[i]);
            curValue.put("number", String.valueOf(sourceStrings[i].length()));
            list.add(curValue);
        }
        return list;
    }
}
