package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.infra.web.MQTTService;
import org.springframework.stereotype.Service;



@Service
public class ToggleAirConditionerState {
    private final AirConditionerPersistence airConditionerPersistence;
    private final MQTTService mqttService;

    public ToggleAirConditionerState(AirConditionerPersistence airConditionerPersistence, MQTTService mqttService) {
        this.airConditionerPersistence = airConditionerPersistence;
        this.mqttService = mqttService;
    }

    public void execute(Long id) {
        airConditionerPersistence.findById(id)
                .map(conditioningAir -> {
                    conditioningAir.setState(!conditioningAir.getState());
                    System.out.println("Turning " + getTurnOnOffMessage(conditioningAir.getState())
                            + " air conditioner " + conditioningAir.getId().toString());
                    //mqttService.publish("home/airConditioner/" + conditioningAir.getId().toString(),
                             //getTurnOnOffMessage(conditioningAir.getState());
                    conditioningAir.setManually(!conditioningAir.getState());
                    return airConditionerPersistence.save(conditioningAir);
                })
                .orElseThrow(() -> new RuntimeException("Conditioning air not found"));
    }

    private String getTurnOnOffMessage(boolean state) {
        return state ? "ON" : "OFF";
    }
}
