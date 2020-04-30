package grit.android.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int INPUT_CODE = 1000;

    TextView parrotTextView;
    TextView translateTextView;
    Button recordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("log lifecycle", getLocalClassName() + ".onCreate()");

        parrotTextView = (TextView) findViewById(R.id.parrotTextView);
        translateTextView = (TextView) findViewById(R.id.translationTextView);
        recordButton = (Button) findViewById(R.id.recordButton);

        recordButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listen();
            }
        });

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void listen() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you want to translate?");

        try {
            startActivityForResult(intent, INPUT_CODE);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("log lifecycle", getLocalClassName() + ".onActivityResult()");

        if (requestCode == INPUT_CODE && resultCode == RESULT_OK && data != null){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            parrotTextView.setText(result.get(0));
            //translateTextView.setText(result.get(0));
            translate(result.get(0));
        }
    }

    private void translate(final String word) {
        Context c = this;
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = "https://api.mymemory.translated.net/get?q=" + word + "&langpair=en|sv";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String translation = parseJSON(response);
                    translateTextView.setText(translation);
                    saveHistory(word, translation);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //translateTextView.setText("Connection error");
                }
            });

        queue.add(stringRequest);
    }

    private void saveHistory(String word, String translation) {
        SharedPreferences sharedPreferences =
                getSharedPreferences("history", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        HashSet<String> set = (HashSet<String>) sharedPreferences.
                getStringSet("history", new HashSet<String>());

        set.add(word + ListItemAdaptor.delim + translation);

        editor.putStringSet("history", set);
        editor.apply();
        editor.commit();
    }

    private String parseJSON(String response) {
        String preText = "translatedText\":\"";
        int indexOfTranslate = response.indexOf(preText);
        String translation = response.subSequence(indexOfTranslate + preText.length(),
                response.indexOf("\"", indexOfTranslate + preText.length())).toString();

        return translation;
    }


}

