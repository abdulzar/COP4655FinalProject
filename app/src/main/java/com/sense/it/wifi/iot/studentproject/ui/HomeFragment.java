package com.sense.it.wifi.iot.studentproject.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sense.it.wifi.iot.studentproject.Common;
import com.sense.it.wifi.iot.studentproject.R;
import com.sense.it.wifi.iot.studentproject.SharedPreference;
import com.sense.it.wifi.iot.studentproject.ViewActivity;
import com.sense.it.wifi.iot.studentproject.adapter.ListAdapter;
import com.sense.it.wifi.iot.studentproject.model.ResturantModel;

import java.util.List;

public class HomeFragment extends Fragment {

    public static final String ARG_ITEM_ID = "favorite_list";

    ListView favoriteListView;
    List<ResturantModel> favorites;

    Activity activity;
    ListAdapter adapter;
    SharedPreference sharedPreference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,
                false);
        // Get favorite items from SharedPreferences.
        favorites = sharedPreference.getFavorites(activity);

        if (favorites == null) {
            showAlert(getResources().getString(R.string.no_favorites_items),
                    getResources().getString(R.string.no_favorites_msg));
        } else {

            if (favorites.size() == 0) {
                showAlert(
                        getResources().getString(R.string.no_favorites_items),
                        getResources().getString(R.string.no_favorites_msg));
            }

            favoriteListView = view.findViewById(R.id.list_fav);
            if (favorites != null) {
                adapter = new ListAdapter(activity, favorites);
                favoriteListView.setAdapter(adapter);

                favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View arg1,
                                            int position, long arg3) {
                        ResturantModel product = (ResturantModel) parent.getItemAtPosition(position);
                        Common.currentResturant = product;
                        startActivity(new Intent(getActivity(), ViewActivity.class));
                        Toast.makeText(getActivity(), product.getName(), Toast.LENGTH_LONG).show();

                    }
                });

                favoriteListView
                        .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(
                                    AdapterView<?> parent, View view,
                                    int position, long id) {

                                ImageView button = (ImageView) view
                                        .findViewById(R.id.iv_share);
                                sharedPreference.removeFavorite(activity,
                                        favorites.get(position));
                                button.setTag("grey");
                                button.setImageResource(R.drawable.ic_fav_grey);
                                adapter.remove(favorites
                                        .get(position));
                                Toast.makeText(
                                        activity,
                                        activity.getResources().getString(
                                                R.string.remove_favr),
                                        Toast.LENGTH_SHORT).show();

                                return true;
                            }
                        });
            }
        }
        return view;
    }

    public void showAlert(String title, String message) {
        if (activity != null && !activity.isFinishing()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);

            // setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // activity.finish();
                            getFragmentManager().popBackStackImmediate();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}