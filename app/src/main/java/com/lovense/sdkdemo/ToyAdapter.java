package com.lovense.sdkdemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovense.sdklibrary.Toy;

import java.util.List;

/**
 * Created  on 2019/5/13 17:54
 *
 * @author zyy
 */
public class ToyAdapter extends RecyclerView.Adapter<ToyAdapter.ViewHolder> {

    Context context;
    List<Toy> toyList ;

    public ToyAdapter(Context context, List<Toy> toyList) {
        this.context = context;
        this.toyList = toyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_toy , null ,false);
        return new ViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Toy toy = toyList.get(i);
        viewHolder.name.setText("name："+toy.getDeviceName());
        viewHolder.id.setText("ID："+toy.getUuid());
        viewHolder.rssi.setText("RSSI："+toy.getRssi());
        // 0 设备已断开 1:设备正在连接 2：设备已连接 3：设备正在断开
        viewHolder.status.setText("status："+(toy.getStatus()==1?"connected":"not connected"));

        viewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ToyActivity.class);
                intent.putExtra("address",toy.getAddress());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return toyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        TextView rssi;
        TextView status;
        LinearLayout ll;
        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tv_id);
            name = itemView.findViewById(R.id.tv_name);
            rssi = itemView.findViewById(R.id.tv_rssi);
            status = itemView.findViewById(R.id.tv_statu);
            ll = itemView.findViewById(R.id.ll_toy);
        }
    }


}
