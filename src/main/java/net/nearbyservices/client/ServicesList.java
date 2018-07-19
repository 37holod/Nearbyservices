package net.nearbyservices.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Widget;

import net.nearbyservices.shared.ServiceDTO;

public class ServicesList extends Composite {
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);

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
	FlexTable header;
	@UiField
	FlexTable table;
	@UiField
	SelectionStyle selectionStyle;

	private Listener listener;
	private int startIndex, selectedRow = -1;
	private List<ServiceDTO> serviceList;

	public ServicesList() {
		initWidget(binder.createAndBindUi(this));

		initTable();

	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {

	}

	@UiHandler("table")
	void onTableClicked(ClickEvent event) {
		Cell cell = table.getCellForEvent(event);
		if (cell != null) {
			int row = cell.getRowIndex();
			selectRow(row);
		}
	}

	/**
	 * Initializes the table so that it contains enough rows for a full page of
	 * emails. Also creates the images that will be used as 'read' flags.
	 */
	private void initTable() {
		header.getColumnFormatter().setWidth(0, "128px");

		header.setText(0, 0, "Author");
		header.setText(0, 1, "Subject");

		table.getColumnFormatter().setWidth(0, "128px");
	}

	private void selectRow(int row) {
		if (serviceList == null) {
			return;
		}
		ServiceDTO item = serviceList.get(row);

		styleRow(selectedRow, false);
		styleRow(row, true);

		selectedRow = row;

		if (listener != null) {
			listener.onItemSelected(item);
		}
	}

	private void styleRow(int row, boolean selected) {
		if (row != -1) {
			String style = selectionStyle.selectedRow();

			if (selected) {
				table.getRowFormatter().addStyleName(row, style);
			} else {
				table.getRowFormatter().removeStyleName(row, style);
			}
		}
	}

	private void update() {
		if (serviceList == null) {
			return;
		}
		table.clear(true);
		for (int i = 0; i < serviceList.size(); ++i) {

			ServiceDTO item = serviceList.get(i);

			table.setText(i, 0, item.getAutor());
			table.setText(i, 1, item.getTitle());
		}
		if (selectedRow == -1) {
			selectRow(0);
		}
		listener.onItemSelected(serviceList.get(selectedRow));
	}

	public void update(String item) {
		AsyncCallback<List<ServiceDTO>> asyncCallback = new AsyncCallback<List<ServiceDTO>>() {

			@Override
			public void onSuccess(List<ServiceDTO> result) {
				serviceList = result;
				update();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};
		if (item != null) {
			itemService.findWithTitle(item, asyncCallback);
		} else {
			itemService.findAllEntries(asyncCallback);
		}
	}

}
