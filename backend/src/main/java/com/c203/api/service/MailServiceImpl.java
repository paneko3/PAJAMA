package com.c203.api.service;

import com.c203.api.dto.MailCheckDto;
import com.c203.api.dto.MailSendDto;
import com.c203.db.Entity.Auth;
import com.c203.db.Repository.MailRepository;
import com.c203.db.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;
    private final UserRepository userRepository;
    private static final String FROM_ADDRESS = "c203ssafy@gmail.com";
    @Autowired
    public MailServiceImpl(MailRepository mailRepository,JavaMailSender javaMailSender,UserRepository userRepository){
        this.mailRepository = mailRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }
    @Override
    public void mailSend(MailSendDto mailSendDto) {
        String authKey = makeAuthNumber();
        SimpleMailMessage message = new SimpleMailMessage();
        String subText = "[PAZAMA] 인증번호 입니다. \n 인증번호 : " + authKey;
        message.setTo(mailSendDto.getId());
        message.setFrom(MailServiceImpl.FROM_ADDRESS);
        message.setSubject("[인증번호]");
        message.setText(subText);
        javaMailSender.send(message); // 메일 전송
        Auth mail = new Auth();
        mail.setAuthEmail(mailSendDto.getId());
        mail.setAuthNum(authKey);
        mailRepository.saveAndFlush(mail);

    }

    public String makeAuthNumber() {
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888)+111111);
        return authKey;
    }

    @Override
    @Transactional
    public boolean mailCheck(String authNumber, String email) {
        if(mailRepository.findByAuthEmailAndAuthNum(email, authNumber).isPresent()){
            mailRepository.deleteByAuthEmail(email);
            return true;
        }
        return false;
    }
}
