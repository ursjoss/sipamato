package ch.difty.sipamato.web.panel.result;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.data.LinkIconColumn;
import ch.difty.sipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.search.SearchOrderChangeEvent;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

/**
 * The result panel shows the results of searches (by filter or by search order) which are provided
 * by the instantiating page through the data provider holding the filter specification.
 *
 * @author u.joss
 */
public class ResultPanel extends GenericPanel<Void> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperService paperService;

    private final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider;

    private DataTable<PaperSlim, String> results;

    /**
     * Instantiate the panel.
     *
     * @param id the id of the panel
     * @param dataProvider the data provider extending {@link SortablePaperSlimProvider} holding the filter specs
     */
    public ResultPanel(String id, SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider) {
        super(id);
        this.dataProvider = dataProvider;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeTable("table");
    }

    private void makeTable(String id) {
        results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, 20);
        results.setOutputMarkupId(true);
        results.add(new TableBehavior().striped().hover());
        queue(results);
    }

    private List<IColumn<PaperSlim, String>> makeTableColumns() {
        final List<IColumn<PaperSlim, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(Paper.ID, Paper.FLD_ID));
        columns.add(makePropertyColumn(Paper.FIRST_AUTHOR, Paper.FLD_FIRST_AUTHOR));
        columns.add(makePropertyColumn(Paper.PUBL_YEAR, Paper.FLD_PUBL_YEAR));
        columns.add(makeClickableColumn(Paper.TITLE, Paper.FLD_TITLE,
                (IModel<PaperSlim> m) -> setResponsePage(new PaperEntryPage(Model.of(paperService.findById(m.getObject().getId()).orElse(new Paper()))))));
        columns.add(makeLinkIconColumn("exclude"));
        return columns;
    }

    private PropertyColumn<PaperSlim, String> makePropertyColumn(String propExpression, String sortProperty) {
        return new PropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression);
    }

    private ClickablePropertyColumn<PaperSlim, String> makeClickableColumn(String propExpression, String sortProperty, SerializableConsumer<IModel<PaperSlim>> consumer) {
        return new ClickablePropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression, consumer);
    }

    private IColumn<PaperSlim, String> makeLinkIconColumn(String id) {
        return new LinkIconColumn<PaperSlim>(new StringResourceModel("column.header." + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<PaperSlim> rowModel) {
                return Model.of("fa fa-fw fa-trash-o text-danger");
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<PaperSlim> rowModel, AjaxLink<Void> link) {
                final Long excludedId = rowModel.getObject().getId();
                info("Excluded " + rowModel.getObject().getDisplayValue());
                target.add(results);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target, excludedId));
            }
        };
    }

}