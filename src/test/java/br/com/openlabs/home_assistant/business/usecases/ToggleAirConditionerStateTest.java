package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.ToggleAirConditionerState;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
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
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);
        conditionerAir.setState(false); // Initially OFF

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(conditionerAir));
        when(airConditionerPersistence.save(any(ConditionerAir.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        toggleAirConditionerState.execute(id);

        // Assert
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence).save(any(ConditionerAir.class));
        verify(mqttService).publish("home/airConditioner/1", "ON");
        assertTrue(conditionerAir.getState());
        assertFalse(conditionerAir.getManually());
    }

    @Test
    void testExecuteNotFound() {
        // Arrange
        Long id = 1L;

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> toggleAirConditionerState.execute(id));
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence, never()).save(any(ConditionerAir.class));
        verify(mqttService, never()).publish(anyString(), anyString());
    }
}
