package es.carlosmilena.androidfirebase.recyclerview1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.carlosmilena.androidfirebase.MostrarDatosFirebaseActivity;
import es.carlosmilena.androidfirebase.MostrarDetallesProductos;
import es.carlosmilena.androidfirebase.R;
import es.carlosmilena.androidfirebase.clases.Producto;
import es.carlosmilena.androidfirebase.utilidades.ImagenesBlobBitmap;

public class ProductoViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final String EXTRA_DETALLES_PRODUCTO = "es.carlosmilena.ejemplo2firebase2324.mostrardetallesproductos.producto";
    public static final String EXTRA_IMAGEN2_PRODUCTO = "es.carlosmilena.ejemplo2firebase2324.mostrardetallesproductos.imagen2";
    private TextView txt_item2_nombre;
    private TextView txt_item2_cantidad;
    private TextView txt_item2_precio;

    private ImageView img_item2_imagen;
    private ListaProductoAdapter1 lpa;

    public ImageView getImg_item2_imagen() {
        return img_item2_imagen;
    }

    public void setImg_item2_imagen(ImageView img_item2_imagen) {
        this.img_item2_imagen = img_item2_imagen;
    }

    public ListaProductoAdapter1 getLpa() {
        return lpa;
    }

    public void setLpa(ListaProductoAdapter1 lpa) {
        this.lpa = lpa;
    }

    public ProductoViewHolder1(@NonNull View itemView, ListaProductoAdapter1 lpa) {
        super(itemView);
        txt_item2_nombre = (TextView) itemView.findViewById(R.id.txt_item2_nombre);
        txt_item2_cantidad = (TextView) itemView.findViewById(R.id.txt_item2_cantidad);
        txt_item2_precio = (TextView) itemView.findViewById(R.id.txt_item2_precio);
        img_item2_imagen = (ImageView) itemView.findViewById(R.id.img_item2_imagen);
        this.lpa = lpa;
        itemView.setOnClickListener(this);
    }

    public TextView getTxt_item2_nombre() {
        return txt_item2_nombre;
    }

    public void setTxt_item2_nombre(TextView txt_item2_nombre) {
        this.txt_item2_nombre = txt_item2_nombre;
    }

    public TextView getTxt_item2_cantidad() {
        return txt_item2_cantidad;
    }

    public void setTxt_item2_cantidad(TextView txt_item2_cantidad) {
        this.txt_item2_cantidad = txt_item2_cantidad;
    }

    public TextView getTxt_item2_precio() {
        return txt_item2_precio;
    }

    public void setTxt_item2_precio(TextView txt_item2_precio) {
        this.txt_item2_precio = txt_item2_precio;
    }

    @Override
    public void onClick(View view) {
       int posicion = getLayoutPosition();
       Producto p = lpa.getProductos().get(posicion);
       Intent intent = new Intent(lpa.getContexto(), MostrarDetallesProductos.class);
       intent.putExtra(EXTRA_DETALLES_PRODUCTO,p);
        img_item2_imagen.buildDrawingCache();
        Bitmap foto_bm = img_item2_imagen.getDrawingCache();
        byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
        intent.putExtra(EXTRA_IMAGEN2_PRODUCTO,fotobytes );
        //intent.putExtra(EXTRA_POSICION_CASILLA, posicion);

       lpa.getContexto().startActivity(intent);
        ((MostrarDatosFirebaseActivity)lpa.getContexto()).finish();

    }
}
