package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
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
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Test
    void testControlAirConditioner_ManualOff() {
        // Arrange
        Long id = 1L;
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);
        conditionerAir.setState(false); // Manually turned off
        conditionerAir.setManually(true);

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditionerAir));

        // Act
        controlStatusByTemperature.controlAirConditioner();

        // Assert
        verify(mqttService, never()).publish(anyString(), anyString());
        verify(airConditionerPersistence, never()).save(any(ConditionerAir.class));
    }

    @Test
    void testControlAirConditioner_ProgrammedOffTime() {
        Long id = 1L;
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);
        conditionerAir.setState(true); // Initially ON
        conditionerAir.setTurnOffTime(LocalTime.of(22, 0));
        conditionerAir.setTurnOnTime(LocalTime.of(6, 0));

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditionerAir));

        // Simula que o horário atual é 23:00
        try (var mockedLocalTime = mockStatic(LocalTime.class)) {
            mockedLocalTime.when(LocalTime::now).thenReturn(LocalTime.of(23, 0));

            // Act
            controlStatusByTemperature.controlAirConditioner();

            // Assert
            verify(mqttService).publish("home/airConditioner/1", "OFF");
            verify(airConditionerPersistence).save(any(ConditionerAir.class));
            assertFalse(conditionerAir.getState());
        }
    }

    @Test
    void testControlAirConditioner_AutomaticControl_TurnOn() {
        // Arrange
        Long id = 1L;
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);
        conditionerAir.setState(false); // Initially OFF

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditionerAir));

        // Act
        controlStatusByTemperature.controlAirConditioner();

        // Assert
        verify(mqttService).publish("home/airConditioner/1", "ON");
        verify(airConditionerPersistence).save(any(ConditionerAir.class));
        assertTrue(conditionerAir.getState());
    }

    @Test
    void testControlAirConditioner_AutomaticControl_TurnOff() {
        // Arrange
        Long id = 1L;
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);
        conditionerAir.setState(true); // Initially ON

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyString(), anyString())).thenReturn(14.0);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyLong(), anyLong())).thenReturn(List.of(conditionerAir));

        // Act
        controlStatusByTemperature.controlAirConditioner();

        // Assert
        verify(mqttService).publish("home/airConditioner/1", "OFF");
        verify(airConditionerPersistence).save(any(ConditionerAir.class));
        assertFalse(conditionerAir.getState());
    }
}
