package cohort46.gracebakeryapi.bakery.category.dao;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.section.model.Section;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Stream;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    Stream<Category> findCategoriesByIsActive(Boolean isactive);
    Stream<Category>  findCategoriesBySectionId(Long section_id, Sort sort);
    Stream<Category>  findCategoriesBySectionIdAndIsActive(Long section_id, Boolean isactive);
}