package com.mohamedelhaddioui.Recommendation.System.Book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.AuthenticationService;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.RegisterRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookRatingService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.UserService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import static com.mohamedelhaddioui.Recommendation.System.Book.enums.Role.ADMIN;
import static com.mohamedelhaddioui.Recommendation.System.Book.enums.Role.MANAGER;

@SpringBootApplication
public class RecommendationSystemBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecommendationSystemBookApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner (BookService bookService, UserService userService, BookRatingService bookRatingService,AuthenticationService service) {
		return args -> {
//			var admin = RegisterRequest.builder()
//					.prenom("Admin")
//					.nom("Admin")
//					.gmail("admin@mail.com")
//					.password("password")
//					.role(ADMIN)
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());

//			var manager = RegisterRequest.builder()
//					.prenom("MANAGER")
//					.nom("MANAGER")
//					.gmail("manager@mail.com")
//					.password("password")
//					.role(MANAGER)
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());

			// Charger les rating-books
//			try (BufferedReader br = Files.newBufferedReader(Paths.get("C:/Users/asus/Music/Recommendation System Book/BX-CSV-Dump/BX-Book-Ratings.csv"), StandardCharsets.ISO_8859_1)) {
//				CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
//				CSVReader csvReader = new CSVReaderBuilder(br).withCSVParser(parser).build();
//				String[] columns;
//				int i=0;
//				csvReader.readNext();
//				while ((columns = csvReader.readNext()) != null) {
//					if (columns.length == 3) {
//						user user = userService.getUser(Long.valueOf(columns[0]));
//						book book = bookService.getBook(columns[1]);
//
//						if (user == null) {
//							System.out.println("User not found for ID: " + columns[0]);
//							continue;  // Skip to the next row
//						}
//
//						if (book == null) {
//							System.out.println("Book not found for ISBN: " + columns[1]);
//							continue;  // Skip to the next row
//						}
//
//						try {
//							BookRating bookRating = BookRating.builder()
//									.book(book)
//									.user(user)
//									.rating(Integer.parseInt(columns[2]))
//									.build();
//							bookRatingService.saveBookRating(bookRating);
//							i++;
//							System.out.println("i : " + i);
//						} catch (Exception e) {
//							System.out.println("Error saving rating: " + e.getMessage());
//						}
//					} else {
//						System.out.println("Invalid row length: " + Arrays.toString(columns));
//					}
//				}
//				System.out.println("Fin");
//			}catch (Exception e) {
//				e.printStackTrace();
//			}

			// Charger les livres
//			try (BufferedReader br = Files.newBufferedReader(Paths.get("C:/Users/asus/Music/Recommendation System Book/BX-CSV-Dump/BX-Books.csv"), StandardCharsets.ISO_8859_1)) {
//				CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
//				CSVReader csvReader = new CSVReaderBuilder(br).withCSVParser(parser).build();
//				String[] columns;
//				int i=0;
//				while ((columns = csvReader.readNext()) != null) {
//					if (columns.length == 8) {
//						String yearOfPublicationStr = columns[3];
//						// Vérifiez si l'année est un nombre valide
//						if (!yearOfPublicationStr.matches("\\d+(\\.0)?")) {
//							System.out.println("Invalid year of publication: " + yearOfPublicationStr);
//							continue;
//						}
//
//						// Convertir en entier et gérer le format décimal
//						long yearOfPublication = (long) Double.parseDouble(yearOfPublicationStr);
//						book b1 = book.builder()
//								.ISBN(columns[0])
//								.bookTitle(columns[1])
//								.bookAuthor(columns[2])
//								.yearOfPublication(yearOfPublication)
//								.publisher(columns[4])
//								.imageURLS(columns[5])
//								.imageURLM(columns[6])
//								.imageURLL(columns[7])
//								.build();
//						bookService.saveBook(b1);
//						i++;
//						System.out.println("i : "+i);
//						System.out.println("save User "+columns[0]);
//					}
//					else {
//						System.out.println("Invalid row length: " + Arrays.toString(columns));
//					}
//				}
//				System.out.println("Fin");
//			}catch (Exception e) {
//				e.printStackTrace();
//			}

//			 Charger les users
//			IOUtils.setByteArrayMaxOverride(200_000_000);
//			try (FileInputStream fis = new FileInputStream(Paths.get("C:/Users/asus/Music/Recommendation System Book/BX-CSV-Dump/users.xlsx").toFile());
//				 Workbook workbook = new XSSFWorkbook(fis)) {
//
//				Sheet sheet = workbook.getSheetAt(0); // Assuming you want the first sheet
//				Pattern userIdPattern = Pattern.compile("\\d+");
//				Pattern agePattern = Pattern.compile("\\d+(\\.0)?");
//				Pattern emailPattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
//
//				int i = 0;
//				for (Row row : sheet) {
//					if (row.getPhysicalNumberOfCells() == 7) {
//						String userIdStr = getCellValue(row.getCell(0));
//						String ageStr = getCellValue(row.getCell(2));
//						String gmail = getCellValue(row.getCell(6));
//						// Vérifications
//						if (!userIdPattern.matcher(userIdStr).matches()) {
//							System.out.println("Invalid userId: " + userIdStr);
//							continue;
//						}
//
//						if (!agePattern.matcher(ageStr).matches()) {
//							System.out.println("Invalid age: " + ageStr);
//							continue;
//						}
//
//						if (!emailPattern.matcher(gmail).matches()) {
//							System.out.println("Invalid email format: " + gmail);
//							continue;
//						}
//
//
//						i++;
//						System.out.println("i: " + i);
//						System.out.println("User " + userIdStr);
//						String gmailWithI = gmail.replace("@", i+"@");
//						var u3 = RegisterRequest.builder()
//								.id(Long.valueOf(userIdStr))
//								.location(getCellValue(row.getCell(1)))
//								.age((long) Double.parseDouble(ageStr))
//								.nom(getCellValue(row.getCell(3)))
//								.prenom(getCellValue(row.getCell(4)))
//								.tel(getCellValue(row.getCell(5)))
//								.gmail(gmailWithI)
//								.password(getCellValue(row.getCell(3))+" "+getCellValue(row.getCell(4)))
//								.role(MANAGER)
//								.build();
//						service.register(u3);
//					} else {
//						System.out.println("Invalid row length: " + row.getPhysicalNumberOfCells());
//					}
//				}
//
//				System.out.println("Fin");
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}








		};
	}
//	private static String getCellValue(Cell cell) {
//		if (cell == null) return "";
//		switch (cell.getCellType()) {
//			case STRING:
//				return cell.getStringCellValue();
//			case NUMERIC:
//				if (DateUtil.isCellDateFormatted(cell)) {
//					return cell.getDateCellValue().toString();
//				} else {
//					return String.valueOf((long) cell.getNumericCellValue()); // Convert double to long
//				}
//			case BOOLEAN:
//				return String.valueOf(cell.getBooleanCellValue());
//			case FORMULA:
//				return cell.getCellFormula();
//			default:
//				return "";
//		}
//	}

}
