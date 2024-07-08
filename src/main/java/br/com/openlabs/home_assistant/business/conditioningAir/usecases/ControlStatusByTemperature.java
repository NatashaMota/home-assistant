package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.infra.web.GeoLocationService;
import br.com.openlabs.home_assistant.infra.web.MQTTService;
import br.com.openlabs.home_assistant.infra.web.WeatherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
public class ControlStatusByTemperature {

    private final AirConditionerPersistence airConditionerPersistence;

    private final WeatherService weatherService;
    private final MQTTService mqttService;
    private final GeoLocationService geoLocationService;

    private double max = 22.0;
    private double min = 15.0;


    public ControlStatusByTemperature(AirConditionerPersistence airConditionerPersistence
            , WeatherService weatherService, MQTTService mqttService, GeoLocationService geoLocationService) {
        this.airConditionerPersistence = airConditionerPersistence;
        this.weatherService = weatherService;
        this.mqttService = mqttService;
        this.geoLocationService = geoLocationService;
    }

    @Scheduled(fixedRate = 60000) // 1 min
    public void controlAirConditioner() {
        LocalTime now = LocalTime.now();
        List<String> geoLocation = geoLocationService.getGeoLocation();

        double currentTemperature = weatherService.getCurrentTemperature(geoLocation.get(0), geoLocation.get(1));
        List<ConditionerAir> conditionerAirs =  airConditionerPersistence.findByLatitudeAndAndLongitude(
                Long.getLong(geoLocation.get(0)), Long.getLong(geoLocation.get(1)));
        System.out.println("Current temperature: " + currentTemperature );
        for (ConditionerAir conditionerAir : conditionerAirs) {

            if(manuallyTurnedOff(conditionerAir)){
                continue;
            }
            if(inOffProgrammedTime(conditionerAir, now)){
                if (conditionerAir.getState()){
                    turnOff(conditionerAir);
                }
                continue;
            }
            automaticControlState(conditionerAir, currentTemperature);
        }
    }

    private void automaticControlState(ConditionerAir conditionerAir, double currentTemperature) {
        // Controle automático
        if (currentTemperature > max && !conditionerAir.getState()) {
            turnOn(conditionerAir);
        } else if (currentTemperature < min && conditionerAir.getState()) {
            turnOff(conditionerAir);
        }
    }

    private Boolean manuallyTurnedOff(ConditionerAir conditionerAir) {
        // manualmente não deve ligar
        if (!conditionerAir.getState() && conditionerAir.getManually()) {
            System.out.println("Air conditioner " + conditionerAir.getId().toString() + " was manually turned OFF.");
            return true;
        }
        return false;
    }

    private Boolean inOffProgrammedTime(ConditionerAir conditionerAir, LocalTime now) {
        if (now.isAfter(conditionerAir.getTurnOffTime()) && now.isBefore(conditionerAir.getTurnOnTime())) {
            System.out.println("Air conditioner " + conditionerAir.getId().toString() + " is in programmed OFF time.");
            return true;
        }
        return false;
    }

    public void turnOff(ConditionerAir conditionerAir){
        System.out.println("Turning OFF air conditioner " + conditionerAir.getId().toString());
        mqttService.publish("home/airConditioner/" + conditionerAir.getId().toString(), "OFF");
        conditionerAir.setState(false);
        airConditionerPersistence.save(conditionerAir);
    }

    public void turnOn(ConditionerAir conditionerAir){
        System.out.println("Turning ON air conditioner " + conditionerAir.getId().toString());
        mqttService.publish("home/airConditioner/" + conditionerAir.getId().toString(), "ON");
        conditionerAir.setState(true);
        airConditionerPersistence.save(conditionerAir);
    }

}