package hust.edu.vn.smsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MakeNotification extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_notification2);
        TextView textView = findViewById(R.id.txtView);

        textView.setText("This is message");
    }
}