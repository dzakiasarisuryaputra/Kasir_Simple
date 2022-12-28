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

public class AdapterInvoice extends RecyclerView.Adapter<AdapterInvoice.InvoiceViewHolder>{
    static ArrayList<ModelMenuMakanan> modelMenuMakanan;
    AdapterInvoice.OnItemClickListener mListener;
    Context mContext;

    public AdapterInvoice(Context mContext, ArrayList<ModelMenuMakanan> modelMenuMakanan) {
        this.mContext = mContext;
        AdapterInvoice.modelMenuMakanan = modelMenuMakanan;
    }

    public void setOnItemClickListener(AdapterInvoice.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public AdapterInvoice.InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new AdapterInvoice.InvoiceViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInvoice.InvoiceViewHolder holder, int position) {
        ModelMenuMakanan inv = modelMenuMakanan.get(position);
        holder.tvNama.setText(inv.getNama());
        String hargaItem = String.valueOf(Integer.parseInt(String.valueOf(inv.getiPrice()))*Integer.parseInt(String.valueOf(inv.getiAmount())));
        holder.tvPrice.setText(inv.getiPrice() + " x " + inv.getiAmount() +" : Rp. " + hargaItem);
        holder.ivMenuMakanan.setImageResource(inv.getId_gambar());
    }

    @Override
    public int getItemCount() {
        return modelMenuMakanan.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivCart;
//        TextView tvProduct, tvStock, tvprice;
//        RelativeLayout relativeLayout;

        public TextView tvNama,tvCount, tvPrice;
        public ImageView ivMenuMakanan;
        public RelativeLayout rlMenuItem;
        private LinearLayout divQty;

        public InvoiceViewHolder(@NonNull View itemView, final AdapterInvoice.OnItemClickListener listener) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvCount = itemView.findViewById(R.id.tvCount);
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
