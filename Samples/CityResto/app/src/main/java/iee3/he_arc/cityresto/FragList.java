package iee3.he_arc.cityresto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.location.Location;
import android.location.LocationManager;
import android.widget.CheckBox;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

public class FragList extends Fragment {

    private CheckBox cbList;

    public FragList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag_list, container, false);

        // Initialize checkbox for switching between map and list
        cbList = (CheckBox) v.findViewById(R.id.cbSwitchMap);
        cbList.setChecked(true);

        /// Switch to Map
        cbList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Check if user want to switch to Map
                if(!cbList.isChecked()){
                    ActMainResto.switchMap(getContext());
                    ActMainResto.setPage(getContext());
                }
            }
        });

        return v;
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
