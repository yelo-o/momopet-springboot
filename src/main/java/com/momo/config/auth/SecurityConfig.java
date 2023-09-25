package com.momo.config.auth;

import com.momo.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security를 활성화
//@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable().headers().frameOptions().disable() //h2-console 화면을 위해 해당 옵션들 disable 처리
            .and()
                .authorizeHttpRequests() //URL별 권한 관리를 설정하는 옵션의 시작점
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile", "/board/**").permitAll() //이쪽 url들은 아무런 권한없이 들어갈 수 있다.
                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //지정된 옵션에는 전체 열람 권한 부여 => 권한이 ROLE_USER인 경우
                .antMatchers("/members/**").hasRole(Role.GUEST.name()) //지정된 옵션에는 전체 열람 권한 부여 => 권한이 ROLE_GUEST인 경우
                .anyRequest().authenticated() //anyRequest() : 설정된 값들 이외 나머지 URL
            .and()
                .logout().logoutSuccessUrl("/") //로그아웃 기능에 대한 여러 설정의 진입점 & 로그아웃 성공 시 "/" 주소로 이동
            .and()
                .oauth2Login() //OAuth2 로그인 기능에 대한 여러 설정의 진입점
                    .userInfoEndpoint() //OAuth2 로그인 성공 이후 사용자 정보 가져올 때의 설정 담당
                        .userService(customOAuth2UserService); //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록
    }

}
