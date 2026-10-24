package com.cs7035.yummcart;

import android.app.*;
import android.os.*;
import android.content.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        show();
    }

    void show() {
        LinearLayout l = Ui.column();
        l.setGravity(Gravity.CENTER_VERTICAL);
        l.setBackground(Ui.bg(Ui.WHITE, 0));
        TextView mark = Ui.text("yumm.", 48, true);
        mark.setTextColor(Ui.PRIMARY);
        l.addView(mark);
        TextView eyebrow = Ui.text("GOOD FOOD, BETTER MOOD", 12, true);
        eyebrow.setTextColor(Ui.GREEN);
        l.addView(eyebrow);
        Ui.space(l, 28);
        TextView hero = Ui.text("Your next\nfavourite bite.", 36, true);
        l.addView(hero);
        TextView sub = Ui.text("Fresh picks, comfort classics and little treats — made for your cravings.", 16, false);
        sub.setTextColor(Ui.MUTED);
        l.addView(sub);
        Ui.space(l, 26);
        Ui.card(l, "TODAY'S KITCHEN PICK  ✦", "Hand-picked dishes made for your cravings.", null);
        Ui.space(l, 8);
        Button signIn = Ui.button("Sign in");
        l.addView(signIn);
        signIn.setOnClickListener(v -> startActivity(new Intent(this, AuthActivity.class).putExtra("signup", false)));
        Button signUp = Ui.button("Create an account");
        signUp.setBackground(Ui.strokeBg(Ui.MINT, 15, Ui.BORDER));
        signUp.setTextColor(Ui.PRIMARY);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.topMargin = Ui.dp(8);
        l.addView(signUp, sp);
        signUp.setOnClickListener(v -> startActivity(new Intent(this, AuthActivity.class).putExtra("signup", true)));
        Ui.space(l, 12);
        TextView foot = Ui.text("CS7035  ·  Practicals 2–6 & 8", 12, false);
        foot.setTextColor(Ui.MUTED);
        l.addView(foot);
        ScrollView sc = new ScrollView(this);
        sc.addView(l);
        setContentView(sc);
        Ui.animate(mark);
        Ui.animate(sub);
    }
}
