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
public class RefreshToken {
    @Id // @Id annotation의 패키지에 주의, 탈퇴나 logout시 find하기 위해서 userEmail을 key로 설정
    private Long gameCompanyId;

    private String refreshTokenValue;

    @TimeToLive
    private Long expiration;
}
