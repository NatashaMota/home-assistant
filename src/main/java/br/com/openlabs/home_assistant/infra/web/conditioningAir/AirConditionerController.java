package br.com.openlabs.home_assistant.infra.web.conditioningAir;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.*;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerStateTimeDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerTemperatureDTO;
import br.com.openlabs.home_assistant.infra.web.CustomResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conditioningAir")
public class AirConditionerController {

    private final ToggleAirConditionerState toggleAirConditionerState;
    private final GetAirConditionerInfo getAirConditionerInfo;
    private final CreateConditioningAir createConditioningAir;
    private final ChangeStateByTime changeStateByTime;
    private final AdjustTemperature adjustTemperature;

    public AirConditionerController(ToggleAirConditionerState toggleAirConditionerState, GetAirConditionerInfo getAirConditionerInfo,
                                    CreateConditioningAir createConditioningAir,
                                    ChangeStateByTime changeStateByTime, AdjustTemperature adjustTemperature) {
        this.toggleAirConditionerState = toggleAirConditionerState;
        this.getAirConditionerInfo = getAirConditionerInfo;
        this.createConditioningAir = createConditioningAir;
        this.changeStateByTime = changeStateByTime;
        this.adjustTemperature = adjustTemperature;
    }

    @PutMapping("/changeState/{id}")
    public CustomResponseEntity<Void> changeState(@PathVariable Long id) {
        try {
            toggleAirConditionerState.execute(id);
            return new CustomResponseEntity<>(HttpStatus.OK, "State changed successfully", HttpStatus.OK.value(), null);
        } catch (RuntimeException e) {
            return new CustomResponseEntity<>(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        }
    }

    @GetMapping("/obtainConditioningAirInfo/{id}")
    public CustomResponseEntity<AirConditionerInfoDTO> obtainConditioningAirInfo(@PathVariable Long id) {
        try {
            AirConditionerInfoDTO info = getAirConditionerInfo.execute(id);
            return new CustomResponseEntity<>(HttpStatus.OK, "Information retrieved successfully", HttpStatus.OK.value(), info);
        } catch (RuntimeException e) {
            return new CustomResponseEntity<>(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        }
    }

    @PostMapping("/createConditioningAir")
    public CustomResponseEntity<Long> createConditioningAir(@Valid @RequestBody AirConditionerInfoDTO airConditionerInfoDTO) {
        try {
            Long id = createConditioningAir.execute(airConditionerInfoDTO);
            return new CustomResponseEntity<>(HttpStatus.CREATED, "Conditioning air created successfully", HttpStatus.CREATED.value(), id);
        } catch (RuntimeException e) {
            return new CustomResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @PutMapping("/changeStateByTime/{id}")
    public CustomResponseEntity<Void> changeTimes(@PathVariable Long id, @Valid @RequestBody AirConditionerStateTimeDTO airConditionerStateTimeDTO) {
        try {
            changeStateByTime.execute(airConditionerStateTimeDTO, id);
            return new CustomResponseEntity<>(HttpStatus.OK, "State times changed successfully", HttpStatus.OK.value(), null);
        } catch (RuntimeException e) {
            return new CustomResponseEntity<>(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        }
    }

    @PutMapping("/changeTemperature/{id}")
    public CustomResponseEntity<Void> changeTemperature(@PathVariable Long id, @Valid @RequestBody AirConditionerTemperatureDTO airConditionerTemperatureDTO) {
        try {
            adjustTemperature.execute(id, airConditionerTemperatureDTO);
            return new CustomResponseEntity<>(HttpStatus.OK, "Temperature changed successfully", HttpStatus.OK.value(), null);
        } catch (RuntimeException e) {
            return new CustomResponseEntity<>(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public CustomResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return new CustomResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
