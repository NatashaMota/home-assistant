package br.com.openlabs.home_assistant.infra.persistence.conditioningAir;
import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirConditionerPersistence extends JpaRepository<AirConditioner, Long> {

    List<AirConditioner> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
