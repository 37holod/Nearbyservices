package net.nearbyservices.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.shared.ServiceDTO;

public class ServicesList extends Composite {
	Logger logger = Logger.getLogger(ServicesList.class.getName());
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);

	public interface Listener {
		void onItemSelected(ServiceDTO item);
	}

	interface Binder extends UiBinder<Widget, ServicesList> {
	}

	interface SelectionStyle extends CssResource {
		String selectedRow();
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	VerticalPanel servicesListVP;

	private Listener listener;
	private List<ServiceDTO> serviceList;

	public ServicesList() {
		logger.log(Level.INFO, "onLoad");
		initWidget(binder.createAndBindUi(this));

	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {
		logger.log(Level.INFO, "onLoad");
	}

	@Override
	protected void onAttach() {
		logger.log(Level.INFO, "onAttach");

		super.onAttach();
	}

	void update() {
		if (serviceList == null) {
			return;
		}
		servicesListVP.clear();
		for (ServiceDTO serviceDTO : serviceList) {

			FocusPanel clickBox = new FocusPanel();
			clickBox.add(new ServiceListItem(serviceDTO));
			clickBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					logger.log(Level.INFO, "clickOn " + serviceDTO.getAutor());
					if (listener != null) {

						listener.onItemSelected(serviceDTO);
					}
				}
			});
			servicesListVP.add(clickBox);
			// servicesListVP.add(new Label(serviceDTO.getTitle()));
		}
	}

	public void update(String item) {
		logger.log(Level.INFO, "update");
		AsyncCallback<List<ServiceDTO>> asyncCallback = new AsyncCallback<List<ServiceDTO>>() {

			@Override
			public void onSuccess(List<ServiceDTO> result) {

				serviceList = result;
				update();
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage(), caught);
			}
		};
		if (item != null) {
			itemService.findWithTitle(item, asyncCallback);
		} else {
			itemService.findAllEntries(asyncCallback);
		}
	}

	@Override
	public String toString() {
		return "ServicesList []";
	}
	
	

}
