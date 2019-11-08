package asynComm;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
/* Classe permettant d'envoyer des requêtes asychrone */
public class SymComManager extends AsyncTask {

    private static final String TAG = SymComManager.class.getSimpleName();
    private static final String DEFAULT_DATA_TYPE = "text/plain";
    private static final String DEFAULT_ENCODING = "";
    private static final String DEFAULT_NETWORK = "LTE";

    private CommunicationEventListener communicationEventListener = null;
    /* Envoie une requête à un serveur*/
    public void sendRequest(String url, String request) {
       sendRequest(url,request,DEFAULT_DATA_TYPE, DEFAULT_ENCODING, DEFAULT_NETWORK );
    }
    /* Envoie une requête à un serveur avec un type de donnée autre que text/plain */
    public void sendRequest(String url, String request,String dataType) {
       Object back[] = {(Object) url,(Object)request,(Object)dataType, DEFAULT_ENCODING, DEFAULT_NETWORK};

        this.execute(back);
    }

    /*
    * Surcharge pour les requêtes compressées
     */
    public void sendRequest(String url, String request,String dataType, String Encoding, String Network) {
        Object back[] = {(Object) url,(Object)request, (Object)dataType, (Object)Encoding, (Object)Network};

        this.execute(back);
    }

    public void setCommunicationEventListener(CommunicationEventListener communicationEventListener) {
        this.communicationEventListener = communicationEventListener;
    }

    /* Permet d'obtenir une connection à un serveur avec des en-têtes définies */
    private  HttpURLConnection connection(String url,String dataType, String Encoding, String Network)throws IOException{

        HttpURLConnection urlConnection =(HttpURLConnection)new URL(url).openConnection();

            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("X-Network", Network); //network speed
            urlConnection.setRequestProperty("Content-Type", dataType);
            if ( !Encoding.equals("") ) {
                urlConnection.setRequestProperty("X-Content-Encoding", Encoding);
            }


            return urlConnection;
    }


    @Override
    /* Fonction apeller lors de l'apelle de .execute*/
    protected String doInBackground(Object[] objects) {

        HttpURLConnection urlSocket = null;
        byte bufferReponse[] = new byte[65000] ;
        InputStream inputError;
        String resp ="";

        String reqest = (String) objects[1];

        try {
            urlSocket = connection((String) objects[0],(String) objects[2], (String) objects[3], (String) objects[4]);
            urlSocket.getOutputStream().write(reqest.getBytes());
            urlSocket.connect();

            int errorCode = urlSocket.getResponseCode();
            /* Si il y a une erreur*/
            if(errorCode != 200){
                inputError = urlSocket.getErrorStream();
                inputError.read(bufferReponse);
                resp = new String(bufferReponse);


            }else{

                BufferedReader r = new BufferedReader(new InputStreamReader(urlSocket.getInputStream()));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                resp = total.toString();

            }


            urlSocket.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(urlSocket != null)
                urlSocket.disconnect();
        }
        return resp ;
    }

    @Override
    protected void onPostExecute(Object s) {
        super.onPostExecute(s);

        try {
            communicationEventListener.handleServerResponse( (String) s);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
