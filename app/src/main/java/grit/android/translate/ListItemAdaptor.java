package grit.android.translate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdaptor extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> history = new ArrayList<String>();

    private static final String delim = ";";

    public ListItemAdaptor(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public Object getItem(int position) {
        return history.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String word, String translation){
        history.add(word + delim + translation);
    }

    public void addItem(String concatString){
        history.add(concatString);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.listview_item_layout, null);

        TextView wordTextView = (TextView) view.findViewById(R.id.wordTextView);
        TextView translateTextView = (TextView) view.findViewById(R.id.translationTextView);

        String[] word = history.get(position).split(delim);

        wordTextView.setText(word[0]);
        translateTextView.setText(word[1]);

        return view;
    }
}
