package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private String name;
    private final TextView abilitatiTextView;
    private final int port;
    private final TextView tipuriTextView;
    private final ImageView pokemonImageView;
    private Socket socket;

    public ClientThread(String name, int port, TextView abilitatiTextView, TextView tipuriTextView, ImageView pokemonImageView) {
        this.port = port;
        this.name = name;
        this.tipuriTextView = tipuriTextView;
        this.pokemonImageView = pokemonImageView;
        this.abilitatiTextView = abilitatiTextView;
    }

    @Override
    public void run() {
        try {
            // tries to establish a socket connection to the server
            socket = new Socket("localhost", port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            // sends the city and information type to the server
            printWriter.println(name);
            printWriter.flush();
            String pokemonInformation;

            // reads the weather information from the server
            while ((pokemonInformation = bufferedReader.readLine()) != null) {
                final String finalizedPokemonInformation = pokemonInformation;

                // updates the UI with the weather information. This is done using postt() method to ensure it is executed on UI thread
                abilitatiTextView.post(() -> abilitatiTextView.setText(finalizedPokemonInformation));
            }
        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
