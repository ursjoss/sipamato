package ch.difty.scipamato.persistance.jooq.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.service.UserService;

/**
 * jOOQ specific implementation of the {@link UserService} interface.
 *
 * Note: This service is deliberately not extending  JooqEntityService as that depending on on this service itself. 
 *
 * @author u.joss
 */
@Service
public class JooqUserService implements UserService {

    private static final long serialVersionUID = 1L;

    private UserRepository repo;

    @Autowired
    public void setRepository(UserRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findPageByFilter(UserFilter filter, PaginationContext paginationContext) {
        return repo.findPageByFilter(filter, paginationContext);
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(UserFilter filter) {
        return repo.countByFilter(filter);
    }

    /** {@inheritDoc} */
    @Override
    public User saveOrUpdate(User user) {
        if (user.getId() == null) {
            return repo.add(user);
        } else {
            return repo.update(user);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Optional<User> findByUserName(String userName) {
        if (userName == null)
            return Optional.empty();
        return Optional.ofNullable(repo.findByUserName(userName));
    }

    /** {@inheritDoc} */
    @Override
    public void remove(User entity) {
        if (entity != null && entity.getId() != null) {
            repo.delete(entity.getId());
        }
    }

    @Override
    public List<Integer> findPageOfIdsByFilter(final UserFilter filter, final PaginationContext paginationContext) {
        return repo.findPageOfIdsByFilter(filter, paginationContext);
    }

}