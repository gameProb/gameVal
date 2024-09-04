package com.project.gameVal.web.probability.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
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
    private Double probability;

    @DynamoDBAttribute
    private String result;
}
