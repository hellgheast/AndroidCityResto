package iee3.hearc.sdztpblocnote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Différents attributs qui changeront lors de l'éxecution de la classe
    private boolean showHide=false; //Pour savoir quel layout charger
    private String TextColor = null;
    //TODO Essayer de comprendre pourquoi l'appel de la couleur ne fonctionne pas ? Peut-être voir l'état du stack au lanchement ?


    //Tout les élements graphiques

    /*Layouts*/
    private RelativeLayout mLayout_Root=null;
    private RelativeLayout mLayout_Second=null;

    /*Buttons*/
    private Button BtnHide  =null;
    private Button BtnBold  =null;
    private Button BtnItal  =null;
    private Button BtnUnder =null;
    /*ImageButton*/
    private ImageButton IBtnSmile=null;
    private ImageButton IBtnWink=null;
    private ImageButton IBtnHappy=null;
    /*RadioGroup*/
    private RadioGroup RGColor=null;
    /*Text*/
    private TextView TxtVPreview=null;
    /*Editeur*/
    private EditText ETxtEditor=null;
    /*SmileyGetter*/
    private SmileyGetter mSmiley=null;

    private static final String mTag = "HE-ARC";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Récupération des différentes View*/
        mSmiley = new SmileyGetter(this);

        /*Récupération des buttons*/
        BtnHide = (Button) findViewById(R.id.Drawable_HideShowButton);
        BtnBold = (Button) findViewById(R.id.Drawable_BoldText);
        BtnItal = (Button) findViewById(R.id.Drawable_ItalicText);
        BtnUnder = (Button) findViewById(R.id.Drawable_UnderText);
        /*Récupération du RadioGroup*/
        RGColor = (RadioGroup) findViewById(R.id.radioColorGroup);

        /*Récupération des smileys*/
        IBtnHappy = (ImageButton) findViewById(R.id.SmileyHappy);
        IBtnSmile = (ImageButton) findViewById(R.id.SmileySmile);
        IBtnWink = (ImageButton) findViewById(R.id.SmileyWink);


        ETxtEditor = (EditText) findViewById(R.id.EditText_Zone);

        this.TextColor=String.valueOf(getResources().getColor(R.color.white));

        Log.i(mTag,"onCreate");


    }

    @Override
    protected void onPostCreate (Bundle savedInstanceState)
    {
        Log.i(mTag,"onPostCreate");
        super.onPostCreate(savedInstanceState);
        /*Handler pour le section d'Edition*/

        ETxtEditor.addTextChangedListener(new TextWatcher() {
            @Override
            /**
             * s est la chaîne de caractères qui est en train de changer
             */
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            /**
             * @param s La chaîne qui a été modifiée
             * @param count Le nombre de caractères concernés
             * @param start L'endroit où commence la modification dans la chaîne
             * @param after La nouvelle taille du texte
             */
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Que faire juste avant que le changement de texte soit pris en compte ?
            }

            @Override
            /**
             * @param s L'endroit où le changement a été effectué
             */
            public void afterTextChanged(Editable s) {
                // Que faire juste après que le changement de texte a été pris en compte ?
            }
        });

    }

    /**/

    /*Handler pour les boutons*/

    /*Handler pour changer la couleur*/
    public void RB_RedHandler(View view)
    {
        this.TextColor = String.valueOf(getResources().getColor(R.color.red));
    }

    public void RB_BlackHandler(View view)
    {
        this.TextColor = String.valueOf(getResources().getColor(R.color.black));
    }

    public void RB_BlueHandler(View view)
    {
        this.TextColor = String.valueOf(getResources().getColor(R.color.blue));
    }

    /*Handler pour mettre le texte en surligne*/
    public void Drawable_SetUnderText(View view)
    {

    }
    /*Handler pour mettre le texte en gras*/
    public void Drawable_SetBoldText(View view)
    {

    }
    /*Handler pour mettre le texte en italique*/
    public void Drawable_SetItalText(View view)
    {

    }


    public void Drawable_ChangeLayout(View view)
    {
        if(showHide==true)
        {
            String t_Text="Yolo";
            this.showHide=false;
            t_Text=ETxtEditor.getText().toString();
            setContentView(R.layout.activity_second);
            ETxtEditor = (EditText) findViewById(R.id.EditText_Zone);
            ETxtEditor.setText(t_Text);
        }
        else
        {
            this.showHide=true;
            setContentView(R.layout.activity_main);
        }
    }



}
