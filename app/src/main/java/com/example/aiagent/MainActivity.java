package com.example.aiagent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    private EditText commandInput;
    private Button sendButton;
    private WebSocket webSocket;
    private final String SERVER_URL = "ws://10.0.2.2:9002"; // Use 10.0.2.2 for emulator; for real device, use your phone's IP (e.g., 192.168.1.x)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, PythonServerService.class));

        commandInput = findViewById(R.id.command_input);
        sendButton = findViewById(R.id.send_button);

        commandInput = findViewById(R.id.command_input);
        sendButton = findViewById(R.id.send_button);

        connectWebSocket();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString().trim();
                if (!command.isEmpty() && webSocket != null) {
                    webSocket.send(command);
                    commandInput.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Not connected or empty command", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_URL).build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                MainActivity.this.webSocket = webSocket;
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Connected to agent", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Response: " + text, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "App closed");
        }
    }
}
