package iee3.he_arc.cityresto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

import iee3.he_arc.cityresto.InternDB.ClassInternRestaurant;


public class FragFavourites extends Fragment {

    private ListView listView;
    private static Context fragFavouriteContext;
    private ClassPermanentDataHelper mClassPermanentDataHelper;
    private ClassInternRestaurant mClassInternRestaurant;
    private ArrayList<ClassInternRestaurant> mArrayListOfRestos;
    private List<Place> mListOfFavourite;
    public FragFavourites() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag_favourites, container, false);

        listView = (ListView) v.findViewById(R.id.lvListFavourite);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragFavouriteContext = getContext();
        mArrayListOfRestos = new ArrayList<ClassInternRestaurant>();

        mClassPermanentDataHelper = new ClassPermanentDataHelper(getContext());
        if(null != mClassPermanentDataHelper.readAllRestaurant()) {
            mArrayListOfRestos = mClassPermanentDataHelper.readAllRestaurant();
        }
        else
        {
            mArrayListOfRestos.clear();
        }

        ClassRestoFavouriteAdapter mClassRestoFavouriteAdapter = new ClassRestoFavouriteAdapter(getActivity(), mArrayListOfRestos);
        listView.setAdapter(mClassRestoFavouriteAdapter);



        // listView Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find which Resto correspond to the position
                String restoID = mArrayListOfRestos.get(position).getPlaceID();

                Intent intent = new Intent(getContext(), ActRestoProfile.class);
                intent.putExtra("markerID", restoID);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
