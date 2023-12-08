package es.carlosmilena.androidfirebase.clases;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Juego implements Serializable{
	private String identificador;
	private final String plataforma;
	private final String nombreJuego;
	private final String genero;
	private final double precioVenta;

	public Juego(String identificador, String plataforma, String nombreJuego, String genero,
				 double precioVenta){
		this.identificador = identificador;
		this.plataforma = plataforma;
		this.nombreJuego = nombreJuego;
		this.genero = genero;
		this.precioVenta = precioVenta;
	}

	public Juego(){
		this.identificador = "";
		this.plataforma = "";
		this.nombreJuego = "";
		this.genero = "";
		this.precioVenta = 0.0;
	}

	public void setIdentificador(String identificador){
		this.identificador = identificador;
	}

	public String getIdentificador(){
		return identificador;
	}

	public String getPlataforma(){
		return plataforma;
	}

	public String getNombreJuego(){
		return nombreJuego;
	}

	public String getGenero(){
		return genero;
	}

	public double getPrecioVenta(){
		return precioVenta;
	}

	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		Juego juego = (Juego) o;
		return identificador.equals(juego.identificador);
	}

	@Override
	public int hashCode(){
		return Objects.hash(identificador);
	}

	@NonNull
	@Override
	public String toString(){
		return "Juego{" + "identificador='" + identificador + '\'' + ", plataforma='" + plataforma +
			   '\'' + ", nombreJuego='" + nombreJuego + '\'' + ", genero='" + genero + '\'' +
			   ", precioVenta=" + precioVenta + '}';
	}
}
