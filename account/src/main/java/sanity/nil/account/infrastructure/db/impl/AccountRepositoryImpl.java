package sanity.nil.account.infrastructure.db.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;
import sanity.nil.account.application.interfaces.AccountRepository;
import sanity.nil.account.domain.entity.Account;
import sanity.nil.account.infrastructure.db.impl.mappers.AccountMapper;
import sanity.nil.account.infrastructure.db.model.AccountModel;
import sanity.nil.account.infrastructure.exceptions.AccountNotFoundException;

import java.util.UUID;

@RequiredArgsConstructor
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class AccountRepositoryImpl implements AccountRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Account getByID(UUID id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountModel> cq = builder.createQuery(AccountModel.class);
        Root<AccountModel> root = cq.from(AccountModel.class);
        Predicate idPredicate = builder.equal(root.get(AccountModel.FIELD_ID), id);
        cq.where(builder.and(idPredicate));
        AccountModel model = entityManager.createQuery(cq).getSingleResult();
        if (model == null) {
            throw new AccountNotFoundException(id);
        }
        return AccountMapper.convertModelToEntity(model);
    }


    @Override
    public void updateBalance(Account account) {
        AccountModel model = AccountMapper.convertEntityToModel(account);

        String jpql = "UPDATE AccountModel a " +
                "SET a.balance = :balance " +
                "WHERE a.id = :id " +
                "AND active = :active";

        entityManager.createQuery(jpql)
                .setParameter("balance", model.getBalance())
                .setParameter("id", model.getId())
                .setParameter("active", true)
                .executeUpdate();
    }

}
