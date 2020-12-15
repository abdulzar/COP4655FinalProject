package com.sense.it.wifi.iot.studentproject.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.sense.it.wifi.iot.studentproject.ApiUrl;
import com.sense.it.wifi.iot.studentproject.Common;
import com.sense.it.wifi.iot.studentproject.R;
import com.sense.it.wifi.iot.studentproject.SharedPreference;
import com.sense.it.wifi.iot.studentproject.ViewActivity;
import com.sense.it.wifi.iot.studentproject.adapter.ListAdapter;
import com.sense.it.wifi.iot.studentproject.adapter.ResturantAdapter;
import com.sense.it.wifi.iot.studentproject.model.ResturantModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResturantFragment extends Fragment implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String ARG_ITEM_ID = "product_list";
    public RecyclerView rc;
    public ResturantAdapter adapter;
    public EditText editSearchTxt;
    public Button btn_search;
    ListView listView;
    List<ResturantModel> products;
    ListAdapter listAdapter;
    Activity activity;
    SharedPreference sharedPreference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_resturant, container, false);

        rc = root.findViewById(R.id.rc);
        listView = root.findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        editSearchTxt = root.findViewById(R.id.edit_search);
        btn_search = root.findViewById(R.id.btn_serch);
        editSearchTxt.setText(ApiUrl.location);
        getResponse(ApiUrl.location);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponse(editSearchTxt.getText().toString());
            }
        });
        return root;
    }

    void getResponse(String locationCountry) {
        // Instantiate the RequestQueue.
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading..");
        pd.setMessage("Get data from YelpApi..Wait");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = ApiUrl.yelp_search_URL + "term=" + ApiUrl.term + "&location=" + locationCountry;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                parseJsonResponse(response);
                pd.dismiss();
                Toast.makeText(getActivity(), "Response Success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "OnErrorResponse", Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ApiUrl.yelpAPIKey);
                return headerMap;
            }
        };

// Add the request to the RequestQueue.
        queue.add(req);
    }

    void parseJsonResponse(JSONObject jsonObject) {
        try {
            JSONArray bussiness = jsonObject.getJSONArray("businesses");
            List<ResturantModel> resturantModelList = new ArrayList<>();
            for (int i = 0; i < bussiness.length(); i++) {
                JSONObject bussinessObject = bussiness.getJSONObject(i);
                String id = bussinessObject.getString("id");
                String name = bussinessObject.getString("name");
                String image_url = bussinessObject.getString("image_url");
                //---------getCatagories
                List<String> cataList = new ArrayList<>();
                cataList.add("Yasir");
                /*JSONArray categoriesArray = bussinessObject.getJSONArray("categories");
                for (int j = 0; j < categoriesArray.length(); j++) {
                    JSONObject catagoryObject = categoriesArray.getJSONObject(i);
                    String cataTitle = catagoryObject.getString("title");
                    cataList.add(cataTitle);
                }*/
                //-------getCoordinates------
                JSONObject coordinatesObject = bussinessObject.getJSONObject("coordinates");
                LatLng latLng = new LatLng(coordinatesObject.getDouble("latitude"), coordinatesObject.getDouble("longitude"));
                //---------getAddress----------
                JSONObject addressObject = bussinessObject.getJSONObject("location");
                String address = addressObject.getString("address1") + "," + addressObject.getString("city") +
                        "," + addressObject.getString("country");
                ResturantModel resturantModel = new ResturantModel(id, name, image_url, address, cataList, latLng);
                resturantModelList.add(resturantModel);

            }
            //-----initializeRecyclerView-----
            Common.resturantModelList = resturantModelList;
            listAdapter = new ListAdapter(getActivity(), resturantModelList);
            listView.setAdapter(listAdapter);
            /*adapter=new ResturantAdapter(getActivity(),resturantModelList);
            rc.setLayoutManager(new LinearLayoutManager(getActivity()));
            rc.setHasFixedSize(true);
            rc.setAdapter(adapter);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ResturantModel product = (ResturantModel) parent.getItemAtPosition(position);
        Common.currentResturant = product;
        startActivity(new Intent(getActivity(), ViewActivity.class));
        Toast.makeText(getActivity(), product.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                   int position, long arg3) {
        ImageView button = (ImageView) view.findViewById(R.id.iv_share);

        String tag = button.getTag().toString();
        if (tag.equalsIgnoreCase("grey")) {
            sharedPreference.addFavorite(activity, Common.resturantModelList.get(position));
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.add_favr),
                    Toast.LENGTH_SHORT).show();

            button.setTag("red");
            button.setImageResource(R.drawable.ic_fav_red);
        } else {
            sharedPreference.removeFavorite(activity, Common.resturantModelList.get(position));
            button.setTag("grey");
            button.setImageResource(R.drawable.ic_fav_grey);

            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.remove_favr),
                    Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}