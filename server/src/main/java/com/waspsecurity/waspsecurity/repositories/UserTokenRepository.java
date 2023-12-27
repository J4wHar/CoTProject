package com.waspsecurity.waspsecurity.repositories;


import com.waspsecurity.waspsecurity.security.UserToken;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends CrudRepository<UserToken, String> {
    Optional<UserToken> findByEmail(String  email ) ;
    @Query("select * from UserToken where tokens.token = @refreshToken")
    Optional<UserToken> findByRefreshToken(@Param("refreshToken") String token);

    @Query("select * from UserToken where tokens.accessToken.token = @accessToken")
    Optional<UserToken> findByAccessToken(@Param("accessToken") String token);
}
