package com.example.duan_appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_appbanhang.Interface.ItemClickDeleteListener;
import com.example.duan_appbanhang.R;
import com.example.duan_appbanhang.mode.DonHang;
import com.example.duan_appbanhang.utils.Utils;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listDonhang;
    ItemClickDeleteListener deleteListener;

    public DonHangAdapter(Context context, List<DonHang> listDonhang,ItemClickDeleteListener itemClickDeleteListener) {
        this.context = context;
        this.listDonhang = listDonhang;
        this.deleteListener = itemClickDeleteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listDonhang.get(position);
        holder.txtDonhang.setText("Đơn hàng: " + donHang.getId());
        holder.txttrangthai.setText(Utils.statusOrder(donHang.getTrangthai()));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteListener.onClickDelete(donHang.getId());
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL, false

        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //adapte chi tiet
        ChitietAdapter chitietAdapter = new ChitietAdapter(context,donHang.getItem());
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setAdapter(chitietAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);


    }

    @Override
    public int getItemCount() {
        return listDonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDonhang,txttrangthai;
        RecyclerView reChitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDonhang = itemView.findViewById(R.id.idDonhang);
            txttrangthai = itemView.findViewById(R.id.idTrangthai);
            reChitiet = itemView.findViewById(R.id.recycleview_chitiet);
        }
    }
}
