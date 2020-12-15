package com.sense.it.wifi.iot.studentproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sense.it.wifi.iot.studentproject.R;
import com.sense.it.wifi.iot.studentproject.SharedPreference;
import com.sense.it.wifi.iot.studentproject.model.ResturantModel;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ResturantModel> {

    List<ResturantModel> products;
    SharedPreference sharedPreference;
    private Context context;

    public ListAdapter(Context context, List<ResturantModel> products) {
        super(context, R.layout.layout_rc_item, products);
        this.context = context;
        this.products = products;
        sharedPreference = new SharedPreference();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public ResturantModel getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_rc_item, null);
            holder = new ViewHolder();
            holder.productNameTxt = (TextView) convertView
                    .findViewById(R.id.iv_title);
            holder.productDescTxt = (TextView) convertView
                    .findViewById(R.id.iv_discription);
            holder.productImag = (ImageView) convertView
                    .findViewById(R.id.iv_icon);
            holder.favoriteImg = (ImageView) convertView
                    .findViewById(R.id.iv_share);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ResturantModel product = (ResturantModel) getItem(position);
        holder.productNameTxt.setText(product.getName());
        holder.productDescTxt.setText(product.getAddress());
        Glide.with(context).load(product.getImage_url()).into(holder.productImag);

        /*If a product exists in shared preferences then set heart_red drawable
         * and set a tag*/
        if (checkFavoriteItem(product)) {
            holder.favoriteImg.setImageResource(R.drawable.ic_fav_red);
            holder.favoriteImg.setTag("red");
        } else {
            holder.favoriteImg.setImageResource(R.drawable.ic_fav_grey);
            holder.favoriteImg.setTag("grey");
        }

        return convertView;
    }

    /*Checks whether a particular product exists in SharedPreferences*/
    public boolean checkFavoriteItem(ResturantModel checkProduct) {
        boolean check = false;
        List<ResturantModel> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (ResturantModel product : favorites) {
                if (product.equals(checkProduct)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(ResturantModel product) {
        super.add(product);
        products.add(product);
        notifyDataSetChanged();
    }

    @Override
    public void remove(ResturantModel product) {
        super.remove(product);
        products.remove(product);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView productNameTxt;
        TextView productDescTxt;
        ImageView productImag;
        ImageView favoriteImg;
    }
}