package iee3.he_arc.cityrestostruct;

/**
 * Created by vincent.meier on 15.12.2015.
 */
import android.os.Parcel;
import android.os.Parcelable;


public class Resto {
    private int color;
    private String pseudo;
    private String text;

    public Resto(int color, String pseudo, String text) {
        this.color = color;
        this.pseudo = pseudo;
        this.text = text;
    }

    public String getPseudo(){
        return pseudo;
    }

    public String getText(){
        return text;
    }

    public int getColor(){
        return color;
    }

    public String toString(){
        String datas = pseudo + " " + text +  Integer.toString(color);
        return datas;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pseudo);
        dest.writeString(text);
        dest.writeString(Integer.toString(color));
    }


    public static final Parcelable.Creator<Resto> CREATOR = new Parcelable.Creator<Resto>()
    {
        @Override
        public Resto createFromParcel(Parcel source)
        {
            return new Resto(source);
        }

        @Override
        public Resto[] newArray(int size)
        {
            return new Resto[size];
        }
    };

    public Resto(Parcel in) {
        this.pseudo = in.readString();
        this.text = in.readString();
        this.color = in.readInt();
    }
    // ...getters
    // ...setters
}