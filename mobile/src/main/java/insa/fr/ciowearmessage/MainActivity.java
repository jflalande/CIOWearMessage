package insa.fr.ciowearmessage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity implements MessageClient.OnMessageReceivedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Wearable.getMessageClient(this).addListener(this);
        }


    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        Log.i("CIO", "Received message: ");
        Log.i("CIO", "  - Path: " + messageEvent.getPath());
        String message = new String(messageEvent.getData());
        Log.i("CIO", "  - Content: " +  message);

        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(tv.getText() + "\n" + message);
    }


}
