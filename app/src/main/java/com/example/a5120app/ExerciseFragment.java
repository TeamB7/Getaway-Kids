package com.example.a5120app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * The ExerciseFragment program implements an class that
 * show the page for displaying single indoor exercise details
 * and record exercise function
 */

public class ExerciseFragment extends Fragment {
    private View view;
    private Button logBtn, finishBtn, likeBtn;
    private WebView exerciseImg;
    private TextView cardTv, metaTv, streTv, nameTv, typeTv, setsTv, repsTv, muscleTv, dateTv;
    private EditText setsEd, repsEd;
    private String exerciseName = "";
    private PopupWindow popupWindow;
    private HashMap<String, String> exerciseHashMap;
    private ImageView check1, check2, check3;
    private ImageButton moreBtn;

    /**
     * initialize the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String exercise = bundle.getString("exercise", null);
            exerciseName = exercise;
        }

        exerciseImg = view.findViewById(R.id.exercise_image);
        nameTv = view.findViewById(R.id.tb_exercise);
        logBtn = view.findViewById(R.id.log_exercise_btn);
        typeTv = view.findViewById(R.id.type_tv);
        setsTv = view.findViewById(R.id.sets_tv);
        repsTv = view.findViewById(R.id.reps_tv);
        cardTv = view.findViewById(R.id.great_tv1);
        metaTv = view.findViewById(R.id.great_tv2);
        streTv = view.findViewById(R.id.great_tv3);
        check1 = view.findViewById(R.id.great_img1);
        check2 = view.findViewById(R.id.great_img2);
        check3 = view.findViewById(R.id.great_img3);
        muscleTv = view.findViewById(R.id.muscle_tv);
        moreBtn = view.findViewById(R.id.imageButton);

        view.setVisibility(View.INVISIBLE);

        GetExerciseAsyncTask getExerciseAsyncTask = new GetExerciseAsyncTask();
        getExerciseAsyncTask.execute();

        final Context context = getContext();
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_up_window, null);

                popupWindow = new PopupWindow(popupView, ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setFocusable(true);
                popupWindow.update();

                likeBtn = popupView.findViewById(R.id.like_btn);
                finishBtn = popupView.findViewById(R.id.finish_btn);
                dateTv = popupView.findViewById(R.id.date_tv);
                setsEd = popupView.findViewById(R.id.sets_ed);
                repsEd = popupView.findViewById(R.id.reps_ed);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                final String formattedDate = df.format(c);

                String dateStr = "Date: " + formattedDate;
                dateTv.setText(dateStr);

                likeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                finishBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp = getActivity().getSharedPreferences("Exercise", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();

                        String sets = setsEd.getText().toString().trim();
                        String reps = repsEd.getText().toString().trim();

                        if (sets.matches("^[0-9]*[1-9][0-9]*$") && reps.matches("^[0-9]*[1-9][0-9]*$")) {
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(exerciseName);
                            jsonArray.put(sets);
                            jsonArray.put(reps);
                            jsonArray.put(formattedDate);

                            int index = sp.getInt("Index", 0);
                            int newIndex = index + 1;

                            if (index == 3) {
                                String str = sp.getString("1", null);
                                ed.putString("0", sp.getString("1", null));
                                ed.putString("1", sp.getString("2", null));
                                ed.putString("2", jsonArray.toString());

                                str = sp.getString("1", null);
                            } else {
                                ed.putInt("Index", newIndex);
                                ed.putString(String.valueOf(index), jsonArray.toString());
                            }

                            String str = sp.getString("2", null);

                            ed.commit();
                            popupWindow.dismiss();
                            LayoutInflater inflater = getLayoutInflater();
                            View customToastroot = inflater.inflate(R.layout.toast, null);
                            TextView msg = (TextView) customToastroot.findViewById(R.id.toastMsg);
                            msg.setText("Workout Saved !");
                            Toast toast = new Toast(getContext());
                            toast.setView(customToastroot);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new SuggestActivityFragment());
                            ft.commit();
                        }else {
                            if(!sets.matches("^[0-9]*[1-9][0-9]*$")){
                                setsEd.setHint("Invalid");
                                setsEd.setHintTextColor(Color.parseColor("#80FF6680"));
                            }
                            if(!reps.matches("^[0-9]*[1-9][0-9]*$")){
                                repsEd.setHint("Invalid");
                                repsEd.setHintTextColor(Color.parseColor("#80FF6680"));
                            }
                        }
                    }

                });
            }
        });


        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_up_window_exercise_more, null);

                popupWindow = new PopupWindow(popupView, ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                Button closeBtn = popupView.findViewById(R.id.close_popup);
                TextView notesTv = popupView.findViewById(R.id.notes_tv);
                String notes = exerciseHashMap.get("modifications");
                notesTv.setText(notes);

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });


        return view;
    }

    /**
     * parsing the json data and add them into a hash map
     */
    private void getData(String exerciseString) throws JSONException {
        JSONArray exerciseJson = new JSONArray(exerciseString);
        exerciseHashMap = new HashMap<String, String>();
        String exerciseName = exerciseJson.getJSONObject(0).getString("Exercise");
        String type = exerciseJson.getJSONObject(0).getString("ExerciseType");
        String muscleGroup = exerciseJson.getJSONObject(0).getString("MuscleGroup");
        String modifications = exerciseJson.getJSONObject(0).getString("Modifications");
        String sets = exerciseJson.getJSONObject(0).getString("Sets");
        String reps = exerciseJson.getJSONObject(0).getString("Reps");
        String gifUrl = exerciseJson.getJSONObject(0).getString("Example");
        int card = exerciseJson.getJSONObject(0).getInt("CardiovascularHealth");
        int meta = exerciseJson.getJSONObject(0).getInt("Metabolism");
        int stre = exerciseJson.getJSONObject(0).getInt("Strength");

//        4893AF ACA9A9
        if (card == 0) {
            check1.setColorFilter(Color.parseColor("#ACA9A9"));
            cardTv.setTextColor(Color.parseColor("#ACA9A9"));
        }

        if (meta == 0) {
            check2.setColorFilter(Color.parseColor("#ACA9A9"));
            metaTv.setTextColor(Color.parseColor("#ACA9A9"));
        }

        if (stre == 0) {
            check3.setColorFilter(Color.parseColor("#ACA9A9"));
            streTv.setTextColor(Color.parseColor("#ACA9A9"));
        }
        exerciseHashMap.put("name", exerciseName);
        exerciseHashMap.put("type", type);
        exerciseHashMap.put("muscleGroup", muscleGroup);
        exerciseHashMap.put("modifications", modifications);
        exerciseHashMap.put("sets", sets);
        exerciseHashMap.put("reps", reps);
        exerciseHashMap.put("gifUrl", gifUrl);
    }

    /**
     * get exercise details from database
     */
    private class GetExerciseAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String exerciseString) {
            try {
                getData(exerciseString);
//                https://www.youtube.com/watch?v=sHLu6-liUL0
//                https://www.youtube.com/embed/sHLu6-liUL0
                nameTv.setText(exerciseHashMap.get("name"));
                repsTv.setText(exerciseHashMap.get("reps"));
                setsTv.setText(exerciseHashMap.get("sets"));
                typeTv.setText(exerciseHashMap.get("type"));
                muscleTv.setText(exerciseHashMap.get("muscleGroup"));

////                exerciseTv.setText(exerciseString);
                String frameVideo = "<iframe width=100% height=100% src=\""
                        + exerciseHashMap.get("gifUrl")
                        + "\" target=\"_parent\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

//                String frameVideo = "<iframe width=100% height=100% " +
//                        "src=\"https://www.youtube.com/embed/sHLu6-liUL0\" " +
//                        "target=\"_parent\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

                exerciseImg.setWebChromeClient(new WebChromeClient());

                WebSettings webSettings = exerciseImg.getSettings();
                webSettings.setJavaScriptEnabled(true);
                exerciseImg.loadData(frameVideo, "text/html", "utf-8");
                exerciseImg.getSettings().setLoadWithOverviewMode(true);
                exerciseImg.getSettings().setUseWideViewPort(true);
                view.setVisibility(View.VISIBLE);

                if (exerciseHashMap.get("modifications").equals("None")) {
                    moreBtn.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = RestClient.getExerciseByName(exerciseName);
            return result;
        }
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }
}
