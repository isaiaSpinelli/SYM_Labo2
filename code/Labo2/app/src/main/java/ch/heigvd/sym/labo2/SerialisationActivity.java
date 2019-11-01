package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import asynComm.SymComManager;

/**
 * SerialisationActivity, permet de faire des requêtes sur des données sérialisable
 */
public class SerialisationActivity  extends AppCompatActivity {
    private TextView reception = null;
    private TextView envoi = null;
    private Button envoiJSON = null;
    private Button envoiXML = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialisation);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
        envoiJSON = findViewById(R.id.buttJSON);
        envoiXML = findViewById(R.id.buttXML);

        envoiJSON.setOnClickListener((v) -> {
            SymComManager mcm = new SymComManager() ;
            person isaia  = new person("Isaia","Spinel1i","m",new Phone("0984","home"));
            Gson jRequest = new Gson();
            envoi.setText(jRequest.toJson(isaia));


            //On suppose que votre classe d'accès est nommée SymComManagerSymComManager
            mcm.setCommunicationEventListener(response -> {

                reception.setText(response);

                response = response.substring(0,response.indexOf("info")-2) + "}";
                person isaiaDeserial = jRequest.fromJson(response,person.class);
                Log.println(Log.INFO,"Serialisation",isaiaDeserial.toString());
                return true;
            });

            mcm.sendRequest( "http://sym.iict.ch/rest/json",jRequest.toJson(isaia),"application/json");


        });

        envoiXML.setOnClickListener((v) -> {
            SymComManager mcm = new SymComManager() ;


            person isaia  = new person("Isaia","Spinel1i","m",new Phone("09847745","home"));

            directory d = new directory(isaia);
            Serializer serializer = new Persister();
            StringWriter sw = new StringWriter();
            try {
                serializer.write(d, sw);
            } catch (Exception e) {
                e.printStackTrace();
                sw.write("error");
            }

            Log.println(Log.INFO,"Serialisation XML",sw.toString());

            String xmlString =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE directory SYSTEM \"http://sym.iict.ch/directory.dtd\"><directory/>";


            envoi.setText(xmlString);


            Log.println(Log.INFO,"Serialisation XML",xmlString);

            mcm.sendRequest( "http://sym.iict.ch/rest/xml",xmlString,"application/xml");

            mcm.setCommunicationEventListener(response -> {
                reception.setText(response);
                return true;
            });
        });
    }

    private static class directory{

         person person = null;
        directory(person persons){
            this.person = persons;
        }
    }

    @Default
    @Order(elements={"name", "firstname", "gender", "phone"})
    private static class person{
        @Element
        private String name;
        @Element
        private String firstname;
        @Element
        private String gender;
        @Element
        private Phone  phone;

        public SerialisationActivity.Phone getPhone() {
            return phone;
        }


        public person(String firstName, String lastName,String gender,Phone phone){
            this.firstname = firstName;
            this.name = lastName;
            this.gender = gender;
            this.phone = phone;
        }

        public String toString(){
            return " lastName:"+name+" firstName:" + firstname;
        }
    }

    @Root
    private static class Phone{
        @Element
        private String phone;
        @Attribute
        private String type;

       public Phone(String phone,String type){
           this.phone = phone;
           this.type = type;
       }

        public String getPhone() {
            return phone;
        }

        public String getType() {
            return type;
        }

    }
}

