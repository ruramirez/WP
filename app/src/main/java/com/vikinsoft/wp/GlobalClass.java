package com.vikinsoft.wp;


import android.app.Application;

import com.vikinsoft.wp.Usuario;

import java.util.List;

/**
 * Created by LikeaLap on 5/13/2016.
 */
public class GlobalClass extends Application {

    public Usuario usuario;
    public List<Categoria> categorias;
    public List<Moneda> monedas;
    public List<EstadoProducto> estadoProductos;

    public GlobalClass()
    {
        categorias = Categoria.getAllCategorias();
        monedas = Moneda.getAllMonedas();
        //this.estadoProductos=EstadoProducto.
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