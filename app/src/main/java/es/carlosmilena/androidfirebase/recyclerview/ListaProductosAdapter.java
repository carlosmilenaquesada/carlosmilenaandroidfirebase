package es.carlosmilena.androidfirebase.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import es.carlosmilena.androidfirebase.R;
import es.carlosmilena.androidfirebase.clases.Producto;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;


public class ListaProductosAdapter extends RecyclerView.Adapter<ProductoViewHolder> {
    // atributos
    private Context contexto = null;
    private ArrayList<Producto> productos = null;
    private LayoutInflater inflate = null;


    public ListaProductosAdapter(Context contexto, ArrayList<Producto> productos ) {
        this.contexto = contexto;
        this.productos = productos;
        inflate =  LayoutInflater.from(this.contexto);
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public ArrayList<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = inflate.inflate(R.layout.item_rv_productos,parent,false);
        ProductoViewHolder evh = new ProductoViewHolder(mItemView,this);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto p = this.getProductos().get(position);
        //----------------------------------------------------------------------
        holder.getTxt_item_nombre().setText("nombre: " + p.getNombre());
        holder.getTxt_item_cantidad().setText("cantidad: " + String.valueOf(p.getCantidad()));
        holder.getTxt_item_precio().setText("precio: " + String.valueOf(p.getPrecio()));
        //----------- codigo para mostrar la foto del producto -----------------------
        String carpeta = p.getIdProducto();
        ImageView imagen = holder.getImg_item_producto();
        ImagenesFirebase.descargarFoto(carpeta,p.getNombre(),imagen);
        ImageView imagen1 = imagen;
        holder.setImg_item_producto(imagen1);
    }


    @Override
    public int getItemCount() {
        return this.productos.size();
    }

    public void addproducto(Producto productoAñadido) {
        productos.add(productoAñadido);
       notifyDataSetChanged();
    }
}
