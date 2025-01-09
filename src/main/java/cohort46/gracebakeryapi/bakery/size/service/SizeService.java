package cohort46.gracebakeryapi.bakery.size.service;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.size.dto.SizeDto;


public interface SizeService {
    SizeDto addSize(SizeDto sizeDto);//Long
    SizeDto findSizeById(Long sizeId);
    SizeDto updateSize(SizeDto sizeDto, Long id);
    Iterable<SizeDto> getSizesAll();
}
