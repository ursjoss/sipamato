package ch.difty.scipamato.web.pages.portal;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.ScipamatoPublicSession;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.navigator.ItemNavigator;
import ch.difty.scipamato.persistence.PublicPaperService;
import ch.difty.scipamato.web.component.SerializableSupplier;
import ch.difty.scipamato.web.pages.BasePage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;

@MountPath("/paper/number/${number}")
public class PublicPaperDetailPage extends BasePage<PublicPaper> {

    private static final long serialVersionUID = 1L;

    public static final String PAGE_PARAM_NUMBER = "number";

    @SpringBean
    private PublicPaperService publicPaperService;

    private final PageReference callingPageRef;

    public PublicPaperDetailPage(final PageParameters parameters) {
        this(parameters, null);
    }

    /**
     * Loads the page with the record specified by the 'id' passed in via PageParameters. If the parameter 'no'
     * contains a valid business key number instead, the page will be loaded by number.
     * @param parameters
     * @param callingPageRef
     *     PageReference that will be used to forward to if the user clicks the back button.
     */
    public PublicPaperDetailPage(final PageParameters parameters, final PageReference callingPageRef) {
        super(parameters);
        this.callingPageRef = callingPageRef;

        tryLoadingRecord(parameters);
    }

    /**
     * Try loading the record by ID. If not reasonable id is supplied, try by number.
     */
    private void tryLoadingRecord(final PageParameters parameters) {
        final long number = parameters.get(PAGE_PARAM_NUMBER).toLong(0l);
        if (number > 0) {
            publicPaperService.findByNumber(number).ifPresent((p -> setModel(Model.of(p))));
        }
        if (getModelObject() == null) {
            warn("Page parameter " + PAGE_PARAM_NUMBER + " was missing or invalid. No paper loaded.");
        }
    }

    public PublicPaperDetailPage(final IModel<PublicPaper> paperModel, final PageReference callingPageRef) {
        super(paperModel);
        this.callingPageRef = callingPageRef;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<Void>("form"));

        final ItemNavigator<Long> pm = ScipamatoPublicSession.get().getPaperIdManager();
        queue(newNavigationButton("previous", GlyphIconType.stepbackward, pm::hasPrevious, () -> {
            pm.previous();
            return pm.getItemWithFocus();
        }));
        queue(newNavigationButton("next", GlyphIconType.stepforward, pm::hasNext, () -> {
            pm.next();
            return pm.getItemWithFocus();
        }));
        makeAndQueueBackButton("back");

        queueTopic(newLabel("caption", getModel()));
        queueTopic(null, newField("title", PublicPaper.TITLE));
        queueTopic(newLabel("reference"), newField("authors", PublicPaper.AUTHORS), newField("title2", PublicPaper.TITLE), newField("location", PublicPaper.LOCATION));
        queueTopic(newLabel("goals"), newField("goals", PublicPaper.GOALS));
        queueTopic(newLabel("population"), newField("population", PublicPaper.POPULATION));
        queueTopic(newLabel("methods"), newField("methods", PublicPaper.METHODS));
        queueTopic(newLabel("result"), newField("result", PublicPaper.RESULT));
        queueTopic(newLabel("comment"), newField("comment", PublicPaper.COMMENT));
    }

    protected BootstrapButton newNavigationButton(String id, GlyphIconType icon, SerializableSupplier<Boolean> isEnabled, SerializableSupplier<Long> idSupplier) {
        final BootstrapButton btn = new BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                final Long number = idSupplier.get();
                if (number != null) {
                    PageParameters pp = getPageParameters();
                    pp.set(PAGE_PARAM_NUMBER, number.longValue());
                    setResponsePage(new PublicPaperDetailPage(pp, callingPageRef));
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(isEnabled.get());
            }
        };
        btn.setDefaultFormProcessing(false);
        btn.setIconType(icon);
        btn.add(new AttributeModifier("title", new StringResourceModel("button." + id + ".title", this, null).getString()));
        btn.setType(Buttons.Type.Primary);
        return btn;
    }

    private void makeAndQueueBackButton(String id) {
        BootstrapButton back = new BootstrapButton(id, new StringResourceModel("button.back.label"), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                if (callingPageRef != null)
                    setResponsePage(callingPageRef.getPage());
                else
                    setResponsePage(PublicPage.class);
            }
        };
        back.setDefaultFormProcessing(false);
        back.add(new AttributeModifier("title", new StringResourceModel("button.back.title", this, null).getString()));
        queue(back);
    }

    private void queueTopic(final Label label, final Label... fields) {
        boolean hasValues = fields.length == 0;
        for (final Label f : fields) {
            if (f.getDefaultModelObject() != null) {
                hasValues = true;
            }
        }
        for (final Label f : fields) {
            f.setVisible(hasValues);
            queue(f);
        }
        if (label != null) {
            label.setVisible(hasValues);
            queue(label);
        }
    }

    private Label newLabel(final String idPart, IModel<?> parameterModel) {
        return new Label(idPart + LABEL_TAG, new StringResourceModel(idPart + LABEL_RESOURCE_TAG, this, parameterModel).getString());
    }

    private Label newLabel(final String idPart) {
        return new Label(idPart + LABEL_TAG, new StringResourceModel(idPart + LABEL_RESOURCE_TAG, this, null).getString() + ":");
    }

    private Label newField(final String id, final String property) {
        return new Label(id, new PropertyModel<PublicPaper>(getModel(), property));
    }

}
