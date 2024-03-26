package org.ltclab.sb_bookstore.repository;

import org.ltclab.sb_bookstore.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);
//    @Override
//    <S extends Category> List<S> saveAll(Iterable<S> categories);

//    default List<Category> saveAll (List<Category> categories) {
//        List<Category> added = new ArrayList<>();
//        for (Category c : categories) {
//            if (!existsByCategoryName(c.getCategoryName())) {
//                added.add(c);
//                save(c);
//            }
//        }
//        return added;
//    }
}
