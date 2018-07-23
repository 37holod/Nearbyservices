package net.nearbyservices.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import net.nearbyservices.shared.ServiceDTO;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("springGwtServices/itemService")
public interface ItemService extends RemoteService {

	public void saveOrUpdate(ServiceDTO item) throws Exception;

	public void delete(ServiceDTO item) throws Exception;

	public ServiceDTO find(long id);

	public List<ServiceDTO> findAllEntries(int start, int end);

	public List<ServiceDTO> findAllEntries();

	public List<String> selectTitles();

	public List<Long> selectIds();

	public List<Long> selectIdsWithTitle(String title);

	public List<ServiceDTO> findWithTitle(String title);

	public List<ServiceDTO> findWithTitle(String title, int start, int end);

}