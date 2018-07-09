package nodomain.knu2018.bandutils.activities.charts;

import android.net.Uri;
import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.ActivityAmount;
import nodomain.knu2018.bandutils.model.ActivityAmounts;
import nodomain.knu2018.bandutils.model.ActivityUser;



public class WeekStepsChartFragmentV2 extends AbstractWeekChartFragmentV2 {
    private static final String TAG = "WeekStepsChartFragmentV";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public WeekStepsChartFragmentV2() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment WeekStepsChartFragmentV2.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static WeekStepsChartFragmentV2 newInstance(String param1, String param2) {
//        WeekStepsChartFragmentV2 fragment = new WeekStepsChartFragmentV2();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_week_steps_chart_fragment_v2, container, false);
//        mGraph = view.findViewById(R.id.graph);
//        return view;
//    }


    @Override
    public String getTitle() {
        return getString(R.string.weekstepschart_steps_a_week);
    }

    @Override
    String getPieDescription(int targetValue) {
//        return getString(R.string.weeksteps_today_steps_description, String.valueOf(targetValue));.
        return null;
    }

    @Override
    int getGoal() {
        return GBApplication.getPrefs().getInt(ActivityUser.PREF_USER_STEPS_GOAL, 10000);
    }

    @Override
    int getOffsetHours() {
        return 0;
    }

    @Override
    float[] getTotalsForActivityAmounts(ActivityAmounts activityAmounts) {
        int totalSteps = 0;
        for (ActivityAmount amount : activityAmounts.getAmounts()) {
            totalSteps += amount.getTotalSteps();
            Log.e("Step", "getTotalsForActivityAmounts: "  + amount.getTotalSteps() );

        }
        return new float[]{totalSteps};
    }

    @Override
    protected String formatPieValue(int value) {
        return String.valueOf(value);
    }

    @Override
    String[] getPieLabels() {
        return new String[]{""};
    }

    @Override
    IValueFormatter getPieValueFormatter() {
        return null;
    }

    @Override
    IValueFormatter getBarValueFormatter() {
        return null;
    }

    @Override
    IAxisValueFormatter getYAxisFormatter() {
        return null;
    }

    @Override
    int[] getColors() {
        return new int[]{akActivity.color};
    }

    @Override
    protected void setupLegend(Chart chart) {

    }




//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }






}
