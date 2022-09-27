package pl.ekookna.magazynapp.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class ArticleStockDto {

    @NotNull
    private Long id;

    @Min(1)
    @NotNull
    private Long amount;
}
