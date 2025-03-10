package cohort46.gracebakeryapi.accounting.security.JWT;


import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key_your_secret_key_your";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 часа
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String createToken(UserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("role", userDetails.getRole());
        claims.put("tocken", encodeUserDetailsToken(userDetails.getUser()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public UserAccount getUserDetails(String token) {
        return decodeUserDetailsToken(extractAllClaims(token).get("tocken" , String.class));
    }

    public Optional<RoleEnum> getUserRole(String token) {
        try {
            return Optional.of(RoleEnum.valueOf(extractAllClaims(token).get("role", String.class)));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String encodeUserDetailsToken(UserAccount user){
        return  SafeAESUtil.encrypt(
                user.getId().toString() + System.lineSeparator()
                        + user.getRole().name() + System.lineSeparator()
                        + user.getFirstName() + System.lineSeparator()
                        + user.getLastName() + System.lineSeparator()
                        + user.getLogin() + System.lineSeparator()
                        + user.getEmail() + System.lineSeparator()
                        + user.getPhone() + System.lineSeparator()
                        + user.getCartId()
        );
    }

    private UserAccount decodeUserDetailsToken(String tocken){
        UserAccount user = new UserAccount();
        String[] data = SafeAESUtil.decrypt(tocken).split(System.lineSeparator());
        try {
            user.setId(Long.valueOf(data[0]));
        } catch (NumberFormatException e) {
            user.setId(null);
        }

        try {
            user.setRole(RoleEnum.valueOf(data[1]));
        } catch (IllegalArgumentException e) {
            user.setRole(null);
        }

        user.setFirstName(data[2]);
        user.setLastName(data[3]);
        user.setLogin(data[4]);
        user.setEmail(data[5]);
        user.setPhone(data[6]);

        try {
            user.setCartId(Long.valueOf(data[7]));
        } catch (NumberFormatException e) {
            user.setCartId(null);
        }
        return user;
    }

    /*
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

     //*/
}
