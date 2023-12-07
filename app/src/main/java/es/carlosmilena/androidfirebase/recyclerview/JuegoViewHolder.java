package es.carlosmilena.androidfirebase.recyclerview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.carlosmilena.androidfirebase.MostrarCatalogoFirebaseActivity;
import es.carlosmilena.androidfirebase.ModificarJuegoActivity;
import es.carlosmilena.androidfirebase.R;
import es.carlosmilena.androidfirebase.clases.Juego;
import es.carlosmilena.androidfirebase.utilidades.ImagenesBlobBitmap;

public class JuegoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
	public static final String EXTRA_DETALLES_PRODUCTO = "es.carlosmilena.androidfirebase" +
                                                         ".mostrardetallesproductos.juego";
	public static final String EXTRA_IMAGEN2_PRODUCTO = "es.carlosmilena.androidfirebase" +
                                                        ".mostrardetallesproductos.imagen2";
	private TextView tvItemIdentificador;
	private TextView tvItemPlataforma;
	private TextView tvItemNombreJuego;
	private TextView tvItemGenero;
	private TextView tvItemPrecioVenta;
	private ImageView ivItemImagen;
	private CatalogoJuegosAdapter lpa;


	public JuegoViewHolder(@NonNull View itemView, CatalogoJuegosAdapter lpa){
		super(itemView);
		tvItemNombreJuego = (TextView) itemView.findViewById(R.id.tvItemIdentificador);
		tvItemGenero = (TextView) itemView.findViewById(R.id.tvItemPlataforma);
		tvItemPrecioVenta = (TextView) itemView.findViewById(R.id.tvItemNombreJuego);
		ivItemImagen = (ImageView) itemView.findViewById(R.id.ivItemImagen);
		this.lpa = lpa;
		itemView.setOnClickListener(this);
	}

    public TextView getTvItemIdentificador(){
        return tvItemIdentificador;
    }

    public void setTvItemIdentificador(TextView tvItemIdentificador){
        this.tvItemIdentificador = tvItemIdentificador;
    }

    public TextView getTvItemPlataforma(){
        return tvItemPlataforma;
    }

    public void setTvItemPlataforma(TextView tvItemPlataforma){
        this.tvItemPlataforma = tvItemPlataforma;
    }

    public TextView getTvItemNombreJuego(){
        return tvItemNombreJuego;
    }

    public void setTvItemNombreJuego(TextView tvItemNombreJuego){
        this.tvItemNombreJuego = tvItemNombreJuego;
    }

    public TextView getTvItemGenero(){
        return tvItemGenero;
    }

    public void setTvItemGenero(TextView tvItemGenero){
        this.tvItemGenero = tvItemGenero;
    }

    public TextView getTvItemPrecioVenta(){
        return tvItemPrecioVenta;
    }

    public void setTvItemPrecioVenta(TextView tvItemPrecioVenta){
        this.tvItemPrecioVenta = tvItemPrecioVenta;
    }

    public ImageView getIvItemImagen(){
        return ivItemImagen;
    }

    public void setIvItemImagen(ImageView ivItemImagen){
        this.ivItemImagen = ivItemImagen;
    }

    public CatalogoJuegosAdapter getLpa(){
        return lpa;
    }

    public void setLpa(CatalogoJuegosAdapter lpa){
        this.lpa = lpa;
    }

    @Override
	public void onClick(View view){
		int posicion = getLayoutPosition();
		Juego p = lpa.getProductos().get(posicion);
		Intent intent = new Intent(lpa.getContexto(), ModificarJuegoActivity.class);
		intent.putExtra(EXTRA_DETALLES_PRODUCTO, p);
		ivItemImagen.buildDrawingCache();
		Bitmap foto_bm = ivItemImagen.getDrawingCache();
		byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
		intent.putExtra(EXTRA_IMAGEN2_PRODUCTO, fotobytes);
		//intent.putExtra(EXTRA_POSICION_CASILLA, posicion);
		lpa.getContexto().startActivity(intent);
		((MostrarCatalogoFirebaseActivity) lpa.getContexto()).finish();
	}
}
