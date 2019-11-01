package asynComm;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SymComManager extends AsyncTask {

    private static final String TAG = SymComManager.class.getSimpleName();
    private static final String DEFAULT_DATA_TYPE = "text/plain";
    private static final String DEFAULT_ENCODING = "";
    private static final String DEFAULT_NETWORK = "LTE";

    private CommunicationEventListener communicationEventListener = null;

    public void sendRequest(String url, String request) {
       sendRequest(url,request,DEFAULT_DATA_TYPE, DEFAULT_ENCODING, DEFAULT_NETWORK );
    }

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
    protected String doInBackground(Object[] objects) {

        HttpURLConnection urlSocket = null;
        byte bufferReponse[] = new byte[200] ;
        InputStream inputError;

        String reqest = (String) objects[1];

        try {
            urlSocket = connection((String) objects[0],(String) objects[2], (String) objects[3], (String) objects[4]);
            urlSocket.getOutputStream().write(reqest.getBytes());
            urlSocket.connect();

            int errorCode = urlSocket.getResponseCode();

            if(errorCode != 200){
                inputError = urlSocket.getErrorStream();
                inputError.read(bufferReponse);
            }else{
                urlSocket.getInputStream().read(bufferReponse);
            }


            urlSocket.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(urlSocket != null)
                urlSocket.disconnect();
        }
        return new String(bufferReponse);
    }

    @Override
    protected void onPostExecute(Object s) {
        super.onPostExecute(s);
        communicationEventListener.handleServerResponse( (String) s);
    }

}
