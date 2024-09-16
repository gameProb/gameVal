package com.project.gameVal.random.generate.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UserRandomResultListRequestDTO {

    private Long gameId;

    private UUID userUUID;
}
