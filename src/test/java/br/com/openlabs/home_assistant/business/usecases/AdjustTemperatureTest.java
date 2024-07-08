package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.AdjustTemperature;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerTemperatureDTO;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdjustTemperatureTest {

    @Mock
    private AirConditionerPersistence airConditionerPersistence;

    @InjectMocks
    private AdjustTemperature adjustTemperature;

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testExecuteSuccess() {
        Long id = 1L;
        AirConditionerTemperatureDTO dto = new AirConditionerTemperatureDTO(25);
        AirConditioner airConditioner = new AirConditioner();
        airConditioner.setId(id);
        airConditioner.setTemperature(15);

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(airConditioner));
        when(airConditionerPersistence.save(any(AirConditioner.class))).thenAnswer(invocation -> invocation.getArgument(0));


        adjustTemperature.execute(id, dto);

        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence).save(any(AirConditioner.class));
        assertEquals(25, airConditioner.getTemperature());
    }

    @Test
    void testExecuteConditioningAirNotFound() {
        Long id = 1L;
        AirConditionerTemperatureDTO dto = new AirConditionerTemperatureDTO(25);

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adjustTemperature.execute(id, dto));
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence, never()).save(any(AirConditioner.class));
    }
}
