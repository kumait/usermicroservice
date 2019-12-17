package io.primecoders.voctrainer.userservice.services;

import io.primecoders.voctrainer.userservice.infra.IdGenerator;
import io.primecoders.voctrainer.userservice.infra.security.TokenService;
import io.primecoders.voctrainer.userservice.models.business.ChangePasswordModel;
import io.primecoders.voctrainer.userservice.models.business.ResetPasswordModel;
import io.primecoders.voctrainer.userservice.models.business.User;
import io.primecoders.voctrainer.userservice.models.common.AccountStatus;
import io.primecoders.voctrainer.userservice.models.entities.UserEntity;
import io.primecoders.voctrainer.userservice.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static io.primecoders.voctrainer.userservice.infra.Logic.*;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final IdGenerator idGenerator;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder, ModelMapper mapper, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .disabled(userEntity.getAccountStatus() != AccountStatus.ACTIVE)
                .accountExpired(false)
                .credentialsExpired(false)
                .authorities(userEntity.getUserType().toString())
                .build();
        return user;
    }

    public User createUser(User user) {
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        userEntity.setId(idGenerator.getNewId());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setAccountStatus(AccountStatus.NEW);
        userRepository.save(userEntity);
        return mapper.map(userEntity, User.class);
    }

    public void activateAccount(String token) {
        String username = tokenService.getUsernameFromAccountActivationToken(token);
        UserEntity userEntity = requireExists(userRepository.findByUsername(username));
        affirm(userEntity.getAccountStatus() != AccountStatus.NEW, "Account is already active or disabled.");

        userEntity.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(userEntity);
    }

    public void resetPassword(ResetPasswordModel resetPasswordModel) {
        String username = tokenService.getUsernameFromPasswordResetToken(resetPasswordModel.getToken());
        UserEntity userEntity = requireExists(userRepository.findByUsername(username));
        userEntity.setPassword(passwordEncoder.encode(resetPasswordModel.getNewPassword()));
        userRepository.save(userEntity);
    }

    public void changePassword(ChangePasswordModel changePasswordModel) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = requireExists(userRepository.findByUsername(username));
        affirmAccess(passwordEncoder.encode(changePasswordModel.getOldPassword()).equals(userEntity.getPassword()));
        userEntity.setPassword(passwordEncoder.encode(changePasswordModel.getNewPassword()));
        userRepository.save(userEntity);
    }


}
