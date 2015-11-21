package iee3.hearc.mysqltest;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import iee3.hearc.mysqltest.DBHelper;

import java.sql.*;

//Utilisation de JDBC dans un environnement android

public class MainActivity extends AppCompatActivity {

    //Attributs graphiques
    TextView myTxt=null;

    //Apprendre à utiliser une base de données via JAVA
    private Connection connection = null;
    private Statement statement = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTxt=(TextView)findViewById(R.id.MyTextView);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //On vérifie si on à un accès à Internet

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new getDetails().execute();
        }
        else
        {
            System.out.println("Aucun accès réseau");
        }


    }

    //Méthodes diverses
    private void showMessage(String msg)
    {
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
    }


    //Test avec des privates class
    private class getDetails extends AsyncTask<Void,Void,Void>
    {
        private ProgressDialog lDialog;
        private ResultSet r;

        @Override
        protected void onPreExecute()
        {
            lDialog = new ProgressDialog(MainActivity.this);
            lDialog.setCancelable(false);
            lDialog.setMessage("Requête en cours");
            showDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DBHelper lDb = new DBHelper();
            r =lDb.SQLRequestSimple();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            hideDialog();
            if(r==null)
            {
               showMessage("Requête refusée !");
            }
            else
            {
              showMessage("Requête acceptée");

                try
                {
                    String yay =r.getString("first_name");
                    showMessage(yay);
                }
                catch (SQLException sqlException)
                {
                    sqlException.printStackTrace();
                }
            }
        }

        //Affichage divers et variées
        private void showDialog()
        {
            if(!lDialog.isShowing())
            {
                lDialog.show();
            }
        }

        private void hideDialog()
        {
            if(lDialog.isShowing())
            {
                lDialog.hide();
            }
        }



    }
}
