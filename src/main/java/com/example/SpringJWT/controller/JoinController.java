package com.example.SpringJWT.controller;

import com.example.SpringJWT.dto.JoinDto;
import com.example.SpringJWT.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class JoinController {

    private final JoinService joinService;


    //@AutoWire 를 사용해도 되지만 직접 주입하는걸 권장
    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(JoinDto joinDto){

        joinService.joinProcess(joinDto);
/*
401, 404 등 회원가입이 안되면 이런 코드를 가져야 되는데 이번에는 간단하게 ok싸인을 넘겨주도록 한다.
 */
        return "ok";
    }
}
