package cohort46.gracebakeryapi.bakery.section.service;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.image.service.ImageServiceImpl;
import cohort46.gracebakeryapi.bakery.section.controller.SectionController;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.bakery.section.dto.SectionDto;
import cohort46.gracebakeryapi.exception.FailedDependencyException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.bakery.section.model.Section;
import cohort46.gracebakeryapi.exception.SectionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final ImageService imageService;
    private SectionController sectionController;

    private final SectionRepository sectionRepository;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public SectionDto addSection(SectionDto sectionDto) {
        if(!checkSource(sectionDto)) throw new FailedDependencyException("Creating failed");
        Section section = modelMapper.map(sectionDto, Section.class);
        section.setId(null);
        section = sectionRepository.save(section);
        if(section != null) {
            return modelMapper.map(section, SectionDto.class);
        }
        return null;
    }

    @Override
    public SectionDto findSectionById(Long sectionId) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new SectionNotFoundException(   sectionId   ));
        return modelMapper.map(section, SectionDto.class);
    }

    @Transactional
    @Override
    public SectionDto updateSection(SectionDto sectionDto, Long id) {
        if(!checkSource(sectionDto)) throw new FailedDependencyException("Updating failed");
        sectionDto.setId(id);
        Section section = sectionRepository.findById(sectionDto.getId()).orElseThrow(() -> new SectionNotFoundException(  sectionDto.getId()    ));
        sectionDto.setImage( imageService.updateImageFileLink(sectionDto.getImage(), section.getImage()) );
        modelMapper.map(sectionDto, section);
        return modelMapper.map(sectionRepository.save(section), SectionDto.class);
    }

    @Transactional
    @Override
    public SectionDto activateSection(Long sectionId, Boolean activate) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new SectionNotFoundException(   sectionId   ));
        section.setIsActive(activate);
        return modelMapper.map(sectionRepository.saveAndFlush(section), SectionDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<SectionDto> findSectionsByIsActive(Boolean isactive) {
        return sectionRepository.findSectionsByIsActive(isactive).map(s -> modelMapper.map(s, SectionDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<SectionDto> getSectionsAll() {
        List<SectionDto> sections = new ArrayList<>(sectionRepository.findSectionsByIsActive(Boolean.TRUE).map(s -> modelMapper.map(s, SectionDto.class)).toList());
        sections.addAll(sectionRepository.findSectionsByIsActive(Boolean.FALSE).map(s -> modelMapper.map(s, SectionDto.class)).toList());
        return sections;
    }

    private boolean checkSource(SectionDto sectionDto) {

        if(sectionRepository.findAll().stream().anyMatch( p -> p.getTitle_de().equals(sectionDto.getTitle_de()) ) ) {
            throw new FailedDependencyException("Title De must be uniq ") ;};

        if(sectionRepository.findAll().stream().anyMatch( p -> p.getTitle_ru().equals(sectionDto.getTitle_ru()) ) ) {
            throw new FailedDependencyException("Title Ru must be uniq ") ;};

        return true;
    }

}