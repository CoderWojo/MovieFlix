package com.movieflix.service;

import com.movieflix.dto.MailBody;

public interface EmailService {
    void sendSimpleMailMessage(MailBody mailBody);
}
