package com.project.gameVal.random.generate.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.prng.SP800SecureRandomBuilder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@Slf4j
@Component
public class RandomGenerator {

    private final Lock lock = new ReentrantLock();

    private final SecureRandom randomForSeed = new SecureRandom();

    private SecureRandom randomForNumbers;

    private byte[] currentSeed;

    // seed로 사용할 난수를 생성할 random은 secure random을 사용해서 예측할 수 없게 생성
    public byte[] getSecureRandomSeed() {
        byte[] seed = new byte[32];
        randomForSeed.nextBytes(seed);
        return seed;
    }

    // 시퀀스로 사용할 난수를 생성할 random은 NIST SP 800-90A DRBG 표준을 만족하는 SP800SecureRandom를 사용하여 균등 분포를 보장  
    public List<Integer> reseedAndGetRandomNumbers(byte[] newSeed, Integer count, Integer digit, Integer bound) {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");

        lock.lock();
        try {
            currentSeed = newSeed;
            randomForNumbers = new SP800SecureRandomBuilder().buildHash(new SHA512Digest(), newSeed, false);
        } finally {
            lock.unlock();
        }

        return IntStream.range(0, count)
                //.parallel() 병렬처리하면 순서가 보장되지 않는 문제
                .mapToObj(i -> (int) (randomForNumbers.nextInt(0, bound) % (Math.pow(10, digit - 1))))
                .toList();
    }
}
