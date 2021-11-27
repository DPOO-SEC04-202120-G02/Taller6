package uniandes.dpoo.taller1.modelo;

public class ExceptionCategoriaRepetida extends Exception{

	public ExceptionCategoriaRepetida() {
		super("Ya hay otra categoria con este nombre.");
	}

}
