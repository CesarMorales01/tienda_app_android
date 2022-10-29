package com.example.tallerinterfazusuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Modelos.Usuarios;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button registro, buttonIngresar;
    private EditText editTextEmailAddress, editTextPassword;
    private String email, contraseña;
    private SharedPreferences prefe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefe= getSharedPreferences("prefe", Context.MODE_PRIVATE);
        registro= findViewById(R.id.registro);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editTextEmailAddress=findViewById(R.id.editTextEmailAddress);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonIngresar= findViewById(R.id.buttonIngresar);
        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()==2){
                    consultarCuenta();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goProductos();
    }

    private void goProductos() {
        Intent intent = new Intent(LoginActivity.this, ProductosActivity.class);
        startActivity(intent);
        finish();
    }

    private void consultarCuenta() {
        String correo="'"+email+"'";
        List<Usuarios> lista = Usuarios.find(Usuarios.class, "email="+correo, null);
        if(lista.size()>0){
            Usuarios user= lista.get(0);
            if(user.getEmail().equals(email) && user.getContraseña().equals(contraseña)){
                iniciarSesion(user);
            }else{
                Toast.makeText(this, "Email o contraseña incorrecta!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Email o contraseña incorrecta!", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarSesion(Usuarios user) {
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString("usuario", user.getEmail()).commit();
        Toast.makeText(this, "Bienvenid@"+ user.getNombre(), Toast.LENGTH_SHORT).show();
        goProductos();
    }

    public  int  validarCampos(){
        int validados=0;
        email=editTextEmailAddress.getText().toString();
        contraseña= editTextPassword.getText().toString();
        if(email.equalsIgnoreCase("")){
            editTextEmailAddress.setError("Campo vacío!");
        }else{
            validados++;
        }
        if(contraseña.equalsIgnoreCase("")){
            editTextPassword.setError("Campo vacío!");
        }else{
            validados++;
        }
        return validados;
    }
}