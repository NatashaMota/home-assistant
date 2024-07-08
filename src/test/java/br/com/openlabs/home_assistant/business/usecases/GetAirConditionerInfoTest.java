package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.GetAirConditionerInfo;

import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class GetAirConditionerInfoTest {

    @Mock
    private AirConditionerPersistence airConditionerPersistence;

    @InjectMocks
    private GetAirConditionerInfo getAirConditionerInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteSuccess() {
        // Arrange
        Long id = 1L;
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);
        conditionerAir.setName("Air Conditioner");
        conditionerAir.setState(true);
        conditionerAir.setTemperature(24);
        conditionerAir.setTurnOffTime(LocalTime.of(22,0));
        conditionerAir.setTurnOnTime(LocalTime.of(6,0));
        conditionerAir.setLatitude(Long.getLong("40.7128"));
        conditionerAir.setLongitude(Long.getLong("-74.0060"));

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(conditionerAir));

        // Act
        AirConditionerInfoDTO result = getAirConditionerInfo.execute(id);

        // Assert
        verify(airConditionerPersistence).findById(id);
        assertEquals("Air Conditioner", result.name());
        assertEquals(true, result.state());
        assertEquals(24, result.temperature());
        assertEquals(LocalTime.of(22,00), result.turnOffTime());
        assertEquals(LocalTime.of(6,0), result.turnOnTime());
        assertEquals(Long.getLong(" 40.7128"), result.latitude());
        assertEquals(Long.getLong("-74.0060"), result.longitude());
    }

    @Test
    void testExecuteNotFound() {
        Long id = 1L;

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.empty());

        AirConditionerInfoDTO result = getAirConditionerInfo.execute(id);

        verify(airConditionerPersistence).findById(id);
        assertNull(result);
    }
}
