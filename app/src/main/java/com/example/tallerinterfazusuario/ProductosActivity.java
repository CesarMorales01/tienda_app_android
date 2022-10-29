package com.example.tallerinterfazusuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.Modelos.Pojo_productos;
import com.example.Modelos.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ProductosActivity extends AppCompatActivity {
    private SharedPreferences prefe;
    private ProgressDialog progressDialog;
    public RequestQueue request;
    private RecyclerView recycle_productos;
    private ArrayList<Pojo_productos> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        prefe= getSharedPreferences("prefe", Context.MODE_PRIVATE);
        request = Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        recycle_productos=findViewById(R.id.recycle_productos);
        recycle_productos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycle_productos.setHasFixedSize(true);
        listaProductos= new ArrayList<>();
        cargarProductos();    
    }

    private void cargarProductos() {
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Utilidades uti= new Utilidades();
        String url1 =uti.getUrlServidor()+"/servidoresNewStoreApp/get_categorias.php?code=promo";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Pojo_productos pojo_promos = null;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject prod = response.getJSONObject(i);
                                pojo_promos = new Pojo_productos();
                                pojo_promos.setId(prod.getString("codigo"));
                                pojo_promos.setDescripcion(prod.getString("nombre"));
                                pojo_promos.setImagen(prod.getString("imagen"));
                                pojo_promos.setProducto(prod.getString("referencia"));
                                listaProductos.add(pojo_promos);
                                cargarAdaptador(listaProductos);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(ProductosActivity.this, "Error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void cargarAdaptador(ArrayList<Pojo_productos> lista) {
        Adaptador_promociones adaptador_promociones = new Adaptador_promociones(lista, getApplicationContext());
        recycle_productos.setAdapter(adaptador_promociones);
        adaptador_promociones.escucharClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(prefe.getString("usuario", "").equalsIgnoreCase("")){
                        createSimpleDialog().show();
                    }else{
                        Intent intent = new Intent(ProductosActivity.this, CarritoActivity.class);
                        String producto = lista.get(recycle_productos.getChildAdapterPosition(view)).getDescripcion();
                        StringTokenizer tokens=new StringTokenizer(producto, "$");
                        String prod= tokens.nextToken();
                        String precio= tokens.nextToken();
                        intent.putExtra("producto", prod);
                        intent.putExtra("precio", precio);
                        startActivity(intent);
                        finish();
                    }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile:
                Intent intent0 = new Intent(ProductosActivity.this, RegistroActivity.class);
                startActivity(intent0);
                finish();
                break;
            case R.id.menu_carrito:
                Intent intent2= new Intent(ProductosActivity.this, CarritoActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Para comprar debes identificarte.")
                .setMessage("Elije una opción:")
                .setPositiveButton("Iniciar sesión",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProductosActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("Registrarme",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProductosActivity.this, RegistroActivity.class);
                                startActivity(intent);
                            }
                        });
        return builder.create();
    }
}