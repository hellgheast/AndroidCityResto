package iee3.he_arc.cityresto.InternDB;


/**
 * Created by mohammed.bensalah on 08.01.2016.
 */
public class ClassInternUser
{
    //Attributs privés
    private String Username;
    private String Password;

    //Constructeurs

    public ClassInternUser()
    {
    }

    public ClassInternUser(String username) {
        Username = username;
    }

    public ClassInternUser(String _Username,String _Password)
    {
        this.Password = _Password;
        this.Username = _Username;
    }



    //Getters
    public String getUsername() {
        return Username;
    }
    public String getPassword() {
        return Password;
    }

    //Setters
    public void setUsername(String username) {
        Username = username;
    }
    public void setPassword(String password) {
        Password = password;
    }
}
