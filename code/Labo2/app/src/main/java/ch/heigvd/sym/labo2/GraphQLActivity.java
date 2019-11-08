package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asynComm.SymComManager;
import ch.heigvd.sym.labo2.GraphQLUtile.Authors;
import ch.heigvd.sym.labo2.GraphQLUtile.Post;

/**
 * GraphQLActivity, permet de faire des requêtes sur un graph QL
 */
public class GraphQLActivity  extends AppCompatActivity {
    private TextView reception = null;
    private TextView envoi = null;
    private Spinner spinner = null;

    private static final String GetAllAuthors =
            "{\"query\": \"{allAuthors{id first_name last_name}}\"}";

    private static final String GetPostByAuthor =
            "{\"query\": \"{author(id: %d){posts{title content}}}\"}";

    //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
    private List auteurList = new ArrayList();
    private String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphql);
        reception = findViewById(R.id.msgToGet);
        spinner = findViewById(R.id.spinner);
        reception.setMovementMethod(new ScrollingMovementMethod());

        GenerateAutorsListe();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "You have selected item : " + auteurList.get(index), Toast.LENGTH_SHORT).show();

                getPostByAutor((Authors) auteurList.get(index));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    private void GenerateAutorsListe() {

        SymComManager mcm = new SymComManager();

        mcm.setCommunicationEventListener(resp -> {
            // Récéption de la réponse
            this.response = (String) resp;
            Log.println(Log.INFO,"GraphQLAuteur",response);
            try {
                JSONArray authorsArray = new JSONObject(response).getJSONObject("data").getJSONArray("allAuthors");

                JSONObject author;

                int size = authorsArray.length();
                for(int i = 0; i < size; i++ ) {
                    author = authorsArray.getJSONObject(i);

                    auteurList.add(new Authors(author.getInt("id"), author.getString("first_name"), author.getString("last_name")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter adapter = new ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    auteurList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Enfin on passe l'adapter au Spinner et c'est tout
            spinner.setAdapter(adapter);

            return true;
        });

        mcm.sendRequest("http://sym.iict.ch/api/graphql",GetAllAuthors,"application/json");
    }

    private List<Post> getPostByAutor(Authors a){

        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
         List<Post> PostList = new ArrayList();

        SymComManager mcm = new SymComManager();

        mcm.setCommunicationEventListener(resp -> {
            // Récéption de la réponse
            this.response = (String) resp;
            Log.println(Log.INFO,"GraphQLAuteur",response);

            try{
                JSONArray postsArray = new JSONObject(response).getJSONObject("data").getJSONObject("author").getJSONArray("posts");

                JSONObject post;
                int size = postsArray.length();
                for(int i = 0; i < size; i++ ) {
                    post = postsArray.getJSONObject(i);
                    PostList.add(new Post(
                            post.getString("title"),
                            post.getString("content"))
                    );
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

            reception.setText( PostList.get(0).toString());

            return true;
        });

        mcm.sendRequest("http://sym.iict.ch/api/graphql",String.format(GetPostByAuthor,a.getId()),"application/json");

        return PostList;
    }
}

