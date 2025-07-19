package app.controller;

import app.entity.Product;
import app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public boolean create(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.updateById(product);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return productService.removeById(id);
    }
}
