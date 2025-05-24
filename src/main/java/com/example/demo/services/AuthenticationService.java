package com.example.demo.services;

import com.example.demo.dto.response.NewOrUpdateUser;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entities.Users;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.util.EmailValidator;
import com.example.demo.util.PasswordValidator;
import com.example.demo.util.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserResponse register(NewOrUpdateUser request){
        //tạo danh sách lỗi
        List<Map<String, String>> errors = new ArrayList<>();

        //tạo 1 đối tượng newUser
        Users newUser = new Users();

        //Optional: Nếu có thì trả về Object Users ko thi trả về Object rỗng
        Optional<Users> optional = userRepository.findByUsername(request.getUsername());

        //bắt từng lỗi

        // optional.isPresent(): Kiểm tra xem optional có giá trị tức là username trùng thì add errors
        if (optional.isPresent()) {
            errors.add(Map.of("usernameError", "Already exist username"));
        }

        if(!PasswordValidator.isValidPassword(request.getPassword())){
            errors.add(Map.of("passwordError",
                    "Password is not strong enough, at least 8 character with special character and number"));
        }

        Optional<Users> optionalPhone = userRepository.findByPhone(request.getPhone());

        if(optional.isPresent()){
            errors.add(Map.of("phoneError", "Already exist phone"));
        }

        if(!EmailValidator.isValidEmail(request.getEmail())){
            errors.add(Map.of("emailError", "Email is not valid"));
        }

        Optional<Users> optionalEmail = userRepository.findByEmail(request.getEmail());

        if(optionalEmail.isPresent()){
            errors.add(Map.of("emailError", "Already exist email"));
        }

        if (!Objects.equals(request.getRole(), Role.ROLE_USER.toString())
                && !Objects.equals(request.getRole(), Role.ROLE_ADMIN.toString())
                && !Objects.equals(request.getRole(), Role.ROLE_ARTIST.toString())) {
            errors.add(Map.of("roleError", "Role is not valid"));
        }

        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }

        //gán giá trị vào newUser
        newUser.setUsername(request.getUsername());
        newUser.setPassword(encoder.encode(request.getPassword()));
        newUser.setFullname(request.getFullName());
        newUser.setAvatar(request.getAvatar());
        newUser.setPhone(request.getPhone());
        newUser.setEmail(request.getEmail());
        newUser.setRole(request.getRole());
        newUser.setDob(request.getDob());
        newUser.setIsDeleted(false);
        newUser.setCreatedAt(new Date());
        newUser.setModifiedAt(new Date());

        userRepository.save(newUser);
        return toUserResponse(newUser);


    }

    public UserResponse toUserResponse(Users user) {
        UserResponse res = new UserResponse();
        BeanUtils.copyProperties(user, res);
        res.setIsDeleted(user.getIsDeleted());
        return res;
    }

}
