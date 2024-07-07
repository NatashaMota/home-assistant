package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
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
    private double max = 22.0;
    private double min = 15.0;


    public ControlStatusByTemperature(AirConditionerPersistence airConditionerPersistence, WeatherService weatherService, MQTTService mqttService) {
        this.airConditionerPersistence = airConditionerPersistence;
        this.weatherService = weatherService;
        this.mqttService = mqttService;
    }

    @Scheduled(fixedRate = 60000) // 1 min
    public void controlAirConditioner() {
        //TODO: usar loc aproximada
        Long locationId = 3457001L;
        LocalTime now = LocalTime.now();

        double currentTemperature = weatherService.getCurrentTemperature(locationId.toString());
        List<ConditioningAir> conditioningAirs =  airConditionerPersistence.findByCidadeId(locationId);
        System.out.println("Current temperature: " + currentTemperature );
        for (ConditioningAir conditioningAir : conditioningAirs) {

            if(manuallyTurnedOff(conditioningAir)){
                continue;
            }
            if(inOffProgrammedTime(conditioningAir, now)){
                if (conditioningAir.getState()){
                    turnOff(conditioningAir);
                }
                continue;
            }
            automaticControlState(conditioningAir, currentTemperature);
        }
    }

    private void automaticControlState(ConditioningAir conditioningAir, double currentTemperature) {
        // Controle automático
        if (currentTemperature > max && !conditioningAir.getState()) {
            turnOn(conditioningAir);
        } else if (currentTemperature < min && conditioningAir.getState()) {
            turnOff(conditioningAir);
        }
    }

    private Boolean manuallyTurnedOff(ConditioningAir conditioningAir) {
        // manualmente não deve ligar
        if (!conditioningAir.getState() && conditioningAir.getManually()) {
            System.out.println("Air conditioner " + conditioningAir.getId().toString() + " was manually turned OFF.");
            return true;
        }
        return false;
    }

    private Boolean inOffProgrammedTime(ConditioningAir conditioningAir, LocalTime now) {
        if (now.isAfter(conditioningAir.getTurnOffTime()) && now.isBefore(conditioningAir.getTurnOnTime())) {
            System.out.println("Air conditioner " + conditioningAir.getId().toString() + " is in programmed OFF time.");
            return true;
        }
        return false;
    }

    public void turnOff(ConditioningAir conditioningAir){
        System.out.println("Turning OFF air conditioner " + conditioningAir.getId().toString());
        mqttService.publish("home/airConditioner/" + conditioningAir.getId().toString(), "OFF");
        conditioningAir.setState(false);
        airConditionerPersistence.save(conditioningAir);
    }

    public void turnOn(ConditioningAir conditioningAir){
        System.out.println("Turning ON air conditioner " + conditioningAir.getId().toString());
        mqttService.publish("home/airConditioner/" + conditioningAir.getId().toString(), "ON");
        conditioningAir.setState(true);
        airConditionerPersistence.save(conditioningAir);
    }

}