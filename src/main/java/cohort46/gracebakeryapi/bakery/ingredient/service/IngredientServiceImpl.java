package cohort46.gracebakeryapi.bakery.ingredient.service;

import cohort46.gracebakeryapi.other.exception.FailedDependencyException;
import cohort46.gracebakeryapi.other.exception.IngredientNotFoundException;
import cohort46.gracebakeryapi.other.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.ingredient.controller.IngredientController;
import cohort46.gracebakeryapi.bakery.ingredient.dao.IngredientRepository;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private IngredientController ingredientController;

    private final IngredientRepository ingredientRepository;
    private final SectionRepository sectionRepository;
    private final ModelMapper modelMapper;

    private final ImageService imageService;

    //@Transactional
    @Override
    public IngredientDto addIngredient(IngredientDto ingredientDto) {
        if(!checkSource(ingredientDto)) throw new FailedDependencyException("Creating failed");
        Ingredient ingredient = modelMapper.map(ingredientDto, Ingredient.class);
        ingredient.setId(null);
        ingredient = ingredientRepository.save(ingredient);
        if(ingredient != null) {
            return modelMapper.map(ingredient, IngredientDto.class);
        }
        return null;
    }

    @Override
    public IngredientDto findIngredientById(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException(ingredientId));
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    @Transactional
    @Override
    public IngredientDto updateIngredient(IngredientDto ingredientDto, Long id) {
        if(!checkSource(ingredientDto)) throw new FailedDependencyException("Updating failed");
        ingredientDto.setId(id);
        Ingredient ingredient = ingredientRepository.findById(ingredientDto.getId()).orElseThrow(() -> new IngredientNotFoundException(ingredientDto.getId()));
        ingredientDto.setImage_de( imageService.updateImageFileLink(ingredientDto.getImage_de(), ingredient.getImage_de()) );
        ingredientDto.setImage_ru( imageService.updateImageFileLink(ingredientDto.getImage_ru(), ingredient.getImage_ru()) );
        modelMapper.map(ingredientDto, ingredient);
        return modelMapper.map(ingredientRepository.save(ingredient), IngredientDto.class);
    }

    @Transactional
    @Override
    public IngredientDto activateIngredient(Long Id, Boolean activate) {
        Ingredient ingredient = ingredientRepository.findById(Id).orElseThrow(() -> new IngredientNotFoundException(Id));
        ingredient.setIsActive(activate);
        return modelMapper.map(ingredientRepository.saveAndFlush(ingredient), IngredientDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<IngredientDto> getIngredientAll() {
        return ingredientRepository.findAll().stream().map(i -> modelMapper.map(i, IngredientDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<IngredientDto> findIngredientsByProduct(Long product_id) {
        return ingredientRepository.findByProductsId(product_id).map(i -> modelMapper.map(i, IngredientDto.class)).toList() ;
    }

    @Transactional
    @Override
    public Ingredient store(Ingredient ingredient) {
        return ingredientRepository.saveAndFlush(ingredient);
    }

    private boolean checkSource(IngredientDto ingredientDto) {

        if(ingredientRepository.findAll().stream().anyMatch( p -> p.getTitle_de().equals(ingredientDto.getTitle_de()) ) ) {
            throw new FailedDependencyException("Title De must be uniq ") ;};

        if(ingredientRepository.findAll().stream().anyMatch( p -> p.getTitle_ru().equals(ingredientDto.getTitle_ru()) ) ) {
            throw new FailedDependencyException("Title Ru must be uniq ") ;};

        return true;
    }

}
