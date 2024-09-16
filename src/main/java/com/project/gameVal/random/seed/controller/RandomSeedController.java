package com.project.gameVal.random.seed.controller;

import com.project.gameVal.random.seed.dto.response.CurrentSeedStateResponseDTO;
import com.project.gameVal.random.seed.service.RandomSeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/random/seed")
public class RandomSeedController {

    private final RandomSeedService randomSeedService;

    @GetMapping("/current")
    public ResponseEntity<CurrentSeedStateResponseDTO> getCurrentSeeds() throws NoSuchAlgorithmException {
        return new ResponseEntity<>(randomSeedService.getCurrentSeedStateResponseDTO(), HttpStatus.OK);
    }
}
