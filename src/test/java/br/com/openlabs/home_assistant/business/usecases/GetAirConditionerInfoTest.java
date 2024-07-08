package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
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
        airConditioner.setName("Air Conditioner");
        airConditioner.setState(true);
        airConditioner.setTemperature(24);
        airConditioner.setTurnOffTime(LocalTime.of(22,0));
        airConditioner.setTurnOnTime(LocalTime.of(6,0));
        airConditioner.setLatitude(Double.valueOf("40.7128"));
        airConditioner.setLongitude(Double.valueOf("-74.0060"));

        when(airConditionerPersistence.findById(id)).thenReturn(Optional.of(airConditioner));

        // Act
        AirConditionerInfoDTO result = getAirConditionerInfo.execute(id);

        // Assert
        verify(airConditionerPersistence).findById(id);
        assertEquals("Air Conditioner", result.name());
        assertEquals(true, result.state());
        assertEquals(24, result.temperature());
        assertEquals(LocalTime.of(22,0), result.turnOffTime());
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
