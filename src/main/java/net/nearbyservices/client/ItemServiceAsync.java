package net.nearbyservices.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.nearbyservices.shared.ServiceDTO;

public interface ItemServiceAsync {

	void delete(ServiceDTO item, AsyncCallback<Void> callback);

	void find(long id, AsyncCallback<ServiceDTO> callback);

	void findAllEntries(AsyncCallback<List<ServiceDTO>> callback);

	void findWithTitle(String title, AsyncCallback<List<ServiceDTO>> callback);

	void saveOrUpdate(ServiceDTO item, AsyncCallback<Void> callback);

	void selectIds(AsyncCallback<List<Long>> callback);

	void selectTitles(AsyncCallback<List<String>> callback);

}
