package com.example.tesshared.Produk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tesshared.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProdukRecyclerViewAdapter extends RecyclerView.Adapter<ProdukRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ResultProduk> results;

    public ProdukRecyclerViewAdapter(Context context,List<ResultProduk> results) {
        this.context = context;
        this.results = results;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_produk, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ResultProduk result = results.get(position);
        holder.textViewKode.setText(result.getKode());
        holder.textViewUkuran.setText(result.getKode());
        holder.textViewHarga.setText(String.valueOf(result.getHarga()));
        holder.textViewKategori.setText(result.getKategori());
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProdukDetail.class);
                i.putExtra("id", result.getId());
                context.startActivity(i);
            }
        });

//        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mIntent = new Intent(view.getContext(), UpdateKampus.class);
//                mIntent.putExtra("id", results.get(position).getId());
//                mIntent.putExtra("kode", results.get(position).getKode());
//                mIntent.putExtra("ukuran", results.get(position).getUkuran());
//                mIntent.putExtra("harga", results.get(position).getHarga());
//                mIntent.putExtra("kategori", results.get(position).getKategori());
//                view.getContext().startActivity(mIntent);
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mIntent = new Intent(view.getContext(), ProdiActivity.class);
//                mIntent.putExtra("id", results.get(position).getId());
//                mIntent.putExtra("nama_univ", results.get(position).getKampus());
//                view.getContext().startActivity(mIntent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewKode;
        TextView textViewUkuran;
        TextView textViewHarga;
        TextView textViewKategori;
        ImageView imageViewImage;
        ImageView edit_btn;
        CardView mCard;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewKode = itemView.findViewById(R.id.kode);
            textViewUkuran = itemView.findViewById(R.id.ukuran);
            textViewHarga = itemView.findViewById(R.id.harga);
            textViewKategori = itemView.findViewById(R.id.kategori);
            imageViewImage = itemView.findViewById(R.id.image);
            mCard = itemView.findViewById(R.id.card1);
        }
    }
}
