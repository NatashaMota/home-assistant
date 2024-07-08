package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.AdjustTemperature;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerTemperatureDTO;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteSuccess() {
        Long id = 1L;
        AirConditionerTemperatureDTO dto = new AirConditionerTemperatureDTO(25);
        ConditioningAir conditioningAir = new ConditioningAir();
        conditioningAir.setId(id);
        conditioningAir.setTemperature(15);

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(conditioningAir));
        when(airConditionerPersistence.save(any(ConditioningAir.class))).thenAnswer(invocation -> invocation.getArgument(0));


        adjustTemperature.execute(id, dto);

        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence).save(any(ConditioningAir.class));
        assertEquals(25, conditioningAir.getTemperature());
    }

    @Test
    void testExecuteConditioningAirNotFound() {
        Long id = 1L;
        AirConditionerTemperatureDTO dto = new AirConditionerTemperatureDTO(25);

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adjustTemperature.execute(id, dto));
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence, never()).save(any(ConditioningAir.class));
    }
}
