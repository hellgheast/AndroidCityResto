package iee3.he_arc.cityresto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;


public class FragParameters extends Fragment {

    public static final String ACCEPTPARAMETERS = "ACCEPTPARAM" ;
    private Button BtnAcceptParameters;
    private Button BtnCancelParameters;
    private CheckBox chk1, chk2, chk3, chk4, chk5, chk6, chk7, chk8, chk9, chk10;
    private int lRadius;


    public FragParameters() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_parameters, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final EditText etRadius = (EditText) getActivity().findViewById(R.id.etSetRadius);

        lRadius = ClassMainStorageManager.getRadius(getContext()); // Read the default radius
        etRadius.setText(lRadius + ""); // Set the default Radius

        // TODO : Vérifier que les checkBoxes ne se reset pas à chaque fois !
        ClassMainStorageManager.initCheckBoxes(getContext()); // Make checkboxes empty

        chk1 = (CheckBox) this.getActivity().findViewById(R.id.cbType1);
        chk2 = (CheckBox) this.getActivity().findViewById(R.id.cbType2);
        chk3 = (CheckBox) this.getActivity().findViewById(R.id.cbType3);
        chk4 = (CheckBox) this.getActivity().findViewById(R.id.cbType4);
        chk5 = (CheckBox) this.getActivity().findViewById(R.id.cbType5);
        chk6 = (CheckBox) this.getActivity().findViewById(R.id.cbType6);
        chk7 = (CheckBox) this.getActivity().findViewById(R.id.cbType7);
        chk8 = (CheckBox) this.getActivity().findViewById(R.id.cbType8);
        chk9 = (CheckBox) this.getActivity().findViewById(R.id.cbType9);
        chk10 = (CheckBox) this.getActivity().findViewById(R.id.cbType10);

        BtnAcceptParameters = (Button) this.getActivity().findViewById(R.id.btnAcceptParam);
        BtnAcceptParameters.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                // Ismail, pardonne moi pour cette méthode barbare xD

                // Set all checkboxes, in case of some were unchecked or checked
                ClassMainStorageManager.setHmTypesChecked("@string/type1", chk1.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type2", chk2.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type3", chk3.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type4", chk4.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type5", chk5.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type6", chk6.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type7", chk7.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type8", chk8.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type9", chk9.isChecked(), getContext());
                ClassMainStorageManager.setHmTypesChecked("@string/type10", chk10.isChecked(), getContext());

                // Set radius chose by user and save it
                lRadius = Integer.parseInt(etRadius.getText().toString());
                ClassMainStorageManager.setRadius(lRadius, getContext());



                // Go to Map/List fragment
                ActMainResto.setPage(getContext());

                // Scroll to the top of the parameters list for further uses
                ScrollView scroll = (ScrollView) getActivity().findViewById(R.id.scrollView);
                scroll.scrollTo(0, scroll.getTop());

                //FragMapList.updateFragMapList();

                //Send
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ACCEPTPARAMETERS));
                //Envoi du message en broadcast afin que l'on puisse rajouter les markeurs correctement
                //Raison : Recreation des markeurs dynamique

            }
        });

        BtnCancelParameters = (Button) this.getActivity().findViewById(R.id.btnCancelParam);
        BtnCancelParameters.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                // Go to Map/List fragment
                ActMainResto.setPage(getContext());

            }
        });
    }

}
