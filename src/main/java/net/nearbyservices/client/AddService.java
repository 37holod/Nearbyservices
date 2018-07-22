package net.nearbyservices.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.shared.ServiceDTO;
import net.nearbyservices.shared.Validator;

public class AddService extends Composite {
	Logger logger = Logger.getLogger(AddService.class.getName());
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);

	interface Binder extends UiBinder<Widget, AddService> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	private Listener listener;

	@UiField
	FlexTable addServicesFt;

	public interface Listener {
		void updateAll();
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public AddService() {
		initWidget(binder.createAndBindUi(this));
		final TextBox textBoxNickName = new TextBox();
		final TextBox textBoxSubject = new TextBox();
		final TextArea textBoxDetail = new TextArea();
		Button buttonAdd = new Button("Add");
		Button buttonAdd50 = new Button("Add50");
		final HTML serverResponseLabel = new HTML();
		addServicesFt.getFlexCellFormatter().setColSpan(0, 0, 2);
		addServicesFt.setWidget(1, 0, new Label("Name"));
		addServicesFt.setWidget(1, 1, textBoxNickName);
		addServicesFt.setWidget(2, 0, new Label("Subject"));
		addServicesFt.setWidget(2, 1, textBoxSubject);
		addServicesFt.setWidget(3, 0, new Label("Detail"));
		addServicesFt.setWidget(3, 1, textBoxDetail);
		addServicesFt.setWidget(4, 0, buttonAdd50);
		addServicesFt.setWidget(4, 1, buttonAdd);
		addServicesFt.setWidget(5, 1, serverResponseLabel);
		addServicesFt.getCellFormatter().setHorizontalAlignment(4, 1, HorizontalPanel.ALIGN_RIGHT);

		buttonAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveBook(textBoxNickName.getText(), textBoxSubject.getText(), textBoxDetail.getText(),
						new AsyncCallback<Void>() {
							public void onFailure(Throwable caught) {

								serverResponseLabel.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(caught.toString());

							}

							public void onSuccess(Void noAnswer) {

								serverResponseLabel.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML("OK");

								listener.updateAll();
							}
						});
			}

		});
		buttonAdd50.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				try {
					new RequestBuilder(RequestBuilder.GET, "/string/sometext.txt").sendRequest("",
							new RequestCallback() {
								@Override
								public void onResponseReceived(Request req, Response resp) {
									String text = resp.getText();
									final DialogBox dialogBox = new DialogBox();
									dialogBox.setText("Remote Procedure Call");
									dialogBox.show();
									save50(10, 0, text, new AsyncCallback<Void>() {

										@Override
										public void onFailure(Throwable caught) {
											dialogBox.hide();
										}

										@Override
										public void onSuccess(Void result) {
											dialogBox.hide();
											serverResponseLabel.removeStyleName("serverResponseLabelError");
											serverResponseLabel.setHTML("OK");

											listener.updateAll();
										}

									});
								}

								@Override
								public void onError(Request res, Throwable throwable) {
									logger.log(Level.SEVERE, throwable.getMessage(), throwable);
									serverResponseLabel.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(throwable.toString());
								}
							});
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}

			}

		});
	}

	private void save50(int number, int count, String detail, AsyncCallback<Void> asyncCallback) {
		saveBook("Name " + count, "Subject " + count, detail, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				next();
			}

			private void next() {
				if (count < number) {
					save50(number, count + 1, detail, asyncCallback);
				} else {
					asyncCallback.onSuccess(null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				next();
			}
		});
	}

	private void saveBook(String name, String subject, String detail, AsyncCallback<Void> asyncCallback) {

		if (Validator.isBlank(name) || Validator.isBlank(subject) || Validator.isBlank(detail)) {
			asyncCallback.onFailure(new Exception("Error"));
			return;
		}

		ServiceDTO bookDTO = new ServiceDTO(null, subject, detail, name);

		itemService.saveOrUpdate(bookDTO, asyncCallback);
	}

	@Override
	public String toString() {
		return "AddService";
	}

}
