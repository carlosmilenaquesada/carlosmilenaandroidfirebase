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

import es.carlosmilena.androidfirebase.clases.Juego;
import es.carlosmilena.androidfirebase.recyclerview.CatalogoJuegosAdapter;

public class MostrarCatalogoFirebaseActivity extends AppCompatActivity{
	private FirebaseDatabase database;
	private DatabaseReference myRefJuegos;
	CatalogoJuegosAdapter adapter;
	private ArrayList<Juego> juegos;
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
			Toast.makeText(this, "Debes loguearte para acceder", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, AutenticacionActivity.class);
			startActivity(intent);
		}
	}

	//------------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_catalogo_firebase);
		etBuscarJuego = (EditText) findViewById(R.id.etBuscarJuego);
		//-------------------------------------------------------------
		rvListaJuegos = (RecyclerView) findViewById(R.id.rvListaJuegos);
		//------------------------------------------------------------
		database = FirebaseDatabase.getInstance();
		juegos = new ArrayList<Juego>();
		//-----------------------------------------------------------
		adapter = new CatalogoJuegosAdapter(this, juegos);
		rvListaJuegos.setAdapter(adapter);
		//-----------------------------------------------------------
		myRefJuegos = database.getReference("juegos");

		myRefJuegos.addValueEventListener(new ValueEventListener(){
			@Override
			public void onDataChange(DataSnapshot snapshot){
				// adapter.getJuegos().clear();
				ArrayList<Juego> juegos = new ArrayList<Juego>();
				for(DataSnapshot dataSnapshot : snapshot.getChildren()){
					Juego a = (Juego) dataSnapshot.getValue(Juego.class);
					a.setIdentificador(dataSnapshot.getRef().getKey());
					juegos.add(a);
				}
				adapter.setJuegos(juegos);
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
	public void buscarJuego(View view){
		String nombre = String.valueOf(etBuscarJuego.getText());
		myRefJuegos = database.getReference("juegos");
		myRefJuegos.addValueEventListener(new ValueEventListener(){
			@Override
			public void onDataChange(DataSnapshot snapshot){
				// adapter.getJuegos().clear();
				ArrayList<Juego> juegos = new ArrayList<Juego>();
				if(view.equals(findViewById(R.id.btnBuscarJuego)));
				for(DataSnapshot dataSnapshot : snapshot.getChildren()){
					Juego a = (Juego) dataSnapshot.getValue(Juego.class);
					if(a.getNombreJuego().toLowerCase().contains(nombre.toLowerCase())){
						juegos.add(a);
					}
				}
				adapter.setJuegos(juegos);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(DatabaseError error){
				// Failed to read value
				Log.i("firebase1", String.valueOf(error.toException()));
			}
		});
	}

	public void volver(View view){

		startActivity(new Intent(MostrarCatalogoFirebaseActivity.this, MenuGeneralActivity.class));
	}
}