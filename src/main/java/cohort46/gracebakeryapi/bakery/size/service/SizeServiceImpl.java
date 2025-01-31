package cohort46.gracebakeryapi.bakery.size.service;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.size.controller.SizeController;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.bakery.size.dto.SizeDto;
import cohort46.gracebakeryapi.exception.*;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private SizeController sizeController;

    private final SizeRepository sizeRepository;
    private final SectionRepository sectionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public SizeDto addSize(SizeDto sizeDto) {
        if(!checkSource(sizeDto)) throw new FailedDependencyException("Creating failed");
        Size size = modelMapper.map(sizeDto, Size.class);
        size.setId(null);
        size = sizeRepository.save(size);
        if(size != null) {
            return modelMapper.map(size, SizeDto.class);
        }
        return null;
    }

    @Transactional
    @Override
    public SizeDto activateSize(Long Id, Boolean activate) {
        Size size = sizeRepository.findById(Id).orElseThrow(() -> new SizeNotFoundException(Id));
        size.setIsActive(activate);
        return modelMapper.map(sizeRepository.saveAndFlush(size), SizeDto.class);
    }

    @Override
    public SizeDto findSizeById(Long sizeId) {
        Size size = sizeRepository.findById(sizeId).orElseThrow(() -> new SizeNotFoundException(sizeId));
        return modelMapper.map(size, SizeDto.class);
    }

    @Transactional
    @Override
    public SizeDto updateSize(SizeDto sizeDto, Long id) {
        if(!checkSource(sizeDto)) throw new FailedDependencyException("Updating failed");
        sizeDto.setId(id);
        Size size = sizeRepository.findById(sizeDto.getId()).orElseThrow(() -> new SizeNotFoundException(sizeDto.getId()));
        modelMapper.map(sizeDto, size);
        return modelMapper.map(sizeRepository.save(size), SizeDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<SizeDto> getSizesAll() {
        //сортировка по полю "persons"  по возрастанию
        return sizeRepository.findAll(Sort.by("persons")).stream().map(s -> modelMapper.map(s, SizeDto.class)).toList() ;
    }

    private boolean checkSource(SizeDto sizeDto) {

        if(sizeRepository.findAll().stream().anyMatch( p -> p.getTitle_de().equals(sizeDto.getTitle_de()) ) ) {
            throw new FailedDependencyException("Title De must be uniq ") ;};

        if(sizeRepository.findAll().stream().anyMatch( p -> p.getTitle_ru().equals(sizeDto.getTitle_ru()) ) ) {
            throw new FailedDependencyException("Title Ru must be uniq ") ;};

        return true;
    }

}
