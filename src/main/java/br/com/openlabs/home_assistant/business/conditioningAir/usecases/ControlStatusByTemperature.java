package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import br.com.openlabs.home_assistant.infra.web.GeoLocationService;
import br.com.openlabs.home_assistant.infra.web.MQTTService;
import br.com.openlabs.home_assistant.infra.web.WeatherService;
import br.com.openlabs.home_assistant.infra.web.conditioningAir.AirConditionerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
public class ControlStatusByTemperature {

    private static final Logger logger = LoggerFactory.getLogger(AirConditionerController.class);


    private final AirConditionerPersistence airConditionerPersistence;

    private final WeatherService weatherService;
    private final MQTTService mqttService;
    private final GeoLocationService geoLocationService;

    private final Double MAX = 22.0;
    private final Double MIN = 15.0;


    public ControlStatusByTemperature(AirConditionerPersistence airConditionerPersistence
            , WeatherService weatherService, MQTTService mqttService, GeoLocationService geoLocationService) {
        this.airConditionerPersistence = airConditionerPersistence;
        this.weatherService = weatherService;
        this.mqttService = mqttService;
        this.geoLocationService = geoLocationService;
    }

    @Scheduled(fixedRate = 6000)
    public void controlAirConditioner() {
        LocalTime now = LocalTime.now();
        List<String> geoLocation = geoLocationService.getGeoLocation();

        double currentTemperature = weatherService.getCurrentTemperature(geoLocation.get(0), geoLocation.get(1));
        List<AirConditioner> airConditioners =  airConditionerPersistence.findByLatitudeAndLongitude(
                Double.valueOf(geoLocation.get(0)), Double.valueOf(geoLocation.get(1)));
        for (AirConditioner airConditioner : airConditioners) {

            if(manuallyTurnedOff(airConditioner)){
                continue;
            }
            if(inOffProgrammedTime(airConditioner, now)){
                if (airConditioner.getState()){
                    turnOff(airConditioner);
                }
                continue;
            }
            automaticControlState(airConditioner, currentTemperature);
        }
    }

    private void automaticControlState(AirConditioner airConditioner, double currentTemperature) {
        // Controle automático
        if (currentTemperature > MAX && !airConditioner.getState()) {
            turnOn(airConditioner);
        } else if (currentTemperature < MIN && airConditioner.getState()) {
            turnOff(airConditioner);
        }
    }

    private Boolean manuallyTurnedOff(AirConditioner airConditioner) {

        if (!airConditioner.getState() && airConditioner.getManually()) {
            logger.info("Air conditioner {} was manually turned OFF.",  airConditioner.getId().toString());
            return true;
        }
        return false;
    }

    private Boolean inOffProgrammedTime(AirConditioner airConditioner, LocalTime now) {
        if (now.isAfter(airConditioner.getTurnOffTime()) && now.isBefore(airConditioner.getTurnOnTime())) {
            logger.info("Air conditioner {} is in programmed OFF time.", airConditioner.getId().toString());
            return true;
        }
        return false;
    }

    public void turnOff(AirConditioner airConditioner){
        logger.info("Turning OFF air conditioner {}", airConditioner.getId().toString());
        mqttService.publish("home/airConditioner/" + airConditioner.getId().toString(), "OFF");
        airConditioner.setState(false);
        airConditionerPersistence.save(airConditioner);
    }

    public void turnOn(AirConditioner airConditioner){
        logger.info("Turning ON air conditioner {}", airConditioner.getId().toString());
        mqttService.publish("home/airConditioner/" + airConditioner.getId().toString(), "ON");
        airConditioner.setState(true);
        airConditionerPersistence.save(airConditioner);
    }

}