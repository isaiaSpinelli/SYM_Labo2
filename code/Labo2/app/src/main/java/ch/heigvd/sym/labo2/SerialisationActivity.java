package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SerialisationActivity, permet de faire des requêtes sur des données sérialisable
 */
public class SerialisationActivity  extends AppCompatActivity {
    private TextView reception = null;
    private TextView envoi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialisation);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
    }
}

