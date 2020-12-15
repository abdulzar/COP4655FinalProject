package com.sense.it.wifi.iot.studentproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {
    ImageView imageView;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setTitle(Common.currentResturant.getName());
        String bussinessId = Common.currentResturant.getId();
        imageView = findViewById(R.id.v_image);
        tv = findViewById(R.id.tv_details);
        getResponse(bussinessId);
    }

    void getResponse(String bussiness_id) {
        // Instantiate the RequestQueue.
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Loading..");
        pd.setMessage("Getting Details data from YelpApi..Wait");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ApiUrl.yelp_detail_URL + bussiness_id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                parseJsonResponse(response);
                pd.dismiss();
                Toast.makeText(ViewActivity.this, "Response Success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(ViewActivity.this, "OnErrorResponse", Toast.LENGTH_SHORT).show();
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

    void parseJsonResponse(JSONObject bussinessObject) {
        try {

            String id = bussinessObject.getString("id");
            String name = bussinessObject.getString("name");
            String image_url = bussinessObject.getString("image_url");
            Glide.with(this).load(image_url).into(imageView);
            String phone = bussinessObject.getString("display_phone");
            Double rating = bussinessObject.getDouble("rating");
            int review_count = bussinessObject.getInt("review_count");
            //-------getCoordinates------
            JSONObject coordinatesObject = bussinessObject.getJSONObject("coordinates");
            LatLng latLng = new LatLng(coordinatesObject.getDouble("latitude"), coordinatesObject.getDouble("longitude"));
            //---------getAddress----------
            JSONObject addressObject = bussinessObject.getJSONObject("location");
            String address = addressObject.getString("address1") + "," + addressObject.getString("city") +
                    "," + addressObject.getString("country");

            String dataString = name + "\n\nRating: " + rating + "\t\tReview_Count:" + review_count +
                    "\n\nPhone: " + phone + "\n\nAddress: " + address;
            tv.setText(dataString);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}