package iee3.he_arc.cityrestostruct;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vincent.meier on 15.12.2015.
 */
public class RestoAdapter extends ArrayAdapter<Resto> {

    //tweets est la liste des models à afficher
    public RestoAdapter(Context context, List<Resto> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_restos,parent, false);
        }

        RestoViewHolder viewHolder = (RestoViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new RestoViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.pseudo);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Resto> resto
        Resto resto = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.pseudo.setText(resto.getPseudo());
        viewHolder.text.setText(resto.getText());
        viewHolder.avatar.setImageDrawable(new ColorDrawable(resto.getColor()));

        return convertView;
    }

    private class RestoViewHolder{
        public TextView pseudo;
        public TextView text;
        public ImageView avatar;
    }
}