package pl.ekookna.magazynapp.warehouse.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import pl.ekookna.magazynapp.admin.exception.UserExistException;
import pl.ekookna.magazynapp.admin.repository.entity.Users;
import pl.ekookna.magazynapp.admin.service.SecurityUserDetailsService;
import pl.ekookna.magazynapp.dto.ArticleDto;
import pl.ekookna.magazynapp.dto.UserDto;
import pl.ekookna.magazynapp.dto.WarehouseDto;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;
import pl.ekookna.magazynapp.warehouse.service.ArticleService;
import pl.ekookna.magazynapp.warehouse.service.ArticleStockService;
import pl.ekookna.magazynapp.warehouse.service.WarehouseService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
public class RestController {

    private SecurityUserDetailsService userDetailsService;
    private WarehouseService warehouseService;
    private ArticleService articleService;
    private ArticleStockService articleStockService;

    private static final String CREATED = "CREATED";

    @PostMapping(value = "/user", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> saveUser(@Valid @NotNull UserDto userDto) {
        try {
            userDetailsService.createUser(userDto);

            return new ResponseEntity<>(CREATED, HttpStatus.CREATED);
        } catch (UserExistException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/warehouse", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> saveWarehouse(@Valid @NotNull WarehouseDto warehouseDto) {
        try {
            warehouseService.save(warehouseDto);

            return new ResponseEntity<>(CREATED, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/article", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> saveArticle(@Valid @NotNull ArticleDto articleDto) {
        try {
            articleService.save(articleDto);
            return new ResponseEntity<>(CREATED, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/article/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addArticleToWarehouse(@RequestPart("file") List<MultipartFile> multipartFiles,
                                                        @NotNull @NotBlank @RequestParam("article") long articleId,
                                                        @NotNull @NotBlank @RequestParam("amount") long amount,
                                                        @NotNull @NotBlank @RequestParam("vat") int vat,
                                                        @NotNull @NotBlank @RequestParam("price") int price) {
        if (multipartFiles.size() > 4)
            return new ResponseEntity<>("Files size is more than 4: " + multipartFiles.size(), HttpStatus.BAD_REQUEST);

        ArticleStock articleStock = new ArticleStock();
        articleStock.setArticleId(articleId);
        articleStock.setAmount(amount);
        articleStock.setVat(vat);
        articleStock.setPrice(price);

        articleStock.setFiles(articleStockService.storeFiles(multipartFiles, articleId));

        try {
            articleStockService.save(articleStock);
            return new ResponseEntity<>(HttpStatus.CREATED.name(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/article/all")
    public ResponseEntity<List<Article>> getAllArticle() {
        return new ResponseEntity<>(articleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/warehouse/all")
    public ResponseEntity<Collection<Warehouse>> getAllWarehouseForLoggedUser(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        Users user = (Users) userDetailsService.loadUserByUsername(username);

        ResponseEntity<Collection<Warehouse>> response;

        if (user.getRole().equals("ADMIN")) {
            response = new ResponseEntity<>(warehouseService.findAll(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(warehouseService.findAllForUser(user), HttpStatus.OK);
        }

        return response;
    }
}
