package net.nearbyservices.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
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
	ListBox shortcutsCatLb;

	private Listener listener;
	private List<TitleItem> titles;

	private abstract class TitleItem {
		protected String title;

		public TitleItem(String title) {
			this.title = title;
		}

		abstract String getTitle();

		abstract String getValue();
	}

	private class All extends TitleItem {

		public All(String title) {
			super(title);
			// TODO Auto-generated constructor stub
		}

		public String getTitle() {
			return title;
		}

		public String getValue() {
			return null;
		}

	}

	private class FilterTitle extends TitleItem {

		public FilterTitle(String title) {
			super(title);
			// TODO Auto-generated constructor stub
		}

		public String getTitle() {
			return title;
		}

		public String getValue() {
			return title;
		}
	}

	public Shortcuts() {
		initWidget(binder.createAndBindUi(this));

		fetchTitles();
		titles = new ArrayList<TitleItem>();

	}

	@UiHandler("shortcutsCatLb")
	void onChange(ChangeEvent event) {
		if (listener != null) {
			listener.onItemSelected(titles.get(shortcutsCatLb.getSelectedIndex()).getValue());
		}
	}

	public void fetchTitles() {
		itemService.selectTitles(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				titles.clear();
				titles.add(new All("all"));
				for (String title : result) {
					titles.add(new FilterTitle(title));
				}
				update();
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});

	}

	protected void update() {
		shortcutsCatLb.clear();
		for (TitleItem titleItem : titles) {
			shortcutsCatLb.addItem(titleItem.getTitle());
		}
		if (listener != null) {
			listener.onItemSelected(titles.get(shortcutsCatLb.getSelectedIndex()).getValue());
		}
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {

	}

	/**
	 * Selects the given row (relative to the current page).
	 *
	 * @param row
	 *            the row to be selected
	 */

}
