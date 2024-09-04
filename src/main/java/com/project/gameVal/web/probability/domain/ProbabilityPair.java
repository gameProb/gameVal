package com.project.gameVal.web.probability.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class ProbabilityPair {
    @DynamoDBAttribute
    @NotNull(message = "Probability must not be null")
    @Positive(message = "Probability must be positive")
    @Max(value = 1, message = "Probability must not exceed 1")
    private Double probability;

    @DynamoDBAttribute
    @NotBlank(message = "Result must not be blank")
    private String result;
}
