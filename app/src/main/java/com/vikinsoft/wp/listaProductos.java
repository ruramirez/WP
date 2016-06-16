package com.vikinsoft.wp;

import android.content.Context;
import android.os.AsyncTask;

import com.vikinsoft.wp.Producto;

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

/**
 * Created by ruben on 6/06/16.
 */
public class listaProductos extends AsyncTask<Integer, Void, Integer> {
    List<Producto> productos = new ArrayList<>();
    private Usuario usuario;
    private Context applicationContext;

    public listaProductos(Context applicationContext)
    {
        this.applicationContext=applicationContext;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        if(integers[0] == 2)
        {
            return this.loadAllWeb();
        }else if (integers[0] == 3) {
            return this.loadByUsuario();
        }
        return -1;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int loadAllWeb() {
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=productos/loadAll");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "id_usuario=0" ;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
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
                System.out.println(responseOutput.toString());
                jsonObject = new JSONArray(responseOutput.toString());
                if (responseOutput.toString().isEmpty() == false) {
                    //
                    for(int i=0;i<jsonObject.length();i++){
                        JSONObject obj3=jsonObject.getJSONObject(i);
                        this.productos.add(new Producto(Integer.parseInt(obj3.getString("id")),this.applicationContext,(GlobalClass) this.applicationContext));
                    }
                    return 1;
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

    public int loadByUsuario() {
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=usuarios/getProductos");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "usuario=5" ;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
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
            JSONArray jsonObject = null;
            try {
                System.out.println(responseOutput.toString());
                jsonObject = new JSONArray(responseOutput.toString());
                if (responseOutput.toString().isEmpty() == false) {
                    //
                    for(int i=0;i<jsonObject.length();i++){
                        JSONObject obj3=jsonObject.getJSONObject(i);
                        this.productos.add(new Producto(Integer.parseInt(obj3.getString("id")),this.applicationContext,(GlobalClass)this.applicationContext));
                    }
                    return 1;
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



}
