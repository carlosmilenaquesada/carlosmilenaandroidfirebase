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
	private final Context contexto;
	private ArrayList<Juego> juegos;
	private final LayoutInflater inflate;

	public CatalogoJuegosAdapter(Context contexto, ArrayList<Juego> juegos){
		this.contexto = contexto;
		this.juegos = juegos;
		inflate = LayoutInflater.from(this.contexto);
	}

	@NonNull
	@Override
	public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
		View mItemView = inflate.inflate(R.layout.item_rv_juego, parent, false);
		return new JuegoViewHolder(mItemView, this);
	}

	public Context getContexto(){
		return contexto;
	}

	public ArrayList<Juego> getJuegos(){
		return juegos;
	}

	public void setJuegos(ArrayList<Juego> juegos){
		this.juegos = juegos;
		notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(@NonNull JuegoViewHolder holder, int position){
		Juego p = this.getJuegos().get(position);
		//----------------------------------------------------------------------
		holder.getTvItemPlataforma().setText(getContexto().getResources().getString(R.string.plataforma, p.getPlataforma()));
		holder.getTvItemNombreJuego().setText(getContexto().getResources().getString(R.string.nombre, p.getNombreJuego()));
		holder.getTvItemGenero().setText(getContexto().getResources().getString(R.string.genero,
				p.getGenero()));
		holder.getTvItemPrecioVenta().setText(getContexto().getResources().getString(R.string.precio, String.valueOf(p.getPrecioVenta())));
		//---------------------------------------------------------------------
		ImageView imagen = holder.getIvItemImagen();
		ImagenesFirebase.descargarFoto(p.getIdentificador(),
				p.getPlataforma() + "_" + p.getNombreJuego(), imagen);
	}

	@Override
	public int getItemCount(){
		return this.juegos.size();
	}
}
