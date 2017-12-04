package com.example.kohjingyu.lemons.shop;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.kohjingyu.lemons.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends Fragment {
    View view;
    EditText equipmentIdEditText;
    OnEquipmentSelectedListener mCallback;
    Map<String,Integer> equipped;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //The fragment id is reused when destroyed and created again
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.shop_fragment_equipment, container, false);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.shop_viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.shop_tabs_papa);
        tabs.setupWithViewPager(viewPager); //Will auto refresh help?

        return view;
    }

    public void setupViewPager(ViewPager viewPager){
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(ShopCloneFragment.newInstance("hat"), "Hat");
        adapter.addFragment(ShopCloneFragment.newInstance("top"), "Top");
        adapter.addFragment(ShopCloneFragment.newInstance("bottom"), "Bottom");
        adapter.addFragment(ShopCloneFragment.newInstance("shoes"), "Shoes");
        adapter.addFragment(ShopCloneFragment.newInstance("mount"), "Mount");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }




    public interface OnEquipmentSelectedListener {
        void addEquipmentToAvatar(String equipmentId);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        equipmentIdEditText = getView().findViewById(R.id.equipmentEditText);
//
//        EditText.OnEditorActionListener enterkeyListener = new EditText.OnEditorActionListener(){
//
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    //when enter is pressed, send the edit text value
//                    mCallback.addEquipmentToAvatar(equipmentIdEditText.getText().toString());
//                }
//                return true;
//            }
//        };

//        equipmentIdEditText.setOnEditorActionListener(enterkeyListener);
    }
}
