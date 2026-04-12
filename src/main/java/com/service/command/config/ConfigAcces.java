package com.service.command.config;

import com.service.command.users.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ConfigAcces {

    private final JwtFilter jwtFilter;

    // Inyectamos el filtro (Usamos @Lazy para que no haya un bucle de dependencias)
    public ConfigAcces(@Lazy JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //User
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/").permitAll()
                        .requestMatchers("/user/**").hasAnyRole("Admin", "Mandated")

                        //Productos
                        .requestMatchers("/product/list").permitAll()
                        .requestMatchers("/product/list/category").permitAll()
                        .requestMatchers("/product/list/name").permitAll()
                        .requestMatchers("/product/nro/").permitAll()
                        .requestMatchers("/product/**").hasAnyRole("Admin", "Mandated")

                        //Order
                        .requestMatchers("/order").hasAnyRole("Admin", "Mandated","WaiterUser","CheckerUser")
                        .requestMatchers("/order/delete/").hasAnyRole("Admin", "Mandated","CheckerUser")
                        .requestMatchers("/order/list/").hasAnyRole("Admin", "Mandated","WaiterUser","CheckerUser")
                        .requestMatchers("/order/status/").hasAnyRole("Admin", "Mandated","CheckerUser")
                        .requestMatchers("/order/").hasAnyRole("Admin", "Mandated","CheckerUser","WaiterUser")
                        // 5. Cualquier otra ruta no especificada arriba requiere estar logueado al menos
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private static final String StrongKey = "asgewg2g52dgJd0248FN23T203880M2@235209D0J90SFDFasfqsfoue2nfo928ev04378bu209ed08hjw00sd23493407b0wd98n8c2";
    private final Key key = Keys.hmacShaKeyFor(StrongKey.getBytes(StandardCharsets.UTF_8));
    private final long expirationTime = 21600000;

    public String generateToken(Users user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("lastName", user.getLastName())
                .claim("role", "ROLE_" + user.getRol().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
