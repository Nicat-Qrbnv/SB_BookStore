package org.ltclab.sb_bookstore.repository;

import feign.Param;
import org.ltclab.sb_bookstore.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.categoryName = :name")
    Category findCategory (@Param("name") String categoryName);
}
