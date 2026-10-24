package com.cs7035.yummcart;

import android.app.*;
import android.os.*;
import android.content.*;
import android.view.*;
import android.widget.*;

public class AuthActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        show(getIntent().getBooleanExtra("signup", false));
    }

    void show(boolean signup) {
        LinearLayout l = Ui.column();
        TextView logo = Ui.text("yumm.", 34, true);
        logo.setTextColor(Ui.PRIMARY);
        l.addView(logo);
        Ui.heading(l, signup ? "Create your account" : "Welcome back", "A little good food is just around the corner.");
        EditText name = null;
        if (signup) {
            name = Ui.field("Full name");
            l.addView(name);
        }
        EditText email = Ui.field("Email address");
        email.setInputType(33);
        l.addView(email);
        EditText pass = Ui.field("Password (4+ characters)");
        pass.setInputType(129);
        l.addView(pass);
        EditText confirm = null;
        if (signup) {
            confirm = Ui.field("Confirm password");
            confirm.setInputType(129);
            l.addView(confirm);
        }
        final EditText nm = name, cf = confirm;
        Button go = Ui.button(signup ? "Create account" : "Sign In");
        l.addView(go);
        go.setOnClickListener(v -> {
            String em = email.getText().toString().trim(), pw = pass.getText().toString();
            if (signup && (nm.getText().toString().trim().isEmpty() || !nm.getText().toString().trim().matches("[A-Za-z .'-]{2,60}"))) {
                Ui.toast("Enter your name using letters");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(em).matches() || pw.length() < 4) {
                em = "demo@yummcart.com";
                email.setText(em);
                pass.setText("1234");
            }
            if (signup && !pw.equals(cf.getText().toString())) {
                Ui.toast("Passwords do not match");
                return;
            }
            new AlertDialog.Builder(this).setTitle(signup ? "Account created" : "You're signed in").setMessage("Academic demo authentication successful.").setPositiveButton("Continue", (d, w) -> {
                startActivity(new Intent(this, FoodActivity.class));
                finish();
            }).show();
        });
        TextView switcher = Ui.text(signup ? "Already have an account? Sign In" : "New to YummCart? Create an account", 14, true);
        switcher.setTextColor(Ui.GREEN);
        l.addView(switcher);
        switcher.setOnClickListener(v -> {
            Intent i = new Intent(this, AuthActivity.class).putExtra("signup", !signup);
            finish();
            startActivity(i);
        });
        ScrollView sc = new ScrollView(this);
        sc.setFillViewport(true);
        sc.addView(l);
        setContentView(sc);
    }
}
