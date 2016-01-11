package iee3.he_arc.cityresto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;


public class ActRestoProfile extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_resto_profile);

        RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setRating(3.67f);

        TextView lRestoName = new TextView(this);
        lRestoName = (TextView) findViewById(R.id.tvProfileRestoName);
        lRestoName.setText("Macdo");

        TextView lRestoAddress = new TextView(this);
        lRestoAddress = (TextView) findViewById(R.id.tvProfileRestoAddress);
        lRestoAddress.setText("Rue de bli");

        TextView lRestoCity = new TextView(this);
        lRestoCity = (TextView) findViewById(R.id.tvProfileRestoCity);
        lRestoCity.setText("2000 Neuchâtel");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_resto_profile, menu);
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
}
