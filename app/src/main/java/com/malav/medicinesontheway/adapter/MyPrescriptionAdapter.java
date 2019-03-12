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
import com.malav.medicinesontheway.model.Prescription;
import com.malav.medicinesontheway.model.Store;
import com.malav.medicinesontheway.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 21/06/17.
 */

public class MyPrescriptionAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Prescription> prescriptionList, filteredList;
    private String TAG = "StoreListAdapter";
    private ItemFilter mFilter = new ItemFilter();

    public MyPrescriptionAdapter(Context context, ArrayList<Prescription> prescriptionList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
        this.filteredList = prescriptionList;
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
            convertView = inflater.inflate(R.layout.item_prescription_list, null);
        }

        Prescription prescription = filteredList.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);

        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);

        name.setText(prescription.getPres_name());

        if(CommonUtils.isNotNull(prescription.getPres_url())) {
            Log.d(TAG, "getView: " + prescription.getPres_url());
            //Load image via Glide
            Glide.clear(profilePic);
            if(CommonUtils.isNotNull(prescription.getPres_url())){
                profilePic.setVisibility(View.VISIBLE);
                Glide.with(context).load(prescription.getPres_url()).crossFade(500).into(profilePic);
            }
        }else{
            profilePic.setImageResource(R.drawable.ic_person);
        }

        ll.setOnClickListener(new MyOnClickListener(context,position,prescription.getPres_id(),prescription.getPres_url()));

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

            final List<Prescription> list = prescriptionList;

            int count = list.size();
            final ArrayList<Prescription> nlist = new ArrayList<>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                Prescription filterableHash = list.get(i);
                filterableString = filterableHash.getPres_name();
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
            filteredList = (ArrayList<Prescription>) results.values;
            notifyDataSetChanged();
        }

    }
}