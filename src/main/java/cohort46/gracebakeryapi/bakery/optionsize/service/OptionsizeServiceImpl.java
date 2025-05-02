package cohort46.gracebakeryapi.bakery.optionsize.service;

import cohort46.gracebakeryapi.other.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.bakery.optionsize.controller.OptionsizeController;
import cohort46.gracebakeryapi.bakery.optionsize.dao.OptionsizeRepository;
import cohort46.gracebakeryapi.bakery.optionsize.dto.OptionsizeDto;
import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import cohort46.gracebakeryapi.other.exception.SizeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptionsizeServiceImpl implements OptionsizeService {
    private OptionsizeController optionsizeController;

    private final OptionsizeRepository optionsizeRepository;
    private final SizeRepository sizeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public OptionsizeDto addOptionsize(OptionsizeDto optionsizeDto) {
        Optionsize optionsize = modelMapper.map(optionsizeDto, Optionsize.class);
        Size size = sizeRepository.findById(optionsizeDto.getSizeid()).orElseThrow(() -> new SizeNotFoundException(optionsizeDto.getSizeid()));
        optionsize.setSize(size);
        optionsize.setId(null);
        optionsize = optionsizeRepository.saveAndFlush(optionsize);
        if(optionsize != null) {
            return modelMapper.map(optionsize, OptionsizeDto.class);
        }
        return null;
    }

    @Override
    public OptionsizeDto findOptionsizeById(Long optionsizeId) {
        Optionsize optionsize = optionsizeRepository.findById(optionsizeId).orElseThrow(() -> new ResourceNotFoundException("bad Optionsize"));
        return modelMapper.map(optionsize, OptionsizeDto.class);
    }

    @Transactional
    @Override
    public OptionsizeDto deleteOptionsize(Long optionsizeId) {
        Optionsize optionsize = optionsizeRepository.findById(optionsizeId).orElseThrow(() -> new ResourceNotFoundException("bad Optionsize"));
        optionsizeRepository.delete(optionsize);
        optionsizeRepository.flush();
        return modelMapper.map(optionsize, OptionsizeDto.class);
    }

    @Transactional
    @Override
    public OptionsizeDto updateOptionsize(OptionsizeDto optionsizeDto) {
        Optionsize optionsize = optionsizeRepository.findById(optionsizeDto.getId()).orElseThrow(() -> new ResourceNotFoundException("bad Optionsize"));
        modelMapper.map(optionsizeDto, optionsize);
        return modelMapper.map(optionsizeRepository.save(optionsize), OptionsizeDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<OptionsizeDto> getOptionsizesAll() {
        //сортировка по цене по возрастанию
        return optionsizeRepository.findAll(Sort.by("price")).stream().map(op -> modelMapper.map(op, OptionsizeDto.class)).toList() ;
    }
    @Transactional
    @Override
    public Optionsize store(Optionsize optionsize) {
        return optionsizeRepository.saveAndFlush(optionsize);
    }
}
