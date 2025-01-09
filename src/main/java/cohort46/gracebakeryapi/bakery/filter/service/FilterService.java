package cohort46.gracebakeryapi.bakery.filter.service;

import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.filter.model.Filter;


public interface FilterService {
    FilterDto addFilter(FilterDto filterDto);//Long
    FilterDto findFilterById(Long filterId);
    FilterDto updateFilter(FilterDto filterDto, Long id);
    Iterable<FilterDto> getFilterAll();
    Iterable<FilterDto> findFiltersByProduct(Long product_id);
    Filter store(Filter filter);
}
