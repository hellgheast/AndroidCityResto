package iee3.he_arc.cityresto.InternDB;

/**
 * Created by mohammed.bensalah on 12.01.2016.
 */
public class ClassInternRestaurant
{
    private  String PlaceID;
    private  String Adress;
    private  String Name;


    public ClassInternRestaurant()
    {

    }

    public ClassInternRestaurant(String placeID) {
        PlaceID = placeID;

    }

    public ClassInternRestaurant(String placeID, String adress, String name)
    {
        PlaceID = placeID;
        Adress = adress;
        Name = name;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public String getAdress() {
        return Adress;
    }

    public String getName() {
        return Name;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public void setName(String name) {
        Name = name;
    }
}

