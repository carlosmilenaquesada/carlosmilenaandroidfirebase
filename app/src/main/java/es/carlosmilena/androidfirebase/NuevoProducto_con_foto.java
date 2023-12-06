package es.carlosmilena.androidfirebase;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import es.carlosmilena.androidfirebase.clases.Producto;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;

public class NuevoProducto_con_foto extends AppCompatActivity {

    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    private EditText edt_nuevo_idp;
    private EditText edt_nuevo_nombrep;
    private EditText edt_nuevo_cantidadp;
    private EditText edt_nuevo_preciop;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    ImageView img_nuevop_foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto_con_foto);
        edt_nuevo_idp = (EditText) findViewById(R.id.edt_nuevo_idp_foto);
        edt_nuevo_nombrep = (EditText) findViewById(R.id.edt_nuevo_nombrep_foto);
        edt_nuevo_cantidadp = (EditText) findViewById(R.id.edt_nuevo_cantidadp_foto);
        edt_nuevo_preciop = (EditText) findViewById(R.id.edt_nuevo_preciop_foto);
        img_nuevop_foto = (ImageView) findViewById(R.id.img_nuevop_foto);
        //-------------------------------------------------------------------------
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }


    public void insertarProducto_foto(View view)
    {
        String identificador = String.valueOf(edt_nuevo_idp.getText());
        String nombre = String.valueOf(edt_nuevo_nombrep.getText());
        int cantidad = Integer.valueOf(String.valueOf(edt_nuevo_cantidadp.getText()));
        double precio = Double.valueOf(String.valueOf(edt_nuevo_preciop.getText()));
        Producto p1 = new Producto(identificador,nombre,cantidad,precio);
        myRef.child("productos").child(p1.getIdProducto()).setValue(p1);
        //---------------------- codigo para añadir la foto al storage ------------------------------
        // codigo para guardar la imagen del usuario en firebase store
        if(imagen_seleccionada != null) {
            String carpeta = p1.getIdProducto();
            ImagenesFirebase.subirFoto(carpeta,p1.getNombre(), img_nuevop_foto);
        }
        Toast.makeText(this,"producto añadido correctamente ", Toast.LENGTH_LONG).show();
    }




    //--------------------------------------------------------------------------
    //--------CODIGO PARA CAMBIAR LA IMAGEN----------------

    public void cambiar_imagen(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK) {
            imagen_seleccionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen_seleccionada);
                img_nuevop_foto.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}