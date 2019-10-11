package ch.heigvd.sym.labo2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class GraphQLActivity  extends AppCompatActivity {
    private TextView reception = null;
    private TextView envoi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphql);
        reception = findViewById(R.id.msgToGet);
        envoi = findViewById(R.id.msgToSend);
    }
}

