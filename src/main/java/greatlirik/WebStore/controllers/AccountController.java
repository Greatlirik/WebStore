package greatlirik.WebStore.controllers;


import greatlirik.WebStore.entities.AccountEntity;
import greatlirik.WebStore.entities.Role;
import greatlirik.WebStore.repositories.AccountRepository;
import greatlirik.WebStore.service.DefaultUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public ModelAndView adminPage(@RequestParam(name = "username", required = false) String username) {
            final Iterable<AccountEntity> accounts = accountRepository.findAll();
            final Map<String, Object> model = Map.of(
                    "accounts", accounts
            );
        return new ModelAndView("admin", model);
    }


    @GetMapping("/account")
    public ModelAndView account() {
        final AccountEntity accountEntity = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(auth -> auth instanceof DefaultUserDetailsService.SimpleUserDetails)
                .map(auth -> (DefaultUserDetailsService.SimpleUserDetails) auth)
                .map(DefaultUserDetailsService.SimpleUserDetails::getAccount)
                .map(AccountEntity::getId)
                .flatMap(accountRepository::findById)
                .orElseThrow();
        final Map<String, Object> model = Map.of(
                "name", accountEntity.getName(),
                "wallet",accountEntity.getWallet(),
                "products",accountEntity.getProducts()

        );
        return new ModelAndView("account", model);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/")
    public String toStore() {
        return "redirect:/store";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(AccountEntity user, Map<String, Object> model) {
        final Optional<AccountEntity> foundUser = accountRepository.findByName(user.getName());
        if (foundUser.isPresent()) {
            model.put("message", "!User exists!");
            return "registration";
        }
        accountRepository.save(user
                .setActive(true)
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setWallet(0.0)
                .setRoles(Set.of(Role.USER))
        );
        return "redirect:/login";
    }


}
