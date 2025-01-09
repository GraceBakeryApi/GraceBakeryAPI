package cohort46.gracebakeryapi.bakery.bakeryoptional.service;

import cohort46.gracebakeryapi.bakery.bakeryoptional.controller.BakeryoptionalController;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dao.BakeryoptionalRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;
import cohort46.gracebakeryapi.bakery.optionsize.service.OptionsizeService;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.exception.BakeryoptionalNotFoundException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.bakery.optionsize.dao.OptionsizeRepository;
import cohort46.gracebakeryapi.exception.SizeNotFoundException;
import cohort46.gracebakeryapi.helperclasses.SizePrice;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BakeryoptionalServiceImpl implements BakeryoptionalService {
    private BakeryoptionalController bakeryoptionalController;

    private final OptionsizeService optionsizeService;

    private final BakeryoptionalRepository bakeryoptionalRepository;
    private final OptionsizeRepository optionsizeRepository;
    private final SizeRepository sizeRepository;
    private final ModelMapper modelMapper;

    private final ImageService imageService;


    @Transactional
    @Override
    public BakeryoptionalDto addBakeryoptional(BakeryoptionalDto bakeryoptionalDto) {
        Bakeryoptional bakeryoptional = modelMapper.map(bakeryoptionalDto, Bakeryoptional.class);
        if (bakeryoptionalDto.getSizeprices() != null && !bakeryoptionalDto.getSizeprices().isEmpty()) {
            for (SizePrice sizePrice : bakeryoptionalDto.getSizeprices()) {
                sizeRepository.findById(sizePrice.getSizeid()).orElseThrow(() -> new SizeNotFoundException( sizePrice.getSizeid() ));
            }
        }
        bakeryoptional.setId(null);
        bakeryoptional = bakeryoptionalRepository.save(bakeryoptional);
        if(bakeryoptional != null) {
            return updateBakeryoptional(bakeryoptionalDto , bakeryoptional.getId());
        }
        return null;
    }

    @Override
    public BakeryoptionalDto findBakeryoptionalById(Long Id) {
        Bakeryoptional bakeryoptional = bakeryoptionalRepository.findById(Id).orElseThrow(() -> new BakeryoptionalNotFoundException(Id));
        return modelMapper.map(bakeryoptional, BakeryoptionalDto.class);
    }

    @Transactional
    @Override
    public BakeryoptionalDto updateBakeryoptional(BakeryoptionalDto bakeryoptionalDto, Long id) {
        bakeryoptionalDto.setId(id);
        Bakeryoptional bakeryoptional = bakeryoptionalRepository.findById(bakeryoptionalDto.getId()).orElseThrow(() -> new BakeryoptionalNotFoundException(  bakeryoptionalDto.getId()   ));
        bakeryoptionalDto.setImage(imageService.updateImageFileLink(bakeryoptionalDto.getImage(), bakeryoptional.getImage()));
        bakeryoptional = modelMapper.map(bakeryoptionalDto, Bakeryoptional.class);
        if (bakeryoptionalDto.getSizeprices() != null && !bakeryoptionalDto.getSizeprices().isEmpty()) {
            optionsizeRepository.deleteAll(bakeryoptional.getOptionsizes());//проверить удаление старых значений!!!!!!!!
            for (SizePrice sizePrice : bakeryoptionalDto.getSizeprices()) {
                Optionsize optionsize = new Optionsize();
                optionsize.setSize(sizeRepository.findById(sizePrice.getSizeid()).orElseThrow(() -> new SizeNotFoundException( sizePrice.getSizeid() )));
                optionsize.setPrice(sizePrice.getPrice());
                optionsize.setBakeryoptional(bakeryoptional);
                optionsize.setId(null);
                optionsize = optionsizeService.store(optionsize);
                bakeryoptional.getOptionsizes().add(optionsize);
            }
        }
        return modelMapper.map(bakeryoptionalRepository.save(bakeryoptional), BakeryoptionalDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<BakeryoptionalDto> findBakeryoptionalsByProduct(Long product_id) {
        //выборка по product_id, сортировка по optionsizes.size.persons
        return bakeryoptionalRepository.findByProductsId(product_id, Sort.by("optionsizes.size.persons")).map(b -> modelMapper.map(b, BakeryoptionalDto.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<BakeryoptionalDto> getBakeryoptionalsAll() {
        //сортировка сначала по product.id, потом сортировка по полю "optionsizes.size.persons"
        Sort sort = Sort.by("products.id").and(Sort.by("optionsizes.size.persons"));
        return bakeryoptionalRepository.findAll(sort).stream().map(b -> modelMapper.map(b, BakeryoptionalDto.class)).toList();
    }

    @Transactional
    @Override
    public Bakeryoptional store(Bakeryoptional option) {
        return bakeryoptionalRepository.saveAndFlush(option);
    }
}
