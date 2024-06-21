package com.sparta.javajyojo.security;

import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.entity.UserRoleEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {return user;}

    // 무조건 상속 받아야 함
    // 권한 가져오는 부분
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    // password 가져오기
    @Override
    public String getPassword() { return user.getPassword(); }

    // username 가져오기
    @Override
    public String getUsername() { return user.getUsername(); }

    // 계정 만료 안됨
    @Override
    public boolean isAccountNonExpired() { return true; }

    //계정 잠겨있지 않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화
    @Override
    public boolean isEnabled() {
        return true;
    }
}
