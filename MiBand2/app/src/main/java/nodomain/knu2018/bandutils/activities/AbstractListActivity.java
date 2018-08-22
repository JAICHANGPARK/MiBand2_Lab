package nodomain.knu2018.bandutils.activities;

import android.os.Bundle;
import android.widget.ListView;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.AbstractItemAdapter;


/**
 * The type Abstract list activity.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractListActivity<T> extends AbstractGBActivity {
    private AbstractItemAdapter<T> itemAdapter;
    private ListView itemListView;

    /**
     * Sets item adapter.
     *
     * @param itemAdapter the item adapter
     */
    public void setItemAdapter(AbstractItemAdapter<T> itemAdapter) {
        this.itemAdapter = itemAdapter;
        itemListView.setAdapter(itemAdapter);
    }

    /**
     * Refresh.
     */
    protected void refresh() {
        this.itemAdapter.loadItems();
    }

    /**
     * Gets item adapter.
     *
     * @return the item adapter
     */
    public AbstractItemAdapter<T> getItemAdapter() {
        return itemAdapter;
    }

    /**
     * Gets item list view.
     *
     * @return the item list view
     */
    public ListView getItemListView() {
        return itemListView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        itemListView = findViewById(R.id.itemListView);
    }
}
