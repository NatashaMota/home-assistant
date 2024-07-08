package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.ToggleAirConditionerState;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import br.com.openlabs.home_assistant.infra.web.MQTTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ToggleAirConditionerStateTest {

    @Mock
    private AirConditionerPersistence airConditionerPersistence;

    @Mock
    private MQTTService mqttService;

    @InjectMocks
    private ToggleAirConditionerState toggleAirConditionerState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteSuccess() {
        // Arrange
        Long id = 1L;
        ConditioningAir conditioningAir = new ConditioningAir();
        conditioningAir.setId(id);
        conditioningAir.setState(false); // Initially OFF

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(conditioningAir));
        when(airConditionerPersistence.save(any(ConditioningAir.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        toggleAirConditionerState.execute(id);

        // Assert
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence).save(any(ConditioningAir.class));
        verify(mqttService).publish("home/airConditioner/1", "ON");
        assertTrue(conditioningAir.getState());
        assertFalse(conditioningAir.getManually());
    }

    @Test
    void testExecuteNotFound() {
        // Arrange
        Long id = 1L;

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> toggleAirConditionerState.execute(id));
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence, never()).save(any(ConditioningAir.class));
        verify(mqttService, never()).publish(anyString(), anyString());
    }
}
