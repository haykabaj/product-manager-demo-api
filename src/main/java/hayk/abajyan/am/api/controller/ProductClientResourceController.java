package hayk.abajyan.am.api.controller;

import hayk.abajyan.am.api.exceptions.ProductNotFoundException;
import hayk.abajyan.am.api.model.Product;
import hayk.abajyan.am.api.servise.ProductService;
import hayk.abajyan.am.api.servise.impl.PagingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product/client")
public class ProductClientResourceController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductClientResourceController.class);


    @Autowired
    public ProductClientResourceController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<?> getProductByName(@RequestParam("name") String name) {
        Optional<Product> product = productService.getByName(name);
        try {
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(Collections.EMPTY_SET + "Product with this name does not exist!", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAll();
        if (products.size() == 0) {
            return new ResponseEntity<>(Collections.EMPTY_LIST + "there is not any products yet !", HttpStatus.OK);
        }
        return ResponseEntity.ok(products);
    }


    @GetMapping("/products_search")
    public ResponseEntity<?> getAllProductsByCriteria(@RequestParam("currency") String currency,
                                                      @RequestParam("languageType") int languageType) {

        List<Product> products = productService.getAll();
        if (products.size() == 0) {
            return new ResponseEntity<>(Collections.EMPTY_LIST + "there is not  any products according to relevant criteria!"
                    , HttpStatus.OK);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@RequestParam(value = "pageNo",
                                                    defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER,
                                                    required = false) int size,
                                            @RequestParam(value = "pageSize",
                                                    defaultValue = PagingConstants.DEFAULT_PAGE_SIZE,
                                                    required = false) int pageSize){
        Page<Product> product = productService.get(size, pageSize);
        return  ResponseEntity.ok(product);
    }


}
