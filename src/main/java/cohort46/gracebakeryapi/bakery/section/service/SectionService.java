package cohort46.gracebakeryapi.bakery.section.service;

import cohort46.gracebakeryapi.bakery.section.dto.SectionDto;

public interface SectionService {
    SectionDto addSection(SectionDto sectionDto);//Long
    SectionDto findSectionById(Long sectionId);
    SectionDto updateSection(SectionDto sectionDto, Long id);
    SectionDto activateSection(Long Id, Boolean activate);
    Iterable<SectionDto> findSectionsByIsActive(Boolean isactive);
    Iterable<SectionDto> getSectionsAll();
}

