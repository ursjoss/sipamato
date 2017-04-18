package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.TableField;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.Sort.Direction;
import ch.difty.sipamato.persistance.jooq.Sort.Order;

@RunWith(MockitoJUnitRunner.class)
public class SortMapperTest {

    private static final String SHOULD_HAVE_THROWN_EXCEPTION = "should have thrown exception";

    private final SortMapper<PaperRecord, Paper, ch.difty.sipamato.db.tables.Paper> mapper = new SortMapper<>();

    @Mock
    private Order orderMock;

    private final List<Order> orders = new ArrayList<>();

    @After
    public void tearDown() {
        verifyNoMoreInteractions(orderMock);
    }

    @Test
    public void mapping_nullSortSpecification_returnsEmptyCollection() {
        assertThat(mapper.map(null, ch.difty.sipamato.db.tables.Paper.PAPER)).isEmpty();
    }

    @Test
    public void mapping_twoSorts_returnsSortFields() {
        orders.add(new Order(Direction.DESC, "authors"));
        orders.add(new Order(Direction.ASC, "title"));

        Collection<SortField<Paper>> sortFields = mapper.map(new Sort(orders), ch.difty.sipamato.db.tables.Paper.PAPER);
        assertThat(sortFields).hasSize(2);

        Iterator<SortField<Paper>> it = sortFields.iterator();
        SortField<Paper> sf = it.next();
        assertThat(sf.getName()).isEqualToIgnoringCase("authors");
        assertThat(sf.getOrder()).isEqualTo(SortOrder.DESC);

        sf = it.next();
        assertThat(sf.getName()).isEqualToIgnoringCase("title");
        assertThat(sf.getOrder()).isEqualTo(SortOrder.ASC);
    }

    @Test
    public void gettingTableField_withExistingField_returnsTableField() {
        String existingFieldName = ch.difty.sipamato.db.tables.Paper.PAPER.AUTHORS.getName();
        TableField<PaperRecord, Paper> field = mapper.getTableField(existingFieldName, ch.difty.sipamato.db.tables.Paper.PAPER);
        assertThat(field).isNotNull();
        assertThat(field.getName()).isEqualTo(existingFieldName);
    }

    @Test
    public void gettingTableField_withNotExistingField_throws() {
        String notExistingFieldName = "foo";
        try {
            mapper.getTableField(notExistingFieldName, ch.difty.sipamato.db.tables.Paper.PAPER);
            fail(SHOULD_HAVE_THROWN_EXCEPTION);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(InvalidDataAccessApiUsageException.class).hasMessage("Could not find table field: foo; nested exception is java.lang.NoSuchFieldException: FOO");
        }
    }

    @Test
    public void gettingTableField_withNullField_throws() {
        String nullFieldName = null;
        try {
            mapper.getTableField(nullFieldName, ch.difty.sipamato.db.tables.Paper.PAPER);
            fail(SHOULD_HAVE_THROWN_EXCEPTION);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortFieldName must not be null.");
        }
    }

    @Test
    public void gettingTableField_withNulTable_returnsTableField() {
        String existingFieldName = ch.difty.sipamato.db.tables.Paper.PAPER.AUTHORS.getName();
        try {
            mapper.getTableField(existingFieldName, null);
            fail(SHOULD_HAVE_THROWN_EXCEPTION);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("table must not be null.");
        }
    }
}
