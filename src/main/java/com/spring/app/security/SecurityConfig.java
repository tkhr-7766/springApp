package com.spring.app.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
                .loginPage("/login") // ログインページのURL
                .loginProcessingUrl("/login") // ログイン処理(POST)のURL
                .usernameParameter("email") // 認証のusernameに使用する属性
                .passwordParameter("password") // 認証のpasswordに使用する属性
                .defaultSuccessUrl("/") // ログイン成功後にデフォルトで飛ばされるURL
                .failureUrl("/login?error") // ログイン失敗時に飛ばされるURL
                .permitAll()) // loginページは常にアクセスを許可する
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")) // ログアウト後のURL
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // cssなど静的ファイルは常にアクセスを許可する
                        .mvcMatchers("/login").permitAll() // loginページは常にアクセスを許可する
                        .anyRequest().authenticated()); // その他のリクエストは常に認証を必要とする

        return http.build();
    }

    @Bean
    public PasswordEncoder PasswordEncoder() {
        // 入力されたパスワードとDBのハッシュ化されたパスワードを照合する際に使用するエンコーダーの設定
        return new BCryptPasswordEncoder();
    }
}
