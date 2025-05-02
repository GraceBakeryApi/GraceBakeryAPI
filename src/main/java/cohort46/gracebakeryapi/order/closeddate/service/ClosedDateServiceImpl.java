package cohort46.gracebakeryapi.order.closeddate.service;

//import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.order.closeddate.controller.ClosedDateController;
import cohort46.gracebakeryapi.order.closeddate.dao.ClosedDateRepository;
import cohort46.gracebakeryapi.order.closeddate.dto.ClosedDateDto;
import cohort46.gracebakeryapi.order.closeddate.model.ClosedDate;
import cohort46.gracebakeryapi.order.order.service.OrderServiceImpl;
import cohort46.gracebakeryapi.other.exception.ClosedDateNotFoundException;
import cohort46.gracebakeryapi.other.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClosedDateServiceImpl implements ClosedDateService {
    private ClosedDateController ClosedDateController;
    private final ClosedDateRepository ClosedDateRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ClosedDateDto addClosedDate(ClosedDateDto ClosedDateDto) {
        ClosedDate ClosedDate = modelMapper.map(ClosedDateDto, ClosedDate.class);
        ClosedDate.setId(null);
        ClosedDate = ClosedDateRepository.save(ClosedDate);
        if(ClosedDate != null) {
            return modelMapper.map(ClosedDate, ClosedDateDto.class);
        }
        return null;
    }

    @Override
    public ClosedDateDto findClosedDateById(Long ClosedDateId) {
        return modelMapper.map(ClosedDateRepository.findById(ClosedDateId).orElseThrow(() -> new ClosedDateNotFoundException(ClosedDateId)), ClosedDateDto.class);
    }

    @Transactional
    @Override
    public ClosedDateDto deleteClosedDate(Long ClosedDateId) {
        ClosedDate ClosedDate = ClosedDateRepository.findById(ClosedDateId).orElseThrow(() -> new ClosedDateNotFoundException(ClosedDateId));
        ClosedDateRepository.delete(ClosedDate);
        ClosedDateRepository.flush();
        return modelMapper.map(ClosedDate, ClosedDateDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ClosedDateDto> findClosedDatesAll(){
        return ClosedDateRepository.findAll().stream().map(a -> modelMapper.map(a, ClosedDateDto.class)).toList();
    }

}