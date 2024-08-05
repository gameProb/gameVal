package com.project.gameVal.common.jwt.repository;

import com.project.gameVal.common.jwt.entity.LogoutAccessToken;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutRedisRepository extends CrudRepository<LogoutAccessToken, String> {
    @Override
    @NonNull
    <S extends LogoutAccessToken> S save(@NonNull S entity);

    @Override
    boolean existsById(@NonNull String accessToken);
}
