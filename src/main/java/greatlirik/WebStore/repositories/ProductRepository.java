package greatlirik.WebStore.repositories;

import greatlirik.WebStore.entities.ProductEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository <ProductEntity, Integer>{
    List <ProductEntity> findAll();
    List<ProductEntity> findAllByTitleContainingIgnoreCase(String title);

}
