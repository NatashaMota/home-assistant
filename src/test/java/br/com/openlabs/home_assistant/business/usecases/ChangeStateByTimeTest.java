package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
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
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Test
    void testExecuteSuccess() {
        // Arrange
        Long id = 1L;
        AirConditionerStateTimeDTO dto = new AirConditionerStateTimeDTO(LocalTime.of(22,0), LocalTime.of(6,0));
        AirConditioner airConditioner = new AirConditioner();
        airConditioner.setId(id);

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(airConditioner));
        when(airConditionerPersistence.save(any(AirConditioner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        changeStateByTime.execute(dto, id);

        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence).save(any(AirConditioner.class));
        assertEquals(LocalTime.of(22,0), airConditioner.getTurnOffTime());
        assertEquals(LocalTime.of(6,0), airConditioner.getTurnOnTime());
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
        verify(airConditionerPersistence, never()).save(any(AirConditioner.class));
    }
}
