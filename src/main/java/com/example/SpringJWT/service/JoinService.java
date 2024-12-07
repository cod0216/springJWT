package com.example.SpringJWT.service;

import com.example.SpringJWT.dto.JoinDto;
import com.example.SpringJWT.entity.UserEntity;
import com.example.SpringJWT.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//@service를 써서 스프링한테 관리 되도록 설정
@Service
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    // 여기서 초기화


    public void joinProcess(JoinDto joinDto){
        String username = joinDto.getUsername();
        String password = joinDto.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if(isExist){

            return;
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        //암호화 ㅐ해서 넣어야됨
        data.setRole("ROLE_ADMIN");
        //ROLE_레벨
        // 지금은 강제로 "ROlE_ADMIN"이라고 넣음

        userRepository.save(data);



    }
}
