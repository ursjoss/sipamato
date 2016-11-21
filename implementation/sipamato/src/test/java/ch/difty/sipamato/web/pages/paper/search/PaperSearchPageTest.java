package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.sipamato.web.pages.BasePageTest;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    @Override
    protected PaperSearchPage makePage() {
        return new PaperSearchPage(new PageParameters());
    }

    @Override
    protected Class<PaperSearchPage> getPageClass() {
        return PaperSearchPage.class;
    }

}
