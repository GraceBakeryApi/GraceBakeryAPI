package cohort46.gracebakeryapi.bakery.filter.service;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.filter.controller.FilterController;
import cohort46.gracebakeryapi.bakery.filter.dao.FilterRepository;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.bakery.product.service.ProductService;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.exception.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {
    private FilterController filterController;

    private final FilterRepository filterRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final ImageService imageService;
    //private final ProductService productService;

    @Transactional
    @Override
    public FilterDto addFilter(FilterDto filterDto) {
        if(!checkSource(filterDto)) throw new FailedDependencyException("Creating failed");
        Filter filter = modelMapper.map(filterDto, Filter.class);
        filter.setId(null);
        filter = filterRepository.save(filter);
        if(filter != null) {
            return modelMapper.map(filter, FilterDto.class);
        }
        return null;
    }

    @Transactional
    @Override
    public FilterDto findFilterById(Long filterId) {
        Filter filter = filterRepository.findById(filterId).orElseThrow(() -> new FilterNotFoundException(filterId));
        return modelMapper.map(filter, FilterDto.class);
    }

    @Transactional
    @Override
    public FilterDto updateFilter(FilterDto filterDto, Long id) {
        if(!checkSource(filterDto)) throw new FailedDependencyException("Updating failed");
        filterDto.setId(id);
        Filter filter = filterRepository.findById(filterDto.getId()).orElseThrow(() -> new FilterNotFoundException(filterDto.getId()));
        filterDto.setImage( imageService.updateImageFileLink(filterDto.getImage(), filter.getImage()) );
        modelMapper.map(filterDto, filter);
        return modelMapper.map(filterRepository.save(filter), FilterDto.class);
    }

    @Transactional
    @Override
    public FilterDto deleteFilter(Long id) {
        Filter filter = filterRepository.findById(id).orElseThrow(() -> new FilterNotFoundException(id));
        Iterable<Product> prstemp = productRepository.findByFiltersContaining(filter);
        prstemp.//productService.findProductsByFilters(List.of(id)).
        forEach(productDto -> {
            Product pr = productRepository.findById(productDto.getId()).orElseThrow(() -> new ProductNotFoundException(productDto.getId()));
            pr.getFilters().remove(filter);
            productRepository.saveAndFlush(pr);
        });
        filterRepository.delete(filter);
        filterRepository.flush();
        return modelMapper.map(filter, FilterDto.class);
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

    private boolean checkSource(FilterDto filterDto) {

        if(filterRepository.findAll().stream().anyMatch( p -> p.getTitle_de().equals(filterDto.getTitle_de()) ) ) {
            throw new FailedDependencyException("Title De must be uniq ") ;};

        if(filterRepository.findAll().stream().anyMatch( p -> p.getTitle_ru().equals(filterDto.getTitle_ru()) ) ) {
            throw new FailedDependencyException("Title Ru must be uniq ") ;};

        return true;
    }

}
