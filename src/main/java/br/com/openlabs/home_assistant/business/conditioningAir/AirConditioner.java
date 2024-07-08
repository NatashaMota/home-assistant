package br.com.openlabs.home_assistant.business.conditioningAir;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@Entity
public class AirConditioner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Boolean state;
    private Integer temperature;
    @NotNull
    private LocalTime turnOffTime;
    @NotNull
    private LocalTime turnOnTime;
    @NotNull
    private Boolean manually;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    public AirConditioner() {
    }

    public AirConditioner(String name, Boolean state, Integer temperature
                        , LocalTime turnOffTime, LocalTime turnOnTime, Double latitude, Double longitude) {
        setName(name);
        setState(state);
        setTemperature(temperature);
        setTimes(turnOffTime, turnOnTime);
        setManually(false);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public void setTimes(LocalTime turnOffTime, LocalTime turnOnTime) {
        this.turnOffTime = turnOffTime;
        this.turnOnTime = turnOnTime;
    }

}
