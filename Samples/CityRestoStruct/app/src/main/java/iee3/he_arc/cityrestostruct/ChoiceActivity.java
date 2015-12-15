package iee3.he_arc.cityrestostruct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.Toast;

public class ChoiceActivity extends AppCompatActivity {

    private Button searchButton;
    private CheckBox chk1, chk2, chk3, chk4, chk5, chk6, chk7, chk8, chk9, chk10;
    StringBuffer result = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        chk1 = (CheckBox) findViewById(R.id.checkBox);
        chk2 = (CheckBox) findViewById(R.id.checkBox2);
        chk3 = (CheckBox) findViewById(R.id.checkBox3);
        chk4 = (CheckBox) findViewById(R.id.checkBox4);
        chk5 = (CheckBox) findViewById(R.id.checkBox5);
        chk6 = (CheckBox) findViewById(R.id.checkBox6);
        chk7 = (CheckBox) findViewById(R.id.checkBox7);
        chk8 = (CheckBox) findViewById(R.id.checkBox8);
        chk9 = (CheckBox) findViewById(R.id.checkBox9);
        chk10 = (CheckBox) findViewById(R.id.checkBox10);



        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                result.append("chk1 : ").append(chk1.isChecked());
                result.append("\nchk2 : ").append(chk2.isChecked());
                result.append("\nchk3 : ").append(chk3.isChecked());
                result.append("\nchk4 : ").append(chk4.isChecked());
                result.append("\nchk5 : ").append(chk5.isChecked());
                result.append("\nchk6 : ").append(chk6.isChecked());
                result.append("\nchk7 : ").append(chk7.isChecked());
                result.append("\nchk8 : ").append(chk8.isChecked());
                result.append("\nchk9 : ").append(chk9.isChecked());
                result.append("\nchk10 : ").append(chk10.isChecked());

                Toast.makeText(ChoiceActivity.this, result.toString(),
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChoiceActivity.this, SecondActivity.class);
                startActivity(intent);
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choice, menu);
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
