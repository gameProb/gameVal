package com.project.gameVal.random.seed.service;

import com.project.gameVal.random.seed.entity.CurrentSeedState;
import com.project.gameVal.random.seed.dto.response.CurrentSeedStateResponseDTO;
import com.project.gameVal.random.seed.entity.RandomSeed;
import com.project.gameVal.random.seed.repository.RandomSeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RandomSeedService {

    private final RandomSeedRepository randomSeedRepository;

    @Value("${generate.random.hashingAlgorithm}")
    private String hashingAlgorithm;

    public RandomSeed saveSeedCompetition(byte[] currentSeed, LocalDateTime createTime) {
        try {
            return randomSeedRepository.save(new RandomSeed(null, currentSeed, createTime));
        } catch (DataIntegrityViolationException e) {
            // 애플리케이션 중에 저장 경합에 실패한 경우 경합에 성공하여 DB에 저장된 값을 가져와서 사용
            log.debug("seed saving competition failed");

            return randomSeedRepository.findBySeedStartTime(createTime);
        }
    }

    public CurrentSeedState getCurrentSeedState() throws IllegalStateException {
        List<RandomSeed> top2ByOrderByCreateTimeDesc = randomSeedRepository.findTop2ByOrderBySeedStartTimeDesc();
        if (top2ByOrderByCreateTimeDesc.size() < 2) throw new IllegalStateException("Seed is not enough");

        RandomSeed beforeSeed = top2ByOrderByCreateTimeDesc.get(1);
        RandomSeed lastSeed = top2ByOrderByCreateTimeDesc.get(0);

        return new CurrentSeedState(beforeSeed, lastSeed);
    }

    private byte[] hashSeed(byte[] seed) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);
        return digest.digest(seed);
    }

    public CurrentSeedStateResponseDTO getCurrentSeedStateResponseDTO() throws NoSuchAlgorithmException {
        CurrentSeedState currentSeedState = getCurrentSeedState();

        return new CurrentSeedStateResponseDTO(currentSeedState.getBeforeSeed().getSeed(), hashSeed(currentSeedState.getLastSeed().getSeed()));
    }
}
