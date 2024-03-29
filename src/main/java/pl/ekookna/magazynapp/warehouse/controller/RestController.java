package pl.ekookna.magazynapp.warehouse.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import pl.ekookna.magazynapp.admin.exception.UserExistException;
import pl.ekookna.magazynapp.admin.repository.entity.Users;
import pl.ekookna.magazynapp.admin.service.SecurityUserDetailsService;
import pl.ekookna.magazynapp.dto.ArticleDto;
import pl.ekookna.magazynapp.dto.ArticleStockDto;
import pl.ekookna.magazynapp.dto.UserDto;
import pl.ekookna.magazynapp.dto.WarehouseDto;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;
import pl.ekookna.magazynapp.warehouse.service.ArticleService;
import pl.ekookna.magazynapp.warehouse.service.ArticleStockService;
import pl.ekookna.magazynapp.warehouse.service.WarehouseService;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@Validated
@AllArgsConstructor
public class RestController implements RestApi {

    private SecurityUserDetailsService userDetailsService;
    private WarehouseService warehouseService;
    private ArticleService articleService;
    private ArticleStockService articleStockService;

    private static final String CREATED = "CREATED";
    public static final String ADMIN = "ADMIN";

    @Override
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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

    @Override
    @PostMapping(value = "/warehouse", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> saveWarehouse(@Valid @NotNull WarehouseDto warehouseDto) {
        try {
            warehouseService.save(warehouseDto);

            return new ResponseEntity<>(CREATED, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PostMapping(value = "/article", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> saveArticle(@Valid @NotNull ArticleDto articleDto) {
        try {
            articleService.save(articleDto);
            return new ResponseEntity<>(CREATED, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //FIXME WYSYŁAĆ JSON'A Z FRONTU W MULTIPARCIE ZAMIAST PARAMETROW
    @Override
    @PostMapping(value = "/article/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addArticleToWarehouse(@Size(max = 4) @RequestPart("file") List<MultipartFile> multipartFiles,
                                                        @Min(0) @RequestParam("article") long articleId,
                                                        @Min(0) @RequestParam("warehouse") long warehouseId,
                                                        @Min(0) @RequestParam("amount") long amount,
                                                        @Min(0) @RequestParam("vat") int vat,
                                                        @Min(0) @RequestParam("price") int price) {
        Article article = articleService.getOne(articleId);
        Warehouse warehouse = warehouseService.getOneById(warehouseId);

        try {
            checkAuthenticationToWarehouse(warehouse);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.FORBIDDEN);
        }

        ArticleStock articleStock = new ArticleStock();
        articleStock.setArticle(article);
        articleStock.getWarehouse().add(warehouse);
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

    private void checkAuthenticationToWarehouse(Warehouse warehouse) throws AuthenticationException {
        Users user = getLoggedUser();

        if (!user.getRole().equals(ADMIN)) {
            if (!user.getWarehouses().contains(warehouse))
                throw new AuthenticationException();
        }
    }

    @Override
    @PostMapping(value = "/article/release", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> collectArticle(@Valid ArticleStockDto articleStockDto) {

        try {
            checkAuthenticationToActicleStock(articleStockDto);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.FORBIDDEN);
        }

        articleStockService.removeArticleStockAmount(articleStockDto);

        return new ResponseEntity<>("DONE", HttpStatus.OK);
    }

    private void checkAuthenticationToActicleStock(ArticleStockDto articleStockDto) throws AuthenticationException {
        if (!getLoggedUser().getRole().equals(ADMIN)) {
            var list = articleStockService.findAllForUser(getLoggedUser())
                    .stream()
                    .filter(f -> f.getId().equals(articleStockDto.getId()))
                    .collect(Collectors.toList());

            if (!list.contains(articleStockService.getOne(articleStockDto.getId())))
                throw new AuthenticationException();
        }
    }

    @Override
    @GetMapping("/article/all")
    public ResponseEntity<List<Article>> getAllArticle() {
        return new ResponseEntity<>(articleService.findAll(), HttpStatus.OK);
    }

    @Override
    @GetMapping("/warehouse/all")
    public ResponseEntity<Collection<Warehouse>> getAllWarehouseForLoggedUser() {
        Users user = getLoggedUser();
        ResponseEntity<Collection<Warehouse>> response;

        if (user.getRole().equals(ADMIN)) {
            response = new ResponseEntity<>(warehouseService.findAll(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(warehouseService.findAllForUser(user), HttpStatus.OK);
        }

        return response;
    }

    @Override
    @GetMapping("/article/stock/all")
    public ResponseEntity<Collection<ArticleStock>> getAllArticleStock() {
        Users user = getLoggedUser();
        ResponseEntity<Collection<ArticleStock>> response;

        if (user.getRole().equals(ADMIN)) {
            response = new ResponseEntity<>(articleStockService.findAll(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(articleStockService.findAllForUser(user), HttpStatus.OK);
        }

        return response;
    }

    private Users getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
    }
}
