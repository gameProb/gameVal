package com.project.gameVal.common.JWT.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

// value 옵션은 redis의 key에 prefix로 들어가서 여러 객체가 redis에 insert될 때 구분 가능하게 해줌
// 기본 TTL이 prod 설정에 맞춰져 있음
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "LogoutAccessToken", timeToLive = 900000)
public class LogoutAccessToken {
    @Id // @Id annotation의 패키지에 주의
    private String accessTokenValue;

    @TimeToLive
    private Long expiration;
}
