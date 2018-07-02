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

public class Shortcuts extends Composite {
	private final ItemServiceAsync itemService = GWT.create(ItemService.class);

	public interface Listener {
		void onItemSelected(String item);
	}

	interface Binder extends UiBinder<Widget, Shortcuts> {
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
	private List<String> titles;

	public Shortcuts() {
		initWidget(binder.createAndBindUi(this));

		initTable();
		fetchTitles();

	}

	public void fetchTitles() {
		itemService.selectTitles(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				titles = result;
				update();
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});

	}


	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {

	}

	@UiHandler("table")
	void onTableClicked(ClickEvent event) {
		// Select the row that was clicked (-1 to account for header row).
		Cell cell = table.getCellForEvent(event);
		if (cell != null) {
			int row = cell.getRowIndex();
			selectRow(row);
		}
	}


	private void initTable() {


		header.setText(0, 0, "Filters");
		

	}

	/**
	 * Selects the given row (relative to the current page).
	 *
	 * @param row
	 *            the row to be selected
	 */
	private void selectRow(int row) {
		if (titles == null) {
			return;
		}
		String item = titles.get(row);
		if (item == null) {
			return;
		}

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
		if (titles == null) {
			return;
		}
		table.clear(true);
		for (int i = 0; i < titles.size(); ++i) {

			String item = titles.get(i);

			table.setText(i, 0, item);
		}
		if (selectedRow == -1) {
			selectRow(0);
		}
		listener.onItemSelected(titles.get(selectedRow));
	}

}
