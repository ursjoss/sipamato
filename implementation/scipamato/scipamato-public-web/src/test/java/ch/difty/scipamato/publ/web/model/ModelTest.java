package ch.difty.scipamato.publ.web.model;

import java.util.Locale;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.scipamato.publ.ScipamatoPublicApplication;

@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("WeakerAccess")
public abstract class ModelTest {

    @Autowired
    private ScipamatoPublicApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    @Before
    public final void setUp() {
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        WicketTester tester = new WicketTester(application);
        Locale locale = new Locale("en_US");
        tester
            .getSession()
            .setLocale(locale);
        setUpLocal();
    }

    /**
     * Override if the actual test class needs a setUp
     */
    protected void setUpLocal() {
    }

}
