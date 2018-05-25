package nodomain.knu2018.bandutils.adapter;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.database.DBHandler;
import nodomain.knu2018.bandutils.database.DBHelper;
import nodomain.knu2018.bandutils.entities.BaseActivitySummary;
import nodomain.knu2018.bandutils.entities.BaseActivitySummaryDao;
import nodomain.knu2018.bandutils.entities.Device;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.model.ActivityKind;
import nodomain.knu2018.bandutils.util.DateTimeUtils;
import nodomain.knu2018.bandutils.util.GB;



public class ActivitySummariesAdapter extends AbstractItemAdapter<BaseActivitySummary> {
    private final GBDevice device;

    public ActivitySummariesAdapter(Context context, GBDevice device) {
        super(context);
        this.device = device;
        loadItems();
    }

    @Override
    public void loadItems() {
        try (DBHandler handler = GBApplication.acquireDB()) {
            BaseActivitySummaryDao summaryDao = handler.getDaoSession().getBaseActivitySummaryDao();
            Device dbDevice = DBHelper.findDevice(device, handler.getDaoSession());

            QueryBuilder<BaseActivitySummary> qb = summaryDao.queryBuilder();
            qb.where(BaseActivitySummaryDao.Properties.DeviceId.eq(dbDevice.getId())).orderDesc(BaseActivitySummaryDao.Properties.StartTime);
            List<BaseActivitySummary> allSummaries = qb.build().list();
            setItems(allSummaries, true);
        } catch (Exception e) {
            GB.toast("Error loading activity summaries.", Toast.LENGTH_SHORT, GB.ERROR, e);
        }
    }

    @Override
    protected String getName(BaseActivitySummary item) {
        String name = item.getName();
        if (name != null && name.length() > 0) {
            return name;
        }

        Date startTime = item.getStartTime();
        if (startTime != null) {
            return DateTimeUtils.formatDateTime(startTime);
        }
        return "Unknown activity";
    }

    @Override
    protected String getDetails(BaseActivitySummary item) {
        return ActivityKind.asString(item.getActivityKind(), getContext());
    }

    @Override
    protected int getIcon(BaseActivitySummary item) {
        return ActivityKind.getIconId(item.getActivityKind());
    }
}
