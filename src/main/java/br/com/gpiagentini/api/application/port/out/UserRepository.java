package br.com.gpiagentini.api.application.port.out;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {
     UserDetails findByLogin(String username);
}
