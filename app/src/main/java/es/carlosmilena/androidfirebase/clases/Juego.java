package es.carlosmilena.androidfirebase.clases;

import java.io.Serializable;
import java.util.Objects;

public class Juego implements Serializable{
	private String identificador;
	private String plataforma;
	private String nombreJuego;
	private String genero;
	private double precioVenta;

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

	public String getIdentificador(){
		return identificador;
	}

	public void setIdentificador(String identificador){
		this.identificador = identificador;
	}

	public String getPlataforma(){
		return plataforma;
	}

	public void setPlataforma(String plataforma){
		this.plataforma = plataforma;
	}

	public String getNombreJuego(){
		return nombreJuego;
	}

	public void setNombreJuego(String nombreJuego){
		this.nombreJuego = nombreJuego;
	}

	public String getGenero(){
		return genero;
	}

	public void setGenero(String genero){
		this.genero = genero;
	}

	public double getPrecioVenta(){
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta){
		this.precioVenta = precioVenta;
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

	@Override
	public String toString(){
		return "Juego{" + "identificador='" + identificador + '\'' + ", plataforma='" +
			   plataforma + '\'' + ", nombreJuego='" + nombreJuego + '\'' + ", genero='" + genero +
			   '\'' + ", precioVenta=" + precioVenta + '}';
	}
}
