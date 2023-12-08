package es.carlosmilena.androidfirebase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import es.carlosmilena.androidfirebase.clases.Juego;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;

public class AgregarNuevoJuegoActivity extends AppCompatActivity{
	private DatabaseReference myRef;
	public static final int NUEVA_IMAGEN = 1;
	Uri imagen_seleccionada = null;
	private Spinner spNuevoPlataforma;
	private EditText edtNuevoNombreJuego;
	private EditText edtNuevoGenero;
	private EditText edtNuevoPrecioVenta;
	private ImageView ivNuevoImagen;
	ArrayAdapter<String> adapter;

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
		setContentView(R.layout.activity_agregar_nuevo_juego);
		spNuevoPlataforma = (Spinner) findViewById(R.id.spNuevoPlataforma);
		adapter = configurarAdaptadorSpinner(spNuevoPlataforma,
				getResources().getStringArray(R.array.plataformas));
		edtNuevoNombreJuego = (EditText) findViewById(R.id.edtNuevoNombreJuego);
		edtNuevoGenero = (EditText) findViewById(R.id.edtNuevoGenero);
		edtNuevoPrecioVenta = (EditText) findViewById(R.id.edtNuevoPrecioVenta);
		ivNuevoImagen = (ImageView) findViewById(R.id.ivNuevoImagen);
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		myRef = database.getReference();
	}

	public void insertarJuegoConFoto(View view){
		String id = myRef.child("juegos").push().getKey();
		if(id != null){
			String plataforma = spNuevoPlataforma.getSelectedItem().toString();
			String nombreJuego = String.valueOf(edtNuevoNombreJuego.getText());
			String genero = String.valueOf(edtNuevoGenero.getText());
			double precioVenta = Double.parseDouble(String.valueOf(edtNuevoPrecioVenta.getText()));
			Juego juego = new Juego(id, plataforma, nombreJuego, genero, precioVenta);
			myRef.child("juegos").child(id).setValue(juego);
			if(imagen_seleccionada != null){
				ImagenesFirebase.subirFoto(id, juego.getPlataforma() + "_" +juego.getNombreJuego(), ivNuevoImagen);
			}
			Toast.makeText(this, "Juego añadido correctamente ", Toast.LENGTH_LONG).show();
		}
	}

	public void cambiarImagenCreacion(View view){
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");
		Intent pickIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");
		Intent chooserIntent = Intent.createChooser(getIntent, "Seleccione una imagen");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
		startActivityForResult(chooserIntent, NUEVA_IMAGEN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK){
			imagen_seleccionada = data.getData();
			Bitmap bitmap;
			try{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
						imagen_seleccionada);
				ivNuevoImagen.setImageBitmap(bitmap);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	private ArrayAdapter<String> configurarAdaptadorSpinner(Spinner spinner, String[] contenido){
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_cerrado,
				contenido);
		adapter.setDropDownViewResource(R.layout.spinner_desplegado);
		spinner.setAdapter(adapter);
		return adapter;
	}

	public void volverAtras(View view){
		startActivity(new Intent(AgregarNuevoJuegoActivity.this, MenuGeneralActivity.class));
	}
}