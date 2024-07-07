package br.com.openlabs.home_assistant.infra.persistence.conditioningAir;
import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirConditionerPersistence extends JpaRepository<ConditioningAir, Long> {

    List<ConditioningAir> findByLatitudeAndAndLongitude(Long latitude, Long longitude);
}
