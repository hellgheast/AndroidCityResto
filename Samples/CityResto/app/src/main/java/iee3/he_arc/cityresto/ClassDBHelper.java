package iee3.he_arc.cityresto;

import android.net.nsd.NsdManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mohammed.bensalah on 22.12.2015.
 */

/*=====================================================================*
 | This file declares the following classes:
 |    ClassDBHelper
 |
 |
 | Description of the class ClassDBHelper :
 | Classe qui permet la communication avec la base de donnée et entre l'application
 |
 |
 | <p>Copyright : HE-ARC, all rights reserved</p>
 | @autor : Mohammed-Ismail Ben Salah
 | @version : 1.0
 |
 | Mofification tag : //MODIF1
 |
 *=====================================================================*/


public class ClassDBHelper
{
    private Connection mConnect=null;
    private Statement mStatement=null;
    private String TAG = ClassDBHelper.class.getSimpleName();

    //Name of the database tables & others constants
    final static String databaseName = "AndroidDB";
    final static String databaseUsername = "HELLGHEAST";
    final static String databasePassword = "Toto22";




    //Table for the UserGestion

    final static String UserTable =     "TUser";
    //Columns of the Table
    final static String UserNumberField     = "idNbUser";   //idNbUser  the number of the User
    final static String UserNameField       = "idUser";     //idUser    the username of the User
    final static String UserNamePwdField    = "UserPwd";    //UserPwd   the password of the User
    final static String UserOwnerField      = "isOwner";    //isOwner   indication if the user is owner

    final static String PersonTable =   "TPerson";
    final static String AvisTable =     "TAvis";
    final static String MenuTable =     "TMenu";
    final static String TypeTable =     "TType";


    final static String RestTable =     "TRestaurant";


    /*Constructeur simple*/
    public ClassDBHelper()
    {
        mConnect=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("DRIVER OK!");
            //mConnect = DriverManager.getConnection("jdbc:mysql://sql4.freesqldatabase.com:3306/sql496314", "sql496314", "mGh81RgL7P"); //Ligne a vec la base de donnée normale
            mConnect = DriverManager.getConnection("jdbc:mysql://b3nj0007.noip.me:10003/"+databaseName, databaseUsername, databasePassword); //Ligne a vec la base de donnée de benji
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

    //TODO Diverses méthodes d'accès aux élements de l'application

    /**
     * InsertUsernamePassword
     * Insert the Username & the Password of a new standard user
     *
     * @param  _Username : Username of the user created
     * @param  _Password : Password of the user created
     * @param  _Owner : Indicate if the user created is an Owner of not
     * @return void
     */
    public  void InsertUsernamePassword (String _Username,String _Password,int _Owner) throws java.sql.SQLException {
        Statement lStatement = null;
        int lRowWork=0;

        lStatement = mConnect.createStatement();


        assert lStatement != null;
        lRowWork=lStatement.executeUpdate
                ("INSERT INTO" + UserTable + "("+UserNameField+","+UserNamePwdField+","+UserOwnerField+")" +
                            "VALUES" +"(" + _Username + "," + _Password +","+_Owner+ ")"+ ";");

        if(0==lRowWork)
        {
            //TODO Gérer les exceptions !
            throw new SQLException("No Insertion !");
        }

    }

    /**
     * UpdateUsernamePassword
     * Update the Username and the Password of the current user
     *
     * @param  _OldUsername : Old Username of the requesting user
     * @param  _NewUserName : New Username of the requesting user
     * @param  _OldPassword : Old Password of the user
     * @param  _NewPassword : New Password of the user
     * @return void
     */
    public void UpdateUsernamePassword (String _OldUsername,String _NewUserName,String _OldPassword,String _NewPassword) throws java.sql.SQLException {
        Statement lStatement = null;
        int lRowWork=0;

        lStatement = mConnect.createStatement();


        lRowWork=lStatement.executeUpdate
                ( "UPDATE " + UserTable +
                        " SET "+ UserNameField+"="+_NewUserName+","+UserNamePwdField+"="+_NewPassword+
                            " WHERE "+UserNameField+"="+_OldUsername + " AND "+UserNamePwdField +"="+_OldPassword + ";");

        if(0==lRowWork)
        {
            //TODO Gérer les exceptions !
            throw new SQLException("User not existing");
        }
    }

    /**
     * ReadUsername
     * Read the entire row of the specified username
     *
     * @param  _Username : Username of the specified user
     * @return ResultSet with the row of the user
     */
    public ResultSet ReadUsername(String _Username) throws java.sql.SQLException
    {
        Statement lStatement = null;
        ResultSet lResultSet=null;
        lStatement = mConnect.createStatement();
        lResultSet=lStatement.executeQuery("SELECT * FROM "+UserTable+" WHERE "+UserNameField+"="+_Username+";");

        return lResultSet;
    }

    /**
     * DeleteUsername
     * Delete the specified user
     *
     * @param  _Username : Username of the specified user
     * @return void
     */
    public void DeleteUsername(String _Username) throws java.sql.SQLException
    {
        Statement lStatement = null;
        lStatement = mConnect.createStatement();
        lStatement.executeQuery(" DELETE FROM "+UserTable+" WHERE "+UserNameField+"="+_Username+";");


    }








}
