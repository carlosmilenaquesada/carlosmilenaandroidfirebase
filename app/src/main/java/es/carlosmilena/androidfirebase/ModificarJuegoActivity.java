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
	Uri imagen_seleccionada = null;

	private EditText edtModificarIdentificador;
	private EditText edtModificarPlataforma;
	private EditText edtModificarNombreJuego;
	private EditText edtModificarGenero;
	private EditText edtModificarPrecioVenta;
	private ImageView ivModificarImagen;


	private Juego p;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modificar_juego);
		ivModificarImagen = (ImageView) findViewById(R.id.ivModificarJuegoImagen);
		Intent intent = getIntent();
		if(intent != null){
			p =	(Juego) intent.getSerializableExtra(JuegoViewHolder.EXTRA_DETALLES_PRODUCTO);
			//---------------------- cargo la foto  ----------------------------------------
			byte[] fotobinaria =
					(byte[]) intent.getByteArrayExtra(JuegoViewHolder.EXTRA_IMAGEN2_PRODUCTO);
			Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobinaria, 200, 200);
			ivModificarImagen.setImageBitmap(fotobitmap);
		}else{
			p = new Juego();
		}
		//----------------------------------------------------------------
		edtModificarIdentificador = (EditText) findViewById(R.id.edtModificarJuegoIdentificador);
		edtModificarPlataforma = (EditText) findViewById(R.id.edtModificarJuegoPlataforma);
		edtModificarNombreJuego = (EditText) findViewById(R.id.edtModificarJuegoNombreJuego);
		edtModificarGenero = (EditText) findViewById(R.id.edtModificarJuegoGenero);
		edtModificarPrecioVenta = (EditText) findViewById(R.id.edtModificarJuegoPrecioVenta);
		ivModificarImagen = (ImageView) findViewById(R.id.ivModificarJuegoImagen);
		//----------------------------------------------------------------
		edtModificarIdentificador.setText(p.getIdentificador());
		edtModificarPlataforma.setText(p.getPlataforma());
		edtModificarNombreJuego.setText(p.getNombreJuego());
		edtModificarGenero.setText(p.getGenero());
		edtModificarPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
	}

	//----------------------------------------------------
	public void borrar_detalles(View view){
		database = FirebaseDatabase.getInstance();
		myRef = database.getReference("productos");
		myRef.child(p.getIdentificador()).removeValue();
		Toast.makeText(this, "juego borrado", Toast.LENGTH_LONG).show();
		//---------------------- borramos la imagen del firebase ------------------
		// borramos la imagen del firebase store
		String carpeta = p.getIdentificador();
		ImagenesFirebase.borrarFoto(carpeta, p.getNombreJuego());
		finish();
	}

	//---------------------------------------------------
	public void editar_detalles(View view){
		database = FirebaseDatabase.getInstance();
		myRef = database.getReference("productos");


		String identificador = String.valueOf(edtModificarIdentificador.getText());
		String plataforma = String.valueOf(edtModificarPlataforma.getText());
		String nombreJuego = String.valueOf(edtModificarNombreJuego.getText());
		String genero = String.valueOf(edtModificarGenero.getText());
		double precioVenta = Double.valueOf(String.valueOf(edtModificarPrecioVenta.getText()));
		Juego p1 = new Juego(identificador, plataforma, nombreJuego, genero, precioVenta);
		Map<String, Object> productos = new HashMap<>();
		productos.put(identificador, p1);//puede dar fallo por el identificador (ponía id)
		myRef.updateChildren(productos);
		//------------------------------------- actualizamos la foto -------------------
		if(imagen_seleccionada != null){
			String carpeta = p.getIdentificador();
			ImagenesFirebase.borrarFoto(carpeta, p.getNombreJuego());
			ImagenesFirebase.subirFoto(carpeta, nombreJuego, ivModificarImagen);
		}
		Toast.makeText(this, "juego actualizado correctamente ", Toast.LENGTH_LONG).show();
		finish();
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
				ivModificarImagen.setImageBitmap(bitmap);
				//---------------------------------------------
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}