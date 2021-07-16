package com.project.cmadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.project.cmadmin.R;
import com.project.cmadmin.fragments.HomeFragment;
import com.project.cmadmin.fragments.HomeFragmentDirections;
import com.project.cmadmin.models.Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeAdapter extends FirebaseRecyclerAdapter<Order, HomeAdapter.MyViewHolder> {

    private Context context;

    public HomeAdapter(@NonNull FirebaseRecyclerOptions<Order> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Order model) {
        RelativeLayout lytOrder = holder.lytOrder;
        TextView tvOrderNo = holder.tvOrderNo;
        TextView tvOrderDate = holder.tvOrderDate;
        TextView tvOrderStatus = holder.tvOrderStatus;

        String [] split = model.getOrderno_status().split("_");
        String status = split[1].toString();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formatdate = "";
        try {
            Date date = format.parse(model.getOrderdate());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatter.format(date);
            formatdate = strDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvOrderNo.setText(model.getOrderno());
        if (status.equals("0")) {
            tvOrderStatus.setText("New");
            tvOrderStatus.setTextColor(context.getResources().getColor(R.color.dark_gray));
        } else if (status.equals("1")){
            tvOrderStatus.setText("Approved");
            tvOrderStatus.setTextColor(context.getResources().getColor(R.color.dark_green));
        } else if (status.equals("2")){
            tvOrderStatus.setText("Rejected");
            tvOrderStatus.setTextColor(context.getResources().getColor(R.color.red));
        }

        tvOrderDate.setText(formatdate);

        lytOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refKey = getRef(position).getKey();

                HomeFragmentDirections.NavigateToViewOrder action =
                        HomeFragmentDirections.navigateToViewOrder(refKey);
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_orders, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lytOrder;
        TextView tvOrderNo, tvOrderDate, tvOrderStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lytOrder = itemView.findViewById(R.id.lyt_order);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
        }
    }
}
