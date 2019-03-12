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
import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.activity.ViewOrderActivity;
import com.malav.medicinesontheway.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 21/06/17.
 */

public class MyOrdersAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Order> orderList, filteredList;
    private String TAG = "StoreListAdapter";
    private ItemFilter mFilter = new ItemFilter();

    public MyOrdersAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.filteredList = orderList;
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

        Order order = filteredList.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);

        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);

        name.setText(order.getStoreName());
        address.setText(order.getStoreAddress());

        ll.setOnClickListener(new MyOnClickListener(context,position,order.getOrderId(),order.getStoreName()));

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

            Intent i = new Intent(context, ViewOrderActivity.class);
            i.putExtra("orderId", id);
            i.putExtra("storeName", name);
            context.startActivity(i);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Order> list = orderList;

            int count = list.size();
            final ArrayList<Order> nlist = new ArrayList<>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                Order filterableHash = list.get(i);
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
            filteredList = (ArrayList<Order>) results.values;
            notifyDataSetChanged();
        }

    }
}
