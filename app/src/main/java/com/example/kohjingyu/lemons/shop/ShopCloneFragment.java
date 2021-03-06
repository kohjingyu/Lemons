package com.example.kohjingyu.lemons.shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kohjingyu.lemons.Player;
import com.example.kohjingyu.lemons.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopCloneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopCloneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopCloneFragment extends Fragment {
    private static final String ARG_ITEM_TYPE = "itemType";
    public String mItemType;
    private JSONArray equipments;
    private OnFragmentInteractionListener mListener;
    private int level;

    public ShopCloneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemType item Name
     * @return A new instance of fragment ShopCloneFragment.
     */
    public static ShopCloneFragment newInstance(String itemType) {
        ShopCloneFragment fragment = new ShopCloneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_TYPE, itemType);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItemType = getArguments().getString(ARG_ITEM_TYPE);
        }

        level = ((ShopActivity)getParentFragment().getActivity()).getLevel();
        Log.i("level", String.valueOf(level));
        equipments = ((ShopActivity)getParentFragment().getActivity()).getEquipments(mItemType); // get equipments for this tab

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.shop_item_display_new, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Player player = Player.getPlayer();
                int positionTemp = position + 1;
                if (positionTemp > level) {
                    Toast.makeText(getContext(),"Level " + positionTemp + " required", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    player.addEquipment(mItemType, equipments.getJSONObject(position).getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                URL avatarURL = player.mapleURLGenerator(player.getEquipped());
                Log.i("url", avatarURL.toString());
                new GetAvatarTask().execute(avatarURL);

            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int[] itemIds;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return equipments.length();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.shop_item_display_viewholder,parent,false);
            ImageView imageView = view.findViewById(R.id.shop_item);
            int positionTemp = position + 1;
            if (positionTemp > level) {
                TextView textView = view.findViewById(R.id.shop_item_textview);
                textView.setVisibility(View.VISIBLE);
                String text = "LEVEL " + positionTemp;
                textView.setText(text);
            }
            String filename = "avatar";
            String packageName = getContext().getPackageName();
            String typeOfResource = "drawable";
            try {
                 filename = mItemType + equipments.getJSONObject(position).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int resId = getContext().getResources().getIdentifier(filename, typeOfResource, packageName);
            imageView.setImageResource(resId);
            return view;

        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.shopitem1,R.drawable.shopitem2,R.drawable.shopitem3,R.drawable.shopitem4,R.drawable.shopitem5,R.drawable.shopitem6
        };
    }



    public class GetAvatarTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            Bitmap avatar = null; //Create new array of bitmap with length depending on the url
            InputStream in = null;
            try {
                in = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            avatar = BitmapFactory.decodeStream(in);
            return avatar;
        }

        @Override
        protected void onPostExecute(Bitmap avatar){
            ImageView imageView = getParentFragment().getActivity().findViewById(R.id.avatar);
            imageView.setImageBitmap(avatar);
        }
    }
}
