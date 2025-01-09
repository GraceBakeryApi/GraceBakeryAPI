package cohort46.gracebakeryapi.bakery.section.controller;

import cohort46.gracebakeryapi.bakery.section.dto.SectionDto;
import cohort46.gracebakeryapi.bakery.section.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SectionController {
    private final SectionService sectionService;

    @PostMapping("/section")
    @ResponseStatus(HttpStatus.CREATED)
    public SectionDto addSection(@RequestBody SectionDto sectionDto) {
        return sectionService.addSection(sectionDto)  ;
    }//Long

    @GetMapping("/section/{id}")
    public SectionDto findSectionById(@PathVariable Long id) {
        return sectionService.findSectionById(id);
    }

    @PutMapping("/section/{id}")
    public SectionDto updateSection( @RequestBody SectionDto sectionDto, @PathVariable Long id) {
        return sectionService.updateSection(sectionDto, id);
    }
    @PatchMapping("/section/{id}/isactive/{isactive}")
    SectionDto activateSection(@PathVariable Long id, @PathVariable Boolean isactive){
        return sectionService.activateSection( id, isactive );
    }

    @GetMapping("/sections/isactive/{isactive}")
    public Iterable<SectionDto> findSectionsByIsActive(@PathVariable Boolean isactive) {
        return sectionService.findSectionsByIsActive(isactive);
    }

    @GetMapping("/sections")
    public Iterable<SectionDto> getSectionsAll() {
        return sectionService.getSectionsAll();
    }
}
