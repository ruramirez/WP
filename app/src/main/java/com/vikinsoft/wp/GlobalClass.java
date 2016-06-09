package com.vikinsoft.wp;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.vikinsoft.wp.Usuario;

import java.util.ArrayList;
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
    public listaProductos listaProductos;
    public List<Usuario> usuarios;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public GlobalClass() {

        this.categorias = Categoria.getAllCategorias();
        this.monedas = Moneda.getAllMonedas();
    }

    public void loadConfig(){
        this.usuarios = new ArrayList<>();
        this.listaProductos = new listaProductos(this);

        try {
            listaProductos.execute(2).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Usuario getUsuariobyID(int id)
    {
        for (Usuario usuario : this.usuarios) {
            if(usuario.getId()== id)
                if(usuario.isLoaded()) {
                    return usuario;
                }
                else
                {
                    return new Usuario(-1,this);
                }
        }
        return   new Usuario(-1,this);


    }

    public Producto getProductobyID(int id) {
        for (Producto producto : listaProductos.getProductos()) {
            if(producto.getId()== id)
                return producto;
        }
        return  null;

    }

    public Categoria getCategoriaID(int id) {
        for (Categoria categoria : this.categorias) {
            if(categoria.getId()== id)
                return categoria;
        }
        return null;
    }

    public Moneda getMonedabyID(int id) {
        for (Moneda moneda : this.monedas) {
            if(moneda.getId()== id)
                return moneda;
        }
        return new Moneda();
    }

}