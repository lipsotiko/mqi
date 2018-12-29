package io.egia.mqi.measure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Optional;

import static io.egia.mqi.helpers.Helpers.getMeasureFromResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MeasureControllerTest {

    @Mock private MeasureRepo measureRepo;
    private Measure existingMeasure;
    private Measure updatedMeasureDescription;
    private Measure updatedMeasureLogic;
    @Captor private ArgumentCaptor<Measure> captor = ArgumentCaptor.forClass(Measure.class);

    @Before
    public void setUp() throws IOException {
        existingMeasure = getMeasureFromResource("fixtures", "sampleMeasure.json");
        existingMeasure.setMeasureId(1L);
        updatedMeasureDescription = getMeasureFromResource("fixtures", "sampleMeasure.json");
        updatedMeasureDescription.setMeasureId(1L);
        updatedMeasureDescription.getMeasureLogic().setDescription("UPDATED DESCRIPTION");
        updatedMeasureLogic = getMeasureFromResource("fixtures", "updatedMeasure.json");
        updatedMeasureLogic.setMeasureId(1L);
        when(measureRepo.findById(1L)).thenReturn(Optional.of(existingMeasure));
    }

    @Test
    public void saving_measures_with_new_description_does_not_update_its_timestamp() {
        MeasureController measureController = new MeasureController(measureRepo, null);
        measureController.putMeasure(updatedMeasureDescription);
        verify(measureRepo, times(1)).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getMeasureLogic()).isEqualTo(updatedMeasureDescription.getMeasureLogic());
        assertThat(captor.getValue().getLastUpdated()).isEqualTo(updatedMeasureDescription.getLastUpdated());
    }

    @Test
    public void saving_measures_with_new_logic_updates_its_timestamp() {
        MeasureController measureController = new MeasureController(measureRepo, null);
        measureController.putMeasure(updatedMeasureLogic);
        verify(measureRepo, times(1)).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getMeasureLogic().getDescription()).isEqualTo(
                existingMeasure.getMeasureLogic().getDescription());
        assertThat(captor.getValue().getMeasureLogic()).isNotEqualTo(existingMeasure.getMeasureLogic());
        assertThat(captor.getValue().getLastUpdated()).isNotEqualTo(existingMeasure.getLastUpdated());
    }
}