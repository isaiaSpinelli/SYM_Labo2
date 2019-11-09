package asynComm;


import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public void sendRequest(String url, byte[] request,String dataType, String Encoding, String Network) {
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
    protected Object doInBackground(Object[] objects) {

        HttpURLConnection urlSocket = null;
        byte bufferReponse[] = new byte[65000] ;
        InputStream inputError;
        String resp ="";
        byte[] byesResp =new byte[65000];

        String reqest;
        Boolean DataIsByte = false;
        byte[] bytesData = null;
        if(objects[1] instanceof String){
            reqest = (String) objects[1];
            bytesData = reqest.getBytes();
        }else if(objects[1] instanceof byte[]){
            DataIsByte = true;
            bytesData = (byte[]) objects[1];
        }


        try {
            urlSocket = connection((String) objects[0],(String) objects[2], (String) objects[3], (String) objects[4]);
            urlSocket.getOutputStream().write(bytesData);
            urlSocket.connect();

            int errorCode = urlSocket.getResponseCode();
            /* Si il y a une erreur */
            if(errorCode != 200){
                inputError = urlSocket.getErrorStream();
                inputError.read(bufferReponse);
                resp = new String(bufferReponse);


            }else{
                if(!DataIsByte) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlSocket.getInputStream()));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = r.readLine()) != null; ) {
                        total.append(line).append('\n');
                    }
                    resp = total.toString();
                }
                else{
                    urlSocket.getInputStream().read(byesResp);
                }

            }


            urlSocket.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(urlSocket != null)
                urlSocket.disconnect();
        }
        if(DataIsByte){
            return byesResp;
        }
        return resp ;
    }

    @Override
    protected void onPostExecute(Object s) {
        super.onPostExecute(s);

        try {
            communicationEventListener.handleServerResponse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
