package com.malav.medicinesontheway.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.activity.AddPrescriptionActivity;
import com.malav.medicinesontheway.model.Store;
import com.malav.medicinesontheway.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 21/06/17.
 */

public class MyOrdersAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Store> storeList, filteredList;
    private String TAG = "StoreListAdapter";
    private ItemFilter mFilter = new ItemFilter();

    public MyOrdersAdapter(Context context, ArrayList<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
        this.filteredList = storeList;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int location) {
        return filteredList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_store_list, null);
        }

        Store store = filteredList.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);

        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);

        name.setText(store.getStoreName());
        address.setText(store.getAddress());

        if(CommonUtils.isNotNull(store.getPic())) {
            Log.d(TAG, "getView: " + store.getPic());
            //Load image via Glide
            Glide.clear(profilePic);
            if(CommonUtils.isNotNull(store.getPic())){
                profilePic.setVisibility(View.VISIBLE);
                Glide.with(context).load(store.getPic()).crossFade(500).into(profilePic);
            }
        }else{
            profilePic.setImageResource(R.drawable.ic_person);
        }

        ll.setOnClickListener(new MyOnClickListener(context,position,store.getStoreId(),store.getStoreName()));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class MyOnClickListener implements View.OnClickListener {
        int position;
        String id, name;
        Context context;

        private MyOnClickListener(Context context, int position, String id, String name) {
            this.context = context;
            this.position = position;
            this.id = id;
            this.name = name;
        }

        @Override
        public void onClick(View v) {

            /*Intent i = new Intent(context, AddPrescriptionActivity.class);
            i.putExtra("storeId", id);
            i.putExtra("storeName", name);
            context.startActivity(i);*/
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Store> list = storeList;

            int count = list.size();
            final ArrayList<Store> nlist = new ArrayList<>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                Store filterableHash = list.get(i);
                filterableString = filterableHash.getStoreName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableHash);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Store>) results.values;
            notifyDataSetChanged();
        }

    }
}
