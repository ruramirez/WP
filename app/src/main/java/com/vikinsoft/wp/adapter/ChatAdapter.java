package com.vikinsoft.wp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vikinsoft.wp.Mensaje;
import com.vikinsoft.wp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruben on 20/06/16.
 */
public class ChatAdapter extends BaseAdapter {

    Context messageContext;
    List<Mensaje> messageList = new ArrayList<>();

    public ChatAdapter(Context context, List<Mensaje> mensajes){
        messageList = mensajes;
        messageContext = context;
    }


    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class MessageViewHolder {
        public ImageView thumbnailImageView;
        public TextView senderView;
        public TextView bodyView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder holder;

        // if there is not already a view created for an item in the Message list.

        if (convertView == null){
            LayoutInflater messageInflater = (LayoutInflater) messageContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            // create a view out of our `message.xml` file
            convertView = messageInflater.inflate(R.layout.mensajes_view, null);

            // create a MessageViewHolder
            holder = new MessageViewHolder();

            // set the holder's properties to elements in `message.xml`
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.senderView = (TextView) convertView.findViewById(R.id.message_sender);
            holder.bodyView = (TextView) convertView.findViewById(R.id.message_body);

            // assign the holder to the view we will return
            convertView.setTag(holder);
        } else {

            // otherwise fetch an already-created view holder
            holder = (MessageViewHolder) convertView.getTag();
        }

        // get the message from its position in the ArrayList
        Mensaje mensaje = (Mensaje) getItem(position);

        // set the elements' contents
        holder.bodyView.setText(mensaje.getMensaje());
        holder.senderView.setText(mensaje.getComprador().getNombre());

        // fetch the user's Twitter avatar from their username
        // and place it into the thumbnailImageView.
        holder.thumbnailImageView.setImageBitmap(mensaje.getComprador().getImagen());

        return convertView;
    }
}
