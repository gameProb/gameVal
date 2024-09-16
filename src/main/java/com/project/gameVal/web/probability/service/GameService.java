package com.project.gameVal.web.probability.service;

import com.project.gameVal.web.probability.dto.GameRegisterDTO;

public interface GameService {
    void save(Long gameCompanyId, GameRegisterDTO gameRegisterDTO);
}
