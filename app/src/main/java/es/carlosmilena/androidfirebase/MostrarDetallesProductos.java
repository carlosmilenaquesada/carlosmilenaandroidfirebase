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
import java.util.HashMap;
import java.util.Map;

import es.carlosmilena.androidfirebase.clases.Producto;
import es.carlosmilena.androidfirebase.recyclerview1.ProductoViewHolder1;
import es.carlosmilena.androidfirebase.utilidades.ImagenesBlobBitmap;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;

public class MostrarDetallesProductos extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private EditText edt_detalles_idp;
    private EditText edt_detalles_nombrep;
    private EditText edt_detalles_cantidad;
    private EditText edt_detalles_preciop;

    private ImageView img_detalles_foto;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;

    private Producto p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_detalles_productos);
        img_detalles_foto = (ImageView) findViewById(R.id.img_detalles_producto);
        Intent intent = getIntent();
        if(intent != null)
        {
            p = (Producto) intent.getSerializableExtra(ProductoViewHolder1.EXTRA_DETALLES_PRODUCTO);
            //---------------------- cargo la foto  ----------------------------------------

            byte[] fotobinaria = (byte[]) intent.getByteArrayExtra(ProductoViewHolder1.EXTRA_IMAGEN2_PRODUCTO);
            Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobinaria, 200,200);
            img_detalles_foto.setImageBitmap(fotobitmap);
        }
        else{
            p = new Producto();
        }
        //----------------------------------------------------------------
        edt_detalles_idp = (EditText) findViewById(R.id.edt_detalles_idp);
        edt_detalles_cantidad = (EditText) findViewById(R.id.edt_detalles_cantidadp);
        edt_detalles_nombrep = (EditText) findViewById(R.id.edt_detalles_nombrep);
        edt_detalles_preciop = (EditText) findViewById(R.id.edt_detalles_preciop);
        img_detalles_foto = (ImageView) findViewById(R.id.img_detalles_producto);
        //----------------------------------------------------------------
        edt_detalles_idp.setText(p.getIdProducto());
        edt_detalles_nombrep.setText(p.getNombre());
        edt_detalles_cantidad.setText(String.valueOf(p.getCantidad()));
        edt_detalles_preciop.setText(String.valueOf(p.getPrecio()));

    }

    //----------------------------------------------------
    public void borrar_detalles(View view)
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("productos");
        myRef.child(p.getIdProducto()).removeValue();
        Toast.makeText(this,"producto borrado", Toast.LENGTH_LONG).show();
        //---------------------- borramos la imagen del firebase ------------------
        // borramos la imagen del firebase store

        String carpeta = p.getIdProducto();
        ImagenesFirebase.borrarFoto(carpeta,p.getNombre());

        finish();
    }
    //---------------------------------------------------
    public void editar_detalles(View view)
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("productos");
        String id = String.valueOf(edt_detalles_idp.getText());
        String nombre = String.valueOf(edt_detalles_nombrep.getText());
        int cantidad = Integer.valueOf(String.valueOf(edt_detalles_cantidad.getText()));
        double precio = Double.valueOf(String.valueOf(edt_detalles_preciop.getText()));
        Producto p1 = new Producto(id,nombre,cantidad,precio);
        Map<String,Object> productos = new HashMap<>();
        productos.put(id,p1);
        myRef.updateChildren(productos);
        //------------------------------------- actualizamos la foto -------------------

            if(imagen_seleccionada != null ) {
            String carpeta = p.getIdProducto();
            ImagenesFirebase.borrarFoto(carpeta, p.getNombre());

            ImagenesFirebase.subirFoto(carpeta,nombre, img_detalles_foto);
        }

        Toast.makeText(this,"producto actualizado correctamente ", Toast.LENGTH_LONG).show();
        finish();
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
                img_detalles_foto.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}