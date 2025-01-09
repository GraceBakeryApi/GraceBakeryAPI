package cohort46.gracebakeryapi.accounting.dao;

import cohort46.gracebakeryapi.accounting.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User, Long> {
    //*
    User findUserByEmail(String email);
    User findUserByPhone(String phone);
    Stream<User> findUserByBirthdate(Long birthdate, Sort sort);
    Stream<User> findUserByFirstName(String firstname, Sort sort);
    Stream<User> findUserByLastName(String lastname, Sort sort);

     //*/

}