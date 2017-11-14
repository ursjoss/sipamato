package ch.difty.scipamato.web.filter;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PaperFilterTest {

    @Test
    public void construct() {
        PaperFilter filter = new PaperFilter();
        filter.setNumber(1l);
        filter.setAuthorMask("am");
        filter.setMethodsMask("mm");
        filter.setPublicationYearFrom(2000);
        filter.setPublicationYearUntil(3000);

        assertThat(filter.getNumber()).isEqualTo(1l);
        assertThat(filter.getAuthorMask()).isEqualTo("am");
        assertThat(filter.getMethodsMask()).isEqualTo("mm");
        assertThat(filter.getPublicationYearFrom()).isEqualTo(2000);
        assertThat(filter.getPublicationYearUntil()).isEqualTo(3000);

        assertThat(filter.toString()).isEqualTo("PaperFilter(number=1, authorMask=am, methodsMask=mm, publicationYearFrom=2000, publicationYearUntil=3000)");
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(PaperFilter.class).withRedefinedSuperclass().suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

}
