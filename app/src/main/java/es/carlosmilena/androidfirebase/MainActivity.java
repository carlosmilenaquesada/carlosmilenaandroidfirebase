package es.carlosmilena.androidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
   // FirebaseDatabase database;
    EditText edt_login_email;
    EditText edt_login_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_login_email = (EditText) findViewById(R.id.edt_login_email);
        edt_login_password = (EditText) findViewById(R.id.edt_login_password);
        //------------------------------------
        mAuth = FirebaseAuth.getInstance();

       // database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference(); // te coloca en la raiz del arbol
       // myRef.child("holamundo").setValue("esto es un hola mundo");
       // Producto p1 = new Producto("p1","balon",5,20.3);
       // Producto p2 = new Producto("p2","manzana",25,2.3);

       // myRef.child("productos").child(p1.getIdProducto()).setValue(p1);
       // myRef.child("productos").push().setValue(p2);
      // myRef.child("productos").child(p1.getIdProducto()).removeValue();
      //  myRef.child("productos").child(p1.getIdProducto()).setValue(null);

    }

    public void insertarProducto(View view)
    {
        Intent intent = new Intent(this,NuevoProductoActivity.class);
        startActivity(intent);
    }

    public void insertarProductoConFoto(View view)
    {
        Intent intent = new Intent(this,NuevoProducto_con_foto.class);
        startActivity(intent);
    }
    public void mostrarProductos(View view)
    {
        Intent intent = new Intent(this,MostrarProductosActivity.class);
        startActivity(intent);
    }

    public void mostrarDatos(View view)
    {
        Intent intent = new Intent(this,MostrarDatosFirebaseActivity.class);
        startActivity(intent);
    }

    public void entrar(View view)
    {
        String email = String.valueOf(edt_login_email.getText());
        String password = String.valueOf(edt_login_password.getText());
        //-----------------------------------
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("firebase1", "logueo correcto");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "autenticacion correcta",
                                    Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,MostrarDatosFirebaseActivity.class);
                                    startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("firebase1", "no ha funcionado el logueo", task.getException());
                            Toast.makeText(MainActivity.this, "autenticacion incorrecta",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //----------------------------------------
    public void salir(View view)
    {
        mAuth.signOut();
        Toast.makeText(this," ha cerrado sesión ", Toast.LENGTH_LONG).show();
    }
    //---------------------------------------
    public void registrarse(View view)
    {
        String email = String.valueOf(edt_login_email.getText()).trim();
        String password = String.valueOf(edt_login_password.getText()).trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("firebase1", "registro realizado correctamente");
                            Toast.makeText(MainActivity.this, "se registro correctamente",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //-------------------------------------------------
                            Intent intent = new Intent(MainActivity.this,MostrarDatosFirebaseActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("firebase1", "no se pudo realizar el registro");

                            Toast.makeText(MainActivity.this, "Falló el registro",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}