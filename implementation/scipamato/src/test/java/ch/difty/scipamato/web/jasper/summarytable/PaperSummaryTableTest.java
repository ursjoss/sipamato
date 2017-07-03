package ch.difty.scipamato.web.jasper.summarytable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.web.jasper.JasperEntityTest;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

public class PaperSummaryTableTest extends JasperEntityTest {

    private static final String BRAND = "brand";
    private static final String CAPTION = "caption";
    private static final String NUMBER_LABEL = "nl";

    private PaperSummaryTable pst;
    private ReportHeaderFields rhf = newReportHeaderFields();

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction_withNullPaper() {
        new PaperSummaryTable(null, rhf, true);
    }

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction_withNullReportHeaderFields() {
        new PaperSummaryTable(p, null, true);
    }

    @Test
    public void instantiating() {
        pst = new PaperSummaryTable(p, rhf, true);
        assertPst();
    }

    private ReportHeaderFields newReportHeaderFields() {
        ReportHeaderFields.Builder b = new ReportHeaderFields.Builder(HEADER_PART, BRAND).withCaption(CAPTION).withMethods(METHODS_LABEL).withNumber(NUMBER_LABEL);
        return b.build();
    }

    private void assertPst() {
        assertThat(pst.getCaption()).isEqualTo(CAPTION);
        assertThat(pst.getBrand()).isEqualTo(BRAND);
        assertThat(pst.getNumberLabel()).isEqualTo(NUMBER_LABEL);

        assertThat(pst.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(pst.getFirstAuthor()).isEqualTo(FIRST_AUTHOR);
        assertThat(pst.getPublicationYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
        assertThat(pst.getGoals()).isEqualTo(GOALS);
        assertThat(pst.getTitle()).isEqualTo(TITLE);

        assertThat(pst.getCodesOfClass1()).isEqualTo("1F");
        assertThat(pst.getCodesOfClass4()).isEqualTo("4A,4C");
        assertThat(pst.getCodesOfClass7()).isEqualTo("7B");
    }

    @Test
    public void constructionWithPaper_notIncludingResults() {
        pst = new PaperSummaryTable(p, rhf, false);
        assertPst();
        assertThat(pst.getResult()).isEmpty();
    }

    @Test
    public void constructionWithPaperWithNoCodeOfClass7_returnsBlank() {
        p.clearCodes();
        pst = new PaperSummaryTable(p, rhf, true);
        assertThat(pst.getCodesOfClass4()).isEqualTo("");
    }
}