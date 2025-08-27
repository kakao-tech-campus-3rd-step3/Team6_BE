package com.icebreaker.be.application.user;

import com.icebreaker.be.application.user.dto.CreateUserCommand;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.application.user.mapper.UserMapper;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Transactional
    public Long createUserIfNotExists(CreateUserCommand cmd) {
        return userRepository.findByPhone(cmd.phone())
                .map(User::getId)
                .orElseGet(() -> createUser(cmd.toEntity()));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    private Long createUser(User user) {
        return userRepository.save(user).getId();
    }
}
