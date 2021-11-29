package uniandes.dpoo.taller1.modelo;

public class ExcEliminarLibros extends Exception{

	public ExcEliminarLibros() {
		super("Ya hay otra categoria con este nombre.");
	}

}
