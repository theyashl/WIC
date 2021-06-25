package com.example.appv1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.appv1.databinding.ActivityMainBinding;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.TrendingShow;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.services.Shows;

import java.util.List;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //mycode
        TextView tv = (TextView)findViewById(R.id.text_home);
        //tv.setText("Welcome to Tutlane");
        /*List<TrendingShow> shows = trend();
        tv.setText(shows.get(0).show.title);*/

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, trend());

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    private List<String> trend(){
        TraktV2 trakt = new TraktV2("api_key");
        Shows traktShows = trakt.shows();
        List<TrendingShow> shows = null;
        List<String> ret = null;
        try {
            // Get trending shows
            Response<List<TrendingShow>> response = traktShows.trending(1, null, Extended.FULL).execute();
            if (response.isSuccessful()) {
                shows = response.body();
                for (TrendingShow trending : shows) {
                    System.out.println("Title: " + trending.show.title);
                }
            } else {
                if (response.code() == 401) {
                    // authorization required, supply a valid OAuth access token
                } else {
                    // the request failed for some other reason
                }
            }
        } catch (Exception e) {
            // see execute() javadoc
        }
        for (int i=0; i<shows.size(); ++i) ret.set(i, shows.get(i).show.title);
        //return shows;
        return ret;
    }

}