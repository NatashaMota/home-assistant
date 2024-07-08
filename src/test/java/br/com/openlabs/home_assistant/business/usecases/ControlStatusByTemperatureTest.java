package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.ControlStatusByTemperature;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.infra.web.GeoLocationService;
import br.com.openlabs.home_assistant.infra.web.MQTTService;
import br.com.openlabs.home_assistant.infra.web.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ControlStatusByTemperatureTest {

    @Mock
    private AirConditionerPersistence airConditionerPersistence;

    @Mock
    private WeatherService weatherService;

    @Mock
    private MQTTService mqttService;

    @Mock
    private GeoLocationService geoLocationService;

    @InjectMocks
    private ControlStatusByTemperature controlStatusByTemperature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testControlAirConditioner_ManualOff() {
        // Arrange
        Long id = 1L;
        ConditioningAir conditioningAir = new ConditioningAir();
        conditioningAir.setId(id);
        conditioningAir.setState(false); // Manually turned off
        conditioningAir.setManually(true);

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditioningAir));

        // Act
        controlStatusByTemperature.controlAirConditioner();

        // Assert
        verify(mqttService, never()).publish(anyString(), anyString());
        verify(airConditionerPersistence, never()).save(any(ConditioningAir.class));
    }

    @Test
    void testControlAirConditioner_ProgrammedOffTime() {
        Long id = 1L;
        ConditioningAir conditioningAir = new ConditioningAir();
        conditioningAir.setId(id);
        conditioningAir.setState(true); // Initially ON
        conditioningAir.setTurnOffTime(LocalTime.of(22, 0));
        conditioningAir.setTurnOnTime(LocalTime.of(6, 0));

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditioningAir));

        // Simula que o horário atual é 23:00
        try (var mockedLocalTime = mockStatic(LocalTime.class)) {
            mockedLocalTime.when(LocalTime::now).thenReturn(LocalTime.of(23, 0));

            // Act
            controlStatusByTemperature.controlAirConditioner();

            // Assert
            verify(mqttService).publish("home/airConditioner/1", "OFF");
            verify(airConditionerPersistence).save(any(ConditioningAir.class));
            assertFalse(conditioningAir.getState());
        }
    }

    @Test
    void testControlAirConditioner_AutomaticControl_TurnOn() {
        // Arrange
        Long id = 1L;
        ConditioningAir conditioningAir = new ConditioningAir();
        conditioningAir.setId(id);
        conditioningAir.setState(false); // Initially OFF

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditioningAir));

        // Act
        controlStatusByTemperature.controlAirConditioner();

        // Assert
        verify(mqttService).publish("home/airConditioner/1", "ON");
        verify(airConditionerPersistence).save(any(ConditioningAir.class));
        assertTrue(conditioningAir.getState());
    }

    @Test
    void testControlAirConditioner_AutomaticControl_TurnOff() {
        // Arrange
        Long id = 1L;
        ConditioningAir conditioningAir = new ConditioningAir();
        conditioningAir.setId(id);
        conditioningAir.setState(true); // Initially ON

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(14.0);
        when(airConditionerPersistence.findByLatitudeAndAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditioningAir));

        // Act
        controlStatusByTemperature.controlAirConditioner();

        // Assert
        verify(mqttService).publish("home/airConditioner/1", "OFF");
        verify(airConditionerPersistence).save(any(ConditioningAir.class));
        assertFalse(conditioningAir.getState());
    }
}
