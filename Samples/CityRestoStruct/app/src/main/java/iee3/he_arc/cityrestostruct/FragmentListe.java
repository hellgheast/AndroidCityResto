package iee3.he_arc.cityrestostruct;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentListe#newInstance} factory method to
 * create an instance of this fragment.
 */

public class FragmentListe extends Fragment {

    ListView mListView;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_liste, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toast.makeText(getActivity(), "salut", Toast.LENGTH_SHORT).show();
        mListView = (ListView) this.getActivity().findViewById(R.id.listView);
        List<Resto> restos = genererRestos();

        // Afin de remplir une ListView, il existe un objet nommé Adapter, qui va prendre en entrée
        // une liste d’objets et un layout XML et va générer pour chaque entrée une cellule formatée
        RestoAdapter adapter = new RestoAdapter(getActivity(), restos);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Object resto = mListView.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity(), RestoActivity.class);
                intent.putExtra("name", resto.toString());
                startActivity(intent);
            }
        });
    }

    // Classe qui génère une liste de restaurants
    private List<Resto> genererRestos() {
        List<Resto> listRestos = new ArrayList<Resto>();
        listRestos.add(new Resto(Color.BLACK, "Pizz'", "Offres le mardi"));
        listRestos.add(new Resto(Color.BLUE, "Chez Isma", "C'est ici que ça se passe !"));
        listRestos.add(new Resto(Color.GREEN, "Boulangerie", "Pain frai"));
        listRestos.add(new Resto(Color.RED, "Brasserie", "Offres spéciales le week-end"));
        listRestos.add(new Resto(Color.GRAY, "McDonald's", "McFlurry 50%"));
        listRestos.add(new Resto(Color.YELLOW, "Bleu Café", "Offres pour étudiants"));
        listRestos.add(new Resto(Color.BLUE, "Burger King's", ""));
        listRestos.add(new Resto(Color.GREEN, "Antidote", "Blabli"));
        return listRestos;
    }
}
