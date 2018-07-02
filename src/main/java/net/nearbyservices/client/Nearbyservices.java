package net.nearbyservices.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import net.nearbyservices.shared.ServiceDTO;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Nearbyservices implements EntryPoint {
	interface CompositeListener {
		void updateAll();
	}

	interface Binder extends UiBinder<DockLayoutPanel, Nearbyservices> {
	}

	interface GlobalResources extends ClientBundle {
		@Source("global.gss")
		CssResource css();
	}

	@UiField
	TopPanel topPanel;
	@UiField
	ServicesList servicesList;
	@UiField
	ServiceDetail serviceDetail;
	@UiField
	Shortcuts shortcuts;

	private static final Binder binder = GWT.create(Binder.class);

	public void onModuleLoad() {
		GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();
		DockLayoutPanel outer = binder.createAndBindUi(this);
		Window.enableScrolling(false);
		Window.setMargin("0px");
		Element topElem = outer.getWidgetContainerElement(topPanel);
		topElem.getStyle().setZIndex(2);
		topElem.getStyle().setOverflow(Overflow.VISIBLE);
		shortcuts.setListener(new Shortcuts.Listener() {

			@Override
			public void onItemSelected(String item) {
				servicesList.update(item);
			}
		});
		servicesList.setListener(new ServicesList.Listener() {

			@Override
			public void onItemSelected(ServiceDTO item) {
				serviceDetail.setItem(item);
			}
		});

		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(outer);
		topPanel.setCompositeListener(new CompositeListener() {

			@Override
			public void updateAll() {
				shortcuts.fetchTitles();
			}
		});
	}

}
