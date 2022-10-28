package com.example.tallerinterfazusuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView recycle_Carrito;
    List<CarritoCompras> listaCarrito;
    private TextView tv_total_carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        tv_total_carrito= findViewById(R.id.tv_total_carrito);
        recycle_Carrito= findViewById(R.id.listCarrito);
        recycle_Carrito.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycle_Carrito.setHasFixedSize(true);
        cargarCarrito();
    }

    private void cargarCarrito() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null) {
            CarritoCompras carrito= new CarritoCompras(bundle.getString("producto"), "1", bundle.getString("precio"));
            carrito.save();
        }
        cargarAdaptador();
    }

    private void cargarAdaptador() {
        listaCarrito = CarritoCompras.listAll(CarritoCompras.class);
        if(listaCarrito.size()>0) {
            Adaptador_carrito adaptador_carrito = new Adaptador_carrito(listaCarrito, getApplicationContext());
            recycle_Carrito.setAdapter(adaptador_carrito);
            tv_total_carrito.setText(String.valueOf(totalCarrito(listaCarrito)));
        }
    }

    private int totalCarrito(List<CarritoCompras> carrito){
        int totalCarrito=0;
        for (int i=0; i<carrito.size(); i++){
            String replace = carrito.get(i).getPrecio().replace(".", "");
            try {
                NumberFormat nf = NumberFormat.getInstance(new Locale("us", "US"));
                Integer salida = nf.parse(replace).intValue();
                totalCarrito=totalCarrito+salida;
            }
            catch (NumberFormatException | ParseException ex){
                ex.printStackTrace();
            }
        }
        return totalCarrito;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CarritoActivity.this, ProductosActivity.class);
        startActivity(intent);
        finish();
    }
}