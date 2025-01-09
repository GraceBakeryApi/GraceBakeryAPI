package cohort46.gracebakeryapi.bakery.optionsize.service;

import cohort46.gracebakeryapi.bakery.optionsize.dto.OptionsizeDto;
import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;


public interface OptionsizeService {
    OptionsizeDto addOptionsize(OptionsizeDto optionsizeDto);//Long
    OptionsizeDto findOptionsizeById(Long optionsizeId);
    OptionsizeDto deleteOptionsize(Long id);
    OptionsizeDto updateOptionsize(OptionsizeDto optionsizeDto);
    Iterable<OptionsizeDto> getOptionsizesAll();
    Optionsize store(Optionsize optionsize);
}
