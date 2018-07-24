package net.nearbyservices.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.lf5.LogLevel;

import cern.colt.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.shared.ServiceDTO;

public class ServicesList extends Composite implements ServicesListI {
	Logger logger = Logger.getLogger(ServicesList.class.getName());
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);
	private int startIndex;
	private int count;
	private SelectionStrategy selectionStrategy;

	public interface Listener {
		void onItemSelected(ServiceDTO item);
	}

	interface Binder extends UiBinder<Widget, ServicesList> {
	}

	interface SelectionStyle extends CssResource {
		String selectedRow();
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	VerticalPanel servicesListVP;

	@UiField
	FlexTable header;

	private Listener listener;
	private List<ServiceDTO> serviceList;
	private NavBar navBar;

	public ServicesList() {
		logger.log(Level.INFO, "onLoad");
		initWidget(binder.createAndBindUi(this));
		navBar = new NavBar(this);
		initHeader();
	}

	private void initHeader() {
		header.setHTML(0, 0, "<b>Services</b>");
		header.setWidget(0, 1, navBar);
		header.getFlexCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);

	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {
		logger.log(Level.INFO, "onLoad");
	}

	@Override
	protected void onAttach() {
		logger.log(Level.INFO, "onAttach");

		super.onAttach();
	}

	void update() {
		if (serviceList == null) {
			return;
		}
		servicesListVP.clear();
		for (final ServiceDTO serviceDTO : serviceList) {

			FocusPanel clickBox = new FocusPanel();
			clickBox.add(new ServiceListItem(serviceDTO));
			clickBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					logger.log(Level.INFO, "clickOn " + serviceDTO.getAutor());
					if (listener != null) {

						listener.onItemSelected(serviceDTO);
					}
				}
			});
			servicesListVP.add(clickBox);
			// servicesListVP.add(new Label(serviceDTO.getTitle()));
		}
	}

	abstract class SelectionStrategy {

		abstract void ids(AsyncCallback<List<Long>> asyncCallback);

		abstract void selection(AsyncCallback<List<ServiceDTO>> asyncCallback);

		int max;

		protected void computeNav(List<Long> result) {
			logger.log(Level.INFO, "selection size " + result.size());
			count = result.size();
			max = startIndex + VISIBLE_SERVICES_COUNT;
			if (max > count) {
				max = count;
			}
			logger.log(Level.INFO, "selection update count = " + count
					+ " startIndex = " + startIndex + " max = " + max);
			navBar.update(startIndex, count, max);

		}

	}

	class WithTitle extends SelectionStrategy {
		String item;

		public WithTitle(String item) {
			WithTitle.this.item = item;
		}

		@Override
		void selection(final AsyncCallback<List<ServiceDTO>> asyncCallback) {
			ids(new AsyncCallback<List<Long>>() {

				@Override
				public void onSuccess(List<Long> result) {
					computeNav(result);
					itemService.findWithTitle(item, startIndex, max
							- startIndex, asyncCallback);
				}

				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, caught.getMessage(), caught);
				}
			});

		}

		@Override
		void ids(AsyncCallback<List<Long>> asyncCallback) {
			itemService.selectIdsWithTitle(item, asyncCallback);

		}

	}

	class AllEntries extends SelectionStrategy {

		@Override
		void selection(final AsyncCallback<List<ServiceDTO>> asyncCallback) {
			ids(new AsyncCallback<List<Long>>() {

				@Override
				public void onSuccess(List<Long> result) {
					computeNav(result);
					itemService.findAllEntries(startIndex, max - startIndex,
							asyncCallback);
				}

				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, caught.getMessage(), caught);
				}
			});

		}

		@Override
		void ids(AsyncCallback<List<Long>> asyncCallback) {
			itemService.selectIds(asyncCallback);
		}

	}

	public void refresh(String item) {
		startIndex = 0;
		selectionStrategy = item == null ? new AllEntries() : new WithTitle(
				item);
		doUpdate();
	}

	private void doUpdate() {
		logger.log(Level.INFO, "update");

		AsyncCallback<List<ServiceDTO>> asyncCallback = new AsyncCallback<List<ServiceDTO>>() {

			@Override
			public void onSuccess(List<ServiceDTO> result) {
				logger.log(Level.INFO, "doUpdate onSuccess " + (result == null));
				if (result != null) {
					logger.log(Level.INFO, "result size " + result.size());
				}
				serviceList = result;
				update();
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage(), caught);
			}
		};

		selectionStrategy.selection(asyncCallback);

	}

	@Override
	public String toString() {
		return "ServicesList []";
	}

	@Override
	public void newer() {
		startIndex -= VISIBLE_SERVICES_COUNT;
		if (startIndex < 0) {
			startIndex = 0;
		}
		doUpdate();
	}

	@Override
	public void older() {
		startIndex += VISIBLE_SERVICES_COUNT;
		if (startIndex >= count) {
			startIndex -= VISIBLE_SERVICES_COUNT;
		}
		doUpdate();
	}

}
