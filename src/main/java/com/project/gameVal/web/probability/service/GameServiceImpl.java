package com.project.gameVal.web.probability.service;

import com.project.gameVal.web.probability.domain.Game;
import com.project.gameVal.web.probability.dto.GameRegisterDTO;
import com.project.gameVal.web.probability.exception.GameAlreadyExistException;
import com.project.gameVal.web.probability.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public void save(GameRegisterDTO gameRegisterDTO) {
        if(gameRepository.existsByName(gameRegisterDTO.getName())) {
            log.warn("GameAlreadyExistException in save");
            throw new GameAlreadyExistException();
        }
        Game game = gameRegisterDTO.toEntity();
        gameRepository.save(game);
    }
}
