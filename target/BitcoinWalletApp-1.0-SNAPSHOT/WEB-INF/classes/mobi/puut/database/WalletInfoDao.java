package mobi.puut.database;

import mobi.puut.entities.WalletInfo;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by Chaklader on 6/24/17.
 */
@Repository
public class WalletInfoDao {

    // provide a logger for the class
    private final Logger loggger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(rollbackFor = Exception.class)
    public List<WalletInfo> getAllWallets() {

        return sessionFactory.getCurrentSession()
                .createQuery("from WalletInfo").getResultList();
    }

    @Transactional(rollbackFor = Exception.class)
    public WalletInfo getByName(String walletName) {
        List<WalletInfo> walletInfos = sessionFactory.getCurrentSession().createQuery("from WalletInfo where name = :name")
                .setParameter("name", walletName).getResultList();

        return Objects.isNull(walletInfos) || walletInfos.isEmpty()
                ? null : walletInfos.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public WalletInfo getById(final Long id) {
        return sessionFactory.getCurrentSession().get(WalletInfo.class, id);
    }

    /**
     * @param name    name of the wallet
     * @param address address of the wallet
     * @return return the created WalletInfo object with provided name and address
     */
    @Transactional(rollbackFor = Exception.class)
    public WalletInfo create(String name, String currency, String address) {

        // create the WalletInfo entity with provided name and address
        WalletInfo walletInfo = new WalletInfo();
        walletInfo.setAddress(address);
        walletInfo.setName(name);
        walletInfo.setCurrency(currency);

        // persist the created instance into the database
        sessionFactory.getCurrentSession().persist(walletInfo);
        return walletInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteWalletInfoById(Long theId) {
        sessionFactory.getCurrentSession().createQuery("delete WalletInfo where id = :id")
                .setParameter("id", theId).executeUpdate();
    }

    @Transactional(rollbackFor = Exception.class)
    public WalletInfo getWalletInfoWithWalletNameAndCurrency(String walletName, String currencyName) {

        loggger.info("\n\nDatabase: the wallet name {} and the currency name {} \n\n", walletName, currencyName);

        List<WalletInfo> walletInfos = sessionFactory.getCurrentSession()
                .createQuery("from WalletInfo where name = :name and currency = :currency")
                .setParameter("name", walletName)
                .setParameter("currency", currencyName).getResultList();

        return Objects.isNull(walletInfos) || walletInfos.isEmpty() ?
                null : walletInfos.get(0);
    }
}