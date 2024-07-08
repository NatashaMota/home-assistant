package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.CreateAirConditioner;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateAirConditionerTest {

    @Mock
    private AirConditionerPersistence airConditionerPersistence;

    @InjectMocks
    private CreateAirConditioner createAirConditioner;

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testExecuteSuccess() {
        // Arrange
        Long id = 1L;
        AirConditionerInfoDTO dto = new AirConditionerInfoDTO("Teste"
                                                                    ,false
                                                                    , 20
                                                                    ,null
                                                                    , null
                                                                    , Long.getLong("-23.5228")
                                                                    , Long.getLong("-46.1883"));
        ConditionerAir conditionerAir = new ConditionerAir();
        conditionerAir.setId(id);

        when(airConditionerPersistence.save(any(ConditionerAir.class))).thenReturn(conditionerAir);

        Long resultId = createAirConditioner.execute(dto);

        verify(airConditionerPersistence).save(any(ConditionerAir.class));
        assertEquals(id, resultId);
    }

    @Test
    void testExecuteFailure() {
        AirConditionerInfoDTO dto = null;

        assertThrows(RuntimeException.class, () -> createAirConditioner.execute(dto));
        verify(airConditionerPersistence, never()).save(any(ConditionerAir.class));
    }
}