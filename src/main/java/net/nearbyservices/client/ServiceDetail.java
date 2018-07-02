package net.nearbyservices.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.shared.ServiceDTO;

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
