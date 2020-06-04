package com.junior;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    private final ProductService service;

    public ProductController(ProductRepository repository, ProductService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public List<Product> findAll() {
        //Utilizando records com DTO.
        return repository.findAll();
    }

    @GetMapping("{id}")
    public @ResponseBody
    ProductRecord findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public Product create(@RequestBody @Valid ProductDTO product) {
        return this.repository.save(product.toEntity());

    }
}
