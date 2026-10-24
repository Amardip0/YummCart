package com.cs7035.yummcart;

import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.*;
import android.widget.*;

public final class Ui {
    public static final int INK = Color.rgb(25, 43, 35), PRIMARY = Color.rgb(35, 91, 67), MINT = Color.rgb(215, 239, 224), LAV = Color.rgb(242, 247, 241), MUTED = Color.rgb(108, 126, 116), WHITE = Color.rgb(255, 255, 250), GREEN = Color.rgb(35, 91, 67), LIME = Color.rgb(197, 226, 123), BORDER = Color.rgb(224, 233, 224);

    static int dp(float v) {
        return (int) (v * App.ctx.getResources().getDisplayMetrics().density + .5f);
    }

    static void applyInsets(View v, float leftDp, float topDp, float rightDp, float bottomDp) {
        if (Build.VERSION.SDK_INT >= 30) {
            v.setOnApplyWindowInsetsListener((view, insets) -> {
                android.graphics.Insets sb = insets.getInsets(WindowInsets.Type.systemBars());
                view.setPadding(dp(leftDp), sb.top + dp(topDp), dp(rightDp), sb.bottom + dp(bottomDp));
                return insets;
            });
        } else if (Build.VERSION.SDK_INT >= 20) {
            v.setOnApplyWindowInsetsListener((view, insets) -> {
                view.setPadding(dp(leftDp), insets.getSystemWindowInsetTop() + dp(topDp), dp(rightDp), insets.getSystemWindowInsetBottom() + dp(bottomDp));
                return insets;
            });
        } else {
            v.setPadding(dp(leftDp), dp(topDp), dp(rightDp), dp(bottomDp));
        }
    }

    static GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    static GradientDrawable strokeBg(int color, int radius, int stroke) {
        GradientDrawable d = bg(color, radius);
        d.setStroke(dp(1), stroke);
        return d;
    }

    static TextView text(String s, int size, boolean bold) {
        TextView t = new TextView(App.ctx);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(INK);
        if (bold) t.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        t.setIncludeFontPadding(true);
        t.setPadding(0, dp(3), 0, dp(3));
        return t;
    }

    static LinearLayout column() {
        LinearLayout l = new LinearLayout(App.ctx);
        l.setOrientation(1);
        applyInsets(l, 18, 16, 18, 16);
        l.setBackgroundColor(WHITE);
        return l;
    }

    static void heading(LinearLayout l, String title, String subtitle) {
        TextView a = text(title, 27, true);
        l.addView(a);
        TextView b = text(subtitle, 14, false);
        b.setTextColor(MUTED);
        l.addView(b);
        space(l, 12);
    }

    static void space(LinearLayout l, int h) {
        View v = new View(App.ctx);
        l.addView(v, new LinearLayout.LayoutParams(1, dp(h)));
    }

    static Button button(String label) {
        Button b = new Button(App.ctx);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextSize(14);
        b.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        b.setTextColor(WHITE);
        b.setMinHeight(dp(46));
        b.setBackground(bg(PRIMARY, 15));
        b.setPadding(dp(14), 0, dp(14), 0);
        if (android.os.Build.VERSION.SDK_INT >= 21) b.setElevation(dp(1));
        return b;
    }

    static Button smallButton(String label) {
        Button b = button(label);
        b.setTextSize(12);
        b.setMinHeight(dp(38));
        b.setPadding(dp(10), 0, dp(10), 0);
        return b;
    }

    static EditText field(String hint) {
        EditText e = new EditText(App.ctx);
        e.setSingleLine(true);
        e.setTextSize(15);
        e.setHint(hint);
        e.setHintTextColor(MUTED);
        e.setTextColor(INK);
        e.setPadding(dp(14), dp(11), dp(14), dp(11));
        e.setBackground(bg(LAV, 13));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(10);
        e.setLayoutParams(p);
        return e;
    }

    static LinearLayout card() {
        LinearLayout c = new LinearLayout(App.ctx);
        c.setOrientation(1);
        c.setPadding(dp(14), dp(13), dp(14), dp(13));
        c.setBackground(strokeBg(WHITE, 18, BORDER));
        if (android.os.Build.VERSION.SDK_INT >= 21) c.setElevation(dp(2));
        return c;
    }

    static void card(LinearLayout l, String title, String desc, Runnable action) {
        LinearLayout c = card();
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(10);
        c.setLayoutParams(p);
        c.addView(text(title, 16, true));
        TextView d = text(desc, 13, false);
        d.setTextColor(MUTED);
        c.addView(d);
        if (action != null) c.setOnClickListener(v -> action.run());
        l.addView(c);
        animate(c);
    }

    static void animate(View v) {
        v.setAlpha(0f);
        v.setTranslationY(dp(8));
        v.animate().alpha(1f).translationY(0).setDuration(260).start();
    }

    static void toast(String s) {
        Toast.makeText(App.ctx, s, Toast.LENGTH_SHORT).show();
    }
}
