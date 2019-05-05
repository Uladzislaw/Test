package by.training.info_system.service;

import by.training.info_system.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service {
    Optional<User> findByLogin(String email);
    boolean registerNewUser(User user);
    boolean isInBlackList(User user);
    Optional<List<User>> readBlackList();
    Optional<List<User>> findUsersWithDiscount();
}
