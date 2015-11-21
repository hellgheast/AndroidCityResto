package iee3.hearc.mysqltest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by mohammed.bensalah on 13.11.2015.
 */
public class DBHelper
{
    private Connection mConnect=null;
    private Statement  mStatement=null;
    private String TAG = DBHelper.class.getSimpleName();

    /*Constructeur simple*/
    public DBHelper()
    {
        mConnect=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("DRIVER OK!");
            //mConnect = DriverManager.getConnection("jdbc:mysql://sql4.freesqldatabase.com:3306/sql496314", "sql496314", "mGh81RgL7P"); //Ligne a vec la base de donnée normale
            mConnect = DriverManager.getConnection("jdbc:mysql://b3nj0007.noip.me:10003/AndroidDB", "HELLGHEAST", "Toto22"); //Ligne a vec la base de donnée de benji
            System.out.println("Connexion OK");


        } catch (ClassNotFoundException notFoundException) {
            notFoundException.printStackTrace();
            System.out.println("Erreur CLASSE");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Erreur SQL");
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println("Erreur Instanciation");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Erreur d'accès");
        }
    }


    /*Méthodes diverses et variées*/
    public ResultSet SQLRequestSimple()
    {
        try
        {

            //Création d'un object Statement
            mStatement = mConnect.createStatement();

            ResultSet result = mStatement.executeQuery("SELECT * FROM Tperson WHERE first_name != \"Bob\";");

            result.beforeFirst();
            result.next();

            //On récupère les MetaData
            ResultSetMetaData resultMeta = result.getMetaData();

            return result;

        }
        catch (SQLException sqlException) {
        sqlException.printStackTrace();
        System.out.println("Erreur de connecxion");
        return null;
        }

    }



}
