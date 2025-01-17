package br.com.gpiagentini.api.infraestructure.persistence.repository;

import br.com.gpiagentini.api.application.port.out.UserRepository;
import br.com.gpiagentini.api.infraestructure.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImp implements UserRepository {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public UserDetails findByLogin(String login) {
        return userJpaRepository.findByLogin(login);
    }
}

interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserDetails findByLogin(String login);
}
