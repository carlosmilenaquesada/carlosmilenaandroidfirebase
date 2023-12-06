package es.carlosmilena.androidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.carlosmilena.androidfirebase.clases.Producto;
import es.carlosmilena.androidfirebase.recyclerview.ListaProductosAdapter;

public class MostrarProductosActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private ArrayList<Producto> productos;
    private ListaProductosAdapter adapter;
    private RecyclerView rv_productos;
    private EditText edt_buscar_productos;
    private DatabaseReference myRefProductos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos);
        //--------------------------------------------------------
        database = FirebaseDatabase.getInstance(); // te conecta a la base de datos
        rv_productos = (RecyclerView) findViewById(R.id.rv_productos1);
        edt_buscar_productos = (EditText) findViewById(R.id.edt_buscar_productos);

/*
        // LEER UN ALUMNO CON CLAVE "clave2"
        // Read from the database alumno key -> "clave2"
        DatabaseReference myRefproductos = database.getReference("productos");
        myRefproductos.child("p23").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Producto value = dataSnapshot.getValue(Producto.class);
                if(value != null) {
                    System.out.println(value.toString());
                    Log.i("firebase1", value.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i( "firebase1", String.valueOf(error.toException()));
            }
        });
        */
/*
        //--------------------------------------------------------
       // LEER TODOS LOS productos
        DatabaseReference myRefproductos = database.getReference("productos");
        myRefproductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<String>();
                List<Producto> productos = new ArrayList<Producto>();
                for (DataSnapshot keynode : snapshot.getChildren()) {
                    keys.add(keynode.getKey());
                    productos.add(keynode.getValue(Producto.class));
                }
                for (String k : keys) {
                    System.out.println(k);
                    Log.i("firebase1", "clave leida " + k);
                }
                for (Producto a : productos) {
                    System.out.println(a.toString());
                    Log.i("firebase1", "producto leido " + a.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

        productos = new ArrayList<Producto>();

        //-----------------------------------------------------------
        adapter = new ListaProductosAdapter(this,productos);
        rv_productos.setAdapter(adapter);
        myRefProductos = database.getReference("productos");
        myRefProductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               // adapter.getProductos().clear();
                ArrayList<Producto> productos = new ArrayList<Producto>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Producto a = (Producto) dataSnapshot.getValue(Producto.class);
                    productos.add(a);
                }
                adapter.setProductos(productos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i( "firebase1", String.valueOf(error.toException()));
            }
        });

        //-------------------------------------------------------------
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            rv_productos.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            // In portrait
            rv_productos.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    //---------------------------------------------------------------------------------
    public void buscarProductos(View view) {

        String texto = String.valueOf(edt_buscar_productos.getText());
        myRefProductos = database.getReference("productos");
        myRefProductos.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             //   ArrayList<String> keys1 = new ArrayList<String>();
                ArrayList<Producto> productos = new ArrayList<Producto>();
                for (DataSnapshot keynode : snapshot.getChildren()) {
            //        keys1.add(keynode.getKey());
                    Producto a = keynode.getValue(Producto.class);
                    if (a.getNombre().contains(texto)) {
                        productos.add(keynode.getValue(Producto.class));
                    }
                }
                adapter.setProductos(productos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("firebase1", String.valueOf(error.toException()));
            }
        });
    }
}