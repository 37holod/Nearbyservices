package net.nearbyservices.server;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import net.nearbyservices.shared.ServiceDTO;

@Repository("serviceDAO")
public class ServiceDAO extends AbstractHibernateJpaDAO<Long, ServiceDTO> {

	@PersistenceContext(unitName = "MyPUnit")
	EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}