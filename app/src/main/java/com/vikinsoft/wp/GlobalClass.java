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

    public GlobalClass()
    {
        categorias = Categoria.getAllCategorias();
        monedas = Moneda.getAllMonedas();
        
    }
}