package hayk.abajyan.am.api.repository;

import hayk.abajyan.am.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM Products where name = ?1", nativeQuery = true)
     Optional<Product> getByName(String name);
}
