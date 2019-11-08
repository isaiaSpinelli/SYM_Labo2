package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import asynComm.SymComManager;



/**
 * AsynchroneActivity, permet de faire des requêtes de manière asynchrone
 */
public class AsynchroneActivity  extends AppCompatActivity {
    private TextView reception = null;
    private EditText envoi = null;
    private Button buttEnvoi = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchrone);
        buttEnvoi = findViewById(R.id.buttonEnvoi);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);


        buttEnvoi.setOnClickListener((v) -> {

            SymComManager mcm = new SymComManager() ;
            mcm.setCommunicationEventListener(response -> {
                reception.setText(response);
                return true;
            });

            mcm.sendRequest("http://sym.iict.ch/rest/txt",envoi.getText().toString());
        });
    }


}

