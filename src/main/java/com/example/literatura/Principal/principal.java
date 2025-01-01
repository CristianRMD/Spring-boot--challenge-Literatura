package com.example.literatura.Principal;

import com.example.literatura.Model.Autor;
import com.example.literatura.Model.DatosLibro;
import com.example.literatura.Model.DatosResults;
import com.example.literatura.Model.Libro;
import com.example.literatura.Service.ConsumoAPI;
import com.example.literatura.Service.ConvierteDatos;
import com.example.literatura.repository.LibroRepository;
import com.fasterxml.jackson.core.JsonToken;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL = "https://gutendex.com/books/?search=";
    private ConsumoAPI consumoAPI=new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
     private LibroRepository repository ;
     public principal(LibroRepository repository){this.repository=repository;}

    public void MostrarMenu() {



        var opcion = -1;
        while(opcion!=0) {

            System.out.println("Escoja la opcion de su preferencia ");
            System.out.println("""
                    1.-Buscar libro por titulo 
                    2.- Listar libros registrados
                    3.- Listar autores registrados
                    4.-Listar autores vivos en un determinado año
                    5.-Listar libros por idioma
                    0.- Salir
                    
                    """);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    obtenerLibrosxTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivoxAño();
                    break;
                case 5:
                    listarLibrosxIdiomas();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion fuera del rango");
            }
        }





    }




    private void listarLibrosxIdiomas() {
        System.out.println("""
                Los Idiomas dados son :
                es: español 
                en : ingles
                fr : frances
                pt : portugues
                """);
        System.out.println("Escribe una opcion");
        var idioma = teclado.nextLine();
        List<Libro> libros = repository.obtenerLibrosXIdioma(idioma);
        libros.forEach( e->MostrarLibro(e));
     }

    private void listarAutoresVivoxAño() {
        System.out.println("Ingrese el año");
        var anio = teclado.nextInt();
        List<Autor> autoresVivos = repository.obtenerAutoresVivos(anio);
        autoresVivos.forEach(e->MostrarAutores(e));
    }

    private void listarAutoresRegistrados() {
            List<Autor> autores= repository.obtenerAutores();
            autores.forEach(this::MostrarAutores);
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros  = repository.findAll();
        libros.forEach(this::MostrarLibro);

     }

    private void obtenerLibrosxTitulo() {
        System.out.println("Ingresa el nombre del titulo  del libro que esta buscando  ");
        String titulo =teclado.nextLine();
            teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL+titulo.replace(" ","+"));

        var results = conversor.obtenerDatos(json, DatosResults.class);

        if(results.datosLibros().isEmpty()){
            System.out.println("No se encontro el libro");
        }else {
            DatosLibro libro = results.datosLibros().get(0);

            Optional<Libro> libroBuscado = repository.buscarLibro(libro.titulo());

            if (libroBuscado.isPresent()) {
                System.out.println("El libro ya se encuentro registrado en la base de datos");

            } else {
                System.out.println("Libro");
                System.out.println("-----------------------");
                System.out.println("Titulo : " + libro.titulo());
                System.out.println("Autor : " + libro.autor().get(0).nombre());
                System.out.println("Idioma : " + libro.idiomas().get(0));
                System.out.println("Numero de descargas : " + libro.NumDescarga());
                System.out.println("-----------------------");
                GuardarDatos(libro);
            }
        }
    }

    private void GuardarDatos(DatosLibro libro){
    Libro claseLibro = new Libro(libro);
    repository.save(claseLibro);

    }

    private void MostrarLibro(Libro libro){
        System.out.println("Libro");
        System.out.println("-----------------------");
        System.out.println("Titulo : "+libro.getTitulo());
        System.out.println("Autor : "+libro.getAutor().getNombre() );
        System.out.println("Idioma : "+libro.getIdiomas());
        System.out.println("Numero de descargas : "+libro.getNumDescarga());
        System.out.println("-----------------------");
        System.out.println("");
    }

private void MostrarAutores (Autor autor){
    System.out.println("Autor");
    System.out.println("-----------------------");
    System.out.println("Nombre : "+autor.getNombre());
    System.out.println("Fecha de Nacimiento: "+autor.getFechaNacimiento() );
    System.out.println("Fecha de deceso : "+autor.getFechaMuerte());
    System.out.println("Libro : "+autor.getLibros().get(0).getTitulo());
    System.out.println("-----------------------");
    System.out.println("");
}
}
