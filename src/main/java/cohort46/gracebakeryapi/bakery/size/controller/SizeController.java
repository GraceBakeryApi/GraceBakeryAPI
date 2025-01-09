package cohort46.gracebakeryapi.bakery.size.controller;

import cohort46.gracebakeryapi.bakery.size.dto.SizeDto;
import cohort46.gracebakeryapi.bakery.size.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class SizeController {
    private final SizeService sizeService;

    @PostMapping("/api/size")
    @ResponseStatus(HttpStatus.CREATED)
    public SizeDto addSize(@RequestBody SizeDto sizeDto) {
        return sizeService.addSize(sizeDto)  ;
    }

    @GetMapping("/api/size/{id}")
    public SizeDto findSizeById(@PathVariable Long id) {
        return sizeService.findSizeById(id);
    }

    @PutMapping("/api/size/{id}")
    public SizeDto updateSize( @RequestBody SizeDto sizeDto, @PathVariable Long id) {
        return sizeService.updateSize(sizeDto, id);
    }

    @GetMapping("/api/sizes")
    public Iterable<SizeDto> getSizesAll() { return sizeService.getSizesAll(); }
}
