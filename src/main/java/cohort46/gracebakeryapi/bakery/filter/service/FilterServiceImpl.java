package cohort46.gracebakeryapi.bakery.filter.service;

import cohort46.gracebakeryapi.bakery.filter.controller.FilterController;
import cohort46.gracebakeryapi.bakery.filter.dao.FilterRepository;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.exception.FilterNotFoundException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.exception.SizeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {
    private FilterController filterController;

    private final FilterRepository filterRepository;
    private final SectionRepository sectionRepository;
    private final ModelMapper modelMapper;

    private final ImageService imageService;

    @Transactional
    @Override
    public FilterDto addFilter(FilterDto filterDto) {
        Filter filter = modelMapper.map(filterDto, Filter.class);
        filter.setId(null);
        filter = filterRepository.save(filter);
        if(filter != null) {
            return modelMapper.map(filter, FilterDto.class);
        }
        return null;
    }

    @Override
    public FilterDto findFilterById(Long filterId) {
        Filter filter = filterRepository.findById(filterId).orElseThrow(() -> new FilterNotFoundException(filterId));
        return modelMapper.map(filter, FilterDto.class);
    }

    @Transactional
    @Override
    public FilterDto updateFilter(FilterDto filterDto, Long id) {
        filterDto.setId(id);
        Filter filter = filterRepository.findById(filterDto.getId()).orElseThrow(() -> new FilterNotFoundException(filterDto.getId()));
        filterDto.setImage( imageService.updateImageFileLink(filterDto.getImage(), filter.getImage()) );
        modelMapper.map(filterDto, filter);
        return modelMapper.map(filterRepository.save(filter), FilterDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<FilterDto> getFilterAll() {
        return filterRepository.findAll().stream().map(i -> modelMapper.map(i, FilterDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<FilterDto> findFiltersByProduct(Long product_id) {
        return filterRepository.findByProductsId(product_id).map(i -> modelMapper.map(i, FilterDto.class)).toList() ;
    }

    @Transactional
    @Override
    public Filter store(Filter filter) {
        return filterRepository.saveAndFlush(filter);
    }
}
