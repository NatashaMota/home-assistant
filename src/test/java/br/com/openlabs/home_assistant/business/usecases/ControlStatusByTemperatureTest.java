package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.ControlStatusByTemperature;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.infra.web.GeoLocationService;
import br.com.openlabs.home_assistant.infra.web.MQTTService;
import br.com.openlabs.home_assistant.infra.web.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
        AirConditioner airConditioner = new AirConditioner();
        airConditioner.setId(id);
        airConditioner.setState(false); // Manually turned off
        airConditioner.setManually(true);

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyDouble(), anyDouble())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyDouble(), anyDouble())).thenReturn(List.of(airConditioner));

        controlStatusByTemperature.controlAirConditioner();

        verify(mqttService, never()).publish(anyString(), anyString());
        verify(airConditionerPersistence, never()).save(any(AirConditioner.class));
    }

    @Test
    void testControlAirConditioner_ProgrammedOffTime() {

        Long id = 1L;
        AirConditioner airConditioner = new AirConditioner();
        airConditioner.setId(id);
        airConditioner.setState(true);
        airConditioner.setTurnOffTime(LocalTime.of(22, 0));
        airConditioner.setTurnOnTime(LocalTime.of(6, 0));

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyDouble(), anyDouble())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyDouble(), anyDouble())).thenReturn(List.of(airConditioner));
        LocalTime now = LocalTime.of(0, 0);
        try (MockedStatic<LocalTime> mockedLocalTime = mockStatic(LocalTime.class)) {
            mockedLocalTime.when(LocalTime::now).thenReturn(now);

            controlStatusByTemperature.controlAirConditioner();

            assertTrue(airConditioner.getState());
        }
    }

    @Test
    void testControlAirConditioner_AutomaticControl_TurnOn() {
        // Arrange
        Long id = 1L;
        AirConditioner airConditioner = new AirConditioner();
        airConditioner.setId(id);
        airConditioner.setState(false); // Initial state is OFF
        airConditioner.setManually(false);
        airConditioner.setTemperature(18);
        airConditioner.setLongitude(-46.0060D);
        airConditioner.setLatitude(-24.7128D);
        airConditioner.setTurnOffTime(LocalTime.of(22, 0));
        airConditioner.setTurnOnTime(LocalTime.of(6, 0));

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("40.7128", "-74.0060"));
        when(weatherService.getCurrentTemperature(anyDouble(), anyDouble())).thenReturn(25.0);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyDouble(), anyDouble())).thenReturn(List.of(airConditioner));

        controlStatusByTemperature.controlAirConditioner();

        verify(airConditionerPersistence).save(any(AirConditioner.class));
        assertTrue(airConditioner.getState()); // The state should be ON
    }

    @Test
    void testControlAirConditioner_AutomaticControl_TurnOff() {
        Long id = 1L;
        AirConditioner airConditioner = new AirConditioner();
        airConditioner.setId(id);
        airConditioner.setState(true); // Initially ON
        airConditioner.setManually(false);
        airConditioner.setTemperature(18);
        airConditioner.setLongitude(-46.0060D);
        airConditioner.setLatitude(-24.7128D);
        airConditioner.setTurnOffTime(LocalTime.of(22, 0));
        airConditioner.setTurnOnTime(LocalTime.of(6, 0));

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("-24.7128", "-46.0060"));
        when(weatherService.getCurrentTemperature(anyDouble(), anyDouble())).thenReturn(21.0D);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyDouble(), anyDouble())).thenReturn(List.of(airConditioner));

        when(geoLocationService.getGeoLocation()).thenReturn(List.of("-24.7128", "-46.0060"));
        when(weatherService.getCurrentTemperature(anyDouble(), anyDouble())).thenReturn(12.0D);
        when(airConditionerPersistence.findByLatitudeAndLongitude(anyDouble(), anyDouble())).thenReturn(List.of(airConditioner));

        // Act
        controlStatusByTemperature.controlAirConditioner();

        // Assert
        assertFalse(airConditioner.getState());
    }
}
