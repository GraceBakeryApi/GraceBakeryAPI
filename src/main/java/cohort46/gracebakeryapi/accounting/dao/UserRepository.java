package cohort46.gracebakeryapi.accounting.dao;

import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    //*
    Optional<UserAccount> findUserByEmail(String email);
    Optional<UserAccount> findUserByPhone(String phone);
    Optional<UserAccount> findUserByLogin(String login);
    Stream<UserAccount> findUserByBirthdate(Long birthdate, Sort sort);
    Stream<UserAccount> findUserByFirstName(String firstname, Sort sort);
    Stream<UserAccount> findUserByLastName(String lastname, Sort sort);
    //Stream<UserAccount> findAllByRole(RoleEnum role);

     //*/

}