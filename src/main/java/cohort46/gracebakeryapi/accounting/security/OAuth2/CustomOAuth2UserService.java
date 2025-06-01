package cohort46.gracebakeryapi.accounting.security.OAuth2;

import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import cohort46.gracebakeryapi.other.exception.OAuth2EmailNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String givenName = (String) attributes.getOrDefault("given_name", "");
        String familyName = (String) attributes.getOrDefault("family_name", "");

        if (email == null || email.trim().isEmpty()) {
            throw new OAuth2EmailNotFoundException();
        }

        UserAccount user = userRepository.findUserByLogin(email)
                .orElse(userRepository.findUserByEmail(email).orElse(null));

        if (user == null) {
            user = UserAccount.builder()
                    .id(null)
                    .email(email)
                    .login(email)
                    .firstName(givenName)
                    .lastName(familyName)
                    .role(RoleEnum.USER)
                    .password(String.valueOf(new Random().nextInt(Integer.MAX_VALUE))) // временный пароль
                    .build();
            userRepository.saveAndFlush(user);
        }

        return new UserDetailsImpl(user);
    }
}
