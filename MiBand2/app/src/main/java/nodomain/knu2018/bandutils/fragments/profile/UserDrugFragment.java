package nodomain.knu2018.bandutils.fragments.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.profile.UserDrugAdapter;
import nodomain.knu2018.bandutils.model.selectdrug.Drugs;

import static nodomain.knu2018.bandutils.Const.DataKeys.USER_ORIGIN_DRUG_INFO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDrugFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class UserDrugFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.lottie_animation)
    LottieAnimationView lottieAnimationView;

    private UserDrugAdapter adapter;



    private OnFragmentInteractionListener mListener;

    public UserDrugFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Paper.init(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_user_drug, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Drugs> drugList = Paper.book().read(USER_ORIGIN_DRUG_INFO);

        if (drugList == null) {
            Snackbar.make(getView().getRootView(), "저장된 정보 없음",Snackbar.LENGTH_SHORT).show();
            // TODO: 2018-06-23 등록된 약물 없을 때
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
            //background.setVisibility(View.VISIBLE);
        } else {
            // TODO: 2018-06-23 등록된 약물 있을 때 이미지 감추기
            adapter = new UserDrugAdapter(getActivity(), drugList);
            recyclerView.setAdapter(this.adapter);
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.cancelAnimation();
            //background.setVisibility(View.INVISIBLE);
        }

//        if (drugList == null){
//
//        }else {
//            adapter = new UserDrugAdapter(getActivity(), drugList);
//            recyclerView.setAdapter(this.adapter);
//        }
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
