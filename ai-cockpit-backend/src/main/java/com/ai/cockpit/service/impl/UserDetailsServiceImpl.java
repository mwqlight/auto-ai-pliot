package com.ai.cockpit.service.impl;

import com.ai.cockpit.entity.User;
import com.ai.cockpit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户详情服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        
        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }
        
        return new UserDetailsImpl(user);
    }
    
    /**
     * 自定义UserDetails实现
     */
    public static class UserDetailsImpl implements UserDetails {
        
        private final User user;
        
        public UserDetailsImpl(User user) {
            this.user = user;
        }
        
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }
            
            return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
        }
        
        @Override
        public String getPassword() {
            return user.getPassword();
        }
        
        @Override
        public String getUsername() {
            return user.getUsername();
        }
        
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
        
        @Override
        public boolean isAccountNonLocked() {
            return user.getStatus() != null && user.getStatus() == 1;
        }
        
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
        
        @Override
        public boolean isEnabled() {
            return user.getStatus() != null && user.getStatus() == 1;
        }
        
        public User getUser() {
            return user;
        }
    }
}