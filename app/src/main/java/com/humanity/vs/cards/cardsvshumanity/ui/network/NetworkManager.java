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

    public void startHost(final SalutCallback clientConnectedCallback, final SalutCallback createHostFailCallback) {
        network.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice device) {
                clientConnectedCallback.call();
                Log.d(App.TAG, "HOST: '" + device.readableName + "' has connected");
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "HOST: created successfully");
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "HOST: failed to create");
                network.stopNetworkService(false);
                createHostFailCallback.call();
            }
        });
    }

    // fixme make infinite loop?
    public void discoverHosts(final SalutCallback doneCallback) {

        network.discoverWithTimeout(new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "CLIENT: host(s) found");
                doneCallback.call();
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "CLIENT: host(s) not found");
                doneCallback.call();
            }
        }, 5000);
    }

    public ArrayList<SalutDevice> getAllHosts() {
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

    public void sendDataToHost(String classOfData, String jsonData) {
        JsonGodLevelData data = new JsonGodLevelData();
        data.classNameOfData = classOfData;
        data.jsonStringData = jsonData;

        network.sendToHost(jsonData, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "Can't send data to host");
            }
        });
    }

    public void sendDataToClients(String classOfData, String jsonData) {
        JsonGodLevelData data = new JsonGodLevelData();
        data.classNameOfData = classOfData;
        data.jsonStringData = jsonData;

        network.sendToAllDevices(jsonData, new SalutCallback() {
            @Override
            public void call() {
                Log.d(App.TAG, "Can't send data to clients");
            }
        });
    }

}
