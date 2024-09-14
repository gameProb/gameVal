package com.project.gameVal.random.generate.controller;

import com.project.gameVal.random.generate.dto.request.RandomNumbersRequestDTO;
import com.project.gameVal.random.generate.dto.request.UserRandomResultListRequestDTO;
import com.project.gameVal.random.generate.dto.response.RandomResultResponseDTO;
import com.project.gameVal.random.generate.dto.response.UserRandomResultListResponseDTO;
import com.project.gameVal.random.generate.service.RandomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/random")
@RequiredArgsConstructor
public class RandomController {

    private final RandomService randomService;

    @PostMapping
    public ResponseEntity<UserRandomResultListResponseDTO> random(@RequestBody @Valid RandomNumbersRequestDTO randomNumbersRequestDTO) throws NoSuchAlgorithmException {
        UserRandomResultListResponseDTO randomNumbers = randomService.getRandomNumbers(randomNumbersRequestDTO);

        System.out.println(randomNumbers.getRandomResults().stream().map(RandomResultResponseDTO::getRandomNumber).toList());

        return new ResponseEntity<>(randomNumbers, HttpStatus.OK);
    }

    @GetMapping("/game/uuid")
    public ResponseEntity<UserRandomResultListResponseDTO> getResultsByUUIDAndGameId(@RequestBody @Valid UserRandomResultListRequestDTO userRandomResultListRequestDTO, Pageable pageable) {
        return new ResponseEntity<>(randomService.getResultsByUUID(userRandomResultListRequestDTO, pageable), HttpStatus.OK);
    }
}
