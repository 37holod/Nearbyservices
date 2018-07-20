package net.nearbyservices.client;

import net.nearbyservices.shared.ServiceDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ServiceDetail extends Composite {

	interface Binder extends UiBinder<Widget, ServiceDetail> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Element subject;
	@UiField
	HTML body;


	public ServiceDetail() {
		initWidget(binder.createAndBindUi(this));
	}

	public void setItem(ServiceDTO item) {
		body.setHTML(item.getDetail());
	}
}
