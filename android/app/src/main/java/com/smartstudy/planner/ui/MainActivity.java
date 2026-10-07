package com.smartstudy.planner.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartstudy.planner.R;
import com.smartstudy.planner.db.DatabaseHelper;
import com.smartstudy.planner.models.Assignment;
import com.smartstudy.planner.models.Exam;
import com.smartstudy.planner.models.Subject;
import com.smartstudy.planner.models.TimetableItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private String currentUsername;
    private DatabaseHelper dbHelper;
    private FrameLayout flContent;
    private TextView tvHeaderUser;
    private Button btnLogout;

    // Nav tabs
    private LinearLayout tabDashboard, tabSubjects, tabAssignments, tabExams, tabTimetable;
    private TextView labelDashboard, labelSubjects, labelAssignments, labelExams, labelTimetable;

    private int activeTab = 0; // 0=Dash, 1=Sub, 2=Assign, 3=Exams, 4=Time
    private String assignmentFilter = "ALL"; // ALL, PENDING, COMPLETED

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DatabaseHelper.getInstance(this);

        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        currentUsername = getIntent().getStringExtra(LoginActivity.KEY_USER);
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = prefs.getString(LoginActivity.KEY_USER, "student");
        }

        flContent = findViewById(R.id.fl_content);
        tvHeaderUser = findViewById(R.id.tv_header_user);
        btnLogout = findViewById(R.id.btn_logout);

        tvHeaderUser.setText("Hi, " + currentUsername + " 👋");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).edit();
                                editor.remove(LoginActivity.KEY_USER);
                                editor.apply();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        initTabs();
        selectTab(0);
    }

    private void initTabs() {
        tabDashboard = findViewById(R.id.tab_dashboard);
        tabSubjects = findViewById(R.id.tab_subjects);
        tabAssignments = findViewById(R.id.tab_assignments);
        tabExams = findViewById(R.id.tab_exams);
        tabTimetable = findViewById(R.id.tab_timetable);

        labelDashboard = findViewById(R.id.label_dashboard);
        labelSubjects = findViewById(R.id.label_subjects);
        labelAssignments = findViewById(R.id.label_assignments);
        labelExams = findViewById(R.id.label_exams);
        labelTimetable = findViewById(R.id.label_timetable);

        tabDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectTab(0); }
        });
        tabSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectTab(1); }
        });
        tabAssignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectTab(2); }
        });
        tabExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectTab(3); }
        });
        tabTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectTab(4); }
        });
    }

    private void selectTab(int index) {
        activeTab = index;

        int activeColor = getResources().getColor(R.color.tab_active);
        int inactiveColor = getResources().getColor(R.color.tab_inactive);

        labelDashboard.setTextColor(index == 0 ? activeColor : inactiveColor);
        labelSubjects.setTextColor(index == 1 ? activeColor : inactiveColor);
        labelAssignments.setTextColor(index == 2 ? activeColor : inactiveColor);
        labelExams.setTextColor(index == 3 ? activeColor : inactiveColor);
        labelTimetable.setTextColor(index == 4 ? activeColor : inactiveColor);

        labelDashboard.setTypeface(null, index == 0 ? Typeface.BOLD : Typeface.NORMAL);
        labelSubjects.setTypeface(null, index == 1 ? Typeface.BOLD : Typeface.NORMAL);
        labelAssignments.setTypeface(null, index == 2 ? Typeface.BOLD : Typeface.NORMAL);
        labelExams.setTypeface(null, index == 3 ? Typeface.BOLD : Typeface.NORMAL);
        labelTimetable.setTypeface(null, index == 4 ? Typeface.BOLD : Typeface.NORMAL);

        refreshContent();
    }

    private void refreshContent() {
        flContent.removeAllViews();
        switch (activeTab) {
            case 0:
                flContent.addView(buildDashboardView());
                break;
            case 1:
                flContent.addView(buildSubjectsView());
                break;
            case 2:
                flContent.addView(buildAssignmentsView());
                break;
            case 3:
                flContent.addView(buildExamsView());
                break;
            case 4:
                flContent.addView(buildTimetableView());
                break;
        }
    }

    // =========================================================
    // TAB 1: DASHBOARD
    // =========================================================

    private View buildDashboardView() {
        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(32));

        List<Subject> subjects = dbHelper.getSubjectsForUser(currentUsername);
        List<Assignment> assignments = dbHelper.getAssignmentsForUser(currentUsername);
        List<Exam> exams = dbHelper.getExamsForUser(currentUsername);

        int totalSubjects = subjects.size();
        int pendingTasks = 0;
        for (Assignment a : assignments) {
            if (!a.isCompleted()) pendingTasks++;
        }
        int upcomingExams = 0;
        for (Exam e : exams) {
            if (!e.isPrepared()) upcomingExams++;
        }

        int avgProgress = 0;
        if (totalSubjects > 0) {
            int sum = 0;
            for (Subject s : subjects) sum += s.getProgress();
            avgProgress = sum / totalSubjects;
        }

        // Welcome banner card
        LinearLayout banner = createCard();
        TextView tvGreeting = new TextView(this);
        tvGreeting.setText("Welcome back, " + currentUsername + "! 🎓");
        tvGreeting.setTextSize(20);
        tvGreeting.setTypeface(null, Typeface.BOLD);
        tvGreeting.setTextColor(getResources().getColor(R.color.primary));
        banner.addView(tvGreeting);

        TextView tvQuote = new TextView(this);
        tvQuote.setText("\"Consistency is the key to mastering your education.\"");
        tvQuote.setTextSize(13);
        tvQuote.setTextColor(getResources().getColor(R.color.text_secondary));
        tvQuote.setPadding(0, dp(4), 0, 0);
        banner.addView(tvQuote);
        root.addView(banner);

        // Stats Grid (2 rows)
        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setPadding(0, dp(12), 0, 0);

        View statSub = createStatCard("📚 Subjects", String.valueOf(totalSubjects), "#4F46E5");
        View statTasks = createStatCard("📝 Pending Tasks", String.valueOf(pendingTasks), "#F59E0B");
        row1.addView(statSub);
        row1.addView(statTasks);
        root.addView(row1);

        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setPadding(0, dp(8), 0, 0);

        View statExams = createStatCard("🎯 Upcoming Exams", String.valueOf(upcomingExams), "#EF4444");
        View statProg = createStatCard("📊 Avg Progress", avgProgress + "%", "#10B981");
        row2.addView(statExams);
        row2.addView(statProg);
        root.addView(row2);

        // Overall Progress Section
        LinearLayout progCard = createCard();
        progCard.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams) progCard.getLayoutParams()).topMargin = dp(14);

        TextView tvProgTitle = new TextView(this);
        tvProgTitle.setText("Overall Study Progress (" + avgProgress + "%)");
        tvProgTitle.setTypeface(null, Typeface.BOLD);
        tvProgTitle.setTextColor(getResources().getColor(R.color.text_primary));
        progCard.addView(tvProgTitle);

        ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pb.setMax(100);
        pb.setProgress(avgProgress);
        pb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(16)));
        ((LinearLayout.LayoutParams) pb.getLayoutParams()).topMargin = dp(8);
        progCard.addView(pb);
        root.addView(progCard);

        // Quick Actions
        TextView tvQuick = new TextView(this);
        tvQuick.setText("Quick Actions");
        tvQuick.setTextSize(16);
        tvQuick.setTypeface(null, Typeface.BOLD);
        tvQuick.setTextColor(getResources().getColor(R.color.text_primary));
        tvQuick.setPadding(0, dp(18), 0, dp(8));
        root.addView(tvQuick);

        LinearLayout actionRow = new LinearLayout(this);
        actionRow.setOrientation(LinearLayout.HORIZONTAL);

        Button btnAddSub = createActionButton("+ Add Subject");
        btnAddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showAddSubjectDialog(); }
        });
        actionRow.addView(btnAddSub);

        Button btnAddTask = createActionButton("+ Add Task");
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showAddAssignmentDialog(); }
        });
        actionRow.addView(btnAddTask);

        Button btnAddEx = createActionButton("+ Add Exam");
        btnAddEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showAddExamDialog(); }
        });
        actionRow.addView(btnAddEx);
        root.addView(actionRow);

        // Upcoming Deadlines Preview
        TextView tvDeadlines = new TextView(this);
        tvDeadlines.setText("Upcoming Urgencies ⏰");
        tvDeadlines.setTextSize(16);
        tvDeadlines.setTypeface(null, Typeface.BOLD);
        tvDeadlines.setTextColor(getResources().getColor(R.color.text_primary));
        tvDeadlines.setPadding(0, dp(18), 0, dp(8));
        root.addView(tvDeadlines);

        List<TimetableItem> urgencyList = getUpcomingItems();
        if (urgencyList.isEmpty()) {
            TextView tvNone = new TextView(this);
            tvNone.setText("🎉 All caught up! No upcoming deadlines.");
            tvNone.setTextColor(getResources().getColor(R.color.text_secondary));
            tvNone.setPadding(dp(8), dp(8), dp(8), dp(8));
            root.addView(tvNone);
        } else {
            int count = Math.min(3, urgencyList.size());
            for (int i = 0; i < count; i++) {
                TimetableItem item = urgencyList.get(i);
                root.addView(createUrgencyItemView(item));
            }
        }

        scroll.addView(root);
        return scroll;
    }

    private View createStatCard(String label, String value, String colorHex) {
        LinearLayout card = createCard();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(dp(4), dp(4), dp(4), dp(4));
        card.setLayoutParams(params);

        TextView tvVal = new TextView(this);
        tvVal.setText(value);
        tvVal.setTextSize(26);
        tvVal.setTypeface(null, Typeface.BOLD);
        tvVal.setTextColor(Color.parseColor(colorHex));
        card.addView(tvVal);

        TextView tvLbl = new TextView(this);
        tvLbl.setText(label);
        tvLbl.setTextSize(13);
        tvLbl.setTextColor(getResources().getColor(R.color.text_secondary));
        tvLbl.setPadding(0, dp(2), 0, 0);
        card.addView(tvLbl);

        return card;
    }

    private Button createActionButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setTextSize(12);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setTextColor(Color.WHITE);
        btn.setBackgroundResource(R.drawable.bg_button);
        btn.setAllCaps(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(40), 1);
        params.setMargins(dp(3), 0, dp(3), 0);
        btn.setLayoutParams(params);
        return btn;
    }

    // =========================================================
    // TAB 2: SUBJECTS
    // =========================================================

    private View buildSubjectsView() {
        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(32));

        // Header with Add Button
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("My Subjects");
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(getResources().getColor(R.color.text_primary));
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        header.addView(title);

        Button btnAdd = new Button(this);
        btnAdd.setText("+ Add Subject");
        btnAdd.setTextColor(Color.WHITE);
        btnAdd.setTextSize(13);
        btnAdd.setAllCaps(false);
        btnAdd.setBackgroundResource(R.drawable.bg_button);
        btnAdd.setPadding(dp(14), dp(8), dp(14), dp(8));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showAddSubjectDialog(); }
        });
        header.addView(btnAdd);
        root.addView(header);

        List<Subject> list = dbHelper.getSubjectsForUser(currentUsername);
        if (list.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No subjects added yet. Tap '+ Add Subject' to get started!");
            empty.setTextColor(getResources().getColor(R.color.text_secondary));
            empty.setPadding(0, dp(32), 0, 0);
            empty.setGravity(Gravity.CENTER);
            root.addView(empty);
        } else {
            for (final Subject s : list) {
                LinearLayout card = createCard();
                card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((LinearLayout.LayoutParams) card.getLayoutParams()).topMargin = dp(12);

                // Top row: Name + Difficulty badge
                LinearLayout topRow = new LinearLayout(this);
                topRow.setOrientation(LinearLayout.HORIZONTAL);
                topRow.setGravity(Gravity.CENTER_VERTICAL);

                TextView tvName = new TextView(this);
                tvName.setText(s.getName());
                tvName.setTextSize(17);
                tvName.setTypeface(null, Typeface.BOLD);
                tvName.setTextColor(getResources().getColor(R.color.text_primary));
                tvName.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                topRow.addView(tvName);

                TextView tvDiff = new TextView(this);
                tvDiff.setText(s.getDifficulty());
                tvDiff.setTextSize(11);
                tvDiff.setTypeface(null, Typeface.BOLD);
                if ("Hard".equalsIgnoreCase(s.getDifficulty())) {
                    tvDiff.setBackgroundResource(R.drawable.bg_badge_hard);
                    tvDiff.setTextColor(getResources().getColor(R.color.danger));
                } else if ("Medium".equalsIgnoreCase(s.getDifficulty())) {
                    tvDiff.setBackgroundResource(R.drawable.bg_badge_medium);
                    tvDiff.setTextColor(getResources().getColor(R.color.warning));
                } else {
                    tvDiff.setBackgroundResource(R.drawable.bg_badge_easy);
                    tvDiff.setTextColor(getResources().getColor(R.color.success));
                }
                topRow.addView(tvDiff);
                card.addView(topRow);

                // Info row: Goals & Hours
                TextView tvHours = new TextView(this);
                tvHours.setText("Weekly: " + s.getWeeklyHours() + "h  •  Target: " + s.getStudyGoal() + "h");
                tvHours.setTextSize(13);
                tvHours.setTextColor(getResources().getColor(R.color.text_secondary));
                tvHours.setPadding(0, dp(6), 0, 0);
                card.addView(tvHours);

                // Progress row
                LinearLayout progRow = new LinearLayout(this);
                progRow.setOrientation(LinearLayout.HORIZONTAL);
                progRow.setGravity(Gravity.CENTER_VERTICAL);
                progRow.setPadding(0, dp(8), 0, 0);

                ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
                pb.setMax(100);
                pb.setProgress(s.getProgress());
                pb.setLayoutParams(new LinearLayout.LayoutParams(0, dp(12), 1));
                progRow.addView(pb);

                TextView tvPct = new TextView(this);
                tvPct.setText("  " + s.getProgress() + "%");
                tvPct.setTextSize(13);
                tvPct.setTypeface(null, Typeface.BOLD);
                tvPct.setTextColor(getResources().getColor(R.color.primary));
                progRow.addView(tvPct);
                card.addView(progRow);

                // Action buttons row (Update Progress & Delete)
                LinearLayout btnRow = new LinearLayout(this);
                btnRow.setOrientation(LinearLayout.HORIZONTAL);
                btnRow.setPadding(0, dp(12), 0, 0);

                Button btnUpdate = new Button(this);
                btnUpdate.setText("Update Progress");
                btnUpdate.setTextSize(12);
                btnUpdate.setAllCaps(false);
                btnUpdate.setTextColor(getResources().getColor(R.color.primary));
                btnUpdate.setBackgroundResource(R.drawable.bg_button_secondary);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, dp(36), 1);
                p1.setMargins(0, 0, dp(6), 0);
                btnUpdate.setLayoutParams(p1);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { showUpdateProgressDialog(s); }
                });
                btnRow.addView(btnUpdate);

                Button btnDel = new Button(this);
                btnDel.setText("Delete");
                btnDel.setTextSize(12);
                btnDel.setAllCaps(false);
                btnDel.setTextColor(getResources().getColor(R.color.danger));
                btnDel.setBackgroundResource(R.drawable.bg_badge_hard);
                btnDel.setLayoutParams(new LinearLayout.LayoutParams(dp(70), dp(36)));
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { confirmDeleteSubject(s); }
                });
                btnRow.addView(btnDel);

                card.addView(btnRow);
                root.addView(card);
            }
        }

        scroll.addView(root);
        return scroll;
    }

    // =========================================================
    // TAB 3: ASSIGNMENTS / TASKS
    // =========================================================

    private View buildAssignmentsView() {
        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(32));

        // Header + Add Button
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("Assignments");
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(getResources().getColor(R.color.text_primary));
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        header.addView(title);

        Button btnAdd = new Button(this);
        btnAdd.setText("+ Add Task");
        btnAdd.setTextColor(Color.WHITE);
        btnAdd.setTextSize(13);
        btnAdd.setAllCaps(false);
        btnAdd.setBackgroundResource(R.drawable.bg_button);
        btnAdd.setPadding(dp(14), dp(8), dp(14), dp(8));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showAddAssignmentDialog(); }
        });
        header.addView(btnAdd);
        root.addView(header);

        // Filter Pills Row
        LinearLayout filterRow = new LinearLayout(this);
        filterRow.setOrientation(LinearLayout.HORIZONTAL);
        filterRow.setPadding(0, dp(12), 0, dp(4));

        filterRow.addView(createFilterButton("All", "ALL"));
        filterRow.addView(createFilterButton("Pending", "PENDING"));
        filterRow.addView(createFilterButton("Completed", "COMPLETED"));
        root.addView(filterRow);

        List<Assignment> all = dbHelper.getAssignmentsForUser(currentUsername);
        List<Assignment> filtered = new ArrayList<>();
        for (Assignment a : all) {
            if ("ALL".equals(assignmentFilter)) filtered.add(a);
            else if ("PENDING".equals(assignmentFilter) && !a.isCompleted()) filtered.add(a);
            else if ("COMPLETED".equals(assignmentFilter) && a.isCompleted()) filtered.add(a);
        }

        if (filtered.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No assignments found in this view.");
            empty.setTextColor(getResources().getColor(R.color.text_secondary));
            empty.setPadding(0, dp(32), 0, 0);
            empty.setGravity(Gravity.CENTER);
            root.addView(empty);
        } else {
            for (final Assignment a : filtered) {
                LinearLayout card = createCard();
                card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((LinearLayout.LayoutParams) card.getLayoutParams()).topMargin = dp(10);

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);

                CheckBox cb = new CheckBox(this);
                cb.setChecked(a.isCompleted());
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dbHelper.updateAssignmentCompleted(a.getId(), isChecked);
                        refreshContent();
                    }
                });
                row.addView(cb);

                LinearLayout details = new LinearLayout(this);
                details.setOrientation(LinearLayout.VERTICAL);
                details.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                TextView tvTitle = new TextView(this);
                tvTitle.setText(a.getAssignmentName());
                tvTitle.setTextSize(16);
                tvTitle.setTypeface(null, Typeface.BOLD);
                tvTitle.setTextColor(getResources().getColor(R.color.text_primary));
                if (a.isCompleted()) {
                    tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvTitle.setTextColor(getResources().getColor(R.color.text_light));
                }
                details.addView(tvTitle);

                TextView tvSub = new TextView(this);
                tvSub.setText(a.getSubjectName() + "  •  Due: " + a.getDueDate());
                tvSub.setTextSize(13);
                tvSub.setTextColor(getResources().getColor(R.color.text_secondary));
                details.addView(tvSub);

                row.addView(details);

                // Priority Badge
                TextView tvPriority = new TextView(this);
                tvPriority.setText(a.getPriority());
                tvPriority.setTextSize(11);
                tvPriority.setTypeface(null, Typeface.BOLD);
                if ("High".equalsIgnoreCase(a.getPriority())) {
                    tvPriority.setBackgroundResource(R.drawable.bg_badge_hard);
                    tvPriority.setTextColor(getResources().getColor(R.color.danger));
                } else if ("Medium".equalsIgnoreCase(a.getPriority())) {
                    tvPriority.setBackgroundResource(R.drawable.bg_badge_medium);
                    tvPriority.setTextColor(getResources().getColor(R.color.warning));
                } else {
                    tvPriority.setBackgroundResource(R.drawable.bg_badge_easy);
                    tvPriority.setTextColor(getResources().getColor(R.color.success));
                }
                tvPriority.setPadding(dp(8), dp(4), dp(8), dp(4));
                row.addView(tvPriority);

                // Delete Icon/Button
                TextView tvDel = new TextView(this);
                tvDel.setText(" 🗑️ ");
                tvDel.setTextSize(16);
                tvDel.setPadding(dp(8), dp(4), 0, dp(4));
                tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelper.deleteAssignment(a.getId());
                        Toast.makeText(MainActivity.this, "Task removed", Toast.LENGTH_SHORT).show();
                        refreshContent();
                    }
                });
                row.addView(tvDel);

                card.addView(row);
                root.addView(card);
            }
        }

        scroll.addView(root);
        return scroll;
    }

    private Button createFilterButton(String text, final String filterVal) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setTextSize(12);
        btn.setAllCaps(false);
        boolean isActive = assignmentFilter.equals(filterVal);
        btn.setTextColor(isActive ? Color.WHITE : getResources().getColor(R.color.text_secondary));
        btn.setBackgroundResource(isActive ? R.drawable.bg_button : R.drawable.bg_card);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(34), 1);
        params.setMargins(dp(3), 0, dp(3), 0);
        btn.setLayoutParams(params);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignmentFilter = filterVal;
                refreshContent();
            }
        });
        return btn;
    }

    // =========================================================
    // TAB 4: EXAMS
    // =========================================================

    private View buildExamsView() {
        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(32));

        // Header + Add Button
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("Upcoming Exams");
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(getResources().getColor(R.color.text_primary));
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        header.addView(title);

        Button btnAdd = new Button(this);
        btnAdd.setText("+ Add Exam");
        btnAdd.setTextColor(Color.WHITE);
        btnAdd.setTextSize(13);
        btnAdd.setAllCaps(false);
        btnAdd.setBackgroundResource(R.drawable.bg_button);
        btnAdd.setPadding(dp(14), dp(8), dp(14), dp(8));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showAddExamDialog(); }
        });
        header.addView(btnAdd);
        root.addView(header);

        List<Exam> list = dbHelper.getExamsForUser(currentUsername);
        if (list.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No exams scheduled yet. Tap '+ Add Exam' to add one!");
            empty.setTextColor(getResources().getColor(R.color.text_secondary));
            empty.setPadding(0, dp(32), 0, 0);
            empty.setGravity(Gravity.CENTER);
            root.addView(empty);
        } else {
            for (final Exam e : list) {
                LinearLayout card = createCard();
                card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((LinearLayout.LayoutParams) card.getLayoutParams()).topMargin = dp(12);

                LinearLayout topRow = new LinearLayout(this);
                topRow.setOrientation(LinearLayout.HORIZONTAL);
                topRow.setGravity(Gravity.CENTER_VERTICAL);

                TextView tvName = new TextView(this);
                tvName.setText("🎯 " + e.getExamName());
                tvName.setTextSize(17);
                tvName.setTypeface(null, Typeface.BOLD);
                tvName.setTextColor(getResources().getColor(R.color.text_primary));
                tvName.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                topRow.addView(tvName);

                // Countdown badge
                String countdown = getDaysRemainingText(e.getExamDate());
                TextView tvCountdown = new TextView(this);
                tvCountdown.setText(countdown);
                tvCountdown.setTextSize(12);
                tvCountdown.setTypeface(null, Typeface.BOLD);
                tvCountdown.setBackgroundResource(R.drawable.bg_badge_hard);
                tvCountdown.setTextColor(getResources().getColor(R.color.danger));
                topRow.addView(tvCountdown);
                card.addView(topRow);

                // Subject & Date
                TextView tvDetails = new TextView(this);
                tvDetails.setText("Subject: " + e.getSubjectName() + "  •  Date: " + e.getExamDate());
                tvDetails.setTextSize(13);
                tvDetails.setTextColor(getResources().getColor(R.color.text_secondary));
                tvDetails.setPadding(0, dp(6), 0, 0);
                card.addView(tvDetails);

                // Actions row: Prepared Toggle + Delete
                LinearLayout actRow = new LinearLayout(this);
                actRow.setOrientation(LinearLayout.HORIZONTAL);
                actRow.setPadding(0, dp(10), 0, 0);

                Button btnPrep = new Button(this);
                btnPrep.setText(e.isPrepared() ? "Prepared ✓" : "Mark as Prepared");
                btnPrep.setTextSize(12);
                btnPrep.setAllCaps(false);
                btnPrep.setTextColor(e.isPrepared() ? getResources().getColor(R.color.success) : getResources().getColor(R.color.primary));
                btnPrep.setBackgroundResource(R.drawable.bg_button_secondary);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, dp(36), 1);
                p1.setMargins(0, 0, dp(6), 0);
                btnPrep.setLayoutParams(p1);
                btnPrep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelper.updateExamPrepared(e.getId(), !e.isPrepared());
                        refreshContent();
                    }
                });
                actRow.addView(btnPrep);

                Button btnDel = new Button(this);
                btnDel.setText("Delete");
                btnDel.setTextSize(12);
                btnDel.setAllCaps(false);
                btnDel.setTextColor(getResources().getColor(R.color.danger));
                btnDel.setBackgroundResource(R.drawable.bg_badge_hard);
                btnDel.setLayoutParams(new LinearLayout.LayoutParams(dp(70), dp(36)));
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelper.deleteExam(e.getId());
                        Toast.makeText(MainActivity.this, "Exam removed", Toast.LENGTH_SHORT).show();
                        refreshContent();
                    }
                });
                actRow.addView(btnDel);

                card.addView(actRow);
                root.addView(card);
            }
        }

        scroll.addView(root);
        return scroll;
    }

    // =========================================================
    // TAB 5: TIMETABLE & SCHEDULE
    // =========================================================

    private View buildTimetableView() {
        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(32));

        TextView title = new TextView(this);
        title.setText("Study Agenda & Timetable");
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(getResources().getColor(R.color.text_primary));
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText("All pending tasks and exams arranged chronologically");
        sub.setTextSize(13);
        sub.setTextColor(getResources().getColor(R.color.text_secondary));
        sub.setPadding(0, dp(4), 0, dp(14));
        root.addView(sub);

        List<TimetableItem> items = getUpcomingItems();
        if (items.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No upcoming tasks or exams! You're completely free. 🎉");
            empty.setTextColor(getResources().getColor(R.color.text_secondary));
            empty.setPadding(0, dp(32), 0, 0);
            empty.setGravity(Gravity.CENTER);
            root.addView(empty);
        } else {
            for (TimetableItem item : items) {
                LinearLayout card = createCard();
                card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((LinearLayout.LayoutParams) card.getLayoutParams()).topMargin = dp(10);

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);

                // Type Icon
                TextView tvIcon = new TextView(this);
                tvIcon.setText("Exam".equals(item.getType()) ? "🎯" : "📝");
                tvIcon.setTextSize(24);
                tvIcon.setPadding(0, 0, dp(12), 0);
                row.addView(tvIcon);

                // Content
                LinearLayout content = new LinearLayout(this);
                content.setOrientation(LinearLayout.VERTICAL);
                content.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                TextView tvTitle = new TextView(this);
                tvTitle.setText(item.getTitle());
                tvTitle.setTextSize(16);
                tvTitle.setTypeface(null, Typeface.BOLD);
                tvTitle.setTextColor(getResources().getColor(R.color.text_primary));
                content.addView(tvTitle);

                TextView tvSub = new TextView(this);
                tvSub.setText(item.getSubject() + "  •  " + item.getDateStr());
                tvSub.setTextSize(13);
                tvSub.setTextColor(getResources().getColor(R.color.text_secondary));
                content.addView(tvSub);
                row.addView(content);

                // Type Badge
                TextView tvType = new TextView(this);
                tvType.setText(item.getType().toUpperCase(Locale.ROOT));
                tvType.setTextSize(11);
                tvType.setTypeface(null, Typeface.BOLD);
                if ("Exam".equals(item.getType())) {
                    tvType.setBackgroundResource(R.drawable.bg_badge_hard);
                    tvType.setTextColor(getResources().getColor(R.color.danger));
                } else {
                    tvType.setBackgroundResource(R.drawable.bg_badge_medium);
                    tvType.setTextColor(getResources().getColor(R.color.warning));
                }
                row.addView(tvType);

                card.addView(row);
                root.addView(card);
            }
        }

        scroll.addView(root);
        return scroll;
    }

    private View createUrgencyItemView(TimetableItem item) {
        LinearLayout card = createCard();
        card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams) card.getLayoutParams()).topMargin = dp(8);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(("Exam".equals(item.getType()) ? "🎯 " : "📝 ") + item.getTitle());
        tvTitle.setTextSize(15);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextColor(getResources().getColor(R.color.text_primary));
        tvTitle.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        row.addView(tvTitle);

        TextView tvDue = new TextView(this);
        tvDue.setText(item.getDateStr());
        tvDue.setTextSize(12);
        tvDue.setTextColor(getResources().getColor(R.color.danger));
        tvDue.setTypeface(null, Typeface.BOLD);
        row.addView(tvDue);

        card.addView(row);
        return card;
    }

    // =========================================================
    // DIALOGS
    // =========================================================

    private void showAddSubjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Subject");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_subject, null);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.et_sub_name);
        final Spinner spDiff = dialogView.findViewById(R.id.sp_sub_difficulty);
        final EditText etGoal = dialogView.findViewById(R.id.et_sub_goal);
        final EditText etWeekly = dialogView.findViewById(R.id.et_sub_weekly);
        final EditText etProg = dialogView.findViewById(R.id.et_sub_progress);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Easy", "Medium", "Hard"});
        spDiff.setAdapter(adapter);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString().trim();
                String diff = spDiff.getSelectedItem().toString();
                String goalStr = etGoal.getText().toString().trim();
                String weeklyStr = etWeekly.getText().toString().trim();
                String progStr = etProg.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Subject name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                double goal = goalStr.isEmpty() ? 30.0 : Double.parseDouble(goalStr);
                double weekly = weeklyStr.isEmpty() ? 4.0 : Double.parseDouble(weeklyStr);
                int prog = progStr.isEmpty() ? 0 : Math.min(100, Math.max(0, Integer.parseInt(progStr)));

                Subject s = new Subject(currentUsername, name, diff, goal, weekly, prog);
                dbHelper.addSubject(s);
                Toast.makeText(MainActivity.this, "Subject added!", Toast.LENGTH_SHORT).show();
                refreshContent();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showUpdateProgressDialog(final Subject s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update: " + s.getName());

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_progress, null);
        builder.setView(dialogView);

        final EditText etProg = dialogView.findViewById(R.id.et_update_progress);
        final EditText etWeekly = dialogView.findViewById(R.id.et_update_weekly);

        etProg.setText(String.valueOf(s.getProgress()));
        etWeekly.setText(String.valueOf(s.getWeeklyHours()));

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String progStr = etProg.getText().toString().trim();
                String weeklyStr = etWeekly.getText().toString().trim();

                if (!progStr.isEmpty()) {
                    s.setProgress(Math.min(100, Math.max(0, Integer.parseInt(progStr))));
                }
                if (!weeklyStr.isEmpty()) {
                    s.setWeeklyHours(Double.parseDouble(weeklyStr));
                }

                dbHelper.updateSubject(s);
                Toast.makeText(MainActivity.this, "Subject updated!", Toast.LENGTH_SHORT).show();
                refreshContent();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void confirmDeleteSubject(final Subject s) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete '" + s.getName() + "'?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteSubject(s.getId());
                        Toast.makeText(MainActivity.this, "Subject deleted", Toast.LENGTH_SHORT).show();
                        refreshContent();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAddAssignmentDialog() {
        List<Subject> subjects = dbHelper.getSubjectsForUser(currentUsername);
        if (subjects.isEmpty()) {
            Toast.makeText(this, "Please add at least one subject first!", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_assignment, null);
        builder.setView(dialogView);

        final EditText etTitle = dialogView.findViewById(R.id.et_assign_title);
        final Spinner spSubject = dialogView.findViewById(R.id.sp_assign_subject);
        final EditText etDueDate = dialogView.findViewById(R.id.et_assign_due_date);
        final Spinner spPriority = dialogView.findViewById(R.id.sp_assign_priority);

        List<String> subNames = new ArrayList<>();
        for (Subject s : subjects) subNames.add(s.getName());
        spSubject.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subNames));

        spPriority.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"High", "Medium", "Low"}));

        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etDueDate);
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = etTitle.getText().toString().trim();
                String subject = spSubject.getSelectedItem().toString();
                String dueDate = etDueDate.getText().toString().trim();
                String priority = spPriority.getSelectedItem().toString();

                if (title.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dueDate.isEmpty()) {
                    dueDate = dateFormat.format(new Date());
                }

                Assignment a = new Assignment(currentUsername, subject, title, dueDate, priority, false);
                dbHelper.addAssignment(a);
                Toast.makeText(MainActivity.this, "Task created!", Toast.LENGTH_SHORT).show();
                refreshContent();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showAddExamDialog() {
        List<Subject> subjects = dbHelper.getSubjectsForUser(currentUsername);
        if (subjects.isEmpty()) {
            Toast.makeText(this, "Please add at least one subject first!", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Schedule an Exam");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_exam, null);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.et_exam_name);
        final Spinner spSubject = dialogView.findViewById(R.id.sp_exam_subject);
        final EditText etDate = dialogView.findViewById(R.id.et_exam_date);

        List<String> subNames = new ArrayList<>();
        for (Subject s : subjects) subNames.add(s.getName());
        spSubject.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subNames));

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etDate);
            }
        });

        builder.setPositiveButton("Schedule", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString().trim();
                String subject = spSubject.getSelectedItem().toString();
                String date = etDate.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Exam name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (date.isEmpty()) {
                    date = dateFormat.format(new Date());
                }

                Exam e = new Exam(currentUsername, subject, name, date, false);
                dbHelper.addExam(e);
                Toast.makeText(MainActivity.this, "Exam scheduled!", Toast.LENGTH_SHORT).show();
                refreshContent();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDatePicker(final EditText target) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                target.setText(dateFormat.format(c.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    // =========================================================
    // UTILS
    // =========================================================

    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_card);
        card.setElevation(dp(2));
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        return card;
    }

    private List<TimetableItem> getUpcomingItems() {
        List<TimetableItem> list = new ArrayList<>();

        List<Assignment> assignments = dbHelper.getAssignmentsForUser(currentUsername);
        for (Assignment a : assignments) {
            if (!a.isCompleted()) {
                Date d = parseDate(a.getDueDate());
                list.add(new TimetableItem(a.getAssignmentName(), a.getSubjectName(), a.getDueDate(), "Task", a.getPriority(), d));
            }
        }

        List<Exam> exams = dbHelper.getExamsForUser(currentUsername);
        for (Exam e : exams) {
            if (!e.isPrepared()) {
                Date d = parseDate(e.getExamDate());
                list.add(new TimetableItem(e.getExamName(), e.getSubjectName(), e.getExamDate(), "Exam", "High", d));
            }
        }

        Collections.sort(list);
        return list;
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null) return null;
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    private String getDaysRemainingText(String dateStr) {
        Date target = parseDate(dateStr);
        if (target == null) return dateStr;

        long diffMs = target.getTime() - System.currentTimeMillis();
        long days = diffMs / (1000 * 60 * 60 * 24);

        if (days < 0) return "Overdue";
        if (days == 0) return "Today!";
        if (days == 1) return "Tomorrow";
        return days + " days left";
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (value * density + 0.5f);
    }
}
