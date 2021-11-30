package uniandes.dpoo.taller1.interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import uniandes.dpoo.taller1.modelo.Categoria;
import uniandes.dpoo.taller1.modelo.ExcEliminarLibros;
import uniandes.dpoo.taller1.modelo.ExceptionCategoriaRepetida;
import uniandes.dpoo.taller1.modelo.Libreria;
import uniandes.dpoo.taller1.modelo.Libro;

/**
 * Esta clase representa a la ventana principal de la aplicación
 */
@SuppressWarnings("serial")
public class InterfazLibreria extends JFrame
{

	// ************************************************************************
	// Atributos
	// ************************************************************************

	/**
	 * Esta es la librería que se muestra durante la ejecución de la aplicación
	 */
	private Libreria libreria;

	// ************************************************************************
	// Elementos de la interfaz
	// ************************************************************************

	/**
	 * Este componente corresponde al menú completo que se encuentra en la parte
	 * superior de la ventana
	 */
	private JMenuBar barraMenu;

	/**
	 * Este componente corresponde al menú archivo
	 */
	private JMenu menuArchivo;

	/**
	 * Este componente corresponde a la opción para cargar los archivos de una
	 * librería
	 */
	private JMenuItem menuAbrir;

	/**
	 * Este componente corresponde a la opción para salir de la aplicación
	 */
	private JMenuItem menuSalir;

	/**
	 * Este componente corresponde al panel donde se muestran las categorías
	 * disponibles en la aplicación
	 */
	private PanelCategorias panelCategorias;

	/**
	 * Este componente corresponde al panel donde se muestra una lista de libros
	 */
	private PanelLibros panelLibros;

	/**
	 * Este componente corresponde al panel donde se muestra la información de un
	 * libro
	 */
	private PanelLibro panelLibro;

	/**
	 * Este componente corresponde al panel con los botones de la parte inferior de
	 * la ventana
	 */
	private PanelBotones panelBotones;

	// ************************************************************************
	// Constructores
	// ************************************************************************

	/**
	 * Construye la ventana principal para la aplicación, pero no carga la
	 * información de ninguna librería.
	 * @param archLib 
	 * @param archCat 
	 * @throws IOException 
	 */
	public InterfazLibreria() throws IOException
	{	
		barraMenu = new JMenuBar();
		setJMenuBar(barraMenu);

		menuArchivo = new JMenu("Archivo");
		barraMenu.add(menuArchivo);

		// Setting the accelerator:
		menuAbrir = new JMenuItem("Abrir", KeyEvent.VK_A);
		menuAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menuAbrir.setActionCommand(ListenerMenu.ABRIR_LIBROS);
		menuAbrir.addActionListener(new ListenerMenu(this));
		menuArchivo.add(menuAbrir);

		menuSalir = new JMenuItem("Salir", KeyEvent.VK_Q);
		menuSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menuSalir.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		menuArchivo.add(menuSalir);

		JPanel panelArriba = new JPanel(new GridLayout(1, 2));
		add(panelArriba, BorderLayout.CENTER);

		JPanel panelIzquierdo = new JPanel(new BorderLayout());
		panelArriba.add(panelIzquierdo);

		panelCategorias = new PanelCategorias(this);
		panelIzquierdo.add(panelCategorias, BorderLayout.NORTH);

		panelLibros = new PanelLibros(this);
		panelIzquierdo.add(panelLibros, BorderLayout.CENTER);

		JPanel panelDerecha = new JPanel(new BorderLayout());
		panelArriba.add(panelDerecha);
		panelLibro = new PanelLibro();
		panelDerecha.add(panelLibro, BorderLayout.CENTER);

		JPanel panelAbajo = new JPanel(new BorderLayout());
		panelBotones = new PanelBotones(this);
		panelAbajo.add(panelBotones, BorderLayout.CENTER);
		add(panelAbajo, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Librería");
		setSize(1000, 700);
		setVisible(true);
	}

	// ************************************************************************
	// Métodos
	// ************************************************************************

	/**
	 * Carga la información de una librería a partir de los archivos datos,
	 * construye un objeto Librería con esa información y lo deja en el atributo
	 * llamado 'libreria'
	 * 
	 * @param archivo_categorias El archivo que tiene la información de las
	 *                           categorías que se usarán para los libros
	 * @param archivo_libros     El archivo que tiene la información de los libros
	 */
	public void cargarArchivos(File archivo_categorias, File archivo_libros)
	{
		try
		{
			libreria = new Libreria(archivo_categorias.getPath(), archivo_libros.getPath());
			panelCategorias.actualizarCategorias(libreria.darCategorias());
			ArrayList<Categoria> nuevas= libreria.ReturnCategorias_new();
			String msg="";
			for (Categoria categoria : nuevas) {
				msg+="Agregada: "+categoria.darNombre()+"\n";
			}
			if(msg.length()!=0) {
				JOptionPane.showMessageDialog(this, msg, "Error de lectura",
						JOptionPane.ERROR_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(this, "No se crearon nuevas categorias", "Error de lectura",
						JOptionPane.WARNING_MESSAGE);
			}
			
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Error al cargar", "Error de lectura",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	/**
	 * Cambia la categoría para la cual se deben mostrar los libros en el panel
	 * panelLibros
	 * 
	 * @param categoria La categoría para la que se deben mostrar los libros ahora
	 */
	public void cambiarCategoria(Categoria categoria)
	{
		ArrayList<Libro> libros = libreria.darLibros(categoria.darNombre());
		panelLibros.actualizarLibros(libros);
		mostrarLibro(libros.get(0));
	}

	/**
	 * Cambia el libro para el cual se debe mostrar la información en el panel
	 * panelLibro
	 * 
	 * @param libro El libro para el que se debe mostrar la información
	 */
	public void mostrarLibro(Libro libro)
	{
		panelLibro.actualizarLibro(libro);
	}

	/**
	 * Le pide al usuario el título de un libro y lo busca en la librería.
	 * 
	 * Si existe un libro, le muestra al usuario la información del libro en el
	 * panel 'panelLibro'.
	 */
	public void buscarLibro()
	{
		String titulo = JOptionPane.showInputDialog(this, "Escriba el título del libro que busca", "titulo");
		if (titulo != null)
		{
			Libro libro = libreria.buscarLibro(titulo);
			if (libro == null)
			{
				JOptionPane.showMessageDialog(this, "No se encontró un libro con ese título", "No hay libro",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				mostrarLibro(libro);
			}
		}
	}

	/**
	 * Busca los libros de un autor a partir de una parte del nombre del autor que
	 * haya dado el usuario.
	 * 
	 * La lista de libros que correspondan al autor dado se muestra en el panel
	 * panelLibros.
	 */
	public void buscarLibrosAutor()
	{
		String autor = JOptionPane.showInputDialog(this, "Escriba al menos una parte del autor que busca", "autor");
		if (autor != null)
		{
			ArrayList<Libro> libros = libreria.buscarLibrosAutor(autor);
			if (libros.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "No hay ningún autor con ese nombre", "No hay libro",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				panelLibros.actualizarLibros(libros);
				mostrarLibro(libros.get(0));
			}
		}
	}

	/**
	 * Le pide al usuario el nombre de un autor y le informa en qué categorías hay
	 * libros de ese autor.
	 */
	public void buscarCategoriasAutor()
	{

		String autor = JOptionPane.showInputDialog(this, "Escriba el nombre del autor que está buscando", "autor");
		if (autor != null)
		{
			ArrayList<Categoria> categorias = libreria.buscarCategoriasAutor(autor);
			if (categorias.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "No hay ningún autor con ese nombre", "No hay libro",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				String mensaje = "Hay libros de ese autor en las siguientes categorías:\n";
				for (Categoria categoria : categorias)
				{
					mensaje += " " + categoria.darNombre() + "\n";
				}
				JOptionPane.showMessageDialog(this, mensaje, "Categorías", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/**
	 * Le informa al usuario la calificación promedio de los libros de la librería,
	 * con base en la información disponible en cada uno de los libros.
	 */
	public void calcularCalificacionPromedio()
	{
		double calificacion = libreria.calificacionPromedio();
		calificacion = (double) ((int) calificacion * 1000) / 1000;
		JOptionPane.showMessageDialog(this, "La calificación promedio de los libros es " + calificacion,
				"Calificación promedio", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Le informa al usuario cuál es la categoría con más libros en la librería.
	 */
	public void categoriaConMasLibros()
	{
		Categoria cat = libreria.categoriaConMasLibros();
		int cantidad = cat.contarLibrosEnCategoria();
		String mensaje = "La categoría con más libros es " + cat.darNombre() + " y tiene " + cantidad + " libros";
		JOptionPane.showMessageDialog(this, mensaje, "Categoría con más libros", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Le informa al usuario la cantidad de libros en la librería para los cuales no
	 * se tiene una portada.
	 */
	public void contarSinPortada()
	{
		int cantidad = libreria.contarLibrosSinPortada();
		String mensaje = "Hay " + cantidad + " libros sin portada";
		JOptionPane.showMessageDialog(this, mensaje, "Libros sin portada", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Le informa al usuario cuál es la categoría cuyos libros están mejor
	 * calificados.
	 */
	public void categoriaMejorCalificacion()
	{
		Categoria cat = libreria.categoriaConMejoresLibros();
		double calificacion = cat.calificacionPromedio();
		calificacion = (double) ((int) calificacion * 1000) / 1000;
		String mensaje = "La categoría con la mejor calificación es " + cat.darNombre()
				+ ".\nLa calificación promedio de los libros es " + calificacion;
		JOptionPane.showMessageDialog(this, mensaje, "Categoría con mejor calificación promedio",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Le informa al usuario si hay un autor que tenga libros en más de una
	 * categoría.
	 */
	public void hayAutorEnVariasCategorias()
	{
		boolean hay = libreria.hayAutorEnVariasCategorias();
		String mensaje = "No hay ningún autor con al menos un libro en dos categorías diferentes.";
		if (hay)
		{
			mensaje = "Hay al menos un autor con al menos un libro en dos categorías diferentes.";
		}
		JOptionPane.showMessageDialog(this, mensaje, "Consulta", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void RenombrarCategoria(){
		String NombreViejo=JOptionPane.showInputDialog("Nombre de la categoria a cambiar:");
		String NombreNuevo=JOptionPane.showInputDialog("Nombre nuevo");
		if(NombreNuevo.equals(NombreViejo)) {
			JOptionPane.showMessageDialog(this,"El nombre viejo y el nuevo no pueden ser iguales","Cuidado",JOptionPane.ERROR_MESSAGE);
		}else {
			try {
				libreria.RenombrarCategoria(NombreViejo, NombreNuevo);
			}catch(ExceptionCategoriaRepetida ec) {
				JOptionPane.showMessageDialog(this,ec.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void eliminarLibros() {
		String grupoAutores = JOptionPane.showInputDialog("Grupo de autores: ");
		String[] arrayStr = grupoAutores.split(",");
		ArrayList<String> autores = new ArrayList<String>(Arrays.asList(arrayStr));
		try {
			int resp = libreria.eliminarLibros(autores);
			JOptionPane.showMessageDialog(this, this, "Operaci�n exitosa, se elimaron " + String.valueOf(resp) + "libros", JOptionPane.INFORMATION_MESSAGE);
		}catch(ExcEliminarLibros ec) {
			JOptionPane.showMessageDialog(this,ec.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	

	// ************************************************************************
	// Main
	// ************************************************************************

	/**
	 * Método inicial de la aplicación
	 * 
	 * @param args Parámetros introducidos por el usuario en la línea de comandos
	 * @throws IOException
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException
	{
		/*
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
		{
			if ("Nimbus".equals(info.getName()))
			{
				UIManager.setLookAndFeel(info.getClassName());
				break;
			}
		}
		*/
		FlatLightLaf.install();
		// UIManager.setLookAndFeel( new FlatDarculaLaf());

		new InterfazLibreria();
	}



}
