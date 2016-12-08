package ch.difty.sipamato.web.panel.search;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.SearchTerm;
import ch.difty.sipamato.entity.filter.SearchTermType;
import ch.difty.sipamato.service.CodeClassService;
import ch.difty.sipamato.service.CodeService;
import ch.difty.sipamato.web.component.data.LinkIconPanel;
import ch.difty.sipamato.web.pages.paper.search.PaperSearchCriteriaPage;
import ch.difty.sipamato.web.panel.PanelTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class SearchOrderPanelTest extends PanelTest<SearchOrderPanel> {

    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private CodeService codeServiceMock;

    @Override
    protected SearchOrderPanel makePanel() {
        SearchCondition sc = new SearchCondition();
        sc.addSearchTerm(SearchTerm.of(1, SearchTermType.STRING.getId(), 1, "authors", "foo"));
        final List<SearchCondition> conditions = Arrays.asList(sc);
        final SearchOrder searchOrder = new SearchOrder(conditions);
        searchOrder.setId(5l);
        return new SearchOrderPanel(PANEL_ID, Model.of(searchOrder));
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, Panel.class);
        assertForm(b + ":form");
    }

    private void assertForm(String b) {
        getTester().assertComponent(b, Form.class);

        String bb = b + ":addSearch";
        getTester().assertComponent(bb, BootstrapAjaxButton.class);
        getTester().assertModelValue(bb, "Add Search Term");

        assertSearchTerms(b + ":searchTerms");
    }

    private void assertSearchTerms(String b) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        getTester().assertComponent(b + ":body", WebMarkupContainer.class);
        getTester().assertComponent(b + ":body:rows", DataGridView.class);
        getTester().assertComponent(b + ":body:rows:1:cells:1:cell", Label.class);
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell", LinkIconPanel.class);
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell:link", AjaxLink.class);
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell:link:image", Label.class);
    }

    @Test
    public void newButtonIsEnabled_ifSearchOrderIdPresent() {
        getTester().startComponentInPage(makePanel());
        getTester().isEnabled(PANEL_ID + ":form:addSearch");
    }

    @Test
    public void newButtonIsDisabled_ifSearchOrderIdNotPresent() {
        getTester().startComponentInPage(new SearchOrderPanel(PANEL_ID, Model.of(new SearchOrder())));
        getTester().isDisabled(PANEL_ID + ":form:addSearch");
    }

    @Test
    public void clickingNewButton_forwardsTopaperSearchCriteriaPage() {
        getTester().startComponentInPage(makePanel());
        FormTester formTester = getTester().newFormTester(PANEL_ID + ":form", false);
        formTester.submit("addSearch");
        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);

        verify(codeClassServiceMock).find(anyString());
        verify(codeServiceMock, times(8)).findCodesOfClass(isA(CodeClassId.class), anyString());
    }

    @Test
    public void clickingDeleteIconLink_() {
        getTester().startComponentInPage(makePanel());
        getTester().assertContains("foo");
        getTester().clickLink("panel:form:searchTerms:body:rows:1:cells:2:cell:link");
        getTester().assertInfoMessages("Removed foo");
    }

}
