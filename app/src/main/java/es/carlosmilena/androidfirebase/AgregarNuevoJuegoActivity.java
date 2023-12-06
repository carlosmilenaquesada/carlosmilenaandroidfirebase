package es.carlosmilena.androidfirebase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import es.carlosmilena.androidfirebase.clases.Juego;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;

public class AgregarNuevoJuegoActivity extends AppCompatActivity{
	private FirebaseDatabase database;
	private DatabaseReference myRef;
	public static final int NUEVA_IMAGEN = 1;
	Uri imagen_seleccionada = null;
	private EditText edtNuevoIdentificador;
	private EditText edtNuevoPlataforma;
	private EditText edtNuevoNombreJuego;
	private EditText edtNuevoGenero;
	private EditText edtNuevoPrecioVenta;
	private ImageView ivNuevoImagen;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_nuevo_juego);
		edtNuevoIdentificador = (EditText) findViewById(R.id.edtNuevoIdentificador);
		edtNuevoPlataforma = (EditText) findViewById(R.id.edtNuevoPlataforma);
		edtNuevoNombreJuego = (EditText) findViewById(R.id.edtNuevoNombreJuego);
		edtNuevoGenero = (EditText) findViewById(R.id.edtNuevoGenero);
		edtNuevoPrecioVenta = (EditText) findViewById(R.id.edtNuevoPrecioVenta);
		ivNuevoImagen = (ImageView) findViewById(R.id.ivNuevoImagen);
		//-------------------------------------------------------------------------
		database = FirebaseDatabase.getInstance();
		myRef = database.getReference();
	}

	public void insertarProducto_foto(View view){
		String identificador = String.valueOf(edtNuevoIdentificador.getText());
		String plataforma = String.valueOf(edtNuevoPlataforma.getText());
		String nombreJuego = String.valueOf(edtNuevoNombreJuego.getText());
		String genero = String.valueOf(edtNuevoGenero.getText());
		double precioVenta = Double.valueOf(String.valueOf(edtNuevoPrecioVenta.getText()));
		Juego p1 = new Juego(identificador, plataforma, nombreJuego, genero, precioVenta);
		myRef.child("productos").child(p1.getIdentificador()).setValue(p1);
		//---------------------- codigo para añadir la foto al storage
		// ------------------------------
		// codigo para guardar la imagen del usuario en firebase store
		if(imagen_seleccionada != null){
			String carpeta = p1.getIdentificador();
			ImagenesFirebase.subirFoto(carpeta, p1.getNombreJuego(), ivNuevoImagen);
		}
		Toast.makeText(this, "juego añadido correctamente ", Toast.LENGTH_LONG).show();
	}
	//--------------------------------------------------------------------------
	//--------CODIGO PARA CAMBIAR LA IMAGEN----------------

	public void cambiar_imagen(View view){
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");
		Intent pickIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");
		Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
		startActivityForResult(chooserIntent, NUEVA_IMAGEN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK){
			imagen_seleccionada = data.getData();
			Bitmap bitmap = null;
			try{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
						imagen_seleccionada);
				ivNuevoImagen.setImageBitmap(bitmap);
				//---------------------------------------------
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}