package com.example.kohjingyu.lemons;


import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LevelUpAlertDialogFragment extends AppCompatDialogFragment {

    public LevelUpAlertDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_level_up_alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(v)
                .create();

    }

    @Override
    public void show(android.support.v4.app.FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

}
