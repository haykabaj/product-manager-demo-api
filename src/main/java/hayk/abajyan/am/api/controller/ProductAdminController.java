package hayk.abajyan.am.api.controller;

import com.sun.istack.NotNull;
import hayk.abajyan.am.api.exceptions.ProductNotFoundException;
import hayk.abajyan.am.api.exceptions.ProductUpdateException;
import hayk.abajyan.am.api.model.Product;
import hayk.abajyan.am.api.servise.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/product/admin")
public class ProductAdminController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductAdminController.class);

    @Autowired
    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) {
        Optional<Product> product = productService.get(id);
        try{
            return ResponseEntity.ok(product);
        }catch (ProductNotFoundException e){
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(Collections.EMPTY_SET, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @NotNull Product product, @RequestParam("languageId") int languageId) {
        product.setLanguageId(languageId);
        productService.add(product);
        logger.info("product successfully created.");
        return ResponseEntity.ok("congrats product successfully created.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable("id") int id) {
        try {
            Product updatedProduct = productService.update(product, id);
            return ResponseEntity.ok(updatedProduct);
        }catch (ProductUpdateException e){
            logger.error(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") int id){
        productService.delete(id);
        return ResponseEntity.ok("product successfully deleted!");

    }
}
