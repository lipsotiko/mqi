package io.egia.mqi.measure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.egia.mqi.helpers.Helpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MeasureControllerTest {

    @Mock
    private MeasureRepo measureRepo;
    @Mock
    private GoogleBackedPublicMeasureRepo googleBackedPublicMeasureRepo;

    private Measure existingMeasure;
    private Measure updatedMeasureDescription;
    private Measure updatedMeasureLogic;
    @Captor
    private ArgumentCaptor<Measure> captor = ArgumentCaptor.forClass(Measure.class);

    @Before
    public void setUp() throws IOException {
        existingMeasure = getMeasureFromResource("fixtures", "sampleMeasure.json");
        existingMeasure.setMeasureId(UUID1);
        updatedMeasureDescription = getMeasureFromResource("fixtures", "sampleMeasure.json");
        updatedMeasureDescription.setMeasureId(UUID1);
        updatedMeasureDescription.getMeasureLogic().setDescription("UPDATED DESCRIPTION");
        updatedMeasureLogic = getMeasureFromResource("fixtures", "updatedMeasure.json");
        updatedMeasureLogic.setMeasureId(UUID1);
        when(measureRepo.findById(UUID1)).thenReturn(Optional.of(existingMeasure));
    }

    @Test
    public void saving_measures_with_new_description_does_not_update_its_timestamp() {
        MeasureController measureController = new MeasureController(measureRepo, null, null);
        measureController.putMeasure(updatedMeasureDescription);
        verify(measureRepo, times(1)).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getMeasureLogic()).isEqualTo(updatedMeasureDescription.getMeasureLogic());
        assertThat(captor.getValue().getLastUpdated()).isEqualTo(updatedMeasureDescription.getLastUpdated());
    }

    @Test
    public void saving_measures_with_new_logic_updates_its_timestamp() {
        MeasureController measureController = new MeasureController(measureRepo, null, null);
        measureController.putMeasure(updatedMeasureLogic);
        verify(measureRepo, times(1)).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getMeasureLogic().getDescription()).isEqualTo(
                existingMeasure.getMeasureLogic().getDescription());
        assertThat(captor.getValue().getMeasureLogic()).isNotEqualTo(existingMeasure.getMeasureLogic());
        assertThat(captor.getValue().getLastUpdated()).isNotEqualTo(existingMeasure.getLastUpdated());
    }

    @Test
    public void uninstalled_public_measures_are_available_to_be_installed() {
        PublicMeasureListItem uninstalledMeasure = new PublicMeasureListItem("22222222-1111-1111-1111-111111111111_DIAS.json");
        when(googleBackedPublicMeasureRepo.getMeasureList()).thenReturn(Collections.singletonList(uninstalledMeasure));

        MeasureController measureController = new MeasureController(measureRepo, null, googleBackedPublicMeasureRepo);
        List<PublicMeasureListItem> publicMeasureList = measureController.getPublicMeasureList();

        PublicMeasureListItem expectedMeasureListItem = new PublicMeasureListItem(UUID2, "DIAS.json", false);
        assertThat(publicMeasureList.get(0)).isEqualTo(expectedMeasureListItem);
    }

    @Test
    public void installed_public_measures_are_NOT_available_to_be_installed() {
        PublicMeasureListItem installedMeasureListItem = new PublicMeasureListItem("11111111-1111-1111-1111-111111111111_sampleMeasure.json");
        when(googleBackedPublicMeasureRepo.getMeasureList()).thenReturn(Collections.singletonList(installedMeasureListItem));

        MeasureController measureController = new MeasureController(measureRepo, null, googleBackedPublicMeasureRepo);
        List<PublicMeasureListItem> publicMeasureList = measureController.getPublicMeasureList();

        PublicMeasureListItem expectedMeasureListItem = new PublicMeasureListItem(UUID1, "sampleMeasure.json", true);
        assertThat(publicMeasureList.get(0)).isEqualTo(expectedMeasureListItem);
    }

}
