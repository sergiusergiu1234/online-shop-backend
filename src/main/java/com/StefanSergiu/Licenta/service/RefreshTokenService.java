package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.entity.RefreshToken;
import com.StefanSergiu.Licenta.exception.TokenRefreshException;
import com.StefanSergiu.Licenta.repository.RefreshTokenRepository;
import com.StefanSergiu.Licenta.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserInfoRepository userRepository;

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(int userId){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserInfo(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(86400000));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0 ){
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request!");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(int userId){
        return refreshTokenRepository.deleteByUserInfo(userRepository.findById(userId).get());
    }
}
