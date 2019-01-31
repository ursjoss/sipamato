package ch.difty.scipamato.core.web.code;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.authentication.LogoutPage;
import ch.difty.scipamato.core.web.common.BasePageTest;

public class CodeEditPageTest extends BasePageTest<CodeEditPage> {

    private final CodeClass cc2 = new CodeClass(2, "Region", null);

    private CodeDefinition cd;

    @MockBean
    private CodeService codeServiceMock;

    @Override
    public void setUpHook() {
        final CodeTranslation kt_de = new CodeTranslation(1, "de", "Name1", "some comment", 1);
        final CodeTranslation kt_de2 = new CodeTranslation(1, "de", "Name1a", null, 1);
        final CodeTranslation kt_en = new CodeTranslation(2, "en", "name1", null, 1);
        final CodeTranslation kt_fr = new CodeTranslation(3, "fr", "nom1", null, 1);
        cd = new CodeDefinition("2A", "de", cc2, 1, false, 1, kt_de, kt_en, kt_fr, kt_de2);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(codeServiceMock);
    }

    @Override
    protected CodeEditPage makePage() {
        return new CodeEditPage(Model.of(cd), null);
    }

    @Override
    protected Class<CodeEditPage> getPageClass() {
        return CodeEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":headerPanel:";
        String bb = b + "code";
        getTester().assertLabel(bb + "Label", "Code");
        getTester().assertModelValue(bb, "2A");

        bb = b + "codeClass";
        getTester().assertLabel(bb + "Label", "Code Class");
        getTester().assertComponent(bb, BootstrapSelect.class);
        getTester().assertModelValue(bb, cc2);
        getTester().assertContains("<option selected=\"selected\" value=\"2\">2 - Region</option>");

        bb = b + "sort";
        getTester().assertLabel(bb + "Label", "Sort");
        getTester().assertComponent(bb, TextField.class);

        bb = b + "internal";
        getTester().assertLabel(bb + "Label", "Internal");
        getTester().assertComponent(bb, CheckBoxX.class);

        getTester().assertComponent(b + "back", BootstrapButton.class);
        getTester().assertComponent(b + "submit", BootstrapButton.class);
        getTester().assertComponent(b + "delete", BootstrapButton.class);

        bb = "form:translations";
        getTester().assertLabel(bb + "Label", "Code Translations and Comments");

        bb += "Panel:translations";
        getTester().assertComponent(bb, RefreshingView.class);
        bb += ":";
        assertTranslation(bb, 1, "de", "Name1", "some comment");
        assertTranslation(bb, 2, "de", "Name1a", null);
        assertTranslation(bb, 3, "en", "name1", null);
        assertTranslation(bb, 4, "fr", "nom1", null);
    }

    private void assertTranslation(final String bb, final int idx, final String langCode, final String name,
        final String comment) {
        getTester().assertLabel(bb + idx + ":langCode", langCode);
        getTester().assertComponent(bb + idx + ":name", TextField.class);
        getTester().assertModelValue(bb + idx + ":name", name);
        getTester().assertComponent(bb + idx + ":comment", TextField.class);
        getTester().assertModelValue(bb + idx + ":comment", comment);
    }

    @Test
    public void submitting_withSuccessfulServiceCall_addsInfoMsg() {
        when(codeServiceMock.saveOrUpdate(isA(CodeDefinition.class))).thenReturn(cd);

        runSubmitTest();

        getTester().assertInfoMessages("Successfully saved code 2A: DE: 'foo','Name1a'; EN: 'name1'; FR: 'nom1'.");
        getTester().assertNoErrorMessage();
    }

    private void runSubmitTest() {
        getTester().startPage(new CodeEditPage(Model.of(cd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("translationsPanel:translations:1:name", "foo");
        assertTranslation("form:translationsPanel:translations:", 1, "de", "Name1", "some comment");
        formTester.submit("headerPanel:submit");
        assertTranslation("form:translationsPanel:translations:", 5, "de", "foo", "some comment");

        verify(codeServiceMock).saveOrUpdate(isA(CodeDefinition.class));
    }

    @Test
    public void submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        when(codeServiceMock.saveOrUpdate(isA(CodeDefinition.class))).thenReturn(null);

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Could not save code 2A.");
    }

    @Test
    public void submitting_withOptimisticLockingException_addsErrorMsg() {
        when(codeServiceMock.saveOrUpdate(isA(CodeDefinition.class))).thenThrow(
            new OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("The tblName with id 2A has been modified concurrently "
                                        + "by another user. Please reload it and apply your changes once more.");
    }

    @Test
    public void submitting_withOtherException_addsErrorMsg() {
        when(codeServiceMock.saveOrUpdate(isA(CodeDefinition.class))).thenThrow(new RuntimeException("fooMsg"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save the code 2A: fooMsg");
    }

    @Test
    public void submitting_withDuplicateKeyConstraintViolationException_addsErrorMsg() {
        String msg = "...Detail: Key (code_class_id, sort)=(2, 1) already exists.; "
                     + "nested exception is org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint...";
        when(codeServiceMock.saveOrUpdate(isA(CodeDefinition.class))).thenThrow(new DuplicateKeyException(msg));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("The sort index 1 is already in use for codes of code class 2.");
    }

    @Test
    public void submitting_withDuplicateKeyConstraintViolationException_withUnexpectedMsg_addsThatErrorMsg() {
        String msg = "something unexpected happened";
        when(codeServiceMock.saveOrUpdate(isA(CodeDefinition.class))).thenThrow(new DuplicateKeyException(msg));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("something unexpected happened");
    }

    @Test
    public void submitting_withDuplicateKeyConstraintViolationException_withNullMsg_addsThatErrorMsg() {
        when(codeServiceMock.saveOrUpdate(isA(CodeDefinition.class))).thenThrow(new DuplicateKeyException(null));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Unexpected DuplicateKeyConstraintViolation");
    }

    @Test
    public void submitting_withNullCode_preventsSave() {
        assertCodeCodeClassMismatch(null);
    }

    @Test
    public void submitting_withBlankCode_preventsSave() {
        assertCodeCodeClassMismatch("");
    }

    @Test
    public void submitting_withCodeCodeClassMismatch_preventsSave() {
        assertCodeCodeClassMismatch("3A");
    }

    private void assertCodeCodeClassMismatch(final String code) {
        getTester().startPage(new CodeEditPage(Model.of(cd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("headerPanel:code", code);
        formTester.submit("headerPanel:submit");

        getTester().assertErrorMessages("The first digit of the Code must match the Code Class Number.");

        verify(codeServiceMock, never()).saveOrUpdate(isA(CodeDefinition.class));
    }

    @Test
    public void submitting_withForeignKeyConstraintViolationException_addsErrorMsg() {
        String msg = "... is still referenced from table \"paper_code\".; nested exception is org.postgresql.util.PSQLException...";
        when(codeServiceMock.delete(anyString(), anyInt())).thenThrow(new DataIntegrityViolationException(msg));

        getTester().startPage(new CodeEditPage(Model.of(cd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:delete");

        verify(codeServiceMock).delete(anyString(), anyInt());

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("You cannot delete code '2A' as it is still assigned to at least one paper.");
    }

    @Test
    public void clickingBackButton_withPageWithoutCallingPageRef_forwardsToCodeListPage() {
        getTester().startPage(new CodeEditPage(Model.of(cd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(CodeListPage.class);

        // from CodeListPage
        verify(codeServiceMock).getCodeClass1("en_us");
        verify(codeServiceMock).countByFilter(isA(CodeFilter.class));
    }

    @Test
    public void clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        getTester().startPage(new CodeEditPage(Model.of(cd), new LogoutPage(new PageParameters()).getPageReference()));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(LogoutPage.class);
    }

}