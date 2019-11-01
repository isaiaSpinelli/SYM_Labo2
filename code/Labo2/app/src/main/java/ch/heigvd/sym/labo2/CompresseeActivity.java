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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;

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
                    String compressedData = compresse(msgToSend);
                    System.out.println(compressedData);

                    SymComManager mcm = new SymComManager();

                    mcm.setCommunicationEventListener(response -> {
                        // Récéption de la réponse
                        reception.setText(response);

                        try {
                            // enlever l'entete
                            String lol = decompresse(response.substring(0, response.indexOf("\n")  ) );
                            System.out.println(lol);
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
    * Reference : http://www.javased.com/?api=java.util.zip.DeflaterOutputStream (Exemple 2 )
    * https://stackoverflow.com/questions/33020765/java-decompress-a-string-compressed-with-zlib-deflate
     */
    public String compresse(final String data) throws IOException {

/*
        final int BUF_SIZE = 1024;

        InputStream dataInput = new ByteArrayInputStream(data.getBytes("UTF-8"));
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
        return bos;*/

/*

        // Encode a String into bytes
        String inputString = data;
        byte[] input = inputString.getBytes("UTF-8");

        // Compress the bytes
        byte[] output = new byte[100];
        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        int compressedDataLength = compresser.deflate(output);

        return output;*/
// will compress "Hello World!"
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos);
        dos.write(data.getBytes());
        dos.flush();
        dos.close();

        // at this moment baos.toByteArray() holds the compressed data of "Hello World!"

        String strreturn = String.valueOf(baos.toByteArray());
        byte[] devrait = baos.toByteArray();

        byte[] reel = strreturn.getBytes();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        InflaterInputStream iis = new InflaterInputStream(bais);

        String result = "";
        byte[] buf = new byte[1024];
        int rlen = -1;
        while ((rlen = iis.read(buf)) != -1) {
            result += new String(Arrays.copyOf(buf, rlen));
        }

        // now result will contain "Hello World!"

        System.out.println("Decompress result: " + result);

        //return result;


        return String.valueOf(baos.toByteArray());



    }

    /*
     * But : Permet de decompresser une chaine de caractère
     *
     * Reference : http://www.javased.com/?api=java.util.zip.DeflaterOutputStream (Exemple 2 )
     */
    public String decompresse(final String data) throws IOException {
/*
        Inflater inflater = new Inflater();
        inflater.setInput(data.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length());
        InflaterOutputStream decompressed = new InflaterOutputStream(outputStream,inflater);

        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = 0;
            try {
                count = inflater.inflate(buffer);
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        inflater.end();

        byte[] output = outputStream.toByteArray();

        return output.toString();*/

/*
        byte[] output = data.getBytes();

        // Decompress the bytes
        Inflater decompresser = new Inflater();
        decompresser.setInput(output, 0, data.length());
        byte[] result = new byte[100];
        int resultLength = 0;
        try {
            resultLength = decompresser.inflate(result);
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        decompresser.end();

        // Decode the bytes into a String
        String outputString = new String(result, 0, resultLength, "UTF-8");

        return outputString;*/



        // will decompress compressed "Hello World!"
        //ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
        InflaterInputStream iis = new InflaterInputStream(bais);

        String result = "";
        byte[] buf = new byte[1024];
        int rlen = -1;
        while ((rlen = iis.read(buf)) != -1) {
            result += new String(Arrays.copyOf(buf, rlen));
        }

        // now result will contain "Hello World!"

        System.out.println("Decompress result: " + result);

        return result;
    }
}

