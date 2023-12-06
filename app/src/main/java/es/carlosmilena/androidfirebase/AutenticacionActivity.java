package es.carlosmilena.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacionActivity extends AppCompatActivity{
	// FirebaseDatabase database;
	EditText etCorreo;
	EditText etPassword;
	private FirebaseAuth mAuth;
	public static final String EXTRA_NOMBRE_USUARIO = "es.carlosmilena.androidfirebase" +
													  ".autenticacionactivity.email";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autenticacion);
		etCorreo = (EditText) findViewById(R.id.etNuevoCorreo);
		etPassword = (EditText) findViewById(R.id.etNuevoPassword);
		//------------------------------------
		mAuth = FirebaseAuth.getInstance();
		Intent intent = getIntent();
		if(intent != null){
			String usuario = intent.getStringExtra(CrearCuentaActivity.EXTRA_CORREO_NUEVACUENTA);
			String password =
					intent.getStringExtra(CrearCuentaActivity.EXTRA_PASSWORD_NUEVACUENTA);
			etCorreo.setText(usuario);
			etPassword.setText(password);
		}
		// database = FirebaseDatabase.getInstance();
		// DatabaseReference myRef = database.getReference(); // te coloca en la raiz del arbol
		// myRef.child("holamundo").setValue("esto es un hola mundo");
		// Juego p1 = new Juego("p1","balon",5,20.3);
		// Juego p2 = new Juego("p2","manzana",25,2.3);
		// myRef.child("productos").child(p1.getIdProducto()).setValue(p1);
		// myRef.child("productos").push().setValue(p2);
		// myRef.child("productos").child(p1.getIdProducto()).removeValue();
		//  myRef.child("productos").child(p1.getIdProducto()).setValue(null);
	}

	public void entrar(View view){
		String email = "anonimo1@carlosmilena.com";//String.valueOf(etCorreo.getText());
		String password = "1234567";//String.valueOf(etPassword.getText());
		if(email.isEmpty() && password.isEmpty()){
			Toast.makeText(AutenticacionActivity.this,
					"Debe introducir una cuenta Email y contraseña para " +
					"acceder", Toast.LENGTH_LONG).show();
			etCorreo.setError("Cuenta Email no puede estar vacío");
			etPassword.setError("Contraseña no puede estar vacío");
			return;
		}
		//-----------------------------------
		mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
				new OnCompleteListener<AuthResult>(){
			@Override
			public void onComplete(@NonNull Task<AuthResult> task){
				if(task.isSuccessful()){
					FirebaseUser user = mAuth.getCurrentUser();
					Toast.makeText(AutenticacionActivity.this, "Autenticación correcta",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(AutenticacionActivity.this,
							MenuGeneralActivity.class);
					mAuth.getCurrentUser().getDisplayName();
					intent.putExtra(EXTRA_NOMBRE_USUARIO,
							mAuth.getCurrentUser().getEmail().substring(0,
									mAuth.getCurrentUser().getEmail().indexOf("@")));
					startActivity(intent);
				}else{
					task.getException();
					Toast.makeText(AutenticacionActivity.this, "Autenticación incorrecta",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	//----------------------------------------
	public void salir(View view){
		mAuth.signOut();
		Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_LONG).show();
	}

	//---------------------------------------
	public void irACrearNuevaCuenta(View view){
		Intent intent = new Intent(AutenticacionActivity.this, CrearCuentaActivity.class);
		startActivity(intent);
	}
}