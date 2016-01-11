package iee3.he_arc.cityresto.InternDB;

/**
 * Created by mohammed.bensalah on 08.01.2016.
 */
public class ClassInternPhotoResto
{
    //Attributs privés
    private  String PlaceID;
    private  String PhotoReference;
    private  String PhotoUri;

    //Constructeurs
    public ClassInternPhotoResto()
    {
    }

    public ClassInternPhotoResto(String placeID) {
        PlaceID = placeID;
    }

    public ClassInternPhotoResto(String placeID, String photoReference) {
        PlaceID = placeID;
        PhotoReference = photoReference;
    }

    public ClassInternPhotoResto(String placeID, String photoReference, String photoUri) {
        PlaceID = placeID;
        PhotoReference = photoReference;
        PhotoUri = photoUri;
    }

    //Getters
    public String getPlaceID() {
        return PlaceID;
    }

    public String getPhotoReference() {
        return PhotoReference;
    }

    public String getPhotoUri() {
        return PhotoUri;
    }

    //Setters

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public void setPhotoReference(String photoReference) {
        PhotoReference = photoReference;
    }

    public void setPhotoUri(String photoUri) {
        PhotoUri = photoUri;
    }
}
