package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.contracts.Command;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.file.Path;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class NotificationService implements Command {

    @Value("${spring.mail.sender.email}")
    @NonFinal
    String applicationEmail;

    JavaMailSender mailSender;

    @Override
    public void execute(DefaultAbsSender bot, Update update) {
        TelegramBotUtils.informAboutDeveloping(bot, update);
    }

    public void sendSimpleMailToSelf(String username, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(applicationEmail);
        message.setTo(applicationEmail);
        message.setSubject("Обращение по поводу приложения Mater от @%s".formatted(username));
        message.setText(text);
        mailSender.send(message);
    }

    public void sendSimpleMailToUser(String destination, String title, String text) {
        sendSimpleMails(List.of(destination), title, text);
    }

    public void sendSimpleMails(List<String> destinations, String title, String text) {
        SimpleMailMessage[] messages = destinations.stream()
                .map(destination -> {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(applicationEmail);
                    message.setTo(destination);
                    message.setSubject(title);
                    message.setText(text); // <p>Сообщение в формате <b>Html</b>.<br>Вторая строка.</p>
                    return message;
                })
                .toArray(SimpleMailMessage[]::new);
        mailSender.send(messages);
    }

    public void sendMessageWithAttachment(String destination,
                                          String title,
                                          String text,
                                          Path fileToAttach) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(applicationEmail);
            helper.setTo(destination);
            helper.setSubject(title);
            helper.setText(text); // <p>Сообщение в формате <b>Html</b>.<br>Вторая строка.</p>
            FileSystemResource file = new FileSystemResource(fileToAttach);
            helper.addAttachment("Вложение", file);
        } catch (MessagingException e) {
            log.error(e);
        }
        mailSender.send(message);
    }
}
