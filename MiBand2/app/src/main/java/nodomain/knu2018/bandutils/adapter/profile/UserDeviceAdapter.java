package nodomain.knu2018.bandutils.adapter.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.devices.DeviceCoordinator;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.util.DeviceHelper;


/**
 * Adapter for displaying GBDevice instances.
 */
public class UserDeviceAdapter extends RecyclerView.Adapter<UserDeviceAdapter.UserDeviceViewHolder> {
    private static final String TAG = "GBDeviceAdapterv2";

    private final Context context;
    private List<GBDevice> deviceList;
    private int expandedDevicePosition = RecyclerView.NO_POSITION;
    private ViewGroup parent;

    public UserDeviceAdapter(Context context, List<GBDevice> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public UserDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_device, parent, false);
        return new UserDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDeviceViewHolder holder, int position) {
        final GBDevice device = deviceList.get(position);
        final DeviceCoordinator coordinator = DeviceHelper.getInstance().getCoordinator(device);

        holder.container.setOnClickListener(v -> {
            if (device.isInitialized() || device.isConnected()) {
                showTransientSnackbar(R.string.controlcenter_snackbar_need_longpress);
            } else {
                showTransientSnackbar(R.string.controlcenter_snackbar_connecting);
                GBApplication.deviceService().connect(device);
            }
        });

        holder.container.setOnLongClickListener(v -> {
            if (device.getState() != GBDevice.State.NOT_CONNECTED) {
                showTransientSnackbar(R.string.controlcenter_snackbar_disconnecting);
                GBApplication.deviceService().disconnect();
            }
            return true;
        });

        holder.deviceImageView.setImageResource(R.drawable.level_list_device);
        //level-list does not allow negative values, hence we always add 100 to the key.

        holder.deviceImageView.setImageLevel(device.getType().getKey() + 100 + (device.isInitialized() ? 100 : 0));

        holder.deviceNameLabel.setText(getUniqueDeviceName(device));
        holder.deviceAddressLabel.setText(getUniqueDeviceAddress(device));

//        if (device.isBusy()) {
//            holder.deviceAddressLabel.setText(device.getBusyTask());
//        } else {
//            holder.deviceAddressLabel.setText(device.getStateString());
//        }
    }


    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class UserDeviceViewHolder extends RecyclerView.ViewHolder {

        CardView container;

        ImageView deviceImageView;
        TextView deviceNameLabel;
        TextView deviceAddressLabel;


        UserDeviceViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.card_view);

            deviceImageView = view.findViewById(R.id.device_image);
            deviceNameLabel = view.findViewById(R.id.device_name);
            deviceAddressLabel = view.findViewById(R.id.device_address);

        }

    }

    private void justifyListViewHeightBasedOnChildren(ListView listView) {
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();

        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    private String getUniqueDeviceName(GBDevice device) {
        String deviceName = device.getName();
        if (!isUniqueDeviceName(device, deviceName)) {
            if (device.getModel() != null) {
                deviceName = deviceName + " " + device.getModel();
                if (!isUniqueDeviceName(device, deviceName)) {
                    deviceName = deviceName + " " + device.getShortAddress();
                }
            } else {
                deviceName = deviceName + " " + device.getShortAddress();
            }
        }
        return deviceName;
    }

    private String getUniqueDeviceAddress(GBDevice device) {
        String deviceName = device.getAddress();
        Log.e(TAG, "getUniqueDeviceAddress: " + device.getName() );
        Log.e(TAG, "getUniqueDeviceAddress: " + device.getModel() );
        Log.e(TAG, "getUniqueDeviceAddress: " + device.getShortAddress() );
//        if (!isUniqueDeviceName(device, deviceName)) {
//            if (device.getModel() != null) {
//                deviceName = deviceName + " " + device.getModel();
//                if (!isUniqueDeviceName(device, deviceName)) {
//                    deviceName = device.getShortAddress();
//                }
//            } else {
//                deviceName = device.getShortAddress();
//            }
//        }
        return deviceName;
    }

    private boolean isUniqueDeviceName(GBDevice device, String deviceName) {
        for (int i = 0; i < deviceList.size(); i++) {
            GBDevice item = deviceList.get(i);
            if (item == device) {
                continue;
            }
            if (deviceName.equals(item.getName())) {
                return false;
            }
        }
        return true;
    }

    private void showTransientSnackbar(int resource) {
        Snackbar snackbar = Snackbar.make(parent, resource, Snackbar.LENGTH_SHORT);

        //View snackbarView = snackbar.getView();

        // change snackbar text color
        //int snackbarTextId = android.support.design.R.id.snackbar_text;
        //TextView textView = snackbarView.findViewById(snackbarTextId);
        //textView.setTextColor();
        //snackbarView.setBackgroundColor(Color.MAGENTA);
        snackbar.show();
    }

}

