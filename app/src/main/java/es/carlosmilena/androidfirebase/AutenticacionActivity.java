package es.carlosmilena.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AutenticacionActivity extends AppCompatActivity{
	EditText etCorreo;
	EditText etPassword;
	private FirebaseAuth mAuth;
	public static final String EXTRA_NOMBRE_USUARIO = "es.carlosmilena.androidfirebase " +
													  "autenticacionactivity.email";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autenticacion);
		etCorreo = (EditText) findViewById(R.id.etCorreoAcceso);
		etPassword = (EditText) findViewById(R.id.etPasswordAcceso);
		mAuth = FirebaseAuth.getInstance();
		Intent intent = getIntent();
		if(intent != null){
			String usuario = intent.getStringExtra(CrearCuentaActivity.EXTRA_CORREO_NUEVACUENTA);
			String password =
					intent.getStringExtra(CrearCuentaActivity.EXTRA_PASSWORD_NUEVACUENTA);
			etCorreo.setText(usuario);
			etPassword.setText(password);
		}
	}

	public void entrar(View view){
		String email = String.valueOf(etCorreo.getText()).trim();
		String password = String.valueOf(etPassword.getText()).trim();
		boolean camposIntroducidos = true;
		if(email.isEmpty()){
			camposIntroducidos = false;
			etCorreo.setError("Cuenta Email no puede estar vacío");
		}
		if(password.isEmpty()){
			camposIntroducidos = false;
			etPassword.setError("Contraseña no puede estar vacío");
		}
		if(!camposIntroducidos){
			Toast.makeText(AutenticacionActivity.this, "Campos requeridos vacíos",
					Toast.LENGTH_SHORT).show();
			return;
		}
		//-----------------------------------
		mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
			if(task.isSuccessful()){
				Toast.makeText(AutenticacionActivity.this, "Autenticación correcta",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(AutenticacionActivity.this,
						MenuGeneralActivity.class);
				Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
				intent.putExtra(EXTRA_NOMBRE_USUARIO,
						Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).substring(0,
								mAuth.getCurrentUser().getEmail().indexOf("@")));
				startActivity(intent);
			}else{
				Toast.makeText(AutenticacionActivity.this, "Autenticación incorrecta",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void salir(View view){
		mAuth.signOut();
		Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_LONG).show();
	}

	public void irACrearNuevaCuenta(View view){
		Intent intent = new Intent(AutenticacionActivity.this, CrearCuentaActivity.class);
		startActivity(intent);
	}
}