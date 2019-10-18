package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019$
 *
 * @reference : https://stackoverflow.com/questions/14037455/inetrnet-connection-checking-thread-android
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import asynComm.SymComManager;

/**
 * DiffereeActivity, permet de faire des requêtes de manière différée
 */
public class DiffereeActivity  extends AppCompatActivity {
    private TextView reception = null;
    private EditText envoi = null;
    private Button buttEnvoi = null;
    private List<String> toSendList = new ArrayList<String>();
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differee);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
        buttEnvoi = findViewById(R.id.buttEnvoi);

        // Lors de l'appui du bouton
        buttEnvoi.setOnClickListener((v) -> {
            // recupere le message a envoyer
            String msgToSend = envoi.getText().toString();
            if (msgToSend.length() > 0) {
                toSendList.add(msgToSend);

                // lance le thread s'il n'est pas deja lance
                if (!isRunning) {
                    isRunning = true;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Attend d'avoir une connection internet (check toutes les 5 secondes)
                            while (!isConnectedNetwork()){
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Envoi tous les messages en attente
                            try {
                                SymComManager mcm = new SymComManager();
                                for (String request : toSendList) {
                                    mcm.setCommunicationEventListener(response -> {
                                        reception.setText(response);
                                        Log.println(Log.INFO, "differre", response);
                                        return true;
                                    });

                                    mcm.sendRequest("http://sym.iict.ch/rest/txt", request);
                                    toSendList.remove(request);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            isRunning = false;
                        }
                    }).start();
                } else {
                    Toast.makeText(DiffereeActivity.this, "Message trop court", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }



    // Retourne true si une connection internet est detectee
    private boolean isConnectedNetwork() {
        NetworkInfo nf = null;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            nf = cm.getActiveNetworkInfo();
        }

        return nf != null && nf.isConnected();

    }

}

