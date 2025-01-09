package cohort46.gracebakeryapi.bakery.category.service;

import cohort46.gracebakeryapi.bakery.category.controller.CategoryController;
import cohort46.gracebakeryapi.bakery.category.dao.CategoryRepository;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.exception.CategoryNotFoundException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.section.model.Section;
import cohort46.gracebakeryapi.exception.SectionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryController categoryController;

    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;
    private final ModelMapper modelMapper;

    private final ImageService imageService;


    //@Transactional
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        sectionRepository.findById(categoryDto.getSectionid());
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setId(null);
        Section section = sectionRepository.findById(categoryDto.getSectionid()).orElseThrow(() -> new CategoryNotFoundException(categoryDto.getSectionid()));
        category.setSection(section);
        category = categoryRepository.save(category);
        if(category != null) {
            return modelMapper.map(category, CategoryDto.class);
        }
        return null;
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        categoryDto.setId(id);
        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new CategoryNotFoundException((categoryDto.getId())));
        categoryDto.setImage( imageService.updateImageFileLink(categoryDto.getImage(), category.getImage()) );
        modelMapper.map(categoryDto, category);
        category.setSection(sectionRepository.findById(categoryDto.getSectionid()).orElseThrow(() -> new SectionNotFoundException(categoryDto.getSectionid())));
        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Transactional
    @Override
    public CategoryDto activateCategory(Long categoryId, Boolean activate) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
        category.setIsActive(activate);
        return modelMapper.map(categoryRepository.saveAndFlush(category), CategoryDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<CategoryDto> findCategoriesByIsActive(Boolean isactive) {
        return categoryRepository.findCategoriesByIsActive(isactive).map(s -> modelMapper.map(s, CategoryDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<CategoryDto> findCategoriesBySection(Long section_id) {
        sectionRepository.findById(section_id).orElseThrow(() -> new CategoryNotFoundException(section_id));
        //сортировка по IsActive по убыванию(сначала активные, потом неактивные)
        return categoryRepository.findCategoriesBySectionId(section_id, Sort.by("isActive").descending()).map(s -> modelMapper.map(s, CategoryDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<CategoryDto> getCategoriesAll() {
        //сортировка сначала по IsActive по убыванию(сначала активные, потом неактивные), потом сортировка по полю "section.id"
        return categoryRepository.findAll(Sort.by("isActive").descending().and(Sort.by("section.id"))).stream().map(c -> modelMapper.map(c, CategoryDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<CategoryDto> findCategoriesBySectionByIsActive(Long section_id, Boolean isactive) {
        sectionRepository.findById(section_id).orElseThrow(() -> new SectionNotFoundException(section_id));
        return categoryRepository.findCategoriesBySectionIdAndIsActive(section_id, isactive).map(s -> modelMapper.map(s, CategoryDto.class)).toList() ;
    }

}