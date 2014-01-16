package sk.gymdb.thinis.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import sk.gymdb.thinis.R;

/**
 * Created by matejkobza on 15.1.2014.
 */
public class SelectClassDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ktorú triedu navštevuješ?");

            builder.setItems(R.array.clazz, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    clas="";
//                    switch (which){
//                        case 0: clas="I.A";
//                            break;
//                        case 1: clas="I.B";
//                            break;
//                        case 2: clas="II.A";
//                            break;
//                        case 3: clas="II.B";
//                            break;
//                        case 4: clas="II.C";
//                            break;
//                        case 5: clas="III.A";
//                            break;
//                        case 6: clas="III.B";
//                            break;
//                        case 7: clas="III.C";
//                            break;
//                        case 8: clas="IV.A";
//                            break;
//                        case 9: clas="IV.B";
//                            break;
//                        case 10: clas="IV.C";
//                            break;
//                        case 11: clas="I.PRIMA";
//                            break;
//                        case 12: clas="II.SEKUNDA";
//                            break;
//                        case 13: clas="III.TERCIA";
//                            break;
//                        case 14: clas="IV.KVARTA";
//                            break;
//                        case 15: clas="VI.SEXTA";
//                            break;
//                        case 16: clas="VII.SEPTIMA";
//                            break;
//                        case 17: clas="VIII.OKTAVA";
//                            break;
//                    }
//                    final SharedPreferences prefs = getPreferences();
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putString("CLASS", clas);
//                    editor.commit();
                }
            });
            return builder.create();
        }
//    }
}
