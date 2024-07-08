package br.com.openlabs.home_assistant.business.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.CreateConditioningAir;
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

class CreateConditioningAirTest {

    @Mock
    private AirConditionerPersistence airConditionerPersistence;

    @InjectMocks
    private CreateConditioningAir createConditioningAir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        ConditioningAir conditioningAir = new ConditioningAir();
        conditioningAir.setId(id);

        when(airConditionerPersistence.save(any(ConditioningAir.class))).thenReturn(conditioningAir);

        Long resultId = createConditioningAir.execute(dto);

        verify(airConditionerPersistence).save(any(ConditioningAir.class));
        assertEquals(id, resultId);
    }

    @Test
    void testExecuteFailure() {
        AirConditionerInfoDTO dto = null;

        assertThrows(RuntimeException.class, () -> createConditioningAir.execute(dto));
        verify(airConditionerPersistence, never()).save(any(ConditioningAir.class));
    }
}