package com.vikinsoft.wp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ruben on 2/06/16.
 */
public class FotosProductos {

    private int id = -1;
    private int id_productp ;
    private Context applicationContext;
    private Bitmap imagen;


    public FotosProductos(Bitmap bitmap,Context context){
        this.imagen = bitmap;
        this.applicationContext = context;
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + this.applicationContext.getPackageName()
                + "/Files/Productos");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name

        System.out.println("awbooooooooooooooooo"+mediaStorageDir.getPath());

        String mImageName = this.id + ".jpg";
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void storeImage(Bitmap image) {
        File pictureFile = this.getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public int saveImageOnWeb()
    {
        try {

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = this.getOutputMediaFile();
            System.out.println("Arhicvooooooooooo"+sourceFile.isFile());
            if (sourceFile.isFile()) {

                try {
                    String upLoadServerUri = "http://vikinsoft.com/weplay/index.php?r=fotosProductos/uploadFoto";

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", this.getOutputMediaFile().getAbsolutePath()+"/"+this.id+".jpg");

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            +  this.getOutputMediaFile().getAbsolutePath()+"/"+this.id+".jpg" + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);
                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();

                    System.out.println(serverResponseCode);

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    System.out.println(responseOutput.toString());

                    if (serverResponseCode == 200) {

                        // messageText.setText(msg);
                        //Toast.makeText(ctx, "File Upload Complete.",
                        //      Toast.LENGTH_SHORT).show();

                        // recursiveDelete(mDirectory1);

                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    System.out.println(e.toString());

                }
                // dialog.dismiss();

            } // End else block


        } catch (Exception ex) {
            // dialog.dismiss();
            System.out.println(ex.toString());

            ex.printStackTrace();
        }
        return 1;
    }


    public int saveOnWeb() {

        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=fotosProductos/create");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                            "id_productp=" + this.id_productp
                    ;
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
                this.storeImage(this.imagen);
                this.saveImageOnWeb();
                return this.id;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(responseOutput.toString());

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
            return this.saveOnWeb();
        }
        if(integers[0] == 3)
        {
            return this.saveImageOnWeb();
        }
        return -1;
    }


    public void setId_productp(int id_productp) {
        this.id_productp = id_productp;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
