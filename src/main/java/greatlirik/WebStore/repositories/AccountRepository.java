package greatlirik.WebStore.repositories;


import greatlirik.WebStore.entities.AccountEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AccountRepository extends PagingAndSortingRepository<AccountEntity,Long> {
    Optional<AccountEntity> findByName(String username);
}
