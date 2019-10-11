package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * DiffereeActivity, permet de faire des requêtes de manière différée
 */
public class DiffereeActivity  extends AppCompatActivity {
    private TextView reception = null;
    private TextView envoi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differee);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
    }
}

