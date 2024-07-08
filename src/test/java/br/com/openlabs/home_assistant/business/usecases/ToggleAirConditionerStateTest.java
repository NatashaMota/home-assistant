package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.ToggleAirConditionerState;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
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
        AirConditioner airConditioner = new AirConditioner();
        airConditioner.setId(id);
        airConditioner.setState(false); // Initially OFF

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(airConditioner));
        when(airConditionerPersistence.save(any(AirConditioner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        toggleAirConditionerState.execute(id);

        // Assert
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence).save(any(AirConditioner.class));
        assertTrue(airConditioner.getState());
        assertFalse(airConditioner.getManually());
    }

    @Test
    void testExecuteNotFound() {
        // Arrange
        Long id = 1L;

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> toggleAirConditionerState.execute(id));
        verify(airConditionerPersistence).findById(id);
        verify(airConditionerPersistence, never()).save(any(AirConditioner.class));
        verify(mqttService, never()).publish(anyString(), anyString());
    }
}
