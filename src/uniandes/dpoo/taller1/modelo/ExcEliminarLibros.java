package uniandes.dpoo.taller1.modelo;

import java.util.ArrayList;

public class ExcEliminarLibros extends Exception{

	public ExcEliminarLibros(ArrayList<String> noPosibles) {
		
		super("Imposible. No se pueden eliminar los siguientes autores: " + construirStr(noPosibles));
	}
	
	public static String construirStr(ArrayList<String> noPosibles) {
		String commaseparatedlist = noPosibles.toString();
		 
        commaseparatedlist
            = commaseparatedlist.replace("[", "")
                  .replace("]", "")
                  .replace(" ", "");
        return commaseparatedlist;
	}
}
