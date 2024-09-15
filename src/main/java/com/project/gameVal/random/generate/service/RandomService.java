package com.project.gameVal.random.generate.service;

import com.google.common.collect.Lists;
import com.project.gameVal.random.generate.dto.request.RandomNumbersRequestDTO;
import com.project.gameVal.random.generate.dto.request.UserRandomResultListRequestDTO;
import com.project.gameVal.random.generate.dto.response.RandomResultResponseDTO;
import com.project.gameVal.random.generate.dto.response.UserRandomResultListResponseDTO;
import com.project.gameVal.random.generate.entity.RandomResult;
import com.project.gameVal.random.generate.entity.RandomSequences;
import com.project.gameVal.random.generate.repository.RandomResultRepository;
import com.project.gameVal.random.generate.util.RandomGenerator;
import com.project.gameVal.random.seed.dto.response.CurrentStateSeeds;
import com.project.gameVal.random.seed.entity.RandomSeed;
import com.project.gameVal.random.seed.service.RandomSeedService;
import com.project.gameVal.web.probability.service.ProbabilityTableService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RandomService {

    private final ReentrantReadWriteLock changeSequenceLock = new ReentrantReadWriteLock();

    private final RandomGenerator randomGenerator;

    private final RandomResultRepository randomResultRepository;

    private final RandomSeedService randomSeedService;

    private final ProbabilityTableService probabilityTableService;


    @Value("${generate.random.secondDuration}")
    private Integer secondDuration;

    @Value("${generate.random.dividePerSecond}")
    private Integer dividePerSecond;

    @Value("${generate.random.countPerDivide}")
    private Integer countPerDivide;

    @Value("${generate.random.bound}")
    private Integer randomBound;

    @Value("${generate.random.hashingAlgorithm}")
    private String hashingAlgorithm;

    @Value("${generate.random.resultDigit}")
    private Integer resultDigit;


    private RandomSequences firstRandomSequences;

    private RandomSequences secondRandomSequences;


    /*
    애플리케이션이 초기에 실행될 경우에 다른 애플리케이션들이 공통적으로 갖고 있는 시퀀스들과 맞추기 위해서
    DB에 저장된 seed값을 불러와서 시퀀스들을 생성하면, 다른 애플리케이션들이 갖고 있는 시퀀스와 동일한 시퀀스가 생성된다.
    또한, 시퀀스를 생성하는 동안 시퀀스들의 유효기간이 지나면, 생성한 시퀀스들은 폐기하고, 같은 로직으로 시퀀스들을 재생성한다.
    애플리케이션이 실행될 때, DB에 seed가 하나도 없거나 부족한 경우(2개 미만)를 고려하여 예외처리하였다.
    */
    @PostConstruct
    private void generateInitialRandomSequence() {
        // 초기 시퀀스 생성, DB를 기반으로 동기화
        // 초기화 동안 갱신 시간이 지난 경우에 재갱신
        do {
            RandomSeed beforeSeed;
            RandomSeed lastSeed;
            try {
                CurrentStateSeeds currentSeedState = randomSeedService.getCurrentSeedState();
                beforeSeed = currentSeedState.getBeforeSeed();
                lastSeed = currentSeedState.getLastSeed();
            } catch (IllegalStateException e) {
                // seed가 부족한 경우 초기화
                int minuteCycle = secondDuration / 60;
                int middleOfMinuteCycle = minuteCycle / 2;
                int minuteCount = 60 / minuteCycle;

                List<Integer> candidateMinutes = IntStream.range(0, minuteCount)
                        .map(i -> i * minuteCycle + middleOfMinuteCycle)
                        .boxed().toList();

                LocalDateTime lastRenewalTime = LocalDateTime.now().withSecond(0).withNano(0);
                int nowMinute = lastRenewalTime.getMinute();
                
                if (nowMinute < candidateMinutes.getFirst()) {
                    int lastToOClock = 60 - candidateMinutes.getLast();
                    lastRenewalTime = lastRenewalTime.minusMinutes(lastToOClock + nowMinute);
                }
                else  {
                    for (int minute : candidateMinutes.reversed()) {
                        if (minute <= nowMinute) {
                            lastRenewalTime = lastRenewalTime.withMinute(minute);
                            break;
                        }
                    }
                }

                LocalDateTime nextRenewalTime = lastRenewalTime.plusMinutes(minuteCycle);


                byte[] newSeedForBefore = randomGenerator.getSecureRandomSeed();
                // seed 저장 경합에 실패하였더라도 경합에 성공한 값을 가져와서 사용
                beforeSeed = randomSeedService.saveSeedCompetition(newSeedForBefore, lastRenewalTime);

                byte[] newSeedForLast = randomGenerator.getSecureRandomSeed();
                // seed 저장 경합에 실패하였더라도 경합에 성공한 값을 가져와서 사용
                lastSeed = randomSeedService.saveSeedCompetition(newSeedForLast, nextRenewalTime);
            }


            List<Integer> randomNumbersForFirst = generateDemandRandomNumbers(beforeSeed.getSeed());
            firstRandomSequences = generateRandomSequences(beforeSeed.getSeedStartTime(), randomNumbersForFirst);

            List<Integer> randomNumbersForSecond = generateDemandRandomNumbers(lastSeed.getSeed());
            secondRandomSequences = generateRandomSequences(lastSeed.getSeedStartTime(), randomNumbersForSecond);
        } while (secondRandomSequences.getSequenceStartTime().plusSeconds(secondDuration / 2).isBefore(LocalDateTime.now()));
    }

    private List<Integer> generateDemandRandomNumbers(byte[] seed) {
        return randomGenerator.reseedAndGetRandomNumbers(seed, secondDuration * dividePerSecond * countPerDivide, resultDigit, randomBound);
    }

    private RandomSequences generateRandomSequences(LocalDateTime sequenceStartTime, List<Integer> randomNumbers) {
        return new RandomSequences(
                UUID.randomUUID(),
                sequenceStartTime,
                ((long) secondDuration * dividePerSecond),
                dividePerSecond * countPerDivide,
                Lists.partition(randomNumbers, countPerDivide)
        );
    }



    /*
    정해진 주기마다 시퀀스를 교체하는 역할

    1. SecureRandom로 안전한 난수를 생성
    2. 생성한 난수 값을 DB에 저장하는 것을 복수 존재할 수 있는 다른 애플리케이션들과 경합하고, 경합에 승리한 값을 가져온다.
    3. 선택된 난수 값을 기반으로 시퀀스들을 생성하고, 시퀀스를 교체한다.

    현재는 5분, 15분, 25분, 35분, 45분, 55분에 10분 주기로 실행
    */
    @Scheduled(cron = "0 #{${generate.random.secondDuration} / 60 / 2}/#{${generate.random.secondDuration} / 60} * * * ?")
    public void changeRandomSequences() {

        byte[] newSeed = randomGenerator.getSecureRandomSeed();
        // 현재 시간의 초를 0으로 맞추고 나노초를 0으로 맞춤 -> 오차 제거
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        RandomSeed randomSeed = randomSeedService.saveSeedCompetition(newSeed, now);

        List<Integer> randomLongNumbers = generateDemandRandomNumbers(randomSeed.getSeed());


        // lock되는 시간을 최소화할 수 있도록 미리 생성
        RandomSequences tempRandomSequencesForSecond = generateRandomSequences(now, randomLongNumbers);

        // sequence 교체
        changeSequenceLock.writeLock().lock();
        try {
            firstRandomSequences = secondRandomSequences;
            secondRandomSequences = tempRandomSequencesForSecond;
        } finally {
            changeSequenceLock.writeLock().unlock();
        }
    }

    private List<RandomResultResponseDTO> getResultFromTable(String randomTableId, List<Double> randomNumbers) {
        return randomNumbers.stream()
                .parallel()
                .map(randomNumber ->
                        new RandomResultResponseDTO(
                                randomNumber,
                                probabilityTableService.findResultByTargetValue(randomTableId, randomNumber)
                        ))
                .toList();
    }

    @Async
    //@Transactional??
    public void saveToDB(Long gameId, String randomTableId, UUID userUUID, UUID sequenceId, List<RandomResultResponseDTO> randomResultResponseDTOS) {
        List<RandomResult> randomResults = randomResultResponseDTOS
                .stream().map(randomResultResponseDTO -> randomResultResponseDTO.toEntity(gameId, userUUID, randomTableId, sequenceId))
                .toList();

        randomResultRepository.saveAll(randomResults);
    }


    private RandomSequences getSequenceByTime(LocalDateTime executionTime) {
        RandomSequences candidateSequence;

        changeSequenceLock.readLock().lock();
        try {
            candidateSequence = (executionTime.isBefore(secondRandomSequences.getSequenceStartTime()))
                    ? firstRandomSequences : secondRandomSequences;
        } finally {
            changeSequenceLock.readLock().unlock();
        }

        if (executionTime.isBefore(candidateSequence.getSequenceStartTime()) || executionTime.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("Request Time is not in range");

        return candidateSequence;
    }

    private Integer bytesToPositiveInt(byte[] hash) {
        long number = 0;
        // 바이트 배열의 앞부분을 사용하여 일정 길이의 정수를 생성
        for (int i = 0; i < resultDigit && i < hash.length; i++) {
            number = (number << 8) | (hash[i] & 0xFF);
        }

        return ((int) (Math.abs(number) % Math.pow(10, resultDigit)));
    }

    private Integer getRandomIntegerByHashedUUID(UUID uuid, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(hashingAlgorithm);
        messageDigest.update(salt);

        byte[] hashedBytesUUID = messageDigest.digest(uuid.toString().getBytes(StandardCharsets.UTF_8));
        return bytesToPositiveInt(hashedBytesUUID);
    }


    /*
    1. request 중에 '유저가 요청을 보낸 시간'이 존재한다. 이 값을 기반으로 미리 생성되어 있던 여러 난수 시퀀스들 중에 하나를 선택한다.
    2. 선택된 난수 시퀀스의 첫 값을 salt로 하여 user uuid를 해싱한다.
    3. 선택된 난수 시퀀스 값들마다 (user uuid를 해싱한 값)을 offset으로 사용하여 최종적인 랜덤 값 시퀀스를 도출해낸다.
    */
    public UserRandomResultListResponseDTO getRandomNumbers(RandomNumbersRequestDTO randomNumbersRequestDTO) throws NoSuchAlgorithmException {
        // 현재는 유저 uuid를 해싱한 값을 가져와서 xor 연산을 수행하여 결과값을 가져오는 로직

        Long gameId = randomNumbersRequestDTO.getGameId();
        String randomTableId = randomNumbersRequestDTO.getRandomTableId();
        
        // 유저에게서 출발한 시간을 토대로 랜덤한 수가 담긴 시퀀스에서 List<Long>을 가져온다.
        LocalDateTime executionTime = randomNumbersRequestDTO.getExecutionTime();
        RandomSequences candidateSequence = getSequenceByTime(executionTime);
        int timeIndex = (int) (Duration.between(executionTime, candidateSequence.getSequenceStartTime()).abs().toMillis() / (1000 / dividePerSecond));
        List<Integer> selectedRandomSequence = candidateSequence.getNumberSequence().get(timeIndex);


        // (유저 uuid를 해싱하고 integer로 만든 값) % (list 길이) 를 하여 offset을 가져온다
        UUID userUUID = randomNumbersRequestDTO.getUserUUID();
        // 선택된 List<Long> 중에 첫 번째를 salt로 사용
        Integer randomNumberByHashedUUID = getRandomIntegerByHashedUUID(userUUID, selectedRandomSequence.getFirst().toString().getBytes(StandardCharsets.UTF_8));
        int offset = randomNumberByHashedUUID % selectedRandomSequence.size();

        // 연속된 랜덤 시퀀스 중에 offset에 해당하는 값 선택
        List<Double> randomNumbers = IntStream.range(offset, offset + randomNumbersRequestDTO.getCount())
                .parallel()
                .mapToObj(i -> selectedRandomSequence.get(i % selectedRandomSequence.size()) / Math.pow(10, resultDigit - 1))
                .toList();

        List<RandomResultResponseDTO> randomResultResponseDTOS = getResultFromTable(randomTableId, randomNumbers);

        // DB에 비동기적으로 저장
        saveToDB(gameId, randomTableId, userUUID, candidateSequence.getId(), randomResultResponseDTOS);

        return new UserRandomResultListResponseDTO(userUUID, randomResultResponseDTOS);
    }


    public UserRandomResultListResponseDTO getResultsByUUID(UserRandomResultListRequestDTO userRandomResultListRequestDTO, Pageable pageable) {
        List<RandomResultResponseDTO> userRandomResultResponses = randomResultRepository
                .findAllByUserUUIDAndGameIdOrderBySequenceIdAsc(
                        UUID.randomUUID(),
                        userRandomResultListRequestDTO.getGameId(),
                        pageable)
                .stream().map(randomResult -> new RandomResultResponseDTO(randomResult.getRandomNumber(), randomResult.getResultName()))
                .toList();

        return new UserRandomResultListResponseDTO(userRandomResultListRequestDTO.getUserUUID(), userRandomResultResponses);
    }
}
