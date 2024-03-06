package com.apex.eqp.inventory.repositories;

import com.apex.eqp.inventory.entities.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface InventoryRepository extends JpaRepository<Product, Integer> {

  @Query("SELECT p from Product p left join RecalledProduct r on p.name = r.name where r.name is null")
  List<Product> getNotRecalledProducts();
}
