package iee3.he_arc.cityresto;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.Browser;
import android.provider.Settings;
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

import iee3.he_arc.cityresto.InternDB.ClassInternUser;

public class ActConnect extends AppCompatActivity {

    private Button btnOKConnect;
    private CheckBox cbRememberMe;
    private EditText lEditTextUserName;
    private EditText lEditTextPassword;
    private String lUserName = "name";
    private String lPassword = "password";
    private String lRegisteredName = "name";
    private String lRegisteredPassword = "password";
    private ProgressDialog progDialog;
    private LocationManager lm;
    final Context context = this;
    private boolean lRememberMe = false;
    private ClassInternUser mClassInternUser;
    private ClassPermanentDataHelper mClassPermanentDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_connect);

        lm = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        btnOKConnect = (Button) findViewById(R.id.btnOKConnect);

        lEditTextUserName = (EditText) findViewById(R.id.etUserName);
        lEditTextPassword = (EditText) findViewById(R.id.etPassword);
        cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe);

        mClassPermanentDataHelper = new ClassPermanentDataHelper(this);

        // Check if an user has choose to be remembered
        mClassInternUser = mClassPermanentDataHelper.readRememberUser();

        if(null != mClassInternUser){

            // User has choose to be remembered !
            // Fill EditBoxes with name and password of the user
            lEditTextUserName.setText(mClassInternUser.getUsername());
            lEditTextPassword.setText(mClassInternUser.getPassword());

            // Keep the checkbox checked
            cbRememberMe.setChecked(true);

        }else{

            // Uncheck Checkbox
            cbRememberMe.setChecked(false);
        }

        //mClassPermanentDataHelper = new ClassPermanentDataHelper(this);

        // Clear types checkboxes
        ClassMainStorageManager.initTypesNamesArray();
        ClassMainStorageManager.initCheckBoxes(this);
        // Connection
        btnOKConnect.setOnClickListener(new View.OnClickListener() {

            // TODO Sauvegarder l'état de la checkbox "Remember Me" en base de donnée permanente !!
            @Override
            public void onClick(View arg0) {

                lUserName = ((EditText) findViewById(R.id.etUserName)).getText().toString();
                lPassword = ((EditText) findViewById(R.id.etPassword)).getText().toString();

                progDialog = new ProgressDialog(ActConnect.this);

                mClassInternUser = mClassPermanentDataHelper.readUser(lUserName);

                // Check if name exists
                if(null == mClassInternUser){
                    Toast.makeText(getBaseContext(),
                            (R.string.LoginNameError), Toast.LENGTH_LONG).show();
                }

                // Check User's password
                else if(lPassword.equals(mClassInternUser.getPassword()))
                {

                    // Login accepted !

                    if(cbRememberMe.isChecked()){
                        lRememberMe = true;
                        mClassPermanentDataHelper.UnrememberAllUser();
                        mClassPermanentDataHelper.RememberUser(mClassInternUser);
                    }
                    else{
                        lRememberMe = false;
                        mClassPermanentDataHelper.UnrememberUser(mClassInternUser);
                    }

                    // check if GPS enabled
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

                                ClassMainStorageManager.lPositionTab = 0; // Prepare to display map
                                ClassMainStorageManager.lDisplayList = false; // Prepare to hide list
                                // Go to next activity
                                Intent intent = new Intent(ActConnect.this, ActMainResto.class);
                                startActivity(intent);
                            }
                        }.start();

                    } else {
                        // Ask user to enable GPS/network in settings
                        showSettingsAlert();
                    }


                } else{
                    Toast.makeText(getBaseContext()
                        ,(R.string.LoginPasswordError)
                            , Toast.LENGTH_LONG).show();
                }

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


    public void showSettingsAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS activation");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to activate it ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
