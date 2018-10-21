package kz.tilsimsozder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

//import com.daimajia.androidanimations.library.Techniques;
//import com.daimajia.androidanimations.library.YoYo;

import kz.tilsimsozder.style.CustomListAdapter;

public class MainActivity extends Activity {
    ListView listView;
    String[] p_name, p_value;
    SlidingDrawer slidingDrawer;
    TextView Threme, mainText;
    int counter_text = 15;
    public static String language = "ru";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        params();
        clikToList();
    }

    private void params(){
        slidingDrawer = (SlidingDrawer)findViewById(R.id.slidingDrawer);
        listView = (ListView) findViewById(R.id.listView);
        myLIst();
        Threme = (TextView)findViewById(R.id.textView2);
        mainText = (TextView)findViewById(R.id.textView3);
    }

    private void myLIst(){
        if(language.equals("ru")){
            p_name = getResources().getStringArray(R.array.prayer_name_ru);
            p_value = getResources().getStringArray(R.array.prayer_value_ru);
        }else {
            p_name = getResources().getStringArray(R.array.prayer_name_kz);
            p_value = getResources().getStringArray(R.array.prayer_value_kz);
        }

        CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, R.layout.custom_list, p_name);
        listView.setAdapter(adapter);
    }

    private void clikToList(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Threme.setText(p_name[position].toUpperCase()+"");
                mainText.setText(p_value[position]+"");
                slidingDrawer.animateClose();
            }
        });
    }

    public void zoomText(View view){
        if(counter_text<=30){
            counter_text++;
            mainText.setTextSize(counter_text);
            //YoYo.with(Techniques.Pulse).duration(1000).playOn(findViewById(view.getId()));
        }else {
            counter_text = 15;
        }
    }

    public void changeLanguage(View view){
        if(language.equals("ru")){
            language = "kk";
        }else {
            language = "ru";
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void share(View view){
        TextView headerTextView = findViewById(R.id.textView2);
        TextView contentTextView = findViewById(R.id.textView3);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, headerTextView.getText() + "\n"+ contentTextView.getText());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}