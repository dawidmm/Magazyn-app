package pl.ekookna.magazynapp.warehouse.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import pl.ekookna.magazynapp.dto.ArticleDto;
import pl.ekookna.magazynapp.dto.ArticleStockDto;
import pl.ekookna.magazynapp.dto.UserDto;
import pl.ekookna.magazynapp.dto.WarehouseDto;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

public interface RestApi {

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> saveUser(@Valid @NotNull UserDto userDto);

    @PostMapping(value = "/warehouse", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> saveWarehouse(@Valid @NotNull WarehouseDto warehouseDto);

    @PostMapping(value = "/article", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> saveArticle(@Valid @NotNull ArticleDto articleDto);

    @PostMapping(value = "/article/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> addArticleToWarehouse(@Size(max = 4) @RequestPart("file") List<MultipartFile> multipartFiles,
                                                 @Min(0) @RequestParam("article") long articleId,
                                                 @Min(0) @RequestParam("warehouse") long warehouseId,
                                                 @Min(0) @RequestParam("amount") long amount,
                                                 @Min(0) @RequestParam("vat") int vat,
                                                 @Min(0) @RequestParam("price") int price);

    @PostMapping(value = "/article/release", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> collectArticle(@Valid ArticleStockDto articleStockDto);

    @GetMapping("/article/all")
    ResponseEntity<List<Article>> getAllArticle();

    @GetMapping("/warehouse/all")
    ResponseEntity<Collection<Warehouse>> getAllWarehouseForLoggedUser(Authentication authentication);

    @GetMapping("/article/stock/all")
    ResponseEntity<Collection<ArticleStock>> getAllArticleStock(Authentication authentication);
}
