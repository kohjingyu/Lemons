package com.example.kohjingyu.lemons.shop;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kohjingyu.lemons.R;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends Fragment {
    View view;
    EditText equipmentIdEditText;
    OnEquipmentSelectedListener mCallback;
    Map<String,Integer> equipped;

    public interface OnEquipmentSelectedListener {
        public void addEquipmentToAvatar(String equipmentId);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnEquipmentSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public EquipmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shop_fragment_equipment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        equipmentIdEditText = getView().findViewById(R.id.equipmentEditText);

        EditText.OnEditorActionListener enterkeyListener = new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    mCallback.addEquipmentToAvatar(equipmentIdEditText.getText().toString());
                }
                return true;
            }
        };

        equipmentIdEditText.setOnEditorActionListener(enterkeyListener);
    }
}
