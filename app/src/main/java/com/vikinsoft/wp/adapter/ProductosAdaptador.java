package com.vikinsoft.wp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ruben on 3/06/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vikinsoft.wp.Producto;
import com.vikinsoft.wp.R;

import java.util.List;

public class ProductosAdaptador extends RecyclerView.Adapter<ProductosAdaptador.MyViewHolder> {


    /**
     * Created by Ravi Tamada on 18/05/16.
     */

        private Context mContext;
        private List<Producto> productos;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, count;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                count = (TextView) view.findViewById(R.id.count);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            }
        }


        public ProductosAdaptador(Context mContext, List<Producto> productos) {
            this.mContext = mContext;
            this.productos = productos;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.productos_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Producto producto = productos.get(position);
            holder.title.setText(producto.getNombre());
            holder.count.setText(producto.getDescripcion());

            // loading album cover using Glide library
            //Glide.with(mContext).load(producto.getImagen()).into(holder.thumbnail);

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Aqui nos vamos a cada uno de los productos en detalle
                }
            });
        }

        @Override
        public int getItemCount() {
            return productos.size();
        }
    }
