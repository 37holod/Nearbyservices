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
	private ServiceDTO item;

	interface Binder extends UiBinder<Widget, ServiceDetail> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Element subject;
	@UiField
	HTML body;

	public ServiceDetail(ServiceDTO item) {
		this.item = item;
		initWidget(binder.createAndBindUi(this));
		body.setHTML(item.getDetail());
	}

	@Override
	public String toString() {
		return "ServiceDetail/" + item.getId();
	}

}
