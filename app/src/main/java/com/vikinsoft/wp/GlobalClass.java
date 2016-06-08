package com.vikinsoft.wp;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.vikinsoft.wp.Usuario;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by LikeaLap on 5/13/2016.
 */
public class GlobalClass extends MultiDexApplication {

    public Usuario usuario;
    public List<Categoria> categorias;
    public List<Moneda> monedas;
    public List<EstadoProducto> estadoProductos;
    public List<Producto> productos;
    public listaProductos listaProductos = new listaProductos();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public GlobalClass()
    {
        categorias = Categoria.getAllCategorias();
        monedas = Moneda.getAllMonedas();


        try {
            listaProductos.execute(2).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        productos = listaProductos.getProductos();


        //this.estadoProductos=EstadoProducto.
    }


    public Producto getProductobyID(int id)
    {
        for (Producto producto : this.productos) {
            if(producto.getId()== id)
                return producto;
        }
        return  null;

    }

    public Categoria getCategoriaID(int id)
    {
        for (Categoria categoria : this.categorias) {
            if(categoria.getId()== id)
                return categoria;
        }
        return null;
    }

    public Moneda getMonedabyID(int id)
    {
        for (Moneda moneda : this.monedas) {
            if(moneda.getId()== id)
                return moneda;
        }
        return null;

    }

}