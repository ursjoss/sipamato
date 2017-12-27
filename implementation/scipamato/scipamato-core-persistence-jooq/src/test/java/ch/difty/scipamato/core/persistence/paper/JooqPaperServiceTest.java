package ch.difty.scipamato.core.persistence.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.filter.PaperFilter;
import ch.difty.scipamato.core.persistence.AbstractServiceTest;
import ch.difty.scipamato.core.persistence.ServiceResult;
import ch.difty.scipamato.core.pubmed.Article;
import ch.difty.scipamato.core.pubmed.ArticleTitle;
import ch.difty.scipamato.core.pubmed.Journal;
import ch.difty.scipamato.core.pubmed.JournalIssue;
import ch.difty.scipamato.core.pubmed.MedlineCitation;
import ch.difty.scipamato.core.pubmed.MedlineJournalInfo;
import ch.difty.scipamato.core.pubmed.PMID;
import ch.difty.scipamato.core.pubmed.PubDate;
import ch.difty.scipamato.core.pubmed.PubmedArticle;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;

public class JooqPaperServiceTest extends AbstractServiceTest<Long, Paper, PaperRepository> {

    private static final long   MINIMUM_NUMBER = 7l;
    private static final String LC             = "de";

    private JooqPaperService service;

    @Mock
    private PaperRepository   repoMock;
    @Mock
    private PaperFilter       filterMock;
    @Mock
    private SearchOrder       searchOrderMock;
    @Mock
    private PaginationContext paginationContextMock;
    @Mock
    private Paper             paperMock, paperMock2, paperMock3;
    @Mock
    private PaperAttachment   attachmentMock;

    private final List<PubmedArticleFacade> articles = new ArrayList<>();

    @Override
    protected Paper getEntity() {
        return paperMock;
    }

    @Override
    protected PaperRepository getRepo() {
        return repoMock;
    }

    private final List<Paper> papers = new ArrayList<>();

    @Override
    public void specificSetUp() {
        service = new JooqPaperService(repoMock, userRepoMock);

        papers.add(paperMock);
        papers.add(paperMock);

        when(paperMock.getCreatedBy()).thenReturn(10);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, searchOrderMock, paginationContextMock, paperMock, paperMock2);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(paperMock);

        Optional<Paper> optPaper = service.findById(id);
        assertThat(optPaper.isPresent()).isTrue();
        assertThat(optPaper.get()).isEqualTo(paperMock);

        verify(repoMock).findById(id);

        verifyAudit(1);
    }

    @Test
    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service.findById(id)
            .isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verifyAudit(2);
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        when(paperMock.getId()).thenReturn(null);
        when(repoMock.add(paperMock)).thenReturn(paperMock);
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).add(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    public void savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        when(paperMock.getId()).thenReturn(17l);
        when(repoMock.update(paperMock)).thenReturn(paperMock);
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).update(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    public void deleting_withNullEntity_doesNothing() {
        service.remove(null);
        verify(repoMock, never()).delete(anyLong(), anyInt());
    }

    @Test
    public void deleting_withEntityWithNullId_doesNothing() {
        when(paperMock.getId()).thenReturn(null);

        service.remove(paperMock);

        verify(paperMock).getId();
        verify(repoMock, never()).delete(anyLong(), anyInt());
    }

    @Test
    public void deleting_withEntityWithNormald_delegatesToRepo() {
        when(paperMock.getId()).thenReturn(3l);
        when(paperMock.getVersion()).thenReturn(17);

        service.remove(paperMock);

        verify(paperMock, times(2)).getId();
        verify(paperMock, times(1)).getVersion();
        verify(repoMock, times(1)).delete(3l, 17);
    }

    @Test
    public void findingBySearchOrder_delegatesToRepo() {
        when(repoMock.findBySearchOrder(searchOrderMock, LC)).thenReturn(papers);
        assertThat(service.findBySearchOrder(searchOrderMock, LC)).containsAll(papers);
        verify(repoMock).findBySearchOrder(searchOrderMock, LC);
    }

    @Test
    public void findingPagedBySearchOrder_delegatesToRepo() {
        when(repoMock.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC)).thenReturn(papers);
        assertThat(service.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC)).isEqualTo(papers);
        verify(repoMock).findPageBySearchOrder(searchOrderMock, paginationContextMock, LC);
    }

    @Test
    public void countingBySearchOrder_delegatesToRepo() {
        when(repoMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2);
        verify(repoMock).countBySearchOrder(searchOrderMock);
    }

    @Test
    public void dumpingEmptyListOfArticles_logsWarnMessage() {
        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();
    }

    @Test
    public void dumpingSingleArticle_whichAlreadyExists_doesNotSave() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa = newPubmedArticle(pmIdValue);
        articles.add(PubmedArticleFacade.of(pa));

        // existing papers
        when(repoMock.findExistingPmIdsOutOf(Arrays.asList(pmIdValue))).thenReturn(Arrays.asList(pmIdValue));

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).hasSize(1)
            .contains("PMID " + pmIdValue);
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock).findExistingPmIdsOutOf(Arrays.asList(pmIdValue));
    }

    @Test
    public void dumpingSingleNewArticle_saves() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa = newPubmedArticle(pmIdValue);
        articles.add(PubmedArticleFacade.of(pa));

        when(repoMock.findExistingPmIdsOutOf(Arrays.asList(pmIdValue))).thenReturn(Arrays.asList());
        when(repoMock.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER)).thenReturn(17l);

        when(repoMock.add(isA(Paper.class))).thenReturn(paperMock2);
        when(paperMock2.getId()).thenReturn(27l);
        when(paperMock2.getPmId()).thenReturn(pmIdValue);

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).hasSize(1)
            .contains("PMID " + pmIdValue + " (id 27)");
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock).findExistingPmIdsOutOf(Arrays.asList(pmIdValue));
        verify(repoMock).findLowestFreeNumberStartingFrom(MINIMUM_NUMBER);
        verify(repoMock).add(isA(Paper.class));
        verify(paperMock2).getId();
        verify(paperMock2).getPmId();
    }

    private PubmedArticle newPubmedArticle(Integer pmIdValue) {
        PubmedArticle pa = new PubmedArticle();
        MedlineCitation mc = new MedlineCitation();
        MedlineJournalInfo mji = new MedlineJournalInfo();
        mc.setMedlineJournalInfo(mji);
        PMID pmId = new PMID();
        pmId.setvalue(String.valueOf(pmIdValue));
        mc.setPMID(pmId);
        Article a = new Article();
        ArticleTitle at = new ArticleTitle();
        a.setArticleTitle(at);
        Journal j = new Journal();
        JournalIssue ji = new JournalIssue();
        PubDate pd = new PubDate();
        ji.setPubDate(pd);
        j.setJournalIssue(ji);
        a.setJournal(j);
        mc.setArticle(a);
        pa.setMedlineCitation(mc);
        return pa;
    }

    @Test
    public void findingByNumber_withNoResult_returnsOptionalEmpty() {
        when(repoMock.findByNumbers(Arrays.asList(1l), LC)).thenReturn(new ArrayList<>());
        Optional<Paper> opt = service.findByNumber(1l, LC);
        assertThat(opt.isPresent()).isFalse();
        verify(repoMock).findByNumbers(Arrays.asList(1l), LC);
    }

    @Test
    public void findingByNumber_withSingleResultFromRepo_returnsThatAsOptional() {
        when(repoMock.findByNumbers(Arrays.asList(1l), LC)).thenReturn(Arrays.asList(paperMock));
        testFindingByNumbers();
    }

    private void testFindingByNumbers() {
        when(userRepoMock.findById(CREATOR_ID)).thenReturn(creatorMock);
        when(userRepoMock.findById(MODIFIER_ID)).thenReturn(modifierMock);

        Optional<Paper> opt = service.findByNumber(1l, LC);
        assertThat(opt.isPresent()).isTrue();
        assertThat(opt.get()).isEqualTo(paperMock);

        verify(repoMock).findByNumbers(Arrays.asList(1l), LC);
        verify(userRepoMock).findById(CREATOR_ID);
        verify(userRepoMock).findById(MODIFIER_ID);
        verify(paperMock).getCreatedBy();
        verify(paperMock).getLastModifiedBy();
        verify(paperMock).setCreatedByName("creatingUser");
        verify(paperMock).setCreatedByFullName("creatingUserFullName");
        verify(paperMock).setLastModifiedByName("modifyingUser");
    }

    @Test
    public void findingByNumber_withMultipleRecordsFromRepo_returnsFirstAsOptional() {
        when(repoMock.findByNumbers(Arrays.asList(1l), LC)).thenReturn(Arrays.asList(paperMock, paperMock2));
        testFindingByNumbers();
    }

    @Test
    public void findingLowestFreeNumberStartingFrom_delegatesToRepo() {
        long minimum = 4l;
        when(repoMock.findLowestFreeNumberStartingFrom(minimum)).thenReturn(17l);
        assertThat(service.findLowestFreeNumberStartingFrom(minimum)).isEqualTo(17l);
        verify(repoMock).findLowestFreeNumberStartingFrom(minimum);
    }

    @Test
    public void findingPageOfIdsByFilter() {
        final List<Long> ids = Arrays.asList(3l, 17l, 5l);
        when(repoMock.findPageOfIdsByFilter(filterMock, paginationContextMock)).thenReturn(ids);
        assertThat(service.findPageOfIdsByFilter(filterMock, paginationContextMock)).isEqualTo(ids);
        verify(repoMock).findPageOfIdsByFilter(filterMock, paginationContextMock);
    }

    @Test
    public void findingPageOfIdsBySearchOrder() {
        final List<Long> ids = Arrays.asList(3l, 17l, 5l);
        when(repoMock.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(ids);
        assertThat(service.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).isEqualTo(ids);
        verify(repoMock).findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    public void excludingFromSearchOrder_delegatesToRepo() {
        long searchOrderId = 4l;
        long paperId = 5l;
        service.excludeFromSearchOrder(searchOrderId, paperId);
        verify(repoMock).excludePaperFromSearchOrderResults(searchOrderId, paperId);
    }

    @Test
    public void reincludingFromSearchOrder_delegatesToRepo() {
        long searchOrderId = 4l;
        long paperId = 5l;
        service.reincludeIntoSearchOrder(searchOrderId, paperId);
        verify(repoMock).reincludePaperIntoSearchOrderResults(searchOrderId, paperId);
    }

    @Test
    public void savingAttachment_delegatesToRepo() {
        PaperAttachment paMock = mock(PaperAttachment.class);
        service.saveAttachment(paMock);
        verify(repoMock).saveAttachment(paMock);
    }

    @Test
    public void loadingAttachmentWithContentById_delegatesToRepo() {
        final Integer id = 7;
        when(repoMock.loadAttachmentWithContentBy(id)).thenReturn(attachmentMock);
        assertThat(service.loadAttachmentWithContentBy(id)).isEqualTo(attachmentMock);
        verify(repoMock).loadAttachmentWithContentBy(id);
    }

    @Test
    public void deletingAttachment_delegatesToRepo() {
        Integer id = 5;
        when(repoMock.deleteAttachment(id)).thenReturn(paperMock);
        assertThat(service.deleteAttachment(id)).isEqualTo(paperMock);
        verify(repoMock).deleteAttachment(id);
    }

    @Test
    public void deletingByIds_delegatesToRepo() {
        List<Long> ids = Arrays.asList(5l, 7l, 9l);
        service.deletePapersWithIds(ids);
        verify(repoMock).delete(ids);
    }

    @Test
    public void findingByFilter_withPaperWithNullCreator() {
        when(paperMock3.getId()).thenReturn(100l);
        when(paperMock3.getCreatedBy()).thenReturn(null);
        when(paperMock3.getLastModifiedBy()).thenReturn(null);
        papers.clear();
        papers.add(paperMock3);
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verify(paperMock3).getCreatedBy();
        verify(paperMock3).setCreatedByName(null);
        verify(paperMock3).setCreatedByFullName(null);
        verify(paperMock3).setLastModifiedByName(null);
    }
}
