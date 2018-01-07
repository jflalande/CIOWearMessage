package insa.fr.ciowearmessage;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MainActivity extends WearableActivity {

    @Override
    protected void onPause() {
        super.onPause();
    }

    private TextView mTextView;
    private CapabilityClient capabilityClient;
    private Set<Node> nodes = new HashSet<>();
    private int counter = 0;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        capabilityClient = Wearable.getCapabilityClient(this);

    }

    public void clickSearchNodes(View view) {
        Log.i("CIO", "Searching nodes...");

        Task<Map<String, CapabilityInfo>> capabilitiesTask =
                capabilityClient.getAllCapabilities(CapabilityClient.FILTER_REACHABLE);

        capabilitiesTask.addOnSuccessListener(
                new OnSuccessListener<Map<String, CapabilityInfo>>() {
                    @Override
                    public void onSuccess(Map<String, CapabilityInfo> capabilityInfoMap) {
                        nodes.clear();
                        if (capabilityInfoMap.isEmpty()) {
                            Log.i("CIO", "No capability advertised :/"); return;
                        }

                        for (String capabilityName : capabilityInfoMap.keySet()) {
                            CapabilityInfo capabilityInfo = capabilityInfoMap.get(capabilityName);
                            Log.i("CIO", "Capability found: " + capabilityInfo.getName());
                            if (capabilityInfo != null) {
                                nodes.addAll(capabilityInfo.getNodes());
                            }
                        }
                        Log.i("CIO", "Nodes found: " + nodes.toString());
                        TextView found = (TextView)findViewById(R.id.foundNodes);
                        StringBuilder printNodes = new StringBuilder();
                        for (Node node : nodes)
                            printNodes.append(node.getDisplayName()).append(" ");
                        found.setText("Nodes: " + printNodes);
                    }
                });
    }

    public void clickSendMessage(View view) {
        Log.i("CIO", "Sending a message...");
        MessageClient clientMessage = Wearable.getMessageClient(this);
        for (Node node : nodes) {
            Log.i("CIO", "  - to " + node.getId());
            String message = "Hello " + counter;
            Task<Integer> sendMessageTask =
                    clientMessage.sendMessage(node.getId(),"CIO", message.getBytes());
            counter++;

        }
    }
}
