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
import es.carlosmilena.androidfirebase.clases.Juego;
import es.carlosmilena.androidfirebase.utilidades.ImagenesFirebase;

public class CatalogoJuegosAdapter extends RecyclerView.Adapter<JuegoViewHolder>{
	private Context contexto;
	private ArrayList<Juego> juegos;
	private LayoutInflater inflate;

	public CatalogoJuegosAdapter(Context contexto, ArrayList<Juego> juegos){
		this.contexto = contexto;
		this.juegos = juegos;
		inflate = LayoutInflater.from(this.contexto);
	}

	@NonNull
	@Override
	public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
		View mItemView = inflate.inflate(R.layout.item_rv_juego, parent, false);
		JuegoViewHolder evh = new JuegoViewHolder(mItemView, this);
		return evh;
	}

	public Context getContexto(){
		return contexto;
	}

	public void setContexto(Context contexto){
		this.contexto = contexto;
	}

	public ArrayList<Juego> getProductos(){
		return juegos;
	}

	public void setProductos(ArrayList<Juego> juegos){
		this.juegos = juegos;
		notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(@NonNull JuegoViewHolder holder, int position){
		Juego p = this.getProductos().get(position);
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
		return this.juegos.size();
	}
}
