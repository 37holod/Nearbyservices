package net.nearbyservices.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import net.nearbyservices.client.ItemService;
import net.nearbyservices.shared.ServiceDTO;
import net.nearbyservices.shared.Validator;

@Service("itemService")
public class ItemServiceImpl extends RemoteServiceServlet implements ItemService {

	private static final long serialVersionUID = -6547737229424190373L;

	private static final Log LOG = LogFactory.getLog(ItemServiceImpl.class);

	@Autowired
	private ServiceDAO serviceDAO;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveOrUpdate(ServiceDTO serviceDto) throws Exception {

		// Verify that the input is valid.
		if (Validator.isBlank(serviceDto.getTitle()) || Validator.isBlank(serviceDto.getAutor())) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Please enter at least the Title and the Autor of the book");
		}

		try {
			if (serviceDto.getId() == null) {
				serviceDAO.persist(serviceDto);
			} else {
				serviceDAO.merge(serviceDto);
			}
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(ServiceDTO book) throws Exception {
		if (book.getId() != null) {
			serviceDAO.remove(book);
		}
	}

	public ServiceDTO find(long id) {
		return serviceDAO.findById(id);
	}

	public List<String> selectTitles() {
		return serviceDAO.selectTitles();
	}

	public List<Long> selectIds() {
		return serviceDAO.selectIds();
	}

	public List<ServiceDTO> findWithTitle(String title) {
		return serviceDAO.findWithTitle(title);
	}

	public List<ServiceDTO> findAllEntries() {
		return serviceDAO.findAll();
	}

}
