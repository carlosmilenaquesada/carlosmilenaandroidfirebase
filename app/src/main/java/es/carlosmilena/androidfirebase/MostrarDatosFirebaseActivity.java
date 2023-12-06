package es.carlosmilena.androidfirebase;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.carlosmilena.androidfirebase.clases.Producto;
import es.carlosmilena.androidfirebase.recyclerview1.ListaProductoAdapter1;

public class MostrarDatosFirebaseActivity extends AppCompatActivity{
	private FirebaseDatabase database;
	private DatabaseReference myRefProductos;
	ListaProductoAdapter1 adapter;
	private ArrayList<Producto> juegos;
	private EditText etBuscarJuego;
	private RecyclerView rvListaJuegos;

	@Override
	protected void onStart(){
		super.onStart();
		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		FirebaseUser usuarioActual = mAuth.getCurrentUser();
		if(usuarioActual != null){
			usuarioActual.reload();
		}else{
			Toast.makeText(this, "debes loguearte primero", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	//------------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_datos_firebase);
		etBuscarJuego = (EditText) findViewById(R.id.etBuscarJuego);
		//-------------------------------------------------------------
		rvListaJuegos = (RecyclerView) findViewById(R.id.rvListaJuegos);
		//------------------------------------------------------------
		database = FirebaseDatabase.getInstance();
		juegos = new ArrayList<Producto>();
		//-----------------------------------------------------------
		adapter = new ListaProductoAdapter1(this, juegos);
		rvListaJuegos.setAdapter(adapter);
		//-----------------------------------------------------------
		myRefProductos = database.getReference("productos");
		myRefProductos.addValueEventListener(new ValueEventListener(){
			@Override
			public void onDataChange(DataSnapshot snapshot){
				// adapter.getProductos().clear();
				ArrayList<Producto> productos = new ArrayList<Producto>();
				for(DataSnapshot dataSnapshot : snapshot.getChildren()){
					Producto a = (Producto) dataSnapshot.getValue(Producto.class);
					productos.add(a);
				}
				adapter.setProductos(productos);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(DatabaseError error){
				// Failed to read value
				Log.i("firebase1", String.valueOf(error.toException()));
			}
		});
		//-----------------------------------------------------------------
		int orientation = getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_LANDSCAPE){
			// In landscape
			rvListaJuegos.setLayoutManager(new GridLayoutManager(this, 2));
		}else{
			// In portrait
			rvListaJuegos.setLayoutManager(new LinearLayoutManager(this));
		}
	}

	//-----------------------------------------------------------
	public void buscarProductos1(View view){
		String nombre = String.valueOf(etBuscarJuego.getText());
		myRefProductos = database.getReference("productos");
		myRefProductos.addValueEventListener(new ValueEventListener(){
			@Override
			public void onDataChange(DataSnapshot snapshot){
				// adapter.getProductos().clear();
				ArrayList<Producto> productos = new ArrayList<Producto>();
				for(DataSnapshot dataSnapshot : snapshot.getChildren()){
					Producto a = (Producto) dataSnapshot.getValue(Producto.class);
					if(a.getNombreJuego().toLowerCase().contains(nombre.toLowerCase())){
						productos.add(a);
					}
				}
				adapter.setProductos(productos);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(DatabaseError error){
				// Failed to read value
				Log.i("firebase1", String.valueOf(error.toException()));
			}
		});
	}
}