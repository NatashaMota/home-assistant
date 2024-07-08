package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.ChangeStateByTime;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerStateTimeDTO;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChangeStateByTimeTest {

    @Mock
    private AirConditionerPersistence airConditionerPersistence;

    @InjectMocks
    private ChangeStateByTime changeStateByTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteSuccess() {
        // Arrange
        Long id = 1L;
        AirConditionerStateTimeDTO dto = new AirConditionerStateTimeDTO(LocalTime.of(22,0), LocalTime.of(6,0));
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(conditionerAir));
        when(airConditionerPersistence.save(any(ConditionerAir.class))).thenAnswer(invocation -> invocation.getArgument(0));

        changeStateByTime.execute(dto, id);

        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence).save(any(ConditionerAir.class));
        assertEquals(LocalTime.of(22,0), conditionerAir.getTurnOffTime());
        assertEquals(LocalTime.of(6,0), conditionerAir.getTurnOnTime());
    }

    @Test
    void testExecuteConditioningAirNotFound() {
        // Arrange
        Long id = 1L;
        AirConditionerStateTimeDTO dto = new AirConditionerStateTimeDTO(LocalTime.of(22,0), LocalTime.of(6,0));

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> changeStateByTime.execute(dto, id));
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence, never()).save(any(ConditionerAir.class));
    }
}
