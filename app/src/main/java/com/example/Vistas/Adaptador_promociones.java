package com.example.Vistas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.Modelos.Pojo_productos;
import com.example.Modelos.Utilidades;

import java.util.ArrayList;

public class Adaptador_promociones extends RecyclerView.Adapter<Adaptador_promociones.Holder> implements View.OnClickListener{
    ArrayList<Pojo_productos> lista;
    private View.OnClickListener listener;
    Context context;
    RequestQueue request;

    public void escucharClick(View.OnClickListener listener) {
        this.listener = listener;
    }

    public Adaptador_promociones(ArrayList<Pojo_productos> lista, Context context) {
        this.lista = lista;
        this.context=context;
        request= Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public Adaptador_promociones.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adaptador_promociones, null, false);
        vista.setOnClickListener(this);
        return new Holder(vista);
    }


    @Override
    public void onBindViewHolder(@NonNull Adaptador_promociones.Holder holder, int position) {
        holder.titulo_promos.setText(lista.get(position).getDescripcion());
        holder.titulo_promos.setTextColor(Color.BLACK);
        if (lista.get(position).getImagen()!=null){
            cargar_imagen(lista.get(position).getImagen(), holder);
        }else{
            holder.image_promos.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    private void cargar_imagen(String imagen, final Holder holder) {
        Utilidades uti= new Utilidades();
        String urlImage= context.getResources().getString(R.string.url)+imagen;
        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.image_promos.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView titulo_promos, titulo_credito;
        private ImageView image_promos;
        public Holder(@NonNull View itemView) {
            super(itemView);
            titulo_promos=itemView.findViewById(R.id.titulo_promos);
            image_promos=itemView.findViewById(R.id.image_promos);
            titulo_credito= itemView.findViewById(R.id.titulo_credito);
        }
    }
}
