package cohort46.gracebakeryapi.bakery.productsize.service;

import cohort46.gracebakeryapi.bakery.productsize.controller.ProductsizeController;
import cohort46.gracebakeryapi.bakery.productsize.dao.ProductsizeRepository;
import cohort46.gracebakeryapi.bakery.productsize.dto.ProductsizeDto;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.exception.SizeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductsizeServiceImpl implements ProductsizeService {
    private ProductsizeController productsizeController;

    private final ProductsizeRepository productsizeRepository;
    private final SizeRepository sizeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ProductsizeDto addProductsize(ProductsizeDto productsizeDto) {
        Productsize productsize = modelMapper.map(productsizeDto, Productsize.class);
        Size size = sizeRepository.findById(productsizeDto.getSizeid()).orElseThrow(() -> new SizeNotFoundException(productsizeDto.getSizeid()));
        productsize.setSize(size);
        productsize.setId(null);
        productsize = productsizeRepository.saveAndFlush(productsize);
        if(productsize != null) {
            return modelMapper.map(productsize, ProductsizeDto.class);
        }
        return null;
    }

    @Override
    public ProductsizeDto findProductsizeById(Long productsizeId) {
        Productsize productsize = productsizeRepository.findById(productsizeId).orElseThrow(() -> new ResourceNotFoundException("bad Productsize"));
        return modelMapper.map(productsize, ProductsizeDto.class);
    }

    @Transactional
    @Override
    public ProductsizeDto deleteProductsize(Long productsizeId) {
        Productsize productsize = productsizeRepository.findById(productsizeId).orElseThrow(() -> new ResourceNotFoundException("bad Productsize"));
        productsizeRepository.delete(productsize);
        productsizeRepository.flush();
        return modelMapper.map(productsize, ProductsizeDto.class);
    }

    @Transactional
    @Override
    public ProductsizeDto updateProductsize(ProductsizeDto productsizeDto, Long id) {
        productsizeDto.setId(id);
        Productsize productsize = productsizeRepository.findById(productsizeDto.getId()).orElseThrow(() -> new ResourceNotFoundException("bad Productsize"));
        modelMapper.map(productsizeDto, productsize);
        return modelMapper.map(productsizeRepository.save(productsize), ProductsizeDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductsizeDto> getProductsizesAll() {
        //сортировка по цене по возрастанию
        return productsizeRepository.findAll(Sort.by("price")).stream().map(op -> modelMapper.map(op, ProductsizeDto.class)).toList() ;
    }
    @Transactional
    @Override
    public Productsize store(Productsize productsize) {
        return productsizeRepository.saveAndFlush(productsize);
    }
}
