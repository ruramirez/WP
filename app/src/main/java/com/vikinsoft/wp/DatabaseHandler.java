package com.vikinsoft.wp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vikinsoft.wp.Usuario;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WePlay";

    // Contacts table name
    private static final String TABLE_usuarios = "usuarios";

    private static final String TABLE_categorias = "categorias";

    private String CREATE_LOCATIONS_TABLE = "CREATE TABLE \"usuarios\" (\n" +
            "    \"id\" INTEGER PRIMARY KEY NOT NULL,\n" +
            "    \"nombre\" TEXT NOT NULL,\n" +
            "    \"email\" TEXT NOT NULL,\n" +
            "    \"password\" TEXT NOT NULL,\n" +
            "    \"facebook\" INTEGER NOT NULL,\n" +
            "    \"emailValido\" INTEGER NOT NULL,\n" +
            "    \"google\" INTEGER NOT NULL,\n" +
            "    \"latitud\" REAL NOT NULL,\n" +
            "    \"longitud\" REAL NOT NULL,\n" +
            "    \"foto\" INTEGER NOT NULL\n" +
            ")";

    private  String CREATE_CATEGORIES_TABLE = "CREATE TABLE \"categorias\" (\n" +
            "    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "    \"id_categoria\" INTEGER NOT NULL,\n" +
            "    \"id_usuario\" INTEGER NOT NULL\n" +
            ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("DB","CREATING Usuarios");
        db.execSQL(CREATE_LOCATIONS_TABLE);
        Log.w("DB","CREATING Categorias");
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.w("DB","UPDATING");
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        // Create tables again
        onCreate(db);
        //db.close();
    }

    boolean saveUsuario(Usuario usuario) {

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            DecimalFormat df = new DecimalFormat("0.000000");
            ContentValues values = new ContentValues();
            values.put("id", usuario.getId());
            values.put("nombre", usuario.getNombre());
            values.put("email", usuario.getEmail());
            values.put("password", usuario.getPassword());
            values.put("facebook", usuario.getFacebook());
            values.put("emailValido", usuario.getEmailValido());
            values.put("google", usuario.getGoogle());
            values.put("latitud", usuario.getLatitud());
            values.put("longitud", usuario.getLongitud());
            values.put("foto", usuario.getFoto());
            //String insert_query = "insert into locations (id,date,latitude,longitude,linked) values ("+date+" ,"+df.format(latitude)+" , "+df.format(longitude)+" , 0);";

            db.insert(TABLE_usuarios, null, values);
            db.close(); // Closing database connection
            return  true;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            //db.insert(TABLE_LOCATIONS, null, values);
            return false;
        }
    }

    public Usuario getUsuario(Activity activity)
    {
        Usuario usuario =new Usuario(this.context,activity);

        try
        {
            String selectQuery = "SELECT * from  " + TABLE_usuarios;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if(cursor.getCount()>0) {
                if (cursor.moveToFirst()) {
                    do {
                        usuario = new Usuario(
                                Integer.parseInt(cursor.getString(0)),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                Integer.parseInt(cursor.getString(4)),
                                Integer.parseInt(cursor.getString(5)),
                                Integer.parseInt(cursor.getString(6)),
                                Double.parseDouble(cursor.getString(7)),
                                Double.parseDouble(cursor.getString(8)),
                                Integer.parseInt(cursor.getString(9)),
                                this.context,
                                activity);
                        // Adding contact to list
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return usuario;

    }

    public int updateusuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("id", usuario.getId());
        values.put("nombre", usuario.getNombre());
        values.put("email", usuario.getEmail());
        values.put("password", usuario.getPassword());
        values.put("facebook", usuario.getFacebook());
        values.put("emailValido", usuario.getEmailValido());
        values.put("google", usuario.getGoogle());
        values.put("latitud", usuario.getLatitud());
        values.put("longitud", usuario.getLongitud());
        values.put("foto", usuario.getFoto());
        // updating row
        return db.update(TABLE_usuarios, values, "id" + " = ?",
                new String[] { String.valueOf(usuario.getId()) });
    }

    public void deleteUsuaro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_usuarios, "id" + " = ?",
                new String[] {String.valueOf(id)});
        db.close();
    }

    boolean saveFavorito(Usuario usuario,Categoria categoria) {


        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_usuario", usuario.getId());
            values.put("id_categoria", categoria.getId());

            db.insert(TABLE_categorias, null, values);
            db.close(); // Closing database connection
            return  true;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            //db.insert(TABLE_LOCATIONS, null, values);
            return false;
        }
    }

    boolean isFavorito(Usuario usuario,Categoria categoria)
    {
        try
        {
            String selectQuery = "SELECT  * FROM " + TABLE_categorias+" WHERE id_usuario="+usuario.getId()
                    +" AND id_categoria="+categoria.getId();

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.getCount() > 0) {
                return  true;
            }

        }catch (Exception e)
        {
            return  false;
        }
        return false;
    }

    public int updateCategorias(Usuario usuario , Categoria categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", categoria.getId());
        values.put("id_categoria", usuario.getNombre());
        values.put("email", usuario.getEmail());

        // updating row
        return db.update(TABLE_usuarios, values, "id" + " = ?",
                new String[] { String.valueOf(usuario.getId()) });
    }

    public void truncateDb()
    {
        try
        {
            String selectQuery = "TRUNCATE " + TABLE_categorias;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list


        }catch (Exception e)
        {
            System.out.println("Error en el truncado "+e);
        }
    }

    public int deleteFavorito(Usuario usuario , Categoria categoria) {
        try
        {
            String selectQuery = "DELETE FROM " + TABLE_categorias+" WHERE id_usuario="+usuario.getId()
                    +" AND id_categoria="+categoria.getId();

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.getCount() > 0) {
                return 1;
            }

        }catch (Exception e)
        {
            System.out.println("Error en el borrado "+e);
            return 0;
        }

        return 0;
    }




    /**
     * All CRUD(Create, Read, Update, Delete) Operations


     public String geteMinDate(Context applicationContext)
    {

        String minDate ="";
        String selectQuery = "SELECT  min(date) FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                minDate=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if(minDate== null)
        {
            minDate="0";
        }
        return minDate;
    }

    // Getting All Contacts
    public List<Location> getActualLocations(Context applicationContext,Long Date ) {
        List<Location> locationList = new ArrayList<Location>();
        // Select All Query
        try
        {
            String selectQuery = "SELECT  longitude, latitude, date FROM " + TABLE_LOCATIONS+ " where date < "+Long.toString(Date);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Location location = new Location(Double.parseDouble(cursor.getString(0)),Double.parseDouble(cursor.getString(1)),Long.valueOf(cursor.getString(2)),0,applicationContext);
                    // Adding contact to list
                    locationList.add(location);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (Exception e)
        {
        }
        // return contact list
        return locationList;
    }

    public void deleteAverageLocations(Long Date , Context applicationContext)
    {
        try
        {
            //Date = Date + 2000;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_LOCATIONS, " date " + " < ?",
                    new String[]{Date.toString()});
            db.close();
        }
        catch (Exception e)
        {
            //db.insert(TABLE_LOCATIONS, null, values);
        }

    }


    public Location getAverageLocation(Long Date , Context applicationContext)
    {

        String selectQuery = "SELECT round(avg(latitude),6), round(avg(longitude),6) FROM " + TABLE_LOCATIONS + " where date <= "+Long.toString(Date+2000)+" AND date >= "+Long.toString(Date)+" ";
        //Log.w("myApp", selectQuery);
        Location location = new Location(new Long("0"),new Long("0"),applicationContext);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            try
            {
                location = new Location(Double.parseDouble(cursor.getString(1)),Double.parseDouble(cursor.getString(0)),Date,0,applicationContext);
            }
            catch (Exception e)
            {
                location = new Location(new Long("0"),new Long("0"),applicationContext);
            }
        }
        cursor.close();
        db.close();
        return location;
    }


    // Getting All Contacts
    public List<Location> getUnlinkedLocations(Context applicationContext) {
        List<Location> locationList = new ArrayList<Location>();
        // Select All Query
        String selectQuery = "SELECT  longitude, latitude, date FROM " + TABLE_LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location(Double.parseDouble(cursor.getString(0)),Double.parseDouble(cursor.getString(1)),Long.valueOf(cursor.getString(2)),0,applicationContext);
                // Adding contact to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return locationList;
    }

    // Deleting single contact
    public void deleteLocation(Long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, "date" + " = ?",
                new String[] { date.toString() });
        db.close();
    }



     // Adding new contact
     void addContact(Contact contact) {
     SQLiteDatabase db = this.getWritableDatabase();

     ContentValues values = new ContentValues();
     values.put(KEY_NAME, contact.getName()); // Contact Name
     values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

     // Inserting Row
     db.insert(TABLE_CONTACTS, null, values);
     db.close(); // Closing database connection
     }

     // Getting single contact
     Contact getContact(int id) {
     SQLiteDatabase db = this.getReadableDatabase();

     Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
     KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
     new String[] { String.valueOf(id) }, null, null, null, null);
     if (cursor != null)
     cursor.moveToFirst();

     Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
     cursor.getString(1), cursor.getString(2));
     // return contact
     return contact;
     }

     // Getting All Contacts
     public List<Contact> getAllContacts() {
     List<Contact> contactList = new ArrayList<Contact>();
     // Select All Query
     String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

     SQLiteDatabase db = this.getWritableDatabase();
     Cursor cursor = db.rawQuery(selectQuery, null);

     // looping through all rows and adding to list
     if (cursor.moveToFirst()) {
     do {
     Contact contact = new Contact();
     contact.setID(Integer.parseInt(cursor.getString(0)));
     contact.setName(cursor.getString(1));
     contact.setPhoneNumber(cursor.getString(2));
     // Adding contact to list
     contactList.add(contact);
     } while (cursor.moveToNext());
     }

     // return contact list
     return contactList;
     }

     // Updating single contact
     public int updateContact(Contact contact) {
     SQLiteDatabase db = this.getWritableDatabase();

     ContentValues values = new ContentValues();
     values.put(KEY_NAME, contact.getName());
     values.put(KEY_PH_NO, contact.getPhoneNumber());

     // updating row
     return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
     new String[] { String.valueOf(contact.getID()) });
     }

     // Deleting single contact
     public void deleteContact(Contact contact) {
     SQLiteDatabase db = this.getWritableDatabase();
     db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
     new String[] { String.valueOf(contact.getID()) });
     db.close();
     }


     // Getting contacts Count
     public int getContactsCount() {
     String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.rawQuery(countQuery, null);
     cursor.close();

     // return count
     return cursor.getCount();
     }
     */

}