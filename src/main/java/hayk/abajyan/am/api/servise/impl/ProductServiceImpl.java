package hayk.abajyan.am.api.servise.impl;

import hayk.abajyan.am.api.exceptions.ProductNotFoundException;
import hayk.abajyan.am.api.exceptions.ProductUpdateException;
import hayk.abajyan.am.api.model.Product;
import hayk.abajyan.am.api.repository.ProductRepository;
import hayk.abajyan.am.api.servise.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> get(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            logger.warn("product does not exist!!");
            throw new ProductNotFoundException("Product not found");
        }
        return product;
    }

    @Override
    public Product add(Product product) {
        return productRepository.save(product);
    }


    @Override
    public Product update(Product product, int id) throws ProductUpdateException {
        Optional<Product> productForUpdate = productRepository.findById(id);
        if (!validateProduct(product, id)) {
            logger.error("Error: invalid update data !");
            throw new ProductUpdateException("invalid update data");
        }
        productForUpdate.get().setName(product.getName());
        productForUpdate.get().setDescription(product.getDescription());
        productForUpdate.get().setPrice(product.getPrice());
        productForUpdate.get().setCreatedDate(product.getCreatedDate());
        productForUpdate.get().setModificationDate(LocalDate.now());
        productRepository.save(productForUpdate.get());
        logger.info("Product successfully updated.");
        return productForUpdate.get();
    }


    @Override
    public void delete(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getByName(String name) {
        Optional<Product> product = productRepository.getByName(name);
        if (!product.isPresent()) {
            logger.warn("product does not exist!!");
            throw new ProductNotFoundException("Product not found");
        }
        return product;
    }


    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> get(int size, int pageSize) {
        Pageable pageable = PageRequest.of(size,pageSize);
        return productRepository.findAll(pageable);
    }

    public List<Product> getProductsByCriteria(String currency, int languageId) {
        List<Product> products = productRepository.findAll();
        return getFilteredProducts(products, currency, languageId);
    }

    private List<Product> getFilteredProducts(List<Product> products, String currency, int languageId) {
        for (Product p : products) {
            if (p.getCurrency().toString().equals(currency) && p.getLanguageId() == languageId) {
                if ((p.getDescription() == null || p.getDescription().trim().equals("")) ||
                        (p.getName() == null || p.getName().trim().equals(""))) {
                    products.remove(p);
                }
            }
        }
        return products;
    }

    private boolean validateProduct(Product product, int id) {
        if (!productRepository.findById(id).isPresent()) {
            logger.warn("Product for update can not found");

            return false;
        }
        if (product.getName() == null || product.getName().trim().equals("")) {
            logger.warn("Product name can not be null or empty!");
            return false;
        }
        if (product.getPrice() <= 0) {
            logger.warn("Product prise can not be 0 or negative !");
            return false;
        }
        return true;
    }
}
