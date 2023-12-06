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

import es.carlosmilena.androidfirebase.clases.Producto;
import es.carlosmilena.androidfirebase.recyclerview1.ProductoViewHolder1;
import es.carlosmilena.androidfirebase.utilidades.ImagenesBlobBitmap;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;

public class MostrarDetallesProductos extends AppCompatActivity{
	private FirebaseDatabase database;
	private DatabaseReference myRef;

	public static final int NUEVA_IMAGEN = 1;
	Uri imagen_seleccionada = null;

	private EditText edtDetalleIdentificador;
	private EditText edtDetallePlataforma;
	private EditText edtDetalleNombreJuego;
	private EditText edtDetalleGenero;
	private EditText edtDetallePrecioVenta;
	private ImageView ivDetalleImagen;


	private Producto p;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_detalles_productos);
		ivDetalleImagen = (ImageView) findViewById(R.id.ivDetalleImagen);
		Intent intent = getIntent();
		if(intent != null){
			p =	(Producto) intent.getSerializableExtra(ProductoViewHolder1.EXTRA_DETALLES_PRODUCTO);
			//---------------------- cargo la foto  ----------------------------------------
			byte[] fotobinaria =
					(byte[]) intent.getByteArrayExtra(ProductoViewHolder1.EXTRA_IMAGEN2_PRODUCTO);
			Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobinaria, 200, 200);
			ivDetalleImagen.setImageBitmap(fotobitmap);
		}else{
			p = new Producto();
		}
		//----------------------------------------------------------------
		edtDetalleIdentificador = (EditText) findViewById(R.id.edtDetalleIdentificador);
		edtDetallePlataforma = (EditText) findViewById(R.id.edtDetallePlataforma);
		edtDetalleNombreJuego = (EditText) findViewById(R.id.edtDetalleNombreJuego);
		edtDetalleGenero = (EditText) findViewById(R.id.edtDetalleGenero);
		edtDetallePrecioVenta = (EditText) findViewById(R.id.edtDetallePrecioVenta);
		ivDetalleImagen = (ImageView) findViewById(R.id.ivDetalleImagen);
		//----------------------------------------------------------------
		edtDetalleIdentificador.setText(p.getIdentificador());
		edtDetallePlataforma.setText(p.getPlataforma());
		edtDetalleNombreJuego.setText(p.getNombreJuego());
		edtDetalleGenero.setText(p.getGenero());
		edtDetallePrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
	}

	//----------------------------------------------------
	public void borrar_detalles(View view){
		database = FirebaseDatabase.getInstance();
		myRef = database.getReference("productos");
		myRef.child(p.getIdentificador()).removeValue();
		Toast.makeText(this, "producto borrado", Toast.LENGTH_LONG).show();
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


		String identificador = String.valueOf(edtDetalleIdentificador.getText());
		String plataforma = String.valueOf(edtDetallePlataforma.getText());
		String nombreJuego = String.valueOf(edtDetalleNombreJuego.getText());
		String genero = String.valueOf(edtDetalleGenero.getText());
		double precioVenta = Double.valueOf(String.valueOf(edtDetallePrecioVenta.getText()));
		Producto p1 = new Producto(identificador, plataforma, nombreJuego, genero, precioVenta);
		Map<String, Object> productos = new HashMap<>();
		productos.put(identificador, p1);//puede dar fallo por el identificador (ponía id)
		myRef.updateChildren(productos);
		//------------------------------------- actualizamos la foto -------------------
		if(imagen_seleccionada != null){
			String carpeta = p.getIdentificador();
			ImagenesFirebase.borrarFoto(carpeta, p.getNombreJuego());
			ImagenesFirebase.subirFoto(carpeta, nombreJuego, ivDetalleImagen);
		}
		Toast.makeText(this, "producto actualizado correctamente ", Toast.LENGTH_LONG).show();
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
				ivDetalleImagen.setImageBitmap(bitmap);
				//---------------------------------------------
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}