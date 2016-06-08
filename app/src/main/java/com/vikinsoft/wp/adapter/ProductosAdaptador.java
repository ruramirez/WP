package com.vikinsoft.wp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.vikinsoft.wp.MainActivity;
import com.vikinsoft.wp.Producto;
import com.vikinsoft.wp.ProductoDetalle;
import com.vikinsoft.wp.R;

import java.util.List;

public class ProductosAdaptador extends RecyclerView.Adapter<ProductosAdaptador.MyViewHolder> {

        private Context mContext;
        private List<Producto> productos;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, count,precio;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                count = (TextView) view.findViewById(R.id.count);
                precio = (TextView) view.findViewById(R.id.precio_card);
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
            final Producto producto = productos.get(position);
            boolean printed = false;
            while(!printed) {
                if (producto.isLoaded()) {
                    holder.title.setText(producto.getNombre());
                    holder.count.setText(producto.getDescripcion());
                    //holder.precio.setText(producto.getPrecio() + " ");
                    holder.precio.setText(producto.getPrecio()+" "+producto.getMoneda().getSimbolo());
                    Glide.with(mContext).load(producto.getFotos().get(0).getUrl()).into(holder.thumbnail);
                    printed=true;
                }
            }

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProductoDetalle.class);
                    Bundle b = new Bundle();
                    b.putInt("id_producto", producto.getId());
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return productos.size();
        }
    }
