package iee3.he_arc.cityresto;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;


public class FragParameters extends Fragment {

    private Button BtnAcceptParameters;
    private Button BtnCancelParameters;
    private CheckBox chk1, chk2, chk3, chk4, chk5, chk6, chk7, chk8, chk9, chk10;
    private int lRadius;

    public FragParameters() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addListenerOnButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_parameters, container, false);

    }

    public void addListenerOnButton() {


       // lRadius = ClassMainStorageManager.getRadius();

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
        BtnAcceptParameters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });
    }

}
