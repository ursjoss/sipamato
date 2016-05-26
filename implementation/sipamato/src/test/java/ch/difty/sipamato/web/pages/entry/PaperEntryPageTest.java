package ch.difty.sipamato.web.pages.entry;

import org.apache.wicket.markup.html.form.Form;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.web.pages.AbstractPageTest;

public class PaperEntryPageTest extends AbstractPageTest<PaperEntryPage> {

    @Override
    protected Class<PaperEntryPage> getPageClass() {
        return PaperEntryPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        assertLabeledTextArea(b, Paper.AUTHORS);
        assertLabeledTextField(b, Paper.FIRST_AUTHOR);
        assertLabeledCheckBox(b, Paper.FIRST_AUTHOR_OVERRIDDEN);
        assertLabeledTextArea(b, Paper.TITLE);
        assertLabeledTextField(b, Paper.LOCATION);

        assertLabeledTextField(b, Paper.ID);
        assertLabeledTextField(b, Paper.PUBL_YEAR);
        assertLabeledTextField(b, Paper.PMID);
        assertLabeledTextField(b, Paper.DOI);
    }
}
