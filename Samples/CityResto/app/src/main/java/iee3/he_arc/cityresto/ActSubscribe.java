package iee3.he_arc.cityresto;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iee3.he_arc.cityresto.InternDB.ClassInternUser;

public class ActSubscribe extends AppCompatActivity {

    private Button btnOKSubscribe;
    String lUserName;
    String lPassword;
    String lPasswordConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_subscr);

        // Declare PermanentDataHelper object
        final ClassPermanentDataHelper mClassPermanentDataHelper = new ClassPermanentDataHelper(this);



        btnOKSubscribe = (Button) findViewById(R.id.btnOKSubscribe);

        // Subscription
        btnOKSubscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                lUserName = ((EditText) findViewById(R.id.etUserName)).getText().toString();
                lPassword = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                lPasswordConfirmed = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();

                if(lPassword.equals(lPasswordConfirmed)) {

                    // Declare ClassInternUser object
                    ClassInternUser mClassInternUser = new ClassInternUser(lUserName, lPassword);

                    mClassPermanentDataHelper.insertUser(mClassInternUser);

                    Intent intent = new Intent(ActSubscribe.this, ActMainResto.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ActSubscribe.this, getString(R.string.NoMatchPassword),
                            Toast.LENGTH_LONG).show();

                }

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_subscr, menu);
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
