package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;


import org.simpleframework.xml.Attribute;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Order;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.util.ArrayList;

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
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialisation);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
        envoiJSON = findViewById(R.id.buttJSON);
        envoiXML = findViewById(R.id.buttXML);

        reception.setMovementMethod(new ScrollingMovementMethod());

        envoiJSON.setOnClickListener((v) -> {
            SymComManager mcm = new SymComManager() ;
            Person isaia  = new Person("Isaia","Spinel1i","m",new Phone("0984","home"));
            Gson jRequest = new Gson();
            envoi.setText(jRequest.toJson(isaia));


            //On suppose que votre classe d'accès est nommée SymComManagerSymComManager
            mcm.setCommunicationEventListener(resp -> {
                // Récéption de la réponse
                this.response = (String) resp;

                reception.setText(response);

                response = response.substring(0,response.indexOf("info")-2) + "}";
                Person isaiaDeserial = jRequest.fromJson(response,Person.class);
                Log.println(Log.INFO,"Serialisation",isaiaDeserial.toString());
                return true;
            });

            mcm.sendRequest( "http://sym.iict.ch/rest/json",jRequest.toJson(isaia),"application/json");


        });

        envoiXML.setOnClickListener((v) -> {

            List<Person> list = new ArrayList();

            list.add(new Person("spinelli","Isaia","m",new Phone("0786571225","mobile")));
            list.add(new Person("simonet","yoann","m",new Phone("546565465","home")));
            SymComManager mcm = new SymComManager() ;
            Directory d = new Directory(list,null);
            Serializer serializer = new Persister();
            StringWriter sw = new StringWriter();
            try {
                serializer.write(d, sw);
            } catch (Exception e) {
                e.printStackTrace();
                sw.write("error");
            }

            Log.println(Log.INFO,"Serialisation XML",sw.toString());

            String xmlString =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE directory SYSTEM \"http://sym.iict.ch/directory.dtd\">" + sw.toString();


            envoi.setText(xmlString);


            mcm.sendRequest( "http://sym.iict.ch/rest/xml",xmlString,"application/xml");

            mcm.setCommunicationEventListener(resp -> {
                // Récéption de la réponse
                this.response = (String) resp;
                String s = response.substring(response.indexOf("</infos>") + 8, response.length() - 1);
                String s1 = response.substring(response.indexOf("<directory>"), response.indexOf("<infos>")) + s;

                reception.setText(s1);

                Directory des = null;

                try {
                   des =  serializer.read(Directory.class, s1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.println(Log.INFO,"Serialisation",des.toString());
                return true;
            });
        });
    }

    private static class Directory{

        @ElementList(inline = true,required = false)
         List<Person> person = null;

        @Element(required=false)
        String infos = "";

        Directory(List<Person> persons,String infos){
            this.person = persons;
            this.infos = infos;
        }

        Directory(){
            this.person = null;
            this.infos = "";
        }

        @Override
        public String toString(){
            String s ="";
            for (Person p: person){
                s += p.toString() + "\n";
            }
            return s;
        }

    }


    @Root(name = "person")
    @Order(elements={"name", "firstname", "gender", "phone"})
    private static class Person{
        @Element
        private String name;
        @Element
        private String firstname;
        @Element
        private String gender;
        @Element(type = Phone.class)
        private Phone  phone;

        public SerialisationActivity.Phone getPhone() {
            return phone;
        }


        public Person(String firstName, String lastName,String gender,Phone phone){
            this.firstname = firstName;
            this.name = lastName;
            this.gender = gender;
            this.phone = phone;
        }
        public Person(){
            this.firstname = "";
            this.name = "";
            this.gender = "";
            this.phone = null;
        }

        public String toString(){
            return " lastName:"+name+" firstName:" + firstname;
        }
    }

    @Root(name = "phone")
    private static class Phone{
        @Text
        private String phone;
        @Attribute
        private String type;

       public Phone(String phone,String type){
           this.phone = phone;
           this.type = type;
       }
        public Phone(){
            this.phone = "";
            this.type = "";
        }

        public String getPhone() {
            return phone;
        }

        public String getType() {
            return type;
        }



    }
}

