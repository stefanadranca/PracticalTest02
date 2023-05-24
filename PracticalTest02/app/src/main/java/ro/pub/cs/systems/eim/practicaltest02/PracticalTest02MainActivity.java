package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText pokemonNameEditText = null;
    private ImageView pokemonImageView = null;
    private TextView abilitatiTextView = null;
    private TextView tipuriTextView = null;
    private ServerThread serverThread = null;
    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            serverThread = new ServerThread(5050);
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();

            String clientAddress = "localhost";
            int clientPort = 5050;

            String name = pokemonNameEditText.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            abilitatiTextView.setText(Constants.EMPTY_STRING);
            abilitatiTextView.setText(Constants.EMPTY_STRING);

            ClientThread clientThread = new ClientThread(clientAddress, clientPort, abilitatiTextView, tipuriTextView, pokemonImageView);
            clientThread.start();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        pokemonNameEditText = findViewById(R.id.pokemon_name);
        pokemonImageView = findViewById(R.id.pokemon_image);
        abilitatiTextView = findViewById(R.id.abilitati);
        tipuriTextView = findViewById(R.id.tipuri);
    }
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}