package greatlirik.WebStore.service;


import greatlirik.WebStore.entities.AccountEntity;
import greatlirik.WebStore.repositories.AccountRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return accountRepository.findByName(name)
                .map(SimpleUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Can't resolve user by name " + name));
    }

    @Data
    @RequiredArgsConstructor
    public static class SimpleUserDetails implements UserDetails {

        private final AccountEntity account;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.account.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
        }

        @Override
        public String getPassword() {
            return account.getPassword();
        }

        @Override
        public String getUsername() {
            return account.getName();
        }

        @Override
        public boolean isAccountNonExpired() {
            return account.isActive();
        }

        @Override
        public boolean isAccountNonLocked() {
            return account.isActive();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return account.isActive();
        }

        @Override
        public boolean isEnabled() {
            return account.isActive();
        }
    }
}
