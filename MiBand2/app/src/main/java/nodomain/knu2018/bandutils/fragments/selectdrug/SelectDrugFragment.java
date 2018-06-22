package nodomain.knu2018.bandutils.fragments.selectdrug;

import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpizarro.autolabel.library.AutoLabelUI;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.selectdrug.SelectDrugAdapter;
import nodomain.knu2018.bandutils.events.selectdrug.DrugDataEvent;
import nodomain.knu2018.bandutils.model.selectdrug.SelectDrug;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectDrugFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectDrugFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectDrugFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private final String KEY_INSTANCE_STATE_PEOPLE = "statePeople";
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private AutoLabelUI mAutoLabel;
    private List<SelectDrug> mPersonList;
    private SelectDrugAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<String> rrapid, rapid, neutral, longtime, mixed;

    EventBus bus = EventBus.getDefault();



    private OnFragmentInteractionListener mListener;

    public SelectDrugFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1
     * @return A new instance of fragment SelectDrugFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectDrugFragment newInstance(String param1) {
        SelectDrugFragment fragment = new SelectDrugFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAllArrayList();
    }

    private void setAllArrayList(){

        rrapid = new ArrayList<>();
        rapid = new ArrayList<>();
        longtime = new ArrayList<>();
        neutral = new ArrayList<>();
        mixed = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            rrapid.add(i, "unknown");
            rapid.add(i, "unknown");
            longtime.add(i, "unknown");
            neutral.add(i, "unknown");
            mixed.add(i, "unknown");
        }

        for (int i = 0; i < 10; i++) {
            mixed.add(i, "unknown");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_drug, container, false);
        findViews(view);
        setListeners();
        setRecyclerView();
        return view;
    }

    private void findViews(View view) {
        mAutoLabel = (AutoLabelUI) view.findViewById(R.id.label_view);
        mAutoLabel.setBackgroundResource(R.drawable.round_corner_background);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            List<SelectDrug> people = savedInstanceState.getParcelableArrayList(KEY_INSTANCE_STATE_PEOPLE);
            if (people != null) {
                mPersonList = people;
                adapter.setPeople(people);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void itemListClicked(int position) {
        SelectDrug person = mPersonList.get(position);
        boolean isSelected = person.isSelected();
        boolean success;
        int index = 0;
        if (isSelected) {
            if (mParam1.equals("1")) {
                rrapid.set(position, "unknown");
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 1));
            } else if (mParam1.equals("2")) {
                rapid.set(position, "unknown");
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 2));
            } else if (mParam1.equals("3")) {
                neutral.set(position, "unknown");
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 3));
            } else if (mParam1.equals("4")) {
                longtime.set(position, "unknown");
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 4));
            } else if (mParam1.equals("5")) {
                mixed.set(position, "unknown");
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 5));
            }

            Log.e(TAG, "itemListClicked: removeLabel " + person.getAge() + ", " + person.getName() + "position :" + position);
            success = mAutoLabel.removeLabel(position);
        } else {

            if (mParam1.equals("1")) {
                rrapid.set(position, person.getName());
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 1));
            } else if (mParam1.equals("2")) {
                rapid.set(position, person.getName());
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 2));
            } else if (mParam1.equals("3")) {
                neutral.set(position, person.getName());
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 3));
            } else if (mParam1.equals("4")) {
                longtime.set(position, person.getName());
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 4));
            } else if (mParam1.equals("5")) {
                mixed.set(position, person.getName());
                bus.post(new DrugDataEvent(rrapid, rapid, neutral, longtime, mixed, index, 5));
            }

            Log.e(TAG, "itemListClicked:  addLabel " + person.getAge() + ", " + person.getName() + "position :" + position);
            success = mAutoLabel.addLabel(person.getName(), position);
        }
        if (success) {
            Log.e(TAG, "itemListClicked:  success " + person.getAge() + ", " + person.getName() + "position :" + position);
            adapter.setItemSelected(position, !isSelected);

//            for (int i = 0; i < r.size(); i++) {
//                Log.e(TAG, "ArrayList Value : " + "index : " + i + "value : " + temp.get(i));
//            }
        }
    }

    private void setListeners() {
        mAutoLabel.setOnLabelsCompletedListener(() -> Snackbar.make(recyclerView, "Completed!", Snackbar.LENGTH_SHORT).show());
        mAutoLabel.setOnRemoveLabelListener((view, position) -> adapter.setItemSelected(position, false));
        mAutoLabel.setOnLabelsEmptyListener(() -> Snackbar.make(recyclerView, "EMPTY!", Snackbar.LENGTH_SHORT).show());
        mAutoLabel.setOnLabelClickListener(v -> Snackbar.make(recyclerView, "" + v.getId(), Snackbar.LENGTH_SHORT).show());
    }

    private void setRecyclerView() {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        mPersonList = new ArrayList<>();

        if (mParam1.equals("1")) {
            // TODO: 2018-02-09 초 속효성 데이터 리스트 처리
            List<String> names = Arrays.asList(getResources().getStringArray(R.array.names_RRapid));
            List<String> ages = Arrays.asList(getResources().getStringArray(R.array.description_RRapid));
            TypedArray photos = getResources().obtainTypedArray(R.array.photos);
            //Populate list
            for (int i = 0; i < names.size(); i++) {
                mPersonList.add(new SelectDrug(names.get(i), ages.get(i), photos.getResourceId(i, -1), false));
            }
            photos.recycle();
        } else if (mParam1.equals("2")) {
            // TODO: 2018-02-09 속효성 데이터 리스트 처리
            List<String> names = Arrays.asList(getResources().getStringArray(R.array.names_Rapid));
            List<String> ages = Arrays.asList(getResources().getStringArray(R.array.description_Rapid));
            TypedArray photos = getResources().obtainTypedArray(R.array.photos);
            for (int i = 0; i < names.size(); i++) {
                mPersonList.add(new SelectDrug(names.get(i), ages.get(i), photos.getResourceId(i, -1), false));
            }
            photos.recycle();
        } else if (mParam1.equals("3")) {
            // TODO: 2018-02-09 중간형 데이터 리스트 처리
            List<String> names = Arrays.asList(getResources().getStringArray(R.array.names_netural));
            List<String> ages = Arrays.asList(getResources().getStringArray(R.array.description_netural));
            TypedArray photos = getResources().obtainTypedArray(R.array.photos);
            for (int i = 0; i < names.size(); i++) {
                mPersonList.add(new SelectDrug(names.get(i), ages.get(i), photos.getResourceId(i, -1), false));
            }
            photos.recycle();
        } else if (mParam1.equals("4")) {
            // TODO: 2018-02-09 지속형 데이터 리스트 처리
            List<String> names = Arrays.asList(getResources().getStringArray(R.array.names_longtime));
            List<String> ages = Arrays.asList(getResources().getStringArray(R.array.description_longtime));
            TypedArray photos = getResources().obtainTypedArray(R.array.photos);
            for (int i = 0; i < names.size(); i++) {
                mPersonList.add(new SelectDrug(names.get(i), ages.get(i), photos.getResourceId(i, -1), false));
            }
            photos.recycle();
        } else if (mParam1.equals("5")) {
            // TODO: 2018-02-09 혼합형 데이터 리스트 처리
            List<String> names = Arrays.asList(getResources().getStringArray(R.array.names_mixed));
            List<String> ages = Arrays.asList(getResources().getStringArray(R.array.description_mixed));
            TypedArray photos = getResources().obtainTypedArray(R.array.photos);
            for (int i = 0; i < names.size(); i++) {
                mPersonList.add(new SelectDrug(names.get(i), ages.get(i), photos.getResourceId(i, -1), false));
            }
            photos.recycle();
        } else {

        }

        adapter = new SelectDrugAdapter(mPersonList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SelectDrugAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemListClicked(position);
            }
        });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_INSTANCE_STATE_PEOPLE, (ArrayList<? extends Parcelable>) adapter.getPeople());
    }
}
