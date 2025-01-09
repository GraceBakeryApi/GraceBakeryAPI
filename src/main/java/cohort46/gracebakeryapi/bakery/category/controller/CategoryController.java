package cohort46.gracebakeryapi.bakery.category.controller;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.category.service.CategoryService;
import cohort46.gracebakeryapi.bakery.section.dto.SectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto)  ;
    }//Long

    @GetMapping("/category/{id}")
    public CategoryDto findCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }

    @PutMapping("/category/{id}")
    public CategoryDto updateCategory( @RequestBody CategoryDto categoryDto, @PathVariable Long id) {
        return categoryService.updateCategory(categoryDto, id);
    }
    @PatchMapping("/category/{id}/isactive/{isactive}")
    CategoryDto activateCategory(@PathVariable Long id, @PathVariable Boolean isactive){
        return categoryService.activateCategory( id, isactive );
    }

    @GetMapping("/categories/isactive/{isactive}")
    public Iterable<CategoryDto> findCategoriesByIsActive(@PathVariable Boolean isactive) {
        return categoryService.findCategoriesByIsActive(isactive);
    }

    @GetMapping("/categories/section/{section_id}")
    public Iterable<CategoryDto> findCategoriesBySection(@PathVariable Long section_id) {
        return categoryService.findCategoriesBySection(section_id);
    }

    @GetMapping("/categories")
    public Iterable<CategoryDto> getCategoriesAll() {
        return categoryService.getCategoriesAll();
    }

    @GetMapping("/categories/section/{section_id}/isactive/{isactive}")
    public Iterable<CategoryDto> findCategoriesBySectionByIsActive(@PathVariable Long section_id, @PathVariable Boolean isactive) {
        return categoryService.findCategoriesBySectionByIsActive(section_id, isactive);
    }
}
