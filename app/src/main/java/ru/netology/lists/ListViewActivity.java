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
    private List<Map<String, String>> simpleAdapterContent = new ArrayList<>();
    private SwipeRefreshLayout swipeLayout;
    private BaseAdapter listContentAdapter;
    private ListView listView;

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

        prepareContent(largeText);

        createAdapter();

        initListView();

        initSwipeLayout();
    }

    private void initSwipeLayout() {
        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareContent(sharedPref.getString(KEY_LARGE_TEXT, null));
                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void initListView() {
        listView = findViewById(R.id.list);

        listView.setAdapter(listContentAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                simpleAdapterContent.remove(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    private void createAdapter() {
        listContentAdapter = new SimpleAdapter(this, simpleAdapterContent, R.layout.list_item,
                new String[]{"paragraph", "number"}, new int[]{R.id.paragraph, R.id.number});
    }

    @NonNull
    private void prepareContent(String value) {
        String[] sourceStrings = value.split("\n\n");

        if (!simpleAdapterContent.isEmpty()) {
            simpleAdapterContent.clear();
        }

        for (int i = 0; i < sourceStrings.length; i++) {
            Map<String, String> curValue = new HashMap<>();
            curValue.put("paragraph", sourceStrings[i]);
            curValue.put("number", String.valueOf(sourceStrings[i].length()));
            simpleAdapterContent.add(curValue);
        }

    }
}
