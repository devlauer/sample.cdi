package de.elnarion.sample.cdi.support;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.arjuna.ats.jta.common.jtaPropertyManager;

public class TransactionServices implements org.jboss.weld.transaction.spi.TransactionServices {

    @Override
    public void cleanup() {
    }

    @Override
    public void registerSynchronization(Synchronization synchronizedObserver) {
        jtaPropertyManager.getJTAEnvironmentBean()
            .getTransactionSynchronizationRegistry()
            .registerInterposedSynchronization(synchronizedObserver);
    }

    @Override
    public boolean isTransactionActive() {
        try {
            return com.arjuna.ats.jta.UserTransaction.userTransaction().getStatus() == Status.STATUS_ACTIVE;
        }
        catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserTransaction getUserTransaction() {
        return com.arjuna.ats.jta.UserTransaction.userTransaction();
    }
}