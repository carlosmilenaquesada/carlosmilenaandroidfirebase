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

public class CrearCuentaActivity extends AppCompatActivity{
	// FirebaseDatabase database;
	EditText etNuevoCorreo;
	EditText etNuevoPassword;
	private FirebaseAuth mAuth;
	public static final String EXTRA_CORREO_NUEVACUENTA = "es.carlosmilena.androidfirebase.crearcuentaactivity.email";
	public static final String EXTRA_PASSWORD_NUEVACUENTA = "es.carlosmilena.androidfirebase.crearcuentaactivity.password";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_cuenta);
		etNuevoCorreo = (EditText) findViewById(R.id.etNuevoCorreo);
		etNuevoPassword = (EditText) findViewById(R.id.etNuevoPassword);
		//------------------------------------
		mAuth = FirebaseAuth.getInstance();

	}

	public void registrarCuenta(View view){
		String email = String.valueOf(etNuevoCorreo.getText()).trim();
		String password = String.valueOf(etNuevoPassword.getText()).trim();
		if(email.isEmpty() && password.isEmpty()){
			Toast.makeText(CrearCuentaActivity.this,
					"Debe introducir una cuenta Email y contraseña para " +
					"registrarse", Toast.LENGTH_LONG).show();
			etNuevoCorreo.setError("Cuenta Email no puede estar vacío");
			etNuevoPassword.setError("Contraseña no puede estar vacío");
			return;
		}
		mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
				new OnCompleteListener<AuthResult>(){
			@Override
			public void onComplete(@NonNull Task<AuthResult> task){
				if(task.isSuccessful()){
					Toast.makeText(CrearCuentaActivity.this, "Registro realizado correctamente",
							Toast.LENGTH_LONG).show();
					FirebaseUser user = mAuth.getCurrentUser();
					//-------------------------------------------------
					Intent intent = new Intent(CrearCuentaActivity.this,
							AutenticacionActivity.class);
					intent.putExtra(EXTRA_CORREO_NUEVACUENTA, email);
					intent.putExtra(EXTRA_PASSWORD_NUEVACUENTA, password);
					startActivity(intent);
				}else{
					Toast.makeText(CrearCuentaActivity.this, "Error al intentar registrar",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}