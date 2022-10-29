package com.example.tallerinterfazusuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Modelos.Usuarios;

import java.util.List;

public class RegistroActivity extends AppCompatActivity {

    private Button regresarLogin, btn_crear_cuenta, btn_eliminar_cuenta;
    private EditText edit_nombre, edit_direccion, edit_tels_crear_cuenta, edit_mail, edit_mail2, edit_contraseña, edit_contraseña2;
    private ImageView img_mostrar_contra_crear_cuenta, img_mostrar_contra_crear_cuenta1;
    private Boolean mostrarContraseña=false;
    public String nombre, direccion, telefono, email1, contraseña1;
    private SharedPreferences prefe;
    private TextView titulo_crear_cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        titulo_crear_cuenta=findViewById(R.id.titulo_crear_cuenta);
        btn_eliminar_cuenta=findViewById(R.id.btn_eliminar_cuenta);
        btn_eliminar_cuenta.setVisibility(View.INVISIBLE);
        prefe= getSharedPreferences("prefe", Context.MODE_PRIVATE);
        edit_nombre= findViewById(R.id.edit_nombre);
        edit_direccion=findViewById(R.id.edit_direccion);
        edit_tels_crear_cuenta= findViewById(R.id.edit_tels_crear_cuenta);
        edit_mail= findViewById(R.id.edit_mail);
        edit_mail2= findViewById(R.id.edit_mail2);
        edit_contraseña= findViewById(R.id.edit_contraseña);
        edit_contraseña2= findViewById(R.id.edit_contraseña2);
        img_mostrar_contra_crear_cuenta= findViewById(R.id.img_mostrar_contra_crear_cuenta);
        img_mostrar_contra_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mostrarContraseña){
                    mostrarContraseña=false;
                    edit_contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    mostrarContraseña=true;
                    edit_contraseña.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        img_mostrar_contra_crear_cuenta1= findViewById(R.id.img_mostrar_contra_crear_cuenta1);
        img_mostrar_contra_crear_cuenta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mostrarContraseña){
                    mostrarContraseña=false;
                    edit_contraseña2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    mostrarContraseña=true;
                    edit_contraseña2.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        regresarLogin=findViewById(R.id.btn_backToLogin);
        btn_crear_cuenta= findViewById(R.id.btn_crear_cuenta);
        btn_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCamposVacios()==9){
                    if(prefe.getString("usuario", "")!=""){
                       editarCuentaEnBD();
                    }else{
                        CrearCuentaEnBD();
                    }
                }
            }
        });
        regresarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefe.getString("usuario", "")!=""){
                    cerrarSesion();
                }else{
                    volverLogin();
                }
            }
        });
        validarSesion();
        btn_eliminar_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarCuentaBD();
            }
        });
    }

    private void cerrarSesion(){
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString("usuario", "").commit();
        Toast.makeText(getApplicationContext(), "Sesión terminada.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistroActivity.this, ProductosActivity.class);
        startActivity(intent);
        finish();
    }

    private void eliminarCuentaBD() {
        String correo="'"+prefe.getString("usuario", "")+"'";
        List<Usuarios> lista = Usuarios.find(Usuarios.class, "email="+correo, null);
        Usuarios usu= lista.get(0);
        usu.delete();
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString("usuario", "").commit();
        Toast.makeText(this, "Cuenta eliminada!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistroActivity.this, ProductosActivity.class);
        startActivity(intent);
        finish();
    }

    private void editarCuentaEnBD() {
        String correo="'"+prefe.getString("usuario", "")+"'";
        List<Usuarios> lista = Usuarios.find(Usuarios.class, "email="+correo, null);
        if(lista.size()>0) {
            Usuarios user = lista.get(0);
            user.setNombre(edit_nombre.getText().toString());
            user.setDireccion(edit_direccion.getText().toString());
            user.setTelefono(edit_tels_crear_cuenta.getText().toString());
            user.setEmail(edit_mail.getText().toString());
            user.setContraseña(edit_contraseña.getText().toString());
            user.save();
            Toast.makeText(this, "Información actualizada!", Toast.LENGTH_SHORT).show();
            goProductos();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goProductos();
    }

    private void goProductos(){
        Intent intent = new Intent(RegistroActivity.this, ProductosActivity.class);
        startActivity(intent);
        finish();
    }

    private void validarSesion() {
        if(prefe.getString("usuario", "")!=""){
            btn_eliminar_cuenta.setVisibility(View.VISIBLE);
            btn_crear_cuenta.setText("Modificar");
            titulo_crear_cuenta.setText("Tus datos");
            regresarLogin.setText("Cerrar sesión");
            String correo="'"+prefe.getString("usuario", "")+"'";
            List<Usuarios> lista = Usuarios.find(Usuarios.class, "email="+correo, null);
            if(lista.size()>0) {
                Usuarios user = lista.get(0);
                edit_nombre.setText(user.getNombre());
                edit_direccion.setText(user.getDireccion());
                edit_tels_crear_cuenta.setText(user.getTelefono());
                edit_mail.setText(user.getEmail());
                edit_mail2.setText(user.getEmail());
                edit_contraseña.setText(user.getContraseña());
                edit_contraseña2.setText(user.getContraseña());
            }
        }
    }

    private void CrearCuentaEnBD() {
        Usuarios usuarios= new Usuarios(nombre, direccion, telefono, email1, contraseña1);
        usuarios.save();
        reiniciarEditText();
        Toast.makeText(this, "Registro realizado!", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString("usuario", email1).commit();
        Toast.makeText(this, "Bienvenid@"+ nombre, Toast.LENGTH_SHORT).show();
        goProductos();
    }

    private void volverLogin() {
        Intent intent= new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void reiniciarEditText() {
        edit_nombre.setText("");
        edit_direccion.setText("");
        edit_tels_crear_cuenta.setText("");
        edit_mail.setText("");
        edit_mail2.setText("");
        edit_contraseña.setText("");
        edit_contraseña2.setText("");
    }

    private int validarCamposVacios() {
        int camposValidos=0;
        nombre= edit_nombre.getText().toString();
        direccion= edit_direccion.getText().toString();
        telefono= edit_tels_crear_cuenta.getText().toString();
        email1= edit_mail.getText().toString();
        String email2= edit_mail2.getText().toString();
        contraseña1= edit_contraseña.getText().toString();
        String contraseña2= edit_contraseña2.getText().toString();
        if(nombre.equalsIgnoreCase("")){
            edit_nombre.setError("Campo vacío!");
        }else{
            camposValidos++;
        }
        if(direccion.equalsIgnoreCase("")){
            edit_direccion.setError("Campo vacío!");
        }else{
            camposValidos++;
        }
        if(telefono.equalsIgnoreCase("")){
            edit_tels_crear_cuenta.setError("Campo vacío!");
        }else{
            camposValidos++;
        }
        if(email1.equalsIgnoreCase("")){
            edit_mail.setError("Campo vacío!");
        }else{
            if(!email1.contains("@")){
                edit_mail.setError("Ingresa un email válido!");
            }else{
                camposValidos++;
            }
        }
        if(email2.equalsIgnoreCase("")){
            edit_mail2.setError("Campo vacío!");
        }else{
            if(!email2.contains("@")){
                edit_mail2.setError("Ingresa un email válido!");
            }else{
                camposValidos++;
            }
        }
        if(contraseña1.equalsIgnoreCase("")){
            edit_contraseña.setError("Campo vacío!");
        }else{
            if(contraseña1.length()<4){
                edit_contraseña.setError("La contraseña debe tener al menos 4 digitos!");
            }else{
                camposValidos++;
            }
        }
        if(contraseña2.equalsIgnoreCase("")){
            edit_contraseña2.setError("Campo vacío!");
        }else{
            if(contraseña2.length()<4){
                edit_contraseña2.setError("La contraseña debe tener al menos 4 digitos!");
            }else{
                camposValidos++;
            }
        }
        if(email1.equals(email2)){
            camposValidos++;
        }else{
            edit_mail.setError("No coinciden los emails!");
            edit_mail2.setError("No coinciden los emails!");
        }
        if(contraseña1.equals(contraseña2)){
            camposValidos++;
        }else{
            edit_contraseña.setError("No coinciden las contraseñas!");
            edit_contraseña2.setError("No coinciden las contraseñas!");
        }
        return camposValidos;
    }
}