package es.carlosmilena.androidfirebase.recyclerview1;

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

public class ListaProductoAdapter1 extends RecyclerView.Adapter<ProductoViewHolder1>{
	private Context contexto;
	private ArrayList<Producto> productos;
	private LayoutInflater inflate;

	public ListaProductoAdapter1(Context contexto, ArrayList<Producto> productos){
		this.contexto = contexto;
		this.productos = productos;
		inflate = LayoutInflater.from(this.contexto);
	}

	@NonNull
	@Override
	public ProductoViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
		View mItemView = inflate.inflate(R.layout.item_rv_productos1, parent, false);
		ProductoViewHolder1 evh = new ProductoViewHolder1(mItemView, this);
		return evh;
	}

	public Context getContexto(){
		return contexto;
	}

	public void setContexto(Context contexto){
		this.contexto = contexto;
	}

	public ArrayList<Producto> getProductos(){
		return productos;
	}

	public void setProductos(ArrayList<Producto> productos){
		this.productos = productos;
		notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(@NonNull ProductoViewHolder1 holder, int position){
		Producto p = this.getProductos().get(position);
		//----------------------------------------------------------------------
		holder.getTvItemIdentificador().setText("identificador: " + p.getIdentificador());
		holder.getTvItemPlataforma().setText("plataforma: " + p.getPlataforma());
		holder.getTvItemNombreJuego().setText("nombre: " + p.getNombreJuego());
		holder.getTvItemGenero().setText("genero: " + p.getGenero());
		holder.getTvItemPrecioVenta().setText("precio: " + p.getPrecioVenta());
		//---------------------------------------------------------------------
		ImageView imagen = holder.getIvItemImagen();
		ImagenesFirebase.descargarFoto(p.getIdentificador(), p.getNombreJuego(), imagen);
	}

	@Override
	public int getItemCount(){
		return this.productos.size();
	}
}
