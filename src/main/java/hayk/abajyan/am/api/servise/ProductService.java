package hayk.abajyan.am.api.servise;

import hayk.abajyan.am.api.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> get(int id);

    Product add(Product product);

    Product update(Product product, int id);

    void delete(int id);

    Optional<Product> getByName(String name);

    List<Product> getAll();

    Page<Product> get(int pageSize, int size);
}
