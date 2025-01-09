package cohort46.gracebakeryapi.bakery.category.service;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import org.springframework.web.bind.annotation.PathVariable;


public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);//Long
    CategoryDto findCategoryById(Long categoryId);
    CategoryDto updateCategory(CategoryDto categoryDto, Long id);
    CategoryDto activateCategory(Long Id, Boolean activate);
    Iterable<CategoryDto> findCategoriesByIsActive(Boolean isactive);
    Iterable<CategoryDto> findCategoriesBySection(Long sectionid);
    Iterable<CategoryDto> getCategoriesAll();
    Iterable<CategoryDto> findCategoriesBySectionByIsActive(Long section_id, Boolean isactive);
}
