package com.project.gameVal.common.jwt.repository;

import com.project.gameVal.common.jwt.entity.BlackListRefreshToken;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRefreshTokenRedisRepository extends CrudRepository<BlackListRefreshToken, String> {

    //CREATE
    @Override
    @NonNull
    <S extends BlackListRefreshToken> S save(@NonNull S entity);

    //DELETE
    @Override
    void deleteById(@NonNull String refreshToken);

    //READ
    @Override
    @NonNull
    Optional<BlackListRefreshToken> findById(@NonNull String refreshToken);

    @Override
    boolean existsById(@NonNull String refreshToken);
}
