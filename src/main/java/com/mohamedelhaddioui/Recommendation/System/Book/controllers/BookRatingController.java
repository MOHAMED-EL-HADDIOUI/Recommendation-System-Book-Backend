package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.mappers.BookMapperImplementation;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.RegisterRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.security.configurations.JwtService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookRatingService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;


@RestController
public class BookRatingController {
    private BookRatingService bookRatingService;
    private BookService bookService;
    private UserService userService;
    private JwtService jwtService;
    @Autowired
    BookMapperImplementation dtoMapper;
    public BookRatingController(BookService bookService,BookRatingService bookRatingService,UserService userService,JwtService jwtService)
    {
        this.bookService = bookService;
        this.userService = userService;
        this.bookRatingService = bookRatingService;
        this.jwtService = jwtService;
    }
    @GetMapping("/bookRatings/{bookId}")
    public int getbookRating(HttpServletRequest request,@PathVariable("bookId")  String bookId){
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        int bookRating = bookRatingService.getRating(userId,bookId);
        return bookRating;
    }
    @PutMapping("/users/update")
    public ResponseEntity<user> updateUserProfile(HttpServletRequest request,@RequestBody RegisterRequest request_) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        user.setPassword(request_.getPassword());
        user.setNom(request_.getNom());
        user.setPrenom(request_.getPrenom());
        user.setAge(request_.getAge());
        user.setLocation(request_.getLocation());
        user.setTel(request_.getTel());
        user user1 = userService.saveUser(user);
        return ResponseEntity.ok(user1);
    }
    @PostMapping("/bookRatings/{bookId}/{rating}")
    public ResponseEntity<BookRating>  postbookRating(HttpServletRequest request,@PathVariable("bookId")  String bookId,@PathVariable("rating")  String rating){
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        book book = bookService.getBook(bookId);
        BookRating bookRating = new BookRating();
        bookRating.setBook(book);
        bookRating.setUser(user);
        bookRating.setRating(Integer.parseInt(rating));
        BookRating bookRating1 = bookRatingService.saveBookRating(bookRating);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookRating1);
    }

    @GetMapping("/bookRatings")
    public List<BookRating> getbookRatings(){
        List<BookRating> list = bookRatingService.getListBookRatings();
        return list;
    }
    @PostMapping("/bookRatings/save")
    public BookRating saveBook(@RequestBody BookRating bookRating)
    {
        BookRating bookRating1 = bookRatingService.saveBookRating(bookRating);
        return bookRating1;
    }
    @DeleteMapping("/bookRatings/{bookRatingId}")
    public ResponseEntity<Void> deletebookRating(@PathVariable("bookRatingId") Long id) {
        bookRatingService.deleteBookRating(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/bookRatings/books")
    public BooksDTO getBooksRatedByAuthenticatedUser(HttpServletRequest request,@RequestParam(name = "page", defaultValue = "0") int page) {
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
    @GetMapping("/books/recommend/{isbn}")
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



    public Page<book> convertListToPage(List<book> books, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        List<book> subList = books.subList(start, end);
        return new PageImpl<>(subList, pageable, books.size());
    }
    @GetMapping("/user")
    public RegisterRequest getUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNom(user.getNom());
        registerRequest.setAge(user.getAge());
        registerRequest.setId(user.getId());
        registerRequest.setGmail(user.getGmail());
        registerRequest.setLocation(user.getLocation());
        registerRequest.setRole(user.getRole());
        registerRequest.setTel(user.getTel());
        registerRequest.setPrenom(user.getPrenom());
        return registerRequest;
    }
}
