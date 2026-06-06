package com.elibrary.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class quickinformationentry {

    // Đường dẫn gốc của project backend
    private static final String BASE_PATH = "C:/MyProject/E-LIBRARY_PROJECT/backend";

    public static void main(String[] args) {
        System.out.println("====== BẮT ĐẦU KHỞI TẠO BƯỚC 4: SERVICE & CONTROLLER ======");
        try {
            // Khởi tạo các lớp nghiệp vụ Service
            generateServices();

            // Khởi tạo các lớp tiếp nhận API Controller
            generateControllers();

            System.out.println("\n====== KHỞI TẠO BƯỚC 4 THÀNH CÔNG! HÃY REFRESH LẠI DỰ ÁN ======");
        } catch (Exception e) {
            System.err.println("❌ Có lỗi xảy ra trong quá trình khởi tạo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writeFile(String relativePath, String content) throws IOException {
        Path fullPath = Paths.get(BASE_PATH, relativePath);
        if (fullPath.getParent() != null) {
            Files.createDirectories(fullPath.getParent());
        }
        Files.writeString(fullPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("-> Đã tạo file: " + fullPath.toAbsolutePath());
    }

    private static void generateServices() throws IOException {
        System.out.println("\n[Tầng Service] Đang tạo các file xử lý logic nghiệp vụ...");

        // 1. AuthService - Xử lý Đăng ký / Đăng nhập
        writeFile("src/main/java/com/elibrary/backend/service/AuthService.java", """
                package com.elibrary.backend.service;

                import com.elibrary.backend.model.User;
                import com.elibrary.backend.repository.UserRepository;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.stereotype.Service;
                import java.util.Optional;

                @Service
                public class AuthService {

                    @Autowired
                    private UserRepository userRepository;

                    // Logic Đăng ký tài khoản mới
                    public User register(User user) {
                        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                            throw new RuntimeException("Tên tài khoản đã tồn tại!");
                        }
                        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                            throw new RuntimeException("Email đã được sử dụng!");
                        }
                        // Tạm thời lưu thô mật khẩu, giai đoạn sau sẽ tích hợp BCryptPasswordEncoder
                        return userRepository.save(user);
                    }

                    // Logic Đăng nhập hệ thống
                    public User login(String username, String password) {
                        User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

                        // Kiểm tra trạng thái tài khoản
                        if (!user.isStatus()) {
                            throw new RuntimeException("Tài khoản của bro đã bị khóa!");
                        }

                        // Tạm thời so sánh thô, giai đoạn bảo mật sau sẽ khớp mã hóa BCrypt
                        if (!user.getPassword().equals(password)) {
                            throw new RuntimeException("Mật khẩu không chính xác!");
                        }

                        return user;
                    }
                }
                """);

        // 2. BookService - Xử lý nghiệp vụ tìm kiếm và quản lý sách
        writeFile("src/main/java/com/elibrary/backend/service/BookService.java", """
                package com.elibrary.backend.service;

                import com.elibrary.backend.model.Book;
                import com.elibrary.backend.repository.BookRepository;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.stereotype.Service;
                import java.util.List;

                @Service
                public class BookService {

                    @Autowired
                    private BookRepository bookRepository;

                    public List<Book> getAllBooks() {
                        return bookRepository.findAll();
                    }

                    public List<Book> searchBooksByTitle(String title) {
                        return bookRepository.findByTitleContainingIgnoreCase(title);
                    }

                    public List<Book> getBooksByCategory(Integer categoryId) {
                        return bookRepository.findByCategoryId(categoryId);
                    }

                    public Book getBookById(Integer id) {
                        return bookRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuốn sách này, bro ơi!"));
                    }

                    public Book incrementViewCount(Integer id) {
                        Book book = getBookById(id);
                        book.setViewCount(book.getViewCount() + 1);
                        return bookRepository.save(book);
                    }
                }
                """);

        // 3. CategoryService - Quản lý thể loại sách số
        writeFile("src/main/java/com/elibrary/backend/service/CategoryService.java", """
                package com.elibrary.backend.service;

                import com.elibrary.backend.model.Category;
                import com.elibrary.backend.repository.CategoryRepository;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.stereotype.Service;
                import java.util.List;

                @Service
                public class CategoryService {

                    @Autowired
                    private CategoryRepository categoryRepository;

                    public List<Category> getAllCategories() {
                        return categoryRepository.findAll();
                    }
                }
                """);
    }

    private static void generateControllers() throws IOException {
        System.out.println("\n[Tầng Controller] Đang tạo các REST API Endpoints kết nối React...");

        // 4. AuthController - Cung cấp API Đăng nhập / Đăng ký
        writeFile("src/main/java/com/elibrary/backend/controller/AuthController.java", """
                package com.elibrary.backend.controller;

                import com.elibrary.backend.model.User;
                import com.elibrary.backend.service.AuthService;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.http.ResponseEntity;
                import org.springframework.web.bind.annotation.*;
                import java.util.Map;

                @RestController
                @RequestMapping("/api/auth")
                @CrossOrigin(origins = "*") // Cho phép React gọi API không bị lỗi CORS
                public class AuthController {

                    @Autowired
                    private AuthService authService;

                    @PostMapping("/register")
                    public ResponseEntity<?> registerUser(@RequestBody User user) {
                        try {
                            User registeredUser = authService.register(user);
                            return ResponseEntity.ok(registeredUser);
                        } catch (Exception e) {
                            return ResponseEntity.badRequest().body(e.getMessage());
                        }
                    }

                    @PostMapping("/login")
                    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
                        try {
                            String username = loginData.get("username");
                            String password = loginData.get("password");
                            User user = authService.login(username, password);
                            return ResponseEntity.ok(user);
                        } catch (Exception e) {
                            return ResponseEntity.badRequest().body(e.getMessage());
                        }
                    }
                }
                """);

        // 5. BookController - Cung cấp API tra cứu sách và thể loại
        writeFile("src/main/java/com/elibrary/backend/controller/BookController.java", """
                package com.elibrary.backend.controller;

                import com.elibrary.backend.model.Book;
                import com.elibrary.backend.model.Category;
                import com.elibrary.backend.service.BookService;
                import com.elibrary.backend.service.CategoryService;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.http.ResponseEntity;
                import org.springframework.web.bind.annotation.*;
                import java.util.List;

                @RestController
                @RequestMapping("/api/books")
                @CrossOrigin(origins = "*")
                public class BookController {

                    @Autowired
                    private BookService bookService;

                    @Autowired
                    private CategoryService categoryService;

                    // Lấy toàn bộ danh sách sách
                    @GetMapping
                    public ResponseEntity<List<Book>> getAllBooks() {
                        return ResponseEntity.ok(bookService.getAllBooks());
                    }

                    // Chi tiết sách và tự động tăng lượt xem (View Count)
                    @GetMapping("/{id}")
                    public ResponseEntity<Book> getBookDetail(@PathVariable Integer id) {
                        bookService.incrementViewCount(id);
                        return ResponseEntity.ok(bookService.getBookById(id));
                    }

                    // Tìm kiếm sách theo tiêu đề chữ hoa/chữ thường bách phát bách trúng
                    @String
                    @GetMapping("/search")
                    public ResponseEntity<List<Book>> searchBooks(@RequestParam String title) {
                        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
                    }

                    // Lọc sách theo danh mục thể loại
                    @GetMapping("/category/{categoryId}")
                    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Integer categoryId) {
                        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId));
                    }

                    // Lấy tất cả thể loại sách số để hiển thị lên Menu thanh điều hướng
                    @GetMapping("/categories")
                    public ResponseEntity<List<Category>> getAllCategories() {
                        return ResponseEntity.ok(categoryService.getAllCategories());
                    }
                }
                """);
    }
}