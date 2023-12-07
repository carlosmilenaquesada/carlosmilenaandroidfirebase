package es.carlosmilena.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuGeneralActivity extends AppCompatActivity{
	TextView tvSaludoAlUsuario;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_general);
		tvSaludoAlUsuario = (TextView) findViewById(R.id.tvSaludoAlUsuario);
		Intent intent = getIntent();
		if(intent != null){
			tvSaludoAlUsuario.setText(
					"¡Hola, " + intent.getStringExtra(AutenticacionActivity.EXTRA_NOMBRE_USUARIO) +
					"!");
		}
	}

	public void insertarProductoConFoto(View view){
		Intent intent = new Intent(this, AgregarNuevoJuegoActivity.class);
		startActivity(intent);
	}

	public void mostrarDatos(View view){
		Intent intent = new Intent(this, MostrarCatalogoFirebaseActivity.class);
		startActivity(intent);
	}
}