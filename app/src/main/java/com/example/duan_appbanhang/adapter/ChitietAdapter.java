package com.example.duan_appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan_appbanhang.R;
import com.example.duan_appbanhang.mode.Item;
import com.example.duan_appbanhang.utils.Utils;

import java.util.List;

public class ChitietAdapter extends RecyclerView.Adapter<ChitietAdapter.MyViewHolder> {


    Context context;
    List<Item> itemList;

    public ChitietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txttensp.setText(item.getTensp()+"");
        holder.txtsoluong.setText("Số lượng: "+item.getSoluong()+"");
        if(item.getHinhanh().contains("http")){
            Glide.with(context).load(item.getHinhanh()).into(holder.imgchitiet);
        }else {
            String hinh = Utils.BASE_URL+"imges/"+item.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imgchitiet);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgchitiet;
        TextView txttensp, txtsoluong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgchitiet = itemView.findViewById(R.id.item_imgchitiet);
            txttensp = itemView.findViewById(R.id.item_tenspchitiet);
            txtsoluong = itemView.findViewById(R.id.item_soluongchitiet);
        }
    }
}
