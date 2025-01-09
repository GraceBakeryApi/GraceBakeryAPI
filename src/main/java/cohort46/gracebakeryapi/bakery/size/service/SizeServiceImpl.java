package cohort46.gracebakeryapi.bakery.size.service;

import cohort46.gracebakeryapi.bakery.size.controller.SizeController;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.bakery.size.dto.SizeDto;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import cohort46.gracebakeryapi.exception.SectionNotFoundException;
import cohort46.gracebakeryapi.exception.SizeNotFoundException;
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
        Size size = modelMapper.map(sizeDto, Size.class);
        size.setId(null);
        size = sizeRepository.save(size);
        if(size != null) {
            return modelMapper.map(size, SizeDto.class);
        }
        return null;
    }

    @Override
    public SizeDto findSizeById(Long sizeId) {
        Size size = sizeRepository.findById(sizeId).orElseThrow(() -> new SizeNotFoundException(sizeId));
        return modelMapper.map(size, SizeDto.class);
    }

    @Transactional
    @Override
    public SizeDto updateSize(SizeDto sizeDto, Long id) {
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
}
