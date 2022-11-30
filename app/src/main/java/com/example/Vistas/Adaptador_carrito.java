package com.example.Vistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Modelos.CarritoCompras;

import java.util.List;

public class Adaptador_carrito  extends RecyclerView.Adapter<Adaptador_carrito.Holder> {
    List<CarritoCompras> lista;
    Context context;

    public Adaptador_carrito(List<CarritoCompras> lista, Context context) {
        this.lista = lista;
        this.context= context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adaptador_carrito, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador_carrito.Holder holder, int position) {
        holder.producto_carrito.setText(lista.get(position).getProducto());
        holder.precio_carrito.setText(lista.get(position).getPrecio());
        holder.cant_carrito.setText(lista.get(position).getCantidad());
        String prod="'"+lista.get(position).getProducto()+"'";
        List<CarritoCompras> lista1 = CarritoCompras.find(CarritoCompras.class, "producto="+prod, null);
        CarritoCompras unidad= lista1.get(0);
        holder.botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unidad.delete();
                Toast.makeText(context, lista.get(position).getProducto()+" eliminado!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), CarritoActivity.class);
                view.getContext().startActivity(intent);
                ((Activity)(view.getContext())).finish();
            }
        });
        holder.masCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuevaCant= Integer.parseInt(unidad.getCantidad())+1;
                unidad.setCantidad(String.valueOf(nuevaCant));
                unidad.save();
                Intent intent = new Intent(view.getContext(), CarritoActivity.class);
                view.getContext().startActivity(intent);
                ((Activity)(view.getContext())).finish();
            }
        });
        holder.menosCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuevaCant= Integer.parseInt(unidad.getCantidad())-1;
                unidad.setCantidad(String.valueOf(nuevaCant));
                unidad.save();
                Intent intent = new Intent(view.getContext(), CarritoActivity.class);
                view.getContext().startActivity(intent);
                ((Activity)(view.getContext())).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView producto_carrito, precio_carrito, cant_carrito;
        private ImageView botonBorrar;
        private ImageButton masCantidad, menosCantidad;
        public Holder(@NonNull View itemView) {
            super(itemView);
            producto_carrito=itemView.findViewById(R.id.producto_carrito);
            precio_carrito=itemView.findViewById(R.id.precio_carrito);
            botonBorrar= itemView.findViewById(R.id.botonBorrar);
            menosCantidad= itemView.findViewById(R.id.menosCantidad);
            masCantidad= itemView.findViewById(R.id.masCantidad);
            cant_carrito=itemView.findViewById(R.id.cant_carrito);
        }
    }
}
