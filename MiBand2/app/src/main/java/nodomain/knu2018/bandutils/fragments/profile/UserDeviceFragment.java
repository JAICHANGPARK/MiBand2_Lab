package nodomain.knu2018.bandutils.fragments.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.profile.UserDeviceAdapter;
import nodomain.knu2018.bandutils.devices.DeviceManager;
import nodomain.knu2018.bandutils.impl.GBDevice;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDeviceFragment.OnFragmentInteractionListener} interface

 * create an instance of this fragment.
 *
 *
 * 연결한 사용자 장비를 가져오는 프레그먼트
 * @author Dreamwalker(박제창)
 * @date : 2018-06-22
 *
 *
 */
public class UserDeviceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "UserDeviceFragment";

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private DeviceManager deviceManager;
    private UserDeviceAdapter adapter;

    public UserDeviceFragment() {
        // Required empty public constructor
    }


//    // TODO: Rename and change types and number of parameters
//    public static UserDeviceFragment newInstance(String param1, String param2) {
//        UserDeviceFragment fragment = new UserDeviceFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_device, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deviceManager = ((GBApplication) Objects.requireNonNull(getActivity()).getApplication()).getDeviceManager();
        List<GBDevice> deviceList = deviceManager.getDevices();
        adapter = new UserDeviceAdapter(getActivity(), deviceList);

        // TODO: 2018-06-22 연결된 디바이스 디버그좀 합시다.. - 박제창
        for (GBDevice device: deviceList) {
            Log.e(TAG, "액세서리 Fragment device 가져오기 " + device.getAddress());
        }

        recyclerView.setAdapter(this.adapter);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
