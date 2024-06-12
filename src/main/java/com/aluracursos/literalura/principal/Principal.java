package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Authors;
import com.aluracursos.literalura.model.Books;
import com.aluracursos.literalura.service.Gutendex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private Scanner keyboard = new Scanner(System.in);

    private final Gutendex consumoApi;

    public Principal(Gutendex consumoApi) {
        this.consumoApi = consumoApi;
    }

    public void clientMenu() {

        var select = -1;
        while (select != 0) {
            var menu = """
                    1- Bucar libro por Titulo.
                    2- Mostrar libros registrados.
                    3- Mostrar autores registrados.
                    4- Listar autores vivos de cierto año.
                    5- Mostrar libros por idiomas.
                    0- Salir
                    """;
            System.out.printf(menu);
            select = keyboard.nextInt();
            keyboard.nextLine();

            switch (select) {
                case 1:
                    findBook();
                    break;
                case 2:
                    showRegisteredBooks();
                    break;
                case 3:
                    showRegisteredAuthors();
                    break;
                case 4:
                    showAuthorsByYear();
                    break;
                case 5:
                    showBooksByLang();
                    break;
                case 0:
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opcion invalida, favor elija una de las opciones en el menu.");
            }
        }
    }

    private void findBook() {
        try {
            System.out.println("Ingrese el título del libro a buscar: ");
            String title = keyboard.nextLine();
            List<Books> books = consumoApi.searchBooksByTitle(title);
            if (books.isEmpty()) {
                System.out.println("No se encontro ningun libro con este titulo.");
            } else {
                books.forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Parece que el nombre que ingreso no es valido.");
        }
    }

    private void showRegisteredBooks() {
        List<Books> books = consumoApi.getRegisteredBooks();
        if (books.isEmpty()) {
            System.out.println("No se ha registrado ningun libro aun, favor hacer una nueva consulta.");
        } else {
            System.out.println("Lista de libros registrados:");
            books.forEach(book -> {
                System.out.println("Título: " + book.getTitle());
                System.out.println("Autor: " + book.getAuthors());
                System.out.println("Idioma: " + book.getLanguages());
                System.out.println("Número de descargas: " + book.getDownloads());
                System.out.println();
            });
        }
    }

    private void showRegisteredAuthors() {
        List<Books> registeredBooks = consumoApi.getRegisteredBooks();
        if (registeredBooks.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            Map<String, Authors> uniqueAuthors = new HashMap<>();
            for (Books book : registeredBooks) {
                Authors authors = book.getAuthor();
                if (authors != null && !uniqueAuthors.containsKey(authors.getName())) {
                    uniqueAuthors.put(authors.getName(), authors);
                }
            }

            for (Map.Entry<String,Authors> entry : uniqueAuthors.entrySet()) {
                Authors authors = entry.getValue();
                System.out.println("Nombre: " + authors.getName());
                System.out.println("Fecha de Nacimiento: " + (authors.getBirth_year() != null ? authors.getBirth_year() : "Desconocida"));
                System.out.println("Fecha de Fallecimiento: " + (authors.getDeath_year() != null ? authors.getDeath_year() : "Desconocida"));
            }
        }
    }

    private void showAuthorsByYear() {
        System.out.println("Ingrese el año de inicio: ");
        int startYear = keyboard.nextInt();
        keyboard.nextLine();
        int currentYear = LocalDate.now().getYear();

        List<Authors> authors = consumoApi.getAuthorsByYearRange(startYear, currentYear);
        if (authors.isEmpty()) {
            System.out.println("No hay autores vivos desde el año especificado hasta la fecha actual.");
        } else {
            System.out.println("Autores vivos desde el año " + startYear + " hasta el año " + currentYear + ":");
            boolean anyLivingAuthor = false;
            for (Authors author : authors) {
                Integer deathYear = author.getDeath_year();
                if (deathYear == null || deathYear > currentYear) {
                    anyLivingAuthor = true;
                    System.out.println("Nombre: " + author.getName());
                    System.out.println("Fecha de nacimiento: " + (author.getBirth_year() != null ? author.getBirth_year() : "Desconocida"));
                    System.out.println("Fecha de muerte: " + (deathYear != null ? deathYear : "Vivo"));
                    System.out.println();
                }
            }
            if (!anyLivingAuthor) {
                System.out.println("No hay autores vivos dentro del rango especificado.");
            }
        }
    }

    private void showBooksByLang() {
        System.out.println("Ingrese el idioma (por ejemplo, 'en' para inglés, 'es' para español): ");
        String lang = keyboard.nextLine();
        List<Books> booksByLang = consumoApi.getRegisteredBooks().stream()
                .filter(book -> book.getLanguages().equalsIgnoreCase(lang))
                .collect(Collectors.toList());
        if (booksByLang.isEmpty()) {
            System.out.println("No existe ningun libro en ese idioma o el idioma que ingreso no es valido.");
        } else {
            booksByLang.forEach(System.out::println);
        }
    }
}
