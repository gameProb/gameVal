package com.project.gameVal.web.probability.controller.api;

import com.project.gameVal.common.jwt.auth.JWTUtil;
import com.project.gameVal.web.probability.domain.ProbabilityTable;
import com.project.gameVal.web.probability.dto.ProbabilityTableDTO;
import com.project.gameVal.web.probability.dto.ResultRequestDTO;
import com.project.gameVal.web.probability.service.ProbabilityTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/probability/api")
@RequiredArgsConstructor
@Slf4j
public class ProbabilityTableAPIController {
    private final ProbabilityTableService probabilityTableService;
    private final JWTUtil jwtUtil;

    @PostMapping("/table/add")
    public ResponseEntity<String> addTable(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody ProbabilityTableDTO probabilityTableDTO) {
        log.info("start add table");
        String accessToken = jwtUtil.getAccessTokenByAuthorizationHeader(authorizationHeader);
        Long gameCompanyId = jwtUtil.getIdByAccessToken(accessToken);
        ProbabilityTable savedProbabilityTable = probabilityTableService.createProbabilityTable(gameCompanyId, probabilityTableDTO);
        return new ResponseEntity<>("successfully probability table added and id is " + savedProbabilityTable.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/result/{tableId}")
    public ResponseEntity<String> getResult(@PathVariable String tableId, @Valid @RequestBody ResultRequestDTO target) {
        String resultByTargetValue = probabilityTableService.findResultByTargetValue(tableId, target.getTarget());
        return new ResponseEntity<>(resultByTargetValue, HttpStatus.OK);
    }
}