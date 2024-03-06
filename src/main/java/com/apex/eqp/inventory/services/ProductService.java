package com.apex.eqp.inventory.services;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.helpers.ProductFilter;
import com.apex.eqp.inventory.repositories.InventoryRepository;
import com.apex.eqp.inventory.repositories.RecalledProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final InventoryRepository inventoryRepository;
    private final RecalledProductRepository recalledProductRepository;

    @Transactional
    public Product save(Product product) {
        return inventoryRepository.save(product);
    }

    public Collection<Product> getAllProduct() {
        Set<String> recalledProductsStringSet = recalledProductRepository.findAll().stream()
                .map(RecalledProduct::getName)
                .collect(Collectors.toSet());
        ProductFilter filter = new ProductFilter(recalledProductsStringSet);

        return filter.removeRecalledFrom(inventoryRepository.findAll());
    }

    // Move the filtering logic to the query instead of filtering in Java
    public Collection<Product> getAllProduct2() {
        return inventoryRepository.getNotRecalledProducts();
    }

    public Optional<Product> findById(Integer id) {
        return inventoryRepository.findById(id);
    }

    public Product deleteById(Integer id) {
        var deleted = inventoryRepository.findById(id);
        if (deleted.isEmpty()) {
            throw new IllegalArgumentException(String.format("Product %s not found", id));
        }
        inventoryRepository.deleteById(id);
        return deleted.get();
    }

    public Product update(Product product) {
        if (!inventoryRepository.existsById(product.getId())) {
            throw new IllegalArgumentException(String.format("Product %s not found", product.getId()));
        }
        inventoryRepository.save(product);
        return product;
    }
}
