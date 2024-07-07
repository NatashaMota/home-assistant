package br.com.openlabs.home_assistant.infra.web.conditioningAir;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.*;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerStateTimeDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerTemperatureDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conditioningAir")
public class AirConditionerController {

    private final ToggleAirConditionerState toggleAirConditionerState;
    private final GetAirConditionerInfo getAirConditionerInfo;
    private final CreateConditioningAir createConditioningAir;
    private final ChangeStateByTime changeStateByTime;
    private final AdjustTemperature adjustTemperature;

    public AirConditionerController(ToggleAirConditionerState toggleAirConditionerState, GetAirConditionerInfo getAirConditionerInfo
                                    , CreateConditioningAir createConditioningAir
                                    , ChangeStateByTime changeStateByTime, AdjustTemperature adjustTemperature) {
        this.toggleAirConditionerState = toggleAirConditionerState;
        this.getAirConditionerInfo = getAirConditionerInfo;
        this.createConditioningAir = createConditioningAir;
        this.changeStateByTime = changeStateByTime;
        this.adjustTemperature = adjustTemperature;
    }

    @PutMapping("/changeState/{id}")
    public ResponseEntity<?> changeState(@PathVariable Long id) {
        try {
            toggleAirConditionerState.execute(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/obtainConditioningAirInfo/{id}")
    public ResponseEntity<?> obtainConditioningAirInfo(@PathVariable Long id) {
        try {
            AirConditionerInfoDTO info = getAirConditionerInfo.execute(id);
            return ResponseEntity.ok(info);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/createConditioningAir")
    public ResponseEntity<?> createConditioningAir(@Valid @RequestBody AirConditionerInfoDTO airConditionerInfoDTO) {
        try {
            Long id = createConditioningAir.execute(airConditionerInfoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/changeStateByTime/{id}")
    public ResponseEntity<?> changeTimes(@PathVariable Long id, @Valid @RequestBody AirConditionerStateTimeDTO airConditionerStateTimeDTO) {

        try {
            changeStateByTime.execute(airConditionerStateTimeDTO, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/changeTemperature/{id}")
    public ResponseEntity<?> changeTemperature(@PathVariable Long id, @Valid @RequestBody AirConditionerTemperatureDTO airConditionerTemperatureDTO) {

        try {
            adjustTemperature.execute(id, airConditionerTemperatureDTO);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
