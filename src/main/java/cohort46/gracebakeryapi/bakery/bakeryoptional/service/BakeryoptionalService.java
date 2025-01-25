package cohort46.gracebakeryapi.bakery.bakeryoptional.service;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import org.springframework.web.bind.annotation.PathVariable;


public interface BakeryoptionalService {
    BakeryoptionalDto addBakeryoptional(BakeryoptionalDto bakeryoptionalDto);//Long
    BakeryoptionalDto findBakeryoptionalById(Long Id);
    BakeryoptionalDto updateBakeryoptional(BakeryoptionalDto bakeryoptionalDto, Long id);
    BakeryoptionalDto activateBakeryoptional(Long id, Boolean isactive);
    Iterable<BakeryoptionalDto> findBakeryoptionalsByProduct(Long product_id);
    Iterable<BakeryoptionalDto> getBakeryoptionalsAll();
    Bakeryoptional store(Bakeryoptional option);
}
