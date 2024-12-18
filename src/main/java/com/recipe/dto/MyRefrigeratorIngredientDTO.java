package com.recipe.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyRefrigeratorIngredientDTO {
    private Long refriIngreId;
    private Long ingredientId;
    private Long refriId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regDate;

}
