package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.UsersDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.UserNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.mappers.BookMapperImplementation;
import com.mohamedelhaddioui.Recommendation.System.Book.security.configurations.JwtService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookRatingService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/books")
@RestController
public class BookController {
    private BookService bookService;
    private JwtService jwtService;
    private UserService userService;
    private BookRatingService bookRatingService;
    @Autowired
    BookMapperImplementation dtoMapper;
    public BookController(BookService bookService,JwtService jwtService, UserService userService,BookRatingService bookRatingService)
    {
        this.bookService = bookService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.bookRatingService = bookRatingService;

    }
    @GetMapping("")
    public List<book> getbooks(){
        List<book> list = bookService.getListBooks();
        return list;
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/x/search/book")
    public BooksDTO getBooks(@RequestParam(name = "keyword", defaultValue = "") String keyword, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "pagesize", defaultValue = "5") int pagesize, @RequestParam(name = "choix", defaultValue = "nom") String choix) throws  BookNotFoundException {
        BooksDTO booksDTO = bookService.getBooks("%" + keyword + "%",choix, page,pagesize);
        return booksDTO;
    }
    @GetMapping("/{bookId}")
    public book getBook(@PathVariable String bookId){
        book book = bookService.getBook(bookId);
        return book;
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/size")
    public int getSizeBooks(){
        int size = bookService.getListBooks().size();
        return size;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
        System.out.println("delete book");
        bookService.deletebook(bookId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/update/{bookId}")
    public ResponseEntity<book> updateBook(@PathVariable String bookId, @RequestBody BookDTO bookDTO) {
        book book =  bookService.updateBook(bookId, bookDTO);
        return ResponseEntity.ok(book);
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("/save")
    public ResponseEntity<book> savebook(@RequestBody BookDTO bookDTO)
    {
        book book = bookService.saveBookDTO(bookDTO);
        return ResponseEntity.ok(book);
    }
    @GetMapping("/search")
    public BooksDTO getBooksByName(@RequestParam(name = "keyword", defaultValue = "") String keyword, @RequestParam(name = "page", defaultValue = "0") int page) throws BookNotFoundException {
        BooksDTO booksDTO = bookService.getBookByNom("%" + keyword + "%", page);
        return booksDTO;
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    @GetMapping("/user")
    public BooksDTO getBooksRatedByAuthenticatedUser(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "0") int page) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        List<book> books = bookRatingService.getBooksRatedByUser(user);
        Page<book> booksPages = convertListToPage(books,page,5) ;
        List<BookDTO> bookDTOList=booksPages.getContent().stream().map(c->dtoMapper.fromBook(c)).collect(Collectors.toList());
        BooksDTO booksDTO= new BooksDTO();
        booksDTO.setBookDTOList(bookDTOList);
        booksDTO.setTotalpage(booksPages.getTotalPages());
        return booksDTO;
    }
    public Page<book> convertListToPage(List<book> books, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        List<book> subList = books.subList(start, end);
        return new PageImpl<>(subList, pageable, books.size());
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    @GetMapping("/recommend")
    public BooksDTO getBooksrecommendforUser(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "0") int page) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);

        try {
            // L'URL de l'API Python
            String urlString = "http://127.0.0.1:5000/recommend";
            URL url = new URL(urlString);

            // Ouvrir une connexion
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Définir la méthode comme POST
            conn.setRequestMethod("POST");

            // Définir les en-têtes pour indiquer du JSON
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            // Activer l'entrée/sortie
            conn.setDoOutput(true);

            // Les données JSON à envoyer dans la requête
            String jsonInputString = "{\"userId\": " + userId + "}";

            // Écrire les données JSON dans le flux de sortie
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Obtenir le code de réponse
            int responseCode = conn.getResponseCode();
            System.out.println("Status Code: " + responseCode);

            // Lire la réponse depuis le flux d'entrée
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parser la réponse JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray recommendationsArray = jsonResponse.getJSONArray("recommendations");

                // Créer une liste de livres à partir des recommandations
                List<book> books = new ArrayList<>();
                for (int i = 0; i < recommendationsArray.length(); i++) {
                    JSONObject recommendation = recommendationsArray.getJSONObject(i);
                    String isbn = recommendation.getString("ISBN");
                    double predictedRating = recommendation.getDouble("predicted_rating");

                    // Créer un objet Book à partir de l'ISBN et de la prédiction
                    book book = bookService.getBook(isbn);
                    books.add(book);
                }

                // Pagination des résultats
                Page<book> booksPages = convertListToPage(books, page, 20);

                // Convertir les entités Book en DTO
                List<BookDTO> bookDTOList = booksPages.getContent().stream()
                        .map(book -> dtoMapper.fromBook(book))
                        .collect(Collectors.toList());

                // Créer l'objet BooksDTO pour la réponse
                BooksDTO booksDTO = new BooksDTO();
                booksDTO.setBookDTOList(bookDTOList);
                booksDTO.setTotalpage(booksPages.getTotalPages());

                return booksDTO;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/similaires/{isbn}")
    public BooksDTO getBooksRecommendForBook(@PathVariable("isbn") String isbn, @RequestParam(name = "page", defaultValue = "0") int page) {
        BooksDTO booksDTO = new BooksDTO();
        try {
            // L'URL de l'API Python
            String urlString = "http://127.0.0.1:5000/book/" + isbn; // Corrected endpoint to match the Python API
            URL url = new URL(urlString);

            // Ouvrir une connexion
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Définir la méthode comme GET
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // Optional: Set timeout for connection
            conn.setReadTimeout(5000); // Optional: Set timeout for reading

            // Obtenir le code de réponse
            int responseCode = conn.getResponseCode();
            System.out.println("Status Code: " + responseCode);

            // Lire la réponse depuis le flux d'entrée
            if (responseCode == HttpURLConnection.HTTP_OK) { // Check if the response is OK
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Parser la réponse JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray recommendationsArray = jsonResponse.getJSONArray("similar_books");

                    // Créer une liste de livres à partir des recommandations
                    List<book> books = new ArrayList<>();
                    for (int i = 0; i < recommendationsArray.length(); i++) {
                        JSONObject recommendation = recommendationsArray.getJSONObject(i);
                        String isbn_ = recommendation.getString("ISBN");

                        // Créer un objet Book à partir de l'ISBN
                        book book = bookService.getBook(isbn_);
                        if (book != null) {
                            books.add(book);
                        }
                    }

                    // Pagination des résultats
                    Page<book> booksPages = convertListToPage(books, page, 5);

                    // Convertir les entités Book en DTO
                    List<BookDTO> bookDTOList = booksPages.getContent().stream()
                            .map(book -> dtoMapper.fromBook(book))
                            .collect(Collectors.toList());

                    // Créer l'objet BooksDTO pour la réponse
                    booksDTO.setBookDTOList(bookDTOList);
                    booksDTO.setTotalpage(booksPages.getTotalPages());
                }
            } else {
                // Handle error response
                System.err.println("Error response from Python API: " + responseCode);
                booksDTO.setBookDTOList(new ArrayList<>());
                booksDTO.setTotalpage(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            booksDTO.setBookDTOList(new ArrayList<>());
            booksDTO.setTotalpage(0);
        }
        return booksDTO;
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/top5ratedbooks")
    public List<book> getTop5RatedBooks() {
        return bookService.getTop5RatedBooks();
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/findTop5ByWeightedRating")
    public List<book> findTop5ByWeightedRating() {
        System.out.println("get Top books");
        return bookService.findTop5ByWeightedRating();
    }
}
