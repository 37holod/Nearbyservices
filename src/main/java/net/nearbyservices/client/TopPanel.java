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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.client.Nearbyservices.CompositeListener;
import net.nearbyservices.shared.ServiceDTO;
import net.nearbyservices.shared.Validator;

public class TopPanel extends Composite {
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	private CompositeListener compositeListener;

	interface Binder extends UiBinder<Widget, TopPanel> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Anchor signInLink;
	@UiField
	Anchor addServiceLink;

	public void setCompositeListener(CompositeListener compositeListener) {
		this.compositeListener = compositeListener;
	}

	public TopPanel() {
		initWidget(binder.createAndBindUi(this));
	}

	@UiHandler("addServiceLink")
	void onAddServiceClicked(ClickEvent event) {
		showDialog();
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

	private void showDialog() {
		DialogBox dialogBox = new DialogBox();
		FlexTable table = new FlexTable();
		TextBox textBoxNickName = new TextBox();
		TextBox textBoxSubject = new TextBox();
		TextBox textBoxDetail = new TextBox();
		Button buttonAdd = new Button("Add");
		Button buttonClose = new Button("Close");
		HTML serverResponseLabel = new HTML();
		table.getFlexCellFormatter().setColSpan(0, 0, 2);
		table.setWidget(1, 0, new Label("Name"));
		table.setWidget(1, 1, textBoxNickName);
		table.setWidget(2, 0, new Label("Subject"));
		table.setWidget(2, 1, textBoxSubject);
		table.setWidget(3, 0, new Label("Detail"));
		table.setWidget(3, 1, textBoxDetail);
		table.setWidget(4, 0, buttonClose);
		table.setWidget(4, 1, buttonAdd);
		table.setWidget(5, 0, serverResponseLabel);
		table.getCellFormatter().setHorizontalAlignment(4, 1, HorizontalPanel.ALIGN_RIGHT);

		dialogBox.setText("New service");
		dialogBox.setModal(true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setWidget(table);

		dialogBox.center();
		dialogBox.show();
		buttonClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();

			}
		});
		buttonAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveBook(textBoxNickName.getText(), textBoxSubject.getText(), textBoxDetail.getText());
			}

			private void saveBook(String name, String subject, String detail) {

				if (Validator.isBlank(name) || Validator.isBlank(subject) || Validator.isBlank(detail)) {
					return;
				}

				ServiceDTO bookDTO = new ServiceDTO(null, subject, detail, name);

				itemService.saveOrUpdate(bookDTO, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(caught.toString());
						dialogBox.center();

					}

					public void onSuccess(Void noAnswer) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("OK");
						dialogBox.center();
						compositeListener.updateAll();
					}
				});
			}
		});
	}

}
