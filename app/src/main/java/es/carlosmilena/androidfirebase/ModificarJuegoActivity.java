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
import java.util.HashMap;
import java.util.Map;

import es.carlosmilena.androidfirebase.clases.Juego;
import es.carlosmilena.androidfirebase.recyclerview.JuegoViewHolder;
import es.carlosmilena.androidfirebase.utilidades.ImagenesBlobBitmap;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;

public class ModificarJuegoActivity extends AppCompatActivity{
	private FirebaseDatabase database;
	private DatabaseReference myRef;
	public static final int NUEVA_IMAGEN = 1;
	Uri imagenSeleccionada = null;
	private Spinner spModificarJuegoPlataforma;
	private EditText edtModificarNombreJuego;
	private EditText edtModificarGenero;
	private EditText edtModificarPrecioVenta;
	private ImageView ivModificarImagen;
	ArrayAdapter<String> adapter;
	private Juego juego;
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
		setContentView(R.layout.activity_modificar_juego);
		ivModificarImagen = (ImageView) findViewById(R.id.ivModificarJuegoImagen);
		Intent intent = getIntent();
		if(intent != null){
			juego = (Juego) intent.getSerializableExtra(JuegoViewHolder.EXTRA_MODIFICAR_JUEGO);
			byte[] fotobinaria =
					(byte[]) intent.getByteArrayExtra(JuegoViewHolder.EXTRA_MODIFICAR_IMAGEN);
			Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobinaria, 200, 200);
			ivModificarImagen.setImageBitmap(fotobitmap);
		}else{
			juego = new Juego();
		}
		spModificarJuegoPlataforma = (Spinner) findViewById(R.id.spModificarJuegoPlataforma);
		adapter = configurarAdaptadorSpinner(spModificarJuegoPlataforma,
				getResources().getStringArray(R.array.plataformas));
		edtModificarNombreJuego = (EditText) findViewById(R.id.edtModificarJuegoNombreJuego);
		edtModificarGenero = (EditText) findViewById(R.id.edtModificarJuegoGenero);
		edtModificarPrecioVenta = (EditText) findViewById(R.id.edtModificarJuegoPrecioVenta);
		ivModificarImagen = (ImageView) findViewById(R.id.ivModificarJuegoImagen);
		edtModificarNombreJuego.setText(juego.getNombreJuego());
		edtModificarGenero.setText(juego.getGenero());
		edtModificarPrecioVenta.setText(String.valueOf(juego.getPrecioVenta()));
	}
	public void borrarJuego(View view){
		database = FirebaseDatabase.getInstance();
		myRef = database.getReference("juegos");
		myRef.child(juego.getIdentificador()).removeValue();
		Toast.makeText(this, "juego borrado", Toast.LENGTH_LONG).show();
		String carpeta = juego.getIdentificador();
		ImagenesFirebase.borrarFoto(carpeta, juego.getNombreJuego());
		startActivity(new Intent(ModificarJuegoActivity.this,
				MostrarCatalogoFirebaseActivity.class));
	}

	public void editarJuego(View view){
		database = FirebaseDatabase.getInstance();
		myRef = database.getReference("juegos");
		String plataforma = spModificarJuegoPlataforma.getSelectedItem().toString();
		String nombreJuego = String.valueOf(edtModificarNombreJuego.getText());
		String genero = String.valueOf(edtModificarGenero.getText());
		double precioVenta = Double.parseDouble(String.valueOf(edtModificarPrecioVenta.getText()));
		Juego j = new Juego(juego.getIdentificador(), plataforma, nombreJuego, genero,
				precioVenta);
		Map<String, Object> juegos = new HashMap<>();
		juegos.put(juego.getIdentificador(), j);
		myRef.updateChildren(juegos);
		if(imagenSeleccionada != null){
			String carpeta = juego.getIdentificador();
			ImagenesFirebase.borrarFoto(carpeta, juego.getPlataforma() + "_" +juego.getNombreJuego());
			ImagenesFirebase.subirFoto(carpeta, plataforma + "_" +nombreJuego, ivModificarImagen);
		}
		Toast.makeText(this, "juego actualizado correctamente ", Toast.LENGTH_LONG).show();
		startActivity(new Intent(ModificarJuegoActivity.this,
				MostrarCatalogoFirebaseActivity.class));
	}

	public void cambiarImagenModificacion(View view){
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");
		Intent pickIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");
		Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
		startActivityForResult(chooserIntent, NUEVA_IMAGEN);
	}

	public void cancelarCambiosModificacion(View view){
		startActivity(new Intent(ModificarJuegoActivity.this,
				MostrarCatalogoFirebaseActivity.class));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK){
			imagenSeleccionada = data.getData();
			Bitmap bitmap;
			try{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
						imagenSeleccionada);
				ivModificarImagen.setImageBitmap(bitmap);
				//---------------------------------------------
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
}