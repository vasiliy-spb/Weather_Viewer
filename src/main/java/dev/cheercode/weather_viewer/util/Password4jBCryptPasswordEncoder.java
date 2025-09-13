package dev.cheercode.weather_viewer.util;

import com.password4j.Hash;
import com.password4j.Password;
import org.springframework.stereotype.Component;

@Component
public class Password4jBCryptPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        Hash hash = Password.hash(password).withBcrypt();
        return hash.getResult();
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return Password.check(rawPassword, encodedPassword).withBcrypt();
    }
}
