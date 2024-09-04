package com.project.gameVal.web.probability.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameCompanyInformInToken {

    private Long id;

    private String name;

    private Role role;
}
