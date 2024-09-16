package com.project.gameVal.random.generate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserRandomResultListResponseDTO {

    private UUID userUUID;

    private List<RandomResultResponseDTO> randomResults;
}
