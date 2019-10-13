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


    private CommunicationEventListener communicationEventListener = null;


    public void sendRequest(String url, String request) {
       Object back[] = {(Object) url,(Object)request};

        this.execute(back);
    }

    public void setCommunicationEventListener(CommunicationEventListener communicationEventListener) {
        this.communicationEventListener = communicationEventListener;
    }


    private  HttpURLConnection connection(String url)throws IOException{

        HttpURLConnection urlConnection =(HttpURLConnection)new URL(url).openConnection();

            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("X-Network", "LTE"); //network speed
            urlConnection.setRequestProperty("Content-Type", "text/plain");

            return urlConnection;
    }


    @Override
    protected String doInBackground(Object[] objects) {

        HttpURLConnection urlSocket = null;
        byte bufferReponse[] = new byte[200] ;

        String reqest = (String) objects[1];

        try {
            urlSocket = connection((String) objects[0]);

            urlSocket.getOutputStream().write(reqest.getBytes());

            urlSocket.connect();

            urlSocket.getInputStream().read(bufferReponse);
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
