package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.h2.tables.Paper.PAPER;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.AUTHORS;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.COMMENT;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.DOI;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.EXPOSURE;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.EXPOSURE_ASSESSMENT;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.EXPOSURE_POLLUTANT;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.FIRST_AUTHOR;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.FIRST_AUTHOR_OVERRIDDEN;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.GOALS;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.INTERN;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.LOCATION;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.METHODS;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.METHOD_CONFOUNDERS;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.METHOD_OUTCOME;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.METHOD_STATISTICS;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.METHOD_STUDY_DESIGN;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.PM_ID;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.POPULATION;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.POPULATION_DURATION;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.POPULATION_PARTICIPANTS;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.POPULATION_PLACE;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.PUBLICATION_YEAR;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.RESULT;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.RESULT_EFFECT_ESTIMATE;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.RESULT_EXPOSURE_RANGE;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.TITLE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetterTest;

public class PaperInsertSetStepSetterTest extends InsertSetStepSetterTest<PaperRecord, Paper> {

    private final InsertSetStepSetter<PaperRecord, Paper> setter = new PaperInsertSetStepSetter();

    @Mock
    private Paper entityMock;

    @Override
    protected InsertSetStepSetter<PaperRecord, Paper> getSetter() {
        return setter;
    }

    @Override
    protected Paper getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock);
    }

    @Override
    protected void entityFixture() {
        PaperRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixture() {
        when(getStep().set(PAPER.PM_ID, PM_ID)).thenReturn(getMoreStep());

        when(getMoreStep().set(PAPER.DOI, DOI)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.AUTHORS, AUTHORS)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.FIRST_AUTHOR, FIRST_AUTHOR)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.FIRST_AUTHOR_OVERRIDDEN, FIRST_AUTHOR_OVERRIDDEN)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.TITLE, TITLE)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.LOCATION, LOCATION)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.PUBLICATION_YEAR, PUBLICATION_YEAR)).thenReturn(getMoreStep());

        when(getMoreStep().set(PAPER.GOALS, GOALS)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.POPULATION, POPULATION)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.EXPOSURE, EXPOSURE)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.METHODS, METHODS)).thenReturn(getMoreStep());

        when(getMoreStep().set(PAPER.POPULATION_PLACE, POPULATION_PLACE)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.POPULATION_PARTICIPANTS, POPULATION_PARTICIPANTS)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.POPULATION_DURATION, POPULATION_DURATION)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.EXPOSURE_POLLUTANT, EXPOSURE_POLLUTANT)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.EXPOSURE_ASSESSMENT, EXPOSURE_ASSESSMENT)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.METHOD_STUDY_DESIGN, METHOD_STUDY_DESIGN)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.METHOD_OUTCOME, METHOD_OUTCOME)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.METHOD_STATISTICS, METHOD_STATISTICS)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.METHOD_CONFOUNDERS, METHOD_CONFOUNDERS)).thenReturn(getMoreStep());

        when(getMoreStep().set(PAPER.RESULT, RESULT)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.COMMENT, COMMENT)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.INTERN, INTERN)).thenReturn(getMoreStep());

        when(getMoreStep().set(PAPER.RESULT_EXPOSURE_RANGE, RESULT_EXPOSURE_RANGE)).thenReturn(getMoreStep());
        when(getMoreStep().set(PAPER.RESULT_EFFECT_ESTIMATE, RESULT_EFFECT_ESTIMATE)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllNonKeyFields() {
        verify(entityMock).getPmId();
        verify(entityMock).getDoi();
        verify(entityMock).getAuthors();
        verify(entityMock).getFirstAuthor();
        verify(entityMock).isFirstAuthorOverridden();
        verify(entityMock).getTitle();
        verify(entityMock).getLocation();
        verify(entityMock).getPublicationYear();

        verify(entityMock).getGoals();
        verify(entityMock).getPopulation();
        verify(entityMock).getExposure();
        verify(entityMock).getMethods();

        verify(entityMock).getPopulationPlace();
        verify(entityMock).getPopulationParticipants();
        verify(entityMock).getPopulationDuration();
        verify(entityMock).getExposurePollutant();
        verify(entityMock).getExposureAssessment();
        verify(entityMock).getMethodStudyDesign();
        verify(entityMock).getMethodOutcome();
        verify(entityMock).getMethodStatistics();
        verify(entityMock).getMethodConfounders();

        verify(entityMock).getResult();
        verify(entityMock).getComment();
        verify(entityMock).getIntern();

        verify(entityMock).getResultExposureRange();
        verify(entityMock).getResultEffectEstimate();
    }

    @Override
    protected void verifySetting() {
        verify(getStep()).set(PAPER.PM_ID, PM_ID);

        verify(getMoreStep()).set(PAPER.DOI, DOI);
        verify(getMoreStep()).set(PAPER.AUTHORS, AUTHORS);
        verify(getMoreStep()).set(PAPER.FIRST_AUTHOR, FIRST_AUTHOR);
        verify(getMoreStep()).set(PAPER.FIRST_AUTHOR_OVERRIDDEN, FIRST_AUTHOR_OVERRIDDEN);
        verify(getMoreStep()).set(PAPER.TITLE, TITLE);
        verify(getMoreStep()).set(PAPER.LOCATION, LOCATION);
        verify(getMoreStep()).set(PAPER.PUBLICATION_YEAR, PUBLICATION_YEAR);

        verify(getMoreStep()).set(PAPER.GOALS, GOALS);
        verify(getMoreStep()).set(PAPER.POPULATION, POPULATION);
        verify(getMoreStep()).set(PAPER.EXPOSURE, EXPOSURE);
        verify(getMoreStep()).set(PAPER.METHODS, METHODS);

        verify(getMoreStep()).set(PAPER.POPULATION_PLACE, POPULATION_PLACE);
        verify(getMoreStep()).set(PAPER.POPULATION_PARTICIPANTS, POPULATION_PARTICIPANTS);
        verify(getMoreStep()).set(PAPER.POPULATION_DURATION, POPULATION_DURATION);
        verify(getMoreStep()).set(PAPER.EXPOSURE_POLLUTANT, EXPOSURE_POLLUTANT);
        verify(getMoreStep()).set(PAPER.EXPOSURE_ASSESSMENT, EXPOSURE_ASSESSMENT);
        verify(getMoreStep()).set(PAPER.METHOD_STUDY_DESIGN, METHOD_STUDY_DESIGN);
        verify(getMoreStep()).set(PAPER.METHOD_OUTCOME, METHOD_OUTCOME);
        verify(getMoreStep()).set(PAPER.METHOD_STATISTICS, METHOD_STATISTICS);
        verify(getMoreStep()).set(PAPER.METHOD_CONFOUNDERS, METHOD_CONFOUNDERS);

        verify(getMoreStep()).set(PAPER.RESULT, RESULT);
        verify(getMoreStep()).set(PAPER.COMMENT, COMMENT);
        verify(getMoreStep()).set(PAPER.INTERN, INTERN);

        verify(getMoreStep()).set(PAPER.RESULT_EXPOSURE_RANGE, RESULT_EXPOSURE_RANGE);
        verify(getMoreStep()).set(PAPER.RESULT_EFFECT_ESTIMATE, RESULT_EFFECT_ESTIMATE);
    }

    @Test
    public void consideringSettingKeyOf_withNullId_doesNotSetId() {
        when(getEntity().getId()).thenReturn(null);
        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());
        verify(getEntity()).getId();
    }

    @Test
    public void consideringSettingKeyOf_withNonNullId_doesSetId() {
        when(getEntity().getId()).thenReturn(ID);

        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());

        verify(getEntity()).getId();
        verify(getMoreStep()).set(PAPER.ID, ID);
    }

}
