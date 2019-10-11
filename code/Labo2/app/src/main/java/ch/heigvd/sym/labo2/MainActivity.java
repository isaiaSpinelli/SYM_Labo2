package ch.heigvd.sym.labo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // GUI elements
    private Button point1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.point1 = findViewById(R.id.buttonPoint1);

        // Then program action associated to "Ok" button
        point1.setOnClickListener((v) -> {

            Intent intent = new Intent(this, ch.heigvd.sym.labo2.Point1Activity.class);

            this.startActivity(intent);


        });
    }

    @Override
    protected void onResume () {
        super.onResume();

    }
}
