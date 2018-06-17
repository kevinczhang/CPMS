package com.example.home_zhang.cpms.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.home_zhang.cpms.DAL.DatabaseHelper;
import com.example.home_zhang.cpms.MainActivity;
import com.example.home_zhang.cpms.R;

import java.net.URLEncoder;

public class ProblemDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_description);

        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null){
            value = b.getInt("prob_Id");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DatabaseHelper db = new DatabaseHelper(this);
        try {
            db.createDataBase();
            db.openDataBase();

            SQLiteDatabase sd = db.getReadableDatabase();
            Cursor cursor = sd.rawQuery("Select id, title, description from questions where source_number = " + value, null);
            cursor.moveToFirst();
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);

            cursor = sd.rawQuery("Select solutions.content From solutions Where Solutions.question_id = '" + id + "'", null);
            cursor.moveToFirst();
            String solution = cursor.getString(0);
            cursor.close();

            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            WebView problemDescription = (WebView) findViewById(R.id.problemDescription);
            WebView problemSolution = (WebView) findViewById(R.id.problemSolution);

            problemDescription.loadData(URLEncoder.encode(description, "utf-8").replaceAll("\\+", "%20"), "text/html; charset=utf-8", "utf-8");
            problemDescription.getSettings().setLoadWithOverviewMode(true);
            problemDescription.getSettings().setUseWideViewPort(true);
            problemDescription.getSettings().setTextZoom(200);

            problemSolution.loadDataWithBaseURL("data://", solution, "text/html", "utf-8", null);
            problemSolution.getSettings().setLoadWithOverviewMode(true);
            problemSolution.getSettings().setUseWideViewPort(true);
            problemSolution.getSettings().setTextZoom(300);
            problemSolution.setWebChromeClient(new WebChromeClient());
            problemSolution.setWebViewClient(new WebViewClient());
            problemSolution.clearCache(true);
            problemSolution.clearHistory();
            problemSolution.getSettings().setJavaScriptEnabled(true);
            problemSolution.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            problemSolution.getSettings().setDomStorageEnabled(true);

            sd.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailView = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(detailView);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
    }

    public void onCardClick(View view)
    {
        flipCard();
    }

    private void flipCard()
    {
        View rootLayout = (View) findViewById(R.id.problem_activity_root);
        View description = (View) findViewById(R.id.problem_activity_description);
        View solution = (View) findViewById(R.id.problem_activity_solution);

        FlipAnimation flipAnimation = new FlipAnimation(description, solution);

        if (description.getVisibility() == View.GONE)
        {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }

    public class FlipAnimation extends Animation {
        private Camera camera;

        private View fromView;
        private View toView;

        private float centerX;
        private float centerY;

        private boolean forward = true;

        /**
         * Creates a 3D flip animation between two views.
         *
         * @param fromView First view in the transition.
         * @param toView   Second view in the transition.
         */
        public FlipAnimation(View fromView, View toView) {
            this.fromView = fromView;
            this.toView = toView;

            setDuration(700);
            setFillAfter(false);
            setInterpolator(new AccelerateDecelerateInterpolator());
        }

        public void reverse() {
            forward = false;
            View switchView = toView;
            toView = fromView;
            fromView = switchView;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            centerX = width / 2;
            centerY = height / 2;
            camera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // Angle around the y-axis of the rotation at the given time
            // calculated both in radians and degrees.
            final double radians = Math.PI * interpolatedTime;
            float degrees = (float) (180.0 * radians / Math.PI);

            // Once we reach the midpoint in the animation, we need to hide the
            // source view and show the destination view. We also need to change
            // the angle by 180 degrees so that the destination does not come in
            // flipped around
            if (interpolatedTime >= 0.5f) {
                degrees -= 180.f;
                fromView.setVisibility(View.GONE);
                toView.setVisibility(View.VISIBLE);
            }

            if (forward)
                degrees = -degrees; //determines direction of rotation when flip begins

            final Matrix matrix = t.getMatrix();
            camera.save();
            camera.rotateY(degrees);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

}
