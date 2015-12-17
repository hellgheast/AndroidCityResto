package iee3.he_arc.googlemapfragmenttest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    public static FragmentManager fragmentManager;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();

        //Fragment fr = new LocationFragment();
        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        //fragmentManager = getFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.location_map, fr);
        //fragmentTransaction.commit();
    }

    public void addListenerOnButton() {


        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fragmentManager = getFragmentManager();

                Toast.makeText(MainActivity.this, "Affichage...",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, AffichFragmentMap.class);
                startActivity(intent);
            }

        });

    }
}