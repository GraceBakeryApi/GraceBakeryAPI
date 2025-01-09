package cohort46.gracebakeryapi.bakery.section.dao;

import cohort46.gracebakeryapi.bakery.section.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface SectionRepository  extends JpaRepository<Section, Long> {
    Stream<Section> findSectionsByIsActive(Boolean isactive);
}


