package cohort46.gracebakeryapi.security;


import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.other.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String login) throws UsernameNotFoundException {
        UserAccount userAccount = userRepository.findUserByLogin(login).orElse(null);
        if (userAccount == null) {
            userAccount = userRepository.findUserByEmail(login).orElseThrow(() -> new UserNotFoundException(  "login " + login ));
        }
        return new UserDetailsImpl(userAccount);
    }
}
