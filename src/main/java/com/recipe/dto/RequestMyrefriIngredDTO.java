package com.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestMyrefriIngredDTO {
    private Long refriId;

    @JsonProperty("ingredId")
    private List<Long> ingredId;
}
