package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.service.PaperSlimService;

/**
 * jOOQ specific implementation of the {@link PaperSlimService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperSlimService implements PaperSlimService {

    private static final long serialVersionUID = 1L;

    private PaperSlimRepository repo;

    @Autowired
    public void setRepository(PaperSlimRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<PaperSlim> findById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findByFilter(PaperFilter filter, Pageable pageable) {
        return repo.findByFilter(filter, pageable).getContent();
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(PaperFilter filter) {
        return repo.countByFilter(filter);
    }

    /** {@inhericDoc} */
    @Override
    public List<PaperSlim> findBySearchOrder(SearchOrder searchOrder) {
        return repo.findBySearchOrder(searchOrder);
    }

    /** {@inhericDoc} */
    @Override
    public Page<PaperSlim> findBySearchOrder(SearchOrder searchOrder, Pageable pageable) {
        return repo.findBySearchOrder(searchOrder, pageable);
    }

    /** {@inhericDoc} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return repo.countBySearchOrder(searchOrder);
    }
}
