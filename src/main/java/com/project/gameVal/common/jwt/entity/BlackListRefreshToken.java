package com.project.gameVal.common.jwt.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "RefreshToken", timeToLive = 1209600000) // TTL을 수정하면 yml도 수정해야함
public class BlackListRefreshToken {

    @Id
    private String refreshTokenValue;

    @TimeToLive
    private Long expiration;
}
