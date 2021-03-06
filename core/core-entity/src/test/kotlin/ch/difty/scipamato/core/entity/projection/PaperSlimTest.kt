package ch.difty.scipamato.core.entity.projection

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PaperSlimTest {

    private var ps: PaperSlim = PaperSlim()

    @BeforeEach
    fun setUp() {
        ps.id = 1L
        ps.number = 10L
        ps.firstAuthor = "firstAuthor"
        ps.title = "title"
        ps.publicationYear = 2016
        ps.newsletterAssociation = NewsletterAssociation(20, "nl", 1, "hl")
    }

    @Test
    fun getting_hasAllFields() {
        getting(20, "nl", 1, "hl")
    }

    private fun getting(nlId: Int? = null, nlIssue: String? = null, nlStatus: Int? = null, nlHeadline: String? = null) {
        ps.id shouldBeEqualTo 1L
        ps.number shouldBeEqualTo 10L
        ps.publicationYear shouldBeEqualTo 2016
        ps.title shouldBeEqualTo "title"
        ps.firstAuthor shouldBeEqualTo "firstAuthor"
        if (nlId != null) {
            with(ps.newsletterAssociation) {
                id shouldBeEqualTo nlId
                issue shouldBeEqualTo nlIssue
                headline shouldBeEqualTo nlHeadline
                publicationStatusId shouldBeEqualTo nlStatus
            }
        } else {
            ps.newsletterAssociation.shouldBeNull()
        }
    }

    @Test
    fun displayValue() {
        ps.displayValue shouldBeEqualTo "firstAuthor (2016): title."
    }

    @Test
    fun testingToString() {
        ps.toString() shouldBeEqualTo
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, " +
            "title=title, newsletter=nl, headline=hl)"
    }

    @Test
    fun testingToString_withNoNewsletter() {
        ps.newsletterAssociation = null
        ps.toString() shouldBeEqualTo
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title)"
    }

    @Test
    fun testingToString_withNoHeadline() {
        ps.newsletterAssociation.headline = null
        ps.toString() shouldBeEqualTo
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title, newsletter=nl)"
    }

    @Test
    fun alternativeConstructor_withoutNewsletter_hasAllFields_exceptNewsletter() {
        ps = PaperSlim(1L, 10L, "firstAuthor", 2016, "title")
        getting(null, null, null, null)
    }

    @Test
    fun alternativeConstructor_withNewsletterFields() {
        ps = PaperSlim(1L, 10L, "firstAuthor", 2016, "title", 20, "nlTitle", 1, "hl")
        getting(20, "nlTitle", 1, "hl")
    }

    @Test
    fun alternativeConstructor_withNewsletter() {
        ps = PaperSlim(1L, 10L, "firstAuthor", 2016, "title", NewsletterAssociation(30, "t", 3, "headline"))
        getting(30, "t", 3, "headline")
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(PaperSlim::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(CREATED.fieldName, CREATOR_ID.fieldName, MODIFIED.fieldName, MODIFIER_ID.fieldName)
            .verify()
    }
}
