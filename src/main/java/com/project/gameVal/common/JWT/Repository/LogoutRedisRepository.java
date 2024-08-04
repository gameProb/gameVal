package com.project.gameVal.common.JWT.Repository;

import com.project.gameVal.common.JWT.Entity.LogoutAccessToken;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutRedisRepository extends CrudRepository<LogoutAccessToken, String> {
    @Override
    @NonNull
    <S extends LogoutAccessToken> S save(@NonNull S entity);

    boolean existsById(@NonNull String accessToken);
}
