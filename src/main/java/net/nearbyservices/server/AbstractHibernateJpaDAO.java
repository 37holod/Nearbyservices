package net.nearbyservices.server;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.nearbyservices.client.ServicesList;

public abstract class AbstractHibernateJpaDAO<K, E> {
	Logger logger = Logger.getLogger(AbstractHibernateJpaDAO.class.getName());
	protected Class<E> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractHibernateJpaDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
	}

	public void persist(E entity) {
		getEntityManager().persist(entity);
	}

	public void remove(E entity) {
		getEntityManager().remove(entity);
	}

	public void refresh(E entity) {
		getEntityManager().refresh(entity);
	}

	public E merge(E entity) {
		return getEntityManager().merge(entity);
	}

	public E findById(K id) {
		return getEntityManager().find(entityClass, id);
	}

	public E flush(E entity) {
		getEntityManager().flush();
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		logger.log(Level.INFO, "findAll ");
		return findAll(0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll(int start, int end) {
		logger.log(Level.INFO, "findAll start " + start + " end " + end);
		String queryStr = "SELECT h FROM " + entityClass.getName() + " h";
		Query query = getEntityManager().createQuery(queryStr, entityClass);
		if (end != 0) {
			query.setFirstResult(start);
			query.setMaxResults(end);
		}
		List<E> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<E> findWithTitle(String title) {
		return findWithTitle(title, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<E> findWithTitle(String title, int start, int end) {
		String queryStr = "SELECT h FROM " + entityClass.getName()
				+ " h WHERE h.title = :tit";
		Query query = getEntityManager().createQuery(queryStr, entityClass)
				.setParameter("tit", title);
		if (end != 0) {
			query.setFirstResult(start);
			query.setMaxResults(end);
		}
		List<E> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<String> selectTitles() {
		String queryStr = "SELECT DISTINCT h.title FROM "
				+ entityClass.getName() + " h";
		Query query = getEntityManager().createQuery(queryStr);
		List<String> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> selectIds() {
		String queryStr = "SELECT h.id FROM " + entityClass.getName() + " h";
		Query query = getEntityManager().createQuery(queryStr);
		List<Long> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> selectIdsWithTitle(String title) {
		List<Long> resultList = null;
		try {
			logger.log(Level.INFO, "selectIdsWithTitle " + title);
			String queryStr = "SELECT h.id FROM " + entityClass.getName()
					+ " h WHERE h.title = :tit";
			Query query = getEntityManager().createQuery(queryStr, entityClass)
					.setParameter("tit", title);
			resultList = query.getResultList();
			logger.log(Level.INFO, "resultList size " + resultList.size());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return resultList;
	}

	public Integer removeAll() {
		String queryStr = "DELETE FROM " + entityClass.getName() + " h";
		Query query = getEntityManager().createQuery(queryStr);
		return query.executeUpdate();
	}

	protected abstract EntityManager getEntityManager();
}
