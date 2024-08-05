package com.project.gameVal.common.jwt.repository;

import com.project.gameVal.common.jwt.entity.RefreshToken;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    //CREATE
    @Override
    @NonNull
    <S extends RefreshToken> S save(@NonNull S entity);

    //DELETE
    @Override
    void deleteById(@NonNull Long gameCompanyId);

    //READ
    @NonNull
    Optional<RefreshToken> findById(@NonNull Long gameCompanyId);

    boolean existsById(@NonNull Long gameCompanyId);
}
