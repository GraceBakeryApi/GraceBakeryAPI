package cohort46.gracebakeryapi.bakery.optionsize.controller;

import cohort46.gracebakeryapi.bakery.optionsize.dto.OptionsizeDto;
import cohort46.gracebakeryapi.bakery.optionsize.service.OptionsizeService;
import cohort46.gracebakeryapi.bakery.size.dto.SizeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class OptionsizeController {
    private final OptionsizeService optionsizeService;

    @PostMapping("/api/optionsize")
    @ResponseStatus(HttpStatus.CREATED)
    public OptionsizeDto addOptionsize(@RequestBody OptionsizeDto optionsizeDto) {
        return optionsizeService.addOptionsize(optionsizeDto)  ;
    }

    @GetMapping("/api/optionsize/{id}")
    public OptionsizeDto findOptionsizeById(@PathVariable Long id) {
        return optionsizeService.findOptionsizeById(id);
    }

    @DeleteMapping("/api/optionsize/{id}")
    public OptionsizeDto deleteOptionsize(@PathVariable Long id) {
        return optionsizeService.deleteOptionsize(id);
    }

    @PutMapping("/api/optionsize")
    public OptionsizeDto updateOptionsize(@RequestBody OptionsizeDto optionsizeDto) {
        return optionsizeService.updateOptionsize(optionsizeDto);
    }

    @GetMapping("/api/optionsizes")
    public Iterable<OptionsizeDto> getSizesAll() { return optionsizeService.getOptionsizesAll(); }
}
