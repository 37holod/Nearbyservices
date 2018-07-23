package net.nearbyservices.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopPanel extends Composite {
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	private Listener listener;

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public interface Listener {
		void onAddServiceClicked();
	}

	interface Binder extends UiBinder<Widget, TopPanel> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Anchor addServiceLink;

	public TopPanel() {
		initWidget(binder.createAndBindUi(this));
	}

	@UiHandler("addServiceLink")
	void onAddServiceClicked(ClickEvent event) {
		// showDialog();
		listener.onAddServiceClicked();
	}

}
