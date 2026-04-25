package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.PasswordRecovery;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.PasswordRecoveryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final PasswordRecoveryRepository passwordRecoveryRepository;
    private final UserService userService;

    @Transactional
    public void startRecovery(
            User user,
            String token
    ){
        System.out.println("actual token: " + token);
        PasswordRecovery passwordRecovery = passwordRecoveryRepository.getPasswordRecoveryByUser(user);//Verificamos si no existe algun registro con mi user
        if (passwordRecovery == null) {
            passwordRecovery = new PasswordRecovery(user, token);
            passwordRecoveryRepository.save(passwordRecovery);
        } else {
            passwordRecovery.setToken(token);
            passwordRecoveryRepository.save(passwordRecovery);
        }
    }

    public void recoverPassword(
            String token,
            String newPassword) {
        PasswordRecovery passwordRecovery = passwordRecoveryRepository.getPasswordRecoveryByToken(token)
                .orElseThrow(()-> new EntityNotFoundException("Token not found"));
        if (!passwordRecovery.isTokenValid()) throw new IllegalArgumentException("Token expired");
        User user = passwordRecovery.getUser();
        userService.resetPassword(user,newPassword);
    }


}
