package ch.heigvd.sym.labo2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Point1Activity  extends AppCompatActivity {
    private TextView reception = null;
    private TextView envoi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point2);
        reception = findViewById(R.id.receptionMessage);
        envoi = findViewById(R.id.envoiMessage);
    }
}

