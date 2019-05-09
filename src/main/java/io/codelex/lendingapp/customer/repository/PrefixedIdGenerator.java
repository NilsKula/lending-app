package io.codelex.lendingapp.customer.repository;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class PrefixedIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
        return this.generateCustomId1() + "-" + this.generateCustomId2();
    }

    private String generateCustomId1() {
        Random random = new Random();
        int one = random.nextInt(10000);
        return String.format("%04d", one);
    }

    private String generateCustomId2() {
        Random random = new Random();
        int two = random.nextInt(10000);
        return String.format("%04d", two);
    }
}
