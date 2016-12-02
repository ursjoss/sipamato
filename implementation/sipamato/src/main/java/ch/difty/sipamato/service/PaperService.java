package ch.difty.sipamato.service;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service methods.
 *
 * @author u.joss
 */
public interface PaperService extends EntityService<Paper, PaperFilter> {

}
