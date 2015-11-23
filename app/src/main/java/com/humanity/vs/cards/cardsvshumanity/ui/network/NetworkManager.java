package com.humanity.vs.cards.cardsvshumanity.ui.network;

import android.app.Activity;
import android.util.Log;

import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.ui.entities_json.JsonGodLevelData;
import com.humanity.vs.cards.cardsvshumanity.ui.entities_json.JsonPlayersInLobby;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

/**
 * Created by robot on 17.11.15.
 */
public class NetworkManager {
    private static final long SLEEP_BETWEEN_CONNECT_AND_SENDING_DATA = 1000;

    private final Activity activity;

    private AllNetworkDataHandler allNetworkDataHandler;

    private SalutServiceData serviceData;
    private SalutDataReceiver dataReceiver;
    private MySalutDataCallback dataCallback;
    private Salut network;

    public NetworkManager(Activity activity, AllNetworkDataHandler allNetworkDataHandler) {
        this.activity = activity;
        this.allNetworkDataHandler = allNetworkDataHandler;
    }

    public void initNetworkService() {

        dataCallback = new MySalutDataCallback(allNetworkDataHandler);
        dataReceiver = new SalutDataReceiver(activity, dataCallback);

        int port = activity.getResources().getInteger(R.integer.game_network_service_port);

        serviceData = new SalutServiceData(
                activity.getString(R.string.game_network_service_name),
                port,
                android.os.Build.MODEL);

        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "Device not supported");
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        network.stopNetworkService(false);
        // will it throw a exception?
        network.unregisterClient(false);
    }

    public void startNetworkService(final SalutCallback clientConnectedCallback, final SalutCallback createServiceFailCallback) {
        network.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice device) {

                Log.d(App.TAG, device.readableName + "' has connected");
                Log.d(App.TAG, "sleep a little (ms): " + SLEEP_BETWEEN_CONNECT_AND_SENDING_DATA);
                try {
                    Thread.sleep(SLEEP_BETWEEN_CONNECT_AND_SENDING_DATA);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(App.TAG, "Fail to sleep");
                }
                clientConnectedCallback.call();
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "service created successfully");
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "service: failed to create");
                network.stopNetworkService(false);
                createServiceFailCallback.call();
            }
        });
    }

    // fixme make infinite loop?
    public void discoverHosts(final SalutCallback doneCallback) {

        network.discoverWithTimeout(new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "CLIENT: service(s) found");
                doneCallback.call();
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "CLIENT: service(s) not found");
                doneCallback.call();
            }
        }, 5000);
    }

    public ArrayList<SalutDevice> getAllDiscoveredDevices() {
        return network.foundDevices;
    }

    public ArrayList<SalutDevice> getAllDevices() {
        ArrayList<SalutDevice> devices = new ArrayList<>();

        for (SalutDevice device : network.registeredClients) {
            if (device != null)
                devices.add(device);
        }

        devices.add(network.thisDevice);

        return devices;
    }

    public void registerWithHost(SalutDevice salutDevice, SalutCallback onRegisterSuccess, SalutCallback onRegisterFail) {
        network.registerWithHost(salutDevice, onRegisterSuccess, onRegisterFail);
    }

    public void updateLobbyForClients() {
        Log.d(App.TAG, "HOST: Trying to send a lobby players list");
        JsonGodLevelData jsonData = JsonPlayersInLobby.getJsonGodLevelData(getAllDevices());

        network.sendToAllDevices(jsonData, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "Can't send data.");
            }
        });
    }

    public SalutDevice thisDevice() {
        return network.thisDevice;
    }

    // fixme IT'S IMPOSSIBLE TO CONTROL WHO WILL BECOME THE HOST. CHANGE ARCHITECTURE. will send to everyone at first time
    public void sendDataToAll(String classOfData, String jsonData) {
        Log.d(App.TAG, "wanna send some data: " + classOfData);

        JsonGodLevelData data = new JsonGodLevelData();
        data.classNameOfData = classOfData;
        data.jsonStringData = jsonData;

        SalutCallback failCallback = new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "Can't send data to host");
            }
        };

        if (network.isRunningAsHost)
            network.sendToAllDevices(data, failCallback);
        else
            network.sendToHost(data, failCallback);
    }

    public boolean isDeviceHost() {
        return network.isRunningAsHost;
    }
}
