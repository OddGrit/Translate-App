package grit.android.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashSet;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Log.d("log lifecycle", getLocalClassName() + ".onCreate()");

        Button returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("history2", MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.
                getStringSet("historySet", new HashSet<String>());
        ListView historyListView = (ListView) findViewById(R.id.historyListView);
        ListItemAdaptor listItemAdaptor = new ListItemAdaptor(this);
        listItemAdaptor.setItems(set);
        historyListView.setAdapter(listItemAdaptor);
    }
}
