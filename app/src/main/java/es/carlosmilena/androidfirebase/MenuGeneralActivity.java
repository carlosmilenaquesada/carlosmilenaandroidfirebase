package es.carlosmilena.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuGeneralActivity extends AppCompatActivity{
	TextView tvSaludoAlUsuario;

	@Override
	protected void onStart(){
		super.onStart();
		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		FirebaseUser usuarioActual = mAuth.getCurrentUser();
		if(usuarioActual != null){
			usuarioActual.reload();
		}else{
			Toast.makeText(this, "Debes loguearte para acceder", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, AutenticacionActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_general);
		tvSaludoAlUsuario = (TextView) findViewById(R.id.tvSaludoAlUsuario);
		Intent intent = getIntent();
		if(intent != null){
			tvSaludoAlUsuario.setText(getString(R.string.saludo,
					intent.getStringExtra(AutenticacionActivity.EXTRA_NOMBRE_USUARIO)));
		}
	}

	public void irAInsertarJuegoConFoto(View view){
		Intent intent = new Intent(this, AgregarNuevoJuegoActivity.class);
		startActivity(intent);
	}

	public void irAMostrarDatos(View view){
		Intent intent = new Intent(this, MostrarCatalogoFirebaseActivity.class);
		startActivity(intent);
	}

	public void salirDeMenuGeneral(View view){
		Intent intent = new Intent(this, AutenticacionActivity.class);
		startActivity(intent);
	}

	public void cerrarAplicacion(View view){
		finishAffinity();
	}
}