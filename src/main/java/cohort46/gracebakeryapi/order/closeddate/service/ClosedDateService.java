package cohort46.gracebakeryapi.order.closeddate.service;

import cohort46.gracebakeryapi.order.closeddate.dto.ClosedDateDto;

public interface ClosedDateService {
    ClosedDateDto addClosedDate(ClosedDateDto ClosedDateDto);//Long
    ClosedDateDto findClosedDateById(Long ClosedDateId);
    ClosedDateDto deleteClosedDate(Long id);
    Iterable<ClosedDateDto> findClosedDatesAll();
}
