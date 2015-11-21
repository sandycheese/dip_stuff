package com.humanity.vs.cards.cardsvshumanity.ui.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.helpers.NetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.INetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.logic.managers.GameManager;
import com.humanity.vs.cards.cardsvshumanity.ui.fragments.GamesOnlineFragment;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.IGameManagerProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.INetworkGameCommandsSenderProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.INetworkManagerProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.network.AllNetworkDataHandler;
import com.humanity.vs.cards.cardsvshumanity.ui.network.MySalutDataCallback;
import com.humanity.vs.cards.cardsvshumanity.ui.network.NetworkManager;
import com.humanity.vs.cards.cardsvshumanity.utils.FragmentsHelper;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

// todo create empty views for recycle view
// todo add wi-fi enabled check
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, INetworkManagerProvider, IGameManagerProvider, INetworkGameCommandsSenderProvider {

    NetworkManager networkManager;
    AllNetworkDataHandler allNetworkDataHandler;

    GameManager gameManager;
    NetworkGameCommandsSender networkGameCommandsSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initNetwork();
        initFields();
        initFirstFragment();
    }

    private void initNetwork() {
        allNetworkDataHandler = new AllNetworkDataHandler(this);

        networkManager = new NetworkManager(this, allNetworkDataHandler);
        networkManager.initNetworkService();
    }

    private void initFields() {
        gameManager = new GameManager();
        networkGameCommandsSender = new NetworkGameCommandsSender(networkManager);
    }

    private void initFirstFragment() {
        FragmentsHelper.setFragment(this, R.id.rlContainer, new GamesOnlineFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    @Override
    public GameManager getGameManager() {
        return this.gameManager;
    }

    @Override
    public INetworkGameCommandsSender getNetworkCommandsSender() {
        return this.networkGameCommandsSender;
    }
}
