package com.oched.booksprj.services;

import com.oched.booksprj.entities.AuthorEntity;
import com.oched.booksprj.entities.BookDescriptionEntity;
import com.oched.booksprj.entities.UserEntity;
import com.oched.booksprj.enumerations.UserRole;
import com.oched.booksprj.repositories.UserRepository;
import com.oched.booksprj.requests.ActionRequest;
import com.oched.booksprj.requests.EditBookRequest;
import com.oched.booksprj.requests.EditUserRequest;
import com.oched.booksprj.requests.NewUserRequest;
import com.oched.booksprj.responses.BookInfoResponse;
import com.oched.booksprj.responses.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @CacheEvict(value = "userListCache", allEntries = true)
    public UserInfoResponse addNewUser(NewUserRequest request) {
        List<UserRole> roleList = new ArrayList<>();
        roleList.add(UserRole.ROLE_USER);

        if(request.getRole().equals("ADMIN")) {
            roleList.add(UserRole.ROLE_ADMIN);
        }

        if(request.getRole().equals("SUPER")) {
            roleList.add(UserRole.ROLE_ADMIN);
            roleList.add(UserRole.ROLE_SUPER);
        }

        UserEntity user = new UserEntity(
                request.getLogin(),
                request.getEmail(),
                this.passwordEncoder.encode(request.getPassword()),
                roleList
        );

        this.userRepository.save(user);

        return new UserInfoResponse(
                user.getId(),
                request.getLogin(),
                request.getEmail()
        );
    }

    @Cacheable(value = "userListCache")
    public List<UserInfoResponse> getUsersList() {
        List<UserEntity> list = new ArrayList<>();

        if (isSuper()) {
            list = this.userRepository.findAll();
        } else {
            for (UserEntity user : this.userRepository.findAll()) {
                if (user.getRoles().size() == 1) {
                    list.add(user);
                }
            }
        }

        return list.stream().map(
                user -> new UserInfoResponse(
                        user.getId(),
                        user.getLogin(),
                        user.getEmail()
                )
        ).collect(Collectors.toList());
    }

    @Cacheable(value = "userListCache")
    public List<UserInfoResponse> getUsersList(boolean rest) {
        List<UserEntity> list = this.userRepository.findAll();

        return list.stream().map(
                user -> new UserInfoResponse(
                        user.getId(),
                        user.getLogin(),
                        user.getEmail()
                )
        ).collect(Collectors.toList());
    }

    @CacheEvict(value = "userListCache", allEntries = true)
    public UserInfoResponse register(NewUserRequest request) {
        List<UserRole> roleList = new ArrayList<>();
        roleList.add(UserRole.ROLE_USER);

        if(userRepository.count() == 0) {
            roleList.add(UserRole.ROLE_ADMIN);
            roleList.add(UserRole.ROLE_SUPER);
        }

        UserEntity user = new UserEntity(
                request.getLogin(),
                request.getEmail(),
                this.passwordEncoder.encode(request.getPassword()),
                roleList
        );

        this.userRepository.save(user);

        return new UserInfoResponse(
                user.getId(),
                request.getLogin(),
                request.getEmail()
        );
    }

    public boolean isSuper() {
        User activeUser;
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!principal.getClass().equals(String.class)) {
            activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return activeUser.getAuthorities().size() > 2;
        }

        return false;
    }

    public boolean isAdmin() {
        User activeUser;
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!principal.getClass().equals(String.class)) {
            activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return activeUser.getAuthorities().size() > 1;
        }

        return false;
    }

    public boolean isUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return !principal.getClass().equals(String.class);
    }

    public Object getById(ActionRequest request) {
        Optional<UserEntity> optional = this.userRepository.findById(request.getId());

        if(optional.isEmpty()) {
            throw new RuntimeException();
        }

        UserEntity user = optional.get();

        return new UserInfoResponse(
                user.getId(),
                user.getLogin(),
                user.getEmail()
        );
    }

    @CacheEvict(value = "userListCache", allEntries = true)
    public void editUser(EditUserRequest request) {
        Optional<UserEntity> optional = this.userRepository.findById(request.getId());

        if(optional.isEmpty()) {
            throw new RuntimeException();
        }

        UserEntity user = optional.get();

        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        if (!request.getPassword().equals("")) {
            user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        }

        List<UserRole> roleList = new ArrayList<>();
        roleList.add(UserRole.ROLE_USER);

        if(request.getRole().equals("ADMIN")) {
            roleList.add(UserRole.ROLE_ADMIN);
        }

        if(request.getRole().equals("SUPER")) {
            roleList.add(UserRole.ROLE_ADMIN);
            roleList.add(UserRole.ROLE_SUPER);
        }

        user.setRoles(roleList);

        this.userRepository.save(user);
    }

    public String getUserRole(ActionRequest request) {
        UserEntity user = userRepository.getById(request.getId());

        List<UserRole> roles = user.getRoles().stream().toList();

        if (roles.contains(UserRole.ROLE_SUPER)) {
            return "Super";
        }

        if (roles.contains(UserRole.ROLE_ADMIN)) {
            return "Admin";
        }

        return "User";
    }

    @CacheEvict(value = "userListCache", allEntries = true)
    public void deleteUser(ActionRequest request) {
        userRepository.deleteById(request.getId());
    }
}
