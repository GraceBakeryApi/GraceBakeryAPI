package cohort46.gracebakeryapi.order.closeddate.controller;

import cohort46.gracebakeryapi.order.closeddate.dto.ClosedDateDto;
import cohort46.gracebakeryapi.order.closeddate.service.ClosedDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClosedDateController {
    private final ClosedDateService closeddateService;

    @PostMapping("/closeddate")
    @ResponseStatus(HttpStatus.CREATED)
    public ClosedDateDto addClosedDate(@RequestBody ClosedDateDto ClosedDateDto) {
        return closeddateService.addClosedDate(ClosedDateDto)  ;
    }

    @GetMapping("/closeddate/{id}")
    public ClosedDateDto findClosedDateById(@PathVariable Long id) {
        return closeddateService.findClosedDateById(id);
    }

    @DeleteMapping("/closeddate/{id}")
    public ClosedDateDto deleteClosedDate(@PathVariable Long id) {
        return closeddateService.deleteClosedDate(id);
    }

    @GetMapping("/closeddates")
    public Iterable<ClosedDateDto> findClosedDatesAll() {
        return closeddateService.findClosedDatesAll();
    }
}
