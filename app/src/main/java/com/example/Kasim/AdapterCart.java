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

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {

    static ArrayList<ModelMenuMakanan> modelMenuMakanan;
    OnItemClickListener mListener;
    Context mContext;

    public AdapterCart(Context mContext, ArrayList<ModelMenuMakanan> modelMenuMakanan) {
        this.mContext = mContext;
        AdapterCart.modelMenuMakanan = modelMenuMakanan;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public AdapterCart.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_cart, parent, false);
        return new CartViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.CartViewHolder holder, int position) {
        ModelMenuMakanan cart = modelMenuMakanan.get(position);
        holder.tvNama.setText(cart.getNama());
        String hargaItem = String.valueOf(Integer.parseInt(String.valueOf(cart.getiPrice()))*Integer.parseInt(String.valueOf(cart.getiAmount())));
        holder.tvPrice.setText(cart.getiPrice() + " x " + cart.getiAmount() +" : Rp. " + hargaItem);
        holder.ivMenuMakanan.setImageResource(cart.getId_gambar());
    }

    @Override
    public int getItemCount() {
        return modelMenuMakanan.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNama, tvCount, tvPrice;
        public ImageView ivMenuMakanan;
        public RelativeLayout rlMenuItem;
        private LinearLayout divQty;

        public CartViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            ivMenuMakanan = itemView.findViewById(R.id.ivMenuMakanan);
            rlMenuItem = itemView.findViewById(R.id.rlMenuItem);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            divQty = itemView.findViewById(R.id.divQty);

            divQty.setVisibility(View.GONE);

            rlMenuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
