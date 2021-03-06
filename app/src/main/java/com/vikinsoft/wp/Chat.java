package com.vikinsoft.wp;

        import android.content.Context;
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
 * Created by ricardo on 20/06/16.
 */
public class Chat extends AsyncTask<Integer, Void, Integer> {

    private int id=-1;
    private Producto producto;
    private Usuario comprador;
    private Usuario vendedor;
    private  int nuevos =0;



    private List<Mensaje> mensajes = new ArrayList<>();
    private Context contexto;


    public List<Mensaje> getMensajes() {
        return this.mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Chat(Producto producto, Usuario comprador, Context applicationContext)
    {
        this.producto=producto;
        this.comprador=comprador;
        this.vendedor=producto.getUsuario();
        this.contexto = applicationContext;
        try {
            this.id=this.execute(1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Chat(int id,Producto producto,Usuario comprador)
    {
        this.id=id;
        this.producto = producto;
        this.vendedor = producto.getUsuario();
        this.comprador = comprador;
    }


    public Chat(int id)
    {
        this.id = id;
        //hacer el metodo load
    }

    public int getNuevos() {
        return nuevos;
    }

    public void setNuevos(int nuevos) {
        this.nuevos = nuevos;
    }

    public void update()
    {
        ChatUpdater chatUpdater = new ChatUpdater(this);
        if(this.mensajes.size()!= chatUpdater.getUpdaterMensajes().size() )
        {
            this.mensajes= chatUpdater.getUpdaterMensajes();
            this.nuevos=1;
        }

    }

    public void addMensaje(String texto)
    {
        long unixTime = System.currentTimeMillis() / 1000L;
        Mensaje mensaje = new Mensaje(this,this.vendedor,this.comprador,this.producto,texto,1,unixTime);
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
                //System.out.println("Respuesta de chat salvado " +responseOutput.toString());
                this.id=Integer.parseInt(jsonObject.getString("id"));
                JSONArray msjs = jsonObject.getJSONArray("mensajes");
                for (int j = 0; j < msjs.length(); j++) {

                    this.mensajes.add(new Mensaje(Integer.parseInt(msjs.getJSONObject(j).getString("id")),
                            this.vendedor,
                            this.comprador,
                            this.producto,
                            msjs.getJSONObject(j).getString("mensaje"),
                            Integer.parseInt(msjs.getJSONObject(j).getString("estado")),
                            Long.parseLong(msjs.getJSONObject(j).getString("timestamp")) ));
                }
                //areglo de jsons

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

    private class ChatUpdater extends AsyncTask<Integer, Void, Integer>
    {
        private Chat chat;
        private List<Mensaje> updaterMensajes = new ArrayList<>();
        public ChatUpdater (Chat chat)
        {
            this.chat= chat
            ;
            try {
                this.execute(1).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        private int update(){
            try {
                URL url = new URL("http://vikinsoft.com/weplay/index.php?r=chats/getMensajes");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters =
                        "id_chat=" + this.chat.getId() +
                                "&cantidad=" + this.chat.mensajes.size();
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
                    //System.out.println("Respuesta de chat salvado " +responseOutput.toString());
                    if(Integer.parseInt(jsonObject.getString("update"))==1) {
                        JSONArray msjs = jsonObject.getJSONArray("mensajes");
                        updaterMensajes = new ArrayList<>();
                        for (int j = 0; j < msjs.length(); j++) {
                            updaterMensajes.add(new Mensaje(Integer.parseInt(msjs.getJSONObject(j).getString("id")),
                                    this.chat.vendedor,
                                    this.chat.comprador,
                                    this.chat.producto,
                                    msjs.getJSONObject(j).getString("mensaje"),
                                    Integer.parseInt(msjs.getJSONObject(j).getString("estado")),
                                    Long.parseLong(msjs.getJSONObject(j).getString("timestamp"))));
                        }
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
            return this.chat.id;
        }

        public List<Mensaje> getUpdaterMensajes() {
            return updaterMensajes;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            if(integers[0] == 1)
            {
                return this.update();
            }


            return -1;
        }
    }

}
