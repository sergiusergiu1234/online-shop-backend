package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.RefreshToken;
import com.StefanSergiu.Licenta.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    @Modifying
    int deleteByUserInfo(UserInfo userInfo);
}
