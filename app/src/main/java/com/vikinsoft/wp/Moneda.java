package com.vikinsoft.wp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ricardo on 23/05/16.
 */
public class Moneda extends AsyncTask<Integer, Void, Integer> {

    private  int id=-1;
    private String nombre="";
    private String simbolo="";
    private boolean loaded=false;

    public Moneda()
    {
        this.loaded=false;
    }

    public Moneda(int id) {
        this.id=id;
        this.execute(2);
    }

    public Moneda(int id, String nombre, String simbolo) {
        this.id=id;
        this.nombre=nombre;
        this.simbolo= simbolo;
        this.loaded=true;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo()
    {
        return simbolo;
    }

    public void setSimbolo(String simbolo){
        this.simbolo = simbolo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static List<Moneda> getAllMonedas() {
        List<Moneda> categorias = new ArrayList<>();
         WebCategorias web = new WebCategorias();
        try {
            web.execute(1).get();
            return web.monedas;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return categorias;

    }

    public int loadWeb() {
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=monedas/load");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "id=" + this.id;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
            int responseCode = connection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(responseOutput.toString());
                this.id=Integer.parseInt(jsonObject.getString("id"));
                if (this.id != -1) {
                    this.nombre = jsonObject.getString("nombre");
                    this.simbolo= jsonObject.getString("simbolo");
                    this.loaded=true;
                    return this.id;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    protected Integer doInBackground(Integer... integers) {
        if(integers[0] == 2)
        {
            return this.loadWeb();
        }

        return -1;
    }

    private static class WebCategorias extends AsyncTask<Integer, Void, Integer>
    {

        private Moneda categoria;
        public List<Moneda> monedas = new ArrayList<>();


        private int getAll()
        {
            try {
                URL url = new URL("http://vikinsoft.com/weplay/index.php?r=monedas/getAll");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //String urlParameters =
                //        "id=" + this..id;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                //dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                JSONArray jsonObject = null;
                try {
                    jsonObject = new JSONArray(responseOutput.toString());
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject row = jsonObject.getJSONObject(i);
                        this.monedas.add(new Moneda(row.getInt("id"),row.getString("nombre"),row.getString("simbolo")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            if (integers[0] == 1) {
                return this.getAll();
            }
            return -1;
        }
    }

}
