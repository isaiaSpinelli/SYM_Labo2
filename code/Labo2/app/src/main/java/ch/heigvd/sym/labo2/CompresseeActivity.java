package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 01.11.2019
 */

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import asynComm.SymComManager;

/**
 * CompresseeActivity, permet de faire des requêtes de manière compressee
 */
public class CompresseeActivity  extends AppCompatActivity {
    private TextView reception = null;
    private EditText envoi = null;
    private Button buttEnvoi = null;

    private final String SERVEUR = "http://sym.iict.ch/rest/txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compressee);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
        buttEnvoi = findViewById(R.id.buttEnvoi);

        // Lors de l'appui du bouton
        buttEnvoi.setOnClickListener((v) -> {
            // recupere le message a envoyer
            String msgToSend = envoi.getText().toString();

            if (msgToSend.length() > 0) {


                System.out.println(msgToSend);

                // compresse les données à envoyer
                try {
                    String compressedData = compress(msgToSend).toString();
                    System.out.println(compressedData);

                    SymComManager mcm = new SymComManager();

                    mcm.setCommunicationEventListener(response -> {
                        // Récéption de la réponse
                        reception.setText(response);

                        Log.println(Log.INFO, "compression", response);
                        return true;
                    });

                    // Envoie la requête des données compressée
                    mcm.sendRequest(SERVEUR, compressedData, "text/plain","deflate","CSD");


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(CompresseeActivity.this, "Message vide", Toast.LENGTH_SHORT).show();
            }

        });


    }

    /*
    * But : Permet de compresser une chaine de caractère
    *
    * Reference : http://www.javased.com/?api=java.util.zip.DeflaterOutputStream (Exemple 2 )
     */
    public ByteArrayOutputStream compress(final String data) throws IOException {

        final int BUF_SIZE = 1024;

        InputStream dataInput = new ByteArrayInputStream(data.getBytes());
        BufferedInputStream in=new BufferedInputStream(dataInput);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        DeflaterOutputStream out=new DeflaterOutputStream(bos,new Deflater(),BUF_SIZE);

        byte[] buf=new byte[BUF_SIZE];
        for (int count=in.read(buf); count != -1; count=in.read(buf)) {
            out.write(buf,0,count);
        }
        in.close();
        out.flush();
        out.close();
        return bos;
    }
}

