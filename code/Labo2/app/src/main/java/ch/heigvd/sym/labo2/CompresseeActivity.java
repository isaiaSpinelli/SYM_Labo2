package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 01.11.2019
 */

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import asynComm.SymComManager;

/**
 * CompresseeActivity, permet de faire des requêtes de manière compressee
 */
public class CompresseeActivity  extends AppCompatActivity {
    private TextView reception = null;
    private EditText envoi = null;
    private Button buttEnvoi = null;
    private String response = null;
    private byte[] respcomp = null;

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

                // compresse les données à envoyer
                try {
                    byte[] compressedData = compresse(msgToSend);
                    String compressedDatastr = compressedData.toString();

                    SymComManager mcm = new SymComManager();

                    mcm.setCommunicationEventListener(resp -> {
                        // Récéption de la réponse
                        this.response = new String((byte[]) resp);
                        respcomp = new byte[response.indexOf("\n")];

                        try {
                            // enlever l'entete
                            String reponseNet = response.substring(0, response.indexOf("\n") );
                            reception.setText(reponseNet);
                            System.arraycopy(resp,0,respcomp,0,reponseNet.length());

                            // Si c'est correte, decomprese les datas
                            String decompresseData = "";
                            decompresseData = decompresse( respcomp );
                            // affiche les data decompressées
                            Toast.makeText(CompresseeActivity.this, "Resultat de la decompression : " + decompresseData, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    });

                    // Envoie la requête des données compressée
                    mcm.sendRequest(SERVEUR, compressedData, "text/plain","gzip, deflate","CSD");

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(CompresseeActivity.this, "Message vide", Toast.LENGTH_SHORT).show();
            }

        });


    }

    /*
    * But : Permet de compresser une chaine de caractère en gzip avec un DeflaterOutputStream
    *
    * Reference : https://stackoverflow.com/questions/33020765/java-decompress-a-string-compressed-with-zlib-deflate
    *
     */
    public byte[] compresse(final String data) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Deflater deflate = new Deflater(Deflater.DEFAULT_COMPRESSION,true);
        DeflaterOutputStream dos = new DeflaterOutputStream(baos,deflate);
        dos.write(data.getBytes());
        dos.flush();
        dos.close();

        return baos.toByteArray() ; //String.valueOf(baos.toByteArray());
    }

    /*
     * But : Permet de decompresser un tableau de byte compressé
     *
     * Reference : https://stackoverflow.com/questions/33020765/java-decompress-a-string-compressed-with-zlib-deflate
     */
    public String decompresse(final byte[] data) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Inflater inflater = new Inflater(true);
        InflaterInputStream iis = new InflaterInputStream(bais, inflater);

        String result = "";
        byte[] buf = new byte[30000];
        int rlen = -1;
        while ((rlen = iis.read(buf)) != -1) {
            result += new String(Arrays.copyOf(buf, rlen));
        }

        return result;
    }
}

