package greatlirik.WebStore.controllers;

import greatlirik.WebStore.entities.AccountEntity;
import greatlirik.WebStore.entities.ProductEntity;
import greatlirik.WebStore.repositories.AccountRepository;
import greatlirik.WebStore.repositories.ProductRepository;
import greatlirik.WebStore.service.DefaultUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/store")
    public ModelAndView page() {
        final Map<String, Object> model = Map.of(
                "products", productRepository.findAll()
        );
        return new ModelAndView("store", model);
    }

    @GetMapping("/add")
    public String addProductPage() {
        return "add";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam(name = "title") String title, @RequestParam(name = "description") String description,
                             @RequestParam(name = "price") Double price, @RequestParam(name = "quantity") Integer quantity) {
        ProductEntity product = new ProductEntity();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product = productRepository.save(product);
        return String.format("redirect:/store", product.getId());
    }


    @GetMapping("/search")
    public ModelAndView pageSearch(@RequestParam(name = "q", required = false) String query) {
        final List<ProductEntity> products = Optional.ofNullable(query)
                .map(productRepository::findAllByTitleContainingIgnoreCase)
                .orElseGet(Collections::emptyList);
        final Map<String, Object> model = Map.of(
                "products", products
        );
        return new ModelAndView("search", model);
    }

    @GetMapping("/products/{id}")
    public ModelAndView addBookPage(@PathVariable("id") Integer id) {
        final ProductEntity product = productRepository.findById(id)
                .orElse(null);//throw 404 here
        final Map<String, Object> model = Map.of(
                "product", product
        );
        return new ModelAndView("product", model);
    }

    @PostMapping("/products/{id}")
    public String buyProduct(@PathVariable("id") Integer id, Map<String, Object> model) {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(auth -> auth instanceof DefaultUserDetailsService.SimpleUserDetails)
                .map(auth -> (DefaultUserDetailsService.SimpleUserDetails) auth)
                .map(DefaultUserDetailsService.SimpleUserDetails::getAccount)
                .map(AccountEntity::getId)
                .flatMap(accountId -> accountRepository
                        .findById(accountId)
                        .flatMap(account -> productRepository
                                .findById(id)
                                .map(productEntity -> {
                                    if (account.getWallet() < productEntity.getPrice()) {
                                        model.put("message", "!Not enough money!");
                                    } else {
                                        productEntity.setQuantity(productEntity.getQuantity() - 1);
                                        account.getProducts().add(productEntity);
                                        account.setWallet(account.getWallet() - productEntity.getPrice());
                                        model.put("message", "!Product purchased!");
                                        accountRepository.save(account);
                                    }

                                    return productEntity;
                                })

                        )
                )
                .ifPresent(productRepository::save);
        return "products/{id}";
    }

}
