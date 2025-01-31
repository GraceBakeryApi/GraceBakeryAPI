package cohort46.gracebakeryapi.bakery.category.service;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.category.controller.CategoryController;
import cohort46.gracebakeryapi.bakery.category.dao.CategoryRepository;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.exception.CategoryNotFoundException;
import cohort46.gracebakeryapi.exception.FailedDependencyException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.section.model.Section;
import cohort46.gracebakeryapi.exception.SectionNotFoundException;
import cohort46.gracebakeryapi.helperclasses.SizePrice;
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


    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        if(!checkSource(categoryDto)) throw new FailedDependencyException("Creating failed");
        Section section = sectionRepository.findById(categoryDto.getSectionid()).orElseThrow(() -> new SectionNotFoundException(categoryDto.getSectionid()));
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setId(null);
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
        if(!checkSource(categoryDto)) throw new FailedDependencyException("Updating failed");
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

    private boolean checkSource(CategoryDto categoryDto) {

        if(categoryRepository.findAll().stream().anyMatch( p -> p.getTitle_de().equals(categoryDto.getTitle_de()) ) ) {
            throw new FailedDependencyException("Title De must be uniq ") ;};

        if(categoryRepository.findAll().stream().anyMatch( p -> p.getTitle_ru().equals(categoryDto.getTitle_ru()) ) ) {
            throw new FailedDependencyException("Title Ru must be uniq ") ;};

        if(sectionRepository.findById(categoryDto.getSectionid()).isEmpty()) {return false; }

        return true;
    }


}