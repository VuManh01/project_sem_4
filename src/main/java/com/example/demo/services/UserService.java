package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateUser;
import com.example.demo.dto.response.common_response.UserResponse;
import com.example.demo.dto.response.display_for_admin.UserDisplayForAdmin;
import com.example.demo.entities.Users;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.util.EmailValidator;
import com.example.demo.util.PasswordValidator;
import com.example.demo.util.PhoneNumberValidator;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<UserResponse> getAllUsers(){
        return userRepository.findAllByIsDeleted(false)
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse toUserResponse(Users user) {
        UserResponse res = new UserResponse();
        BeanUtils.copyProperties(user, res);
        res.setIsDeleted(user.getIsDeleted());
        return res;
    }

    public List<UserDisplayForAdmin> getAllUsersDisplayForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        return userRepository.findAllNotDeletedPaging(false, pageable)
                .stream()
                .map(this::toUserDisplayForAdmin)
                .collect(Collectors.toList());
    }
    public UserDisplayForAdmin toUserDisplayForAdmin(Users user) {
        UserDisplayForAdmin res = new UserDisplayForAdmin();
        BeanUtils.copyProperties(user, res);
        res.setIsDeleted(user.getIsDeleted());
        return res;
    }

    public UserResponse findById(int id) {
        Optional<Users> op = userRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any user with id: " + id);
        }
        return toUserResponse(op.get());
    }

    public UserDisplayForAdmin findUserDisplayForAdminById(int id) {
        Optional<Users> op = userRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any user with id: " + id);
        }
        return toUserDisplayForAdmin(op.get());
    }

    //Service: edit information of user by admin
    public UserResponse updateUser(NewOrUpdateUser request) {
        List<Map<String, String>> errors = new ArrayList<>();
        Optional<Users> op = userRepository.findByIdAndIsDeleted(request.getId(), false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any user with id: " + request.getId());
        }

        // nếu ko null thì mới check unique title(do là album nên cần check trùng title)
        Optional<Users> opTitle = userRepository.findByUsername(request.getUsername());
        if (opTitle.isPresent() && opTitle.get().getUsername() != op.get().getUsername()) {
            errors.add(Map.of("usernameError", "Already exist username"));
        }

        if (!request.getPassword().isEmpty()) {
            if (!PasswordValidator.isValidPassword(request.getPassword())) {
                errors.add(Map.of("passwordError",
                        "Password is not strong enough, at least 8 characters with special character and number"));
            }
        }

        if (!PhoneNumberValidator.isValidPhoneNumber(request.getPhone())) {
            errors.add(Map.of("phoneError", "Phone number is not valid"));
        }

        Optional<Users> opPhone = userRepository.findByPhone(request.getPhone());
        if (opPhone.isPresent() && opPhone.get().getPhone() != op.get().getPhone()) {
            errors.add(Map.of("phoneError", "Already exist phone number"));
        }

        if (!EmailValidator.isValidEmail(request.getEmail())) {
            errors.add(Map.of("emailError", "Email is not valid"));
        }
        Optional<Users> opEmail = userRepository.findByEmail(request.getEmail());
        if (opEmail.isPresent() && opEmail.get().getEmail() != op.get().getEmail()) {
            errors.add(Map.of("emailError", "Already exist email"));
        }


        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        Users user = op.get();
        if (!StringUtils.isEmpty(request.getAvatar())) {
            //check xem có ảnh ko, có thì thay mới, ko thì thôi
            fileService.deleteImageFile(user.getAvatar());
            user.setAvatar(request.getAvatar());
        }
        user.setUsername(request.getUsername());
        if (!request.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(request.getPassword()));
        } else {
            user.setPassword(op.get().getPassword());
        }

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setDob(request.getDob());
        user.setModifiedAt(new Date());
        userRepository.save(user);
        return toUserResponse(user);
    }

    //Service: deleted information of user by admin
    //vẫn lưu information trong database chỉ bị is_deleted = 1
    public boolean deleteById(int id) {
        Optional<Users> op = userRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any user with id: " + id);
        }
        Users existing = op.get();
        existing.setIsDeleted(true);
        userRepository.save(existing);
        return true;
    }

    public List<UserDisplayForAdmin> getSearchUserDisplayForAdmin(String searchTxt, int page){
        Pageable pageable = PageRequest.of(page, 10);

        return userRepository.searchNotDeletedPaging(searchTxt, false, pageable)
                .stream()
                .map(this::toUserDisplayForAdmin)
                .collect(Collectors.toList());
    }
}
