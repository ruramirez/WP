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
    public List<EstadoProducto> estados;
    public listaProductos listaProductos;
    public List<Usuario> usuarios;
    public List<Chat> chats = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public GlobalClass() {

        this.categorias = Categoria.getAllCategorias();
        this.monedas = Moneda.getAllMonedas();
        this.estados = EstadoProducto.getAllEstados();
        this.chats=new ArrayList<>();
        chatUpdateTread.start();

    }

    Thread chatUpdateTread = new Thread()
    {
        @Override
        public void run() {
            while(true) {
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GlobalClass appstate = (GlobalClass) getApplicationContext();
                for (Chat chat : appstate.chats) {
                    chat.update();
                }
            }

        }
    };

    public Chat getChatByID(int id)
    {
        for (Chat chat : this.chats) {
            if (chat.getId() ==id) {
                return chat;
            }
        }
        return new Chat(id);
    }

    public ArrayList<Chat> getChatByProducto(Producto producto) {

        ArrayList<Chat> selectedChats = new ArrayList<>();
        for (Chat chat : this.chats) {
            if (producto.getId()== chat.getProducto().getId()) {
                selectedChats.add(chat);
            }
        }
        return selectedChats;
    }


    public Chat getChatByComprador(Usuario comprador,Producto producto) {
        for (Chat chat : this.chats) {
            if (chat.getComprador().getId() == comprador.getId() && producto.getId()== chat.getProducto().getId()) {
                return chat;
            }
        }
        Chat chat = new Chat(producto, comprador,getApplicationContext());
        this.chats.add(chat);
        return chat;
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

    public void searchByUsuario(){
        this.listaProductos = new listaProductos(this);

        try {
            listaProductos.execute(3).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void searchByVendiendo(){
        this.listaProductos = new listaProductos(this);

        try {
            listaProductos.execute(3).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void searchByVendidos(){
        this.listaProductos = new listaProductos(this);

        try {
            listaProductos.execute(3).get();
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
        return  new Producto(id,getApplicationContext(),this);

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

    public EstadoProducto getElementoByID(int id)
    {
        for (EstadoProducto estadoProducto : this.estados) {
            if(estadoProducto.getId()== id)
                return estadoProducto;
        }
        return null;
    }

}