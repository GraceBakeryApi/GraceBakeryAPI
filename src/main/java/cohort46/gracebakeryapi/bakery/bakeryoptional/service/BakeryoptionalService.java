package cohort46.gracebakeryapi.bakery.bakeryoptional.service;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;


public interface BakeryoptionalService {
    BakeryoptionalDto addBakeryoptional(BakeryoptionalDto bakeryoptionalDto);//Long
    BakeryoptionalDto findBakeryoptionalById(Long Id);
    BakeryoptionalDto updateBakeryoptional(BakeryoptionalDto bakeryoptionalDto, Long id);
    Iterable<BakeryoptionalDto> findBakeryoptionalsByProduct(Long product_id);
    Iterable<BakeryoptionalDto> getBakeryoptionalsAll();
    Bakeryoptional store(Bakeryoptional option);
}
