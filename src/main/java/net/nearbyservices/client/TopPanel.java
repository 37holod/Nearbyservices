package net.nearbyservices.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.client.Nearbyservices.CompositeListener;
import net.nearbyservices.shared.ServiceDTO;

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
	Anchor signInLink;
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

	@UiHandler("signInLink")
	void onSignInClicked(ClickEvent event) {
		retrieveAllEntries();
	}

	private void retrieveAllEntries() {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending request to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();

			}
		});

		itemService.findAllEntries(

				new AsyncCallback<List<ServiceDTO>>() {

					public void onFailure(Throwable caught) {
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(caught.toString());
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(List<ServiceDTO> data) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");

						if (data != null && !data.isEmpty()) {
							StringBuffer buffer = new StringBuffer();
							for (ServiceDTO book : data) {
								buffer.append(book.toString());
								buffer.append("<br /><br />");
							}
							serverResponseLabel.setHTML(buffer.toString());
						} else {
							serverResponseLabel.setHTML("No book information store in the database.");
						}
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
	}

}
