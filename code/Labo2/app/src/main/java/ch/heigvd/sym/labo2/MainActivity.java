package ch.heigvd.sym.labo2;
/**
 * @Authors : Simonet Yoann et Spinelli Isaïa
 * @Date    : 10.11.2019
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * MainActivity, classe principale qui permet d'appeler toutes les autres activitées
 */
public class MainActivity extends AppCompatActivity {

    // GUI elements
    private Button ButtAsynchrone = null;
    private Button ButtDifferee = null;
    private Button ButtSerialisation = null;
    private Button ButtCompressee = null;
    private Button ButtGraphQL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Appuie sur le bouton pour la transmission asynchrone
        this.ButtAsynchrone = findViewById(R.id.buttAsynchrone);
        ButtAsynchrone.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ch.heigvd.sym.labo2.AsynchroneActivity.class);
            this.startActivity(intent);
        });

        // Appuie sur le bouton pour la transmission Differee
        this.ButtDifferee = findViewById(R.id.buttDifferee);
        ButtDifferee.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ch.heigvd.sym.labo2.DiffereeActivity.class);
            this.startActivity(intent);
        });

        // Appuie sur le bouton pour la transmission Serialisation
        this.ButtSerialisation = findViewById(R.id.buttSeri);
        ButtSerialisation.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ch.heigvd.sym.labo2.SerialisationActivity.class);
            this.startActivity(intent);
        });

        // Appuie sur le bouton pour la transmission compressee
        this.ButtCompressee = findViewById(R.id.buttComp);
        ButtCompressee.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ch.heigvd.sym.labo2.CompresseeActivity.class);
            this.startActivity(intent);
        });

        // Appuie sur le bouton pour la transmission graph QL
        this.ButtGraphQL = findViewById(R.id.buttGraphQL);
        ButtGraphQL.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ch.heigvd.sym.labo2.GraphQLActivity.class);
            this.startActivity(intent);
        });
    }

    @Override
    protected void onResume () {
        super.onResume();

    }
}
