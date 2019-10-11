package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



/**
 * AsynchroneActivity, permet de faire des requêtes de manière asynchrone
 */
public class AsynchroneActivity  extends AppCompatActivity {
    private TextView reception = null;
    private EditText envoi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchrone);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
    }
}

