package net.nearbyservices.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sun.javafx.collections.SetListenerHelper;

import net.nearbyservices.shared.ServiceDTO;

public class ServiceListItem extends Composite {
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);
	private ServiceDTO serviceDTO;

	interface Binder extends UiBinder<Widget, ServiceListItem> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Label authorLabel;
	@UiField
	Label subjectLabel;
	@UiField
	Label detailsLabel;
	@UiField
	Image image;

	// @UiField
	// Button button;

	public ServiceListItem(ServiceDTO serviceDTO) {
		initWidget(binder.createAndBindUi(this));
		this.serviceDTO = serviceDTO;

		// button.setText(author);
		authorLabel.setText(serviceDTO.getAutor());
		subjectLabel.setText(serviceDTO.getTitle());
		detailsLabel.setText(serviceDTO.getDetail());
		image.setUrl("/images/ava.png");
	}

}
