package com.example.Kasim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMenuMakanan extends RecyclerView.Adapter<AdapterMenuMakanan.TransViewHolder> {

    static ArrayList<ModelMenuMakanan> modelMenuMakanan;
    OnItemClickListener mListener;
    static Context mContext;
    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    public AdapterMenuMakanan(Context mContext, ArrayList<ModelMenuMakanan> modelMenuMakanan) {
        this.mContext = mContext;
        AdapterMenuMakanan.modelMenuMakanan = modelMenuMakanan;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public AdapterMenuMakanan.TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lis_row_menu_makanan, parent, false);
        View view;
        if (viewType == LIST_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lis_row_menu_makanan, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row_menu_makanan, parent, false);
        }
        return new TransViewHolder(view, mListener);
    }

    @Override
    public int getItemViewType (int position) {
        if (isSwitchView){
            return LIST_ITEM;
        }else{
            return GRID_ITEM;
        }
    }

    public boolean toggleItemViewType () {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterMenuMakanan.TransViewHolder holder, int position) {
        ModelMenuMakanan cart = modelMenuMakanan.get(position);
        holder.tvNama.setText(cart.getNama());
        holder.tvDeskripsi.setText(cart.getDeskripsi());
        holder.tvPrice.setText("Rp. " + cart.getiPrice());
        holder.ivMenuMakanan.setImageResource(cart.getId_gambar());

    }

    @Override
    public int getItemCount() {
        return modelMenuMakanan.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class TransViewHolder extends RecyclerView.ViewHolder {
        int counter = 0;
        public TextView tvNama, tvDeskripsi, tvCount, tvPrice, tvTotalPrice;
        public ImageView ivMenuMakanan, ivMinQty, ivAddQty;
        public RelativeLayout rlMenuItem;
        private LinearLayout divQty;

        public TransViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivMenuMakanan = itemView.findViewById(R.id.ivMenuMakanan);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            rlMenuItem = itemView.findViewById(R.id.rlMenuItem);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            divQty = itemView.findViewById(R.id.divQty);
            ivMinQty = itemView.findViewById(R.id.ivMinQty);
            ivAddQty = itemView.findViewById(R.id.ivAddQty);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);

            ivAddQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
//                            String stockNow = listTrans.get(position).getStock();
//                            if (Integer.parseInt(stockNow) > 0) {
                                if (divQty.getVisibility() != View.VISIBLE) {
                                    divQty.setVisibility(View.VISIBLE);
                                }
                                counter++;
                            modelMenuMakanan.get(position).setiAmount(counter);
                                tvCount.setText("" + counter);
                            tvTotalPrice.setText("Total : " + modelMenuMakanan.get(position).getiPrice()*counter);
//                            }
                        }
                    }
                }
            });

            ivMinQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
//                            String stockNow = listTrans.get(position).getStock();
//                            if (Integer.parseInt(stockNow) > 0) {
                            if (modelMenuMakanan.get(position).getiAmount() > 0){
                                if (divQty.getVisibility() != View.VISIBLE) {
                                    divQty.setVisibility(View.VISIBLE);
                                }
                                counter--;
                                modelMenuMakanan.get(position).setiAmount(counter);
                                tvCount.setText("" + counter);
                                tvTotalPrice.setText("Total : " + modelMenuMakanan.get(position).getiPrice()*counter);
                            }
//                            }
                        }
                    }
                }
            });
        }

    }
}
