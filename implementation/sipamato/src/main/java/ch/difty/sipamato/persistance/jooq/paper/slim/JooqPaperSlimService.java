package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyService;
import ch.difty.sipamato.persistance.jooq.Page;
import ch.difty.sipamato.persistance.jooq.Pageable;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.service.PaperSlimService;

/**
 * jOOQ specific implementation of the {@link PaperSlimService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperSlimService extends JooqReadOnlyService<Long, PaperSlim, PaperFilter, PaperSlimRepository> implements PaperSlimService {

    private static final long serialVersionUID = 1L;

    /** {@inhericDoc} */
    @Override
    public List<PaperSlim> findBySearchOrder(SearchOrder searchOrder) {
        return getRepository().findBySearchOrder(searchOrder);
    }

    /** {@inhericDoc} */
    @Override
    public Page<PaperSlim> findBySearchOrder(SearchOrder searchOrder, Pageable pageable) {
        return getRepository().findBySearchOrder(searchOrder, pageable);
    }

    /** {@inhericDoc} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }
}
