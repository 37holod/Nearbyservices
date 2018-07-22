package net.nearbyservices.client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.client.TopPanel.Listener;
import net.nearbyservices.shared.ServiceDTO;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Nearbyservices implements EntryPoint {
	Logger logger = Logger.getLogger(Nearbyservices.class.getName());

	interface CompositeListener {
		void updateAll();
	}

	interface Binder extends UiBinder<DockLayoutPanel, Nearbyservices> {
	}

	interface GlobalResources extends ClientBundle {
		@Source("global.gss")
		CssResource css();
	}

	private Map<String, Composite> history;
	@UiField
	TopPanel topPanel;
	@UiField
	Shortcuts shortcuts;
	@UiField
	SimplePanel centerContainer;

	private ServicesList servicesList;

	private static final Binder binder = GWT.create(Binder.class);

	public void onModuleLoad() {
		GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();
		DockLayoutPanel outer = binder.createAndBindUi(this);
		Window.enableScrolling(false);
		Window.setMargin("0px");
		Element topElem = outer.getWidgetContainerElement(topPanel);
		topElem.getStyle().setZIndex(2);
		topElem.getStyle().setOverflow(Overflow.VISIBLE);
		servicesList = new ServicesList();
		AddService addService = new AddService();
		history = new HashMap<>();
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(outer);
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				logger.log(Level.INFO, "historyToken " + historyToken);
				try {
					if (history.containsKey(historyToken)) {
						replaceWidget(history.get(historyToken));
					} else {
						replaceWidget(servicesList);
						servicesList.update(null);
					}

				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}

		});

		shortcuts.setListener(new Shortcuts.Listener() {

			@Override
			public void onItemSelected(String item) {
				logger.log(Level.INFO, "shortcuts onItemSelected");
				String key = servicesList.toString();
				history.put(key, servicesList);
				History.newItem(key);
				servicesList.update(item);
			}
		});
		servicesList.setListener(new ServicesList.Listener() {

			@Override
			public void onItemSelected(ServiceDTO item) {
				logger.log(Level.INFO, "servicesList onItemSelected");
				ServiceDetail serviceDetail = new ServiceDetail(item);
				String key = serviceDetail.toString();
				history.put(key, serviceDetail);
				History.newItem(key);
			}
		});
		topPanel.setListener(new Listener() {

			@Override
			public void onAddServiceClicked() {

				String key = addService.toString();
				history.put(key, addService);
				History.newItem(key);

			}
		});
		addService.setListener(new AddService.Listener() {

			@Override
			public void updateAll() {
				shortcuts.fetchTitles();
			}
		});
		replaceWidget(servicesList);
	}

	private void replaceWidget(Widget widget) {
		centerContainer.clear();
		centerContainer.setWidget(widget);
	}

}
