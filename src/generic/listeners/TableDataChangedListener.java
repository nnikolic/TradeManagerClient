package generic.listeners;

import generic.events.TableDataChangedEvent;

public interface TableDataChangedListener {
	public void updateTable(TableDataChangedEvent tbce);
}
