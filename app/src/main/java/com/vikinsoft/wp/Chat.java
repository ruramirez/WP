package com.vikinsoft.wp;

        import android.os.AsyncTask;

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
 * Created by ricardo on 20/06/16.
 */
public class Chat extends AsyncTask<Integer, Void, Integer> {

    private int id=-1;
    private Producto producto;
    private Usuario comprador;
    private Usuario vendedor;
    private List<Mensaje> mensajes = new ArrayList<>();


    public Chat( Producto producto, Usuario comprador)
    {
        this.producto=producto;
        this.comprador=comprador;
        this.vendedor=producto.getUsuario();
        try {
            this.id=this.execute(1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void addMensaje(String texto)
    {
        long unixTime = System.currentTimeMillis() / 1000L;
        Mensaje mensaje = new Mensaje(this.vendedor,this.comprador,this.producto,texto,1,unixTime);
        this.mensajes.add(mensaje);
    }


    private int save(){
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=chats/save");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                            "id_producto=" + this.producto.getId() +
                            "&id_comprador=" + this.comprador.getId();
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
                return Integer.parseInt(jsonObject.getString("id"));

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
        return this.id;
    }


    @Override
    protected Integer doInBackground(Integer... integers) {
        if(integers[0] == 1)
        {
            return this.save();
        }


        return -1;
    }


    public int getId() {
        return id;
    }

    public Producto getProducto() {
        return producto;
    }

    public Usuario getComprador() {
        return comprador;
    }
}
