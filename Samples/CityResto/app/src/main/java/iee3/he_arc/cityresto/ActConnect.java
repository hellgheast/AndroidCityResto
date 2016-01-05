package iee3.he_arc.cityresto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.input.CharSequenceReader;

public class ActConnect extends AppCompatActivity {

    private Button btnOKConnect;
    private CheckBox cbRememberMe;
    String lUserName = "name";
    String lPassword = "password";
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_connect);

        //startService(new Intent(ActConnect.this, ServiceGoogleHelper.class));
        //ClassMainStorageManager.gps = new ServiceGoogleHelper();
        //ClassMainStorageManager.gps.getLastLocationLatLng();

        btnOKConnect = (Button) findViewById(R.id.btnOKConnect);

        // Connection
        btnOKConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                lUserName = ((EditText) findViewById(R.id.etUserName)).getText().toString();
                lPassword = ((EditText) findViewById(R.id.etPassword)).getText().toString();

                progDialog = new ProgressDialog(ActConnect.this);

                // Use GPS to find location
                //ClassMainStorageManager.gps = new ServiceGPSTracker(ActConnect.this);




                // check if GPS enabled
                if(ClassMainStorageManager.gps.getLastLocationLatLng().latitude != 0 &&
                        ClassMainStorageManager.gps.getLastLocationLatLng().longitude != 0){

                    new CountDownTimer(3000, 3000) {

                        public void onTick(long millisUntilFinished) {

                            progDialog = ProgressDialog.show(ActConnect.this,
                                    "Localisation",
                                    "Please wait during localisation...", true);

                        }

                        public void onFinish() {

                            if (progDialog != null) {
                                progDialog.dismiss();
                                progDialog = null;
                            }

                            // Go to next activity
                            Intent intent = new Intent(ActConnect.this, ActMainResto.class);
                            startActivity(intent);
                        }
                    }.start();

                }else{
                    // Ask user to enable GPS/network in settings
                    ClassMainStorageManager.gps.showSettingsAlert();


                }


                /*


                // check if GPS enabled
                if(ClassMainStorageManager.gps.canGetLocation()){

                    new CountDownTimer(4000, 4000) {

                        public void onTick(long millisUntilFinished) {

                            progDialog = ProgressDialog.show(ActConnect.this,
                                    "Localisation",
                                    "Please wait during localisation...", true);

                        }

                        public void onFinish() {

                            if (progDialog != null) {
                                progDialog.dismiss();
                                progDialog = null;
                            }

                            // Go to next activity
                            Intent intent = new Intent(ActConnect.this, ActMainResto.class);
                            startActivity(intent);
                        }
                    }.start();

                }else{
                    // Ask user to enable GPS/network in settings
                    ClassMainStorageManager.gps.showSettingsAlert();


                }
*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_connect, menu);
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
