package com.cs7035.yummcart;

import android.app.*;
import android.os.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.content.ContentValues;

import java.util.*;

public class FoodActivity extends Activity {
    Db db;
    LinearLayout root, body;
    String section = "Home", activeFilter = "";
    ImageView profileImage;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        db = new Db(this);
        showSplash();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "User Profile");
        menu.add(0, 2, 1, "About Us");
        menu.add(0, 3, 2, "Settings");
        menu.add(0, 4, 3, "Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                section = "Account";
                render();
                return true;
            case 2:
                new AlertDialog.Builder(this).setTitle("About YummCart").setMessage("Good food, thoughtfully delivered. This student project demonstrates Android navigation, SQLite, camera integration and order management.").setPositiveButton("OK", null).show();
                return true;
            case 3:
                new AlertDialog.Builder(this).setTitle("Settings").setMessage("Your demo data is stored locally on this device. No payment details are collected.").setPositiveButton("OK", null).show();
                return true;
            case 4:
                new AlertDialog.Builder(this).setTitle("Log out?").setMessage("Are you sure you want to logout?").setNegativeButton("No", null).setPositiveButton("Yes", (d, w) -> finishAffinity()).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void showSplash() {
        LinearLayout s = Ui.column();
        s.setGravity(Gravity.CENTER);
        s.setBackgroundColor(Ui.PRIMARY);
        TextView mark = Ui.text("y.", 64, true);
        mark.setTextColor(Ui.LIME);
        mark.setGravity(Gravity.CENTER);
        s.addView(mark);
        TextView name = Ui.text("YummCart", 32, true);
        name.setTextColor(Ui.WHITE);
        name.setGravity(Gravity.CENTER);
        s.addView(name);
        TextView tag = Ui.text("GOOD FOOD. BETTER MOOD.", 12, true);
        tag.setTextColor(Ui.MINT);
        tag.setGravity(Gravity.CENTER);
        s.addView(tag);
        Ui.space(s, 22);
        ProgressBar progress = new ProgressBar(this);
        progress.setIndeterminateTintList(android.content.res.ColorStateList.valueOf(Ui.LIME));
        s.addView(progress, new LinearLayout.LayoutParams(Ui.dp(44), Ui.dp(44)));
        setContentView(s);
        mark.setAlpha(0f);
        mark.setScaleX(.7f);
        mark.setScaleY(.7f);
        mark.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(650).start();
        new Handler().postDelayed(() -> render(), 1000);
    }

    void render() {
        if (getIntent().hasExtra("section")) {
            section = getIntent().getStringExtra("section");
            getIntent().removeExtra("section");
        }
        root = new LinearLayout(this);
        root.setOrientation(1);
        root.setBackgroundColor(Ui.WHITE);
        Ui.applyInsets(root, 16, 10, 16, 6);
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(0);
        LinearLayout brand = new LinearLayout(this);
        brand.setOrientation(1);
        TextView logo = Ui.text("YummCart", 25, true);
        logo.setTextColor(Ui.PRIMARY);
        brand.addView(logo);
        TextView tag = Ui.text("GOOD FOOD. BETTER MOOD.", 10, true);
        tag.setTextColor(Ui.MUTED);
        brand.addView(tag);
        header.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));
        TextView menu = Ui.text("•••", 25, true);
        menu.setTextColor(Ui.PRIMARY);
        menu.setGravity(Gravity.CENTER);
        menu.setBackground(Ui.bg(Ui.LAV, 30));
        header.addView(menu, new LinearLayout.LayoutParams(Ui.dp(46), Ui.dp(46)));
        menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add(0, 1, 0, "User Profile");
            popup.getMenu().add(0, 2, 1, "About Us");
            popup.getMenu().add(0, 3, 2, "Settings");
            popup.getMenu().add(0, 4, 3, "Logout");
            popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
            popup.show();
        });
        root.addView(header);
        Ui.space(root, 12);
        ScrollView sc = new ScrollView(this);
        sc.setFillViewport(false);
        body = new LinearLayout(this);
        body.setOrientation(1);
        body.setPadding(0, 0, 0, Ui.dp(12));
        sc.addView(body);
        root.addView(sc, new LinearLayout.LayoutParams(-1, 0, 1));
        if (section.equals("Home") || section.equals("Categories"))
            foods(section.equals("Categories") ? activeFilter : "");
        else if (section.equals("Cart")) showCart();
        else account();
        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(0);
        nav.setPadding(Ui.dp(5), Ui.dp(7), Ui.dp(5), Ui.dp(4));
        nav.setBackground(Ui.strokeBg(Ui.WHITE, 18, Ui.BORDER));
        String[] labels = {"⌂\nHome", "◈\nExplore", "▣\nCart", "●\nAccount"};
        String[] keys = {"Home", "Categories", "Cart", "Account"};
        for (int i = 0; i < keys.length; i++) {
            final String key = keys[i];
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(1);
            item.setGravity(Gravity.CENTER);
            item.setPadding(0, Ui.dp(3), 0, Ui.dp(3));
            TextView t = Ui.text(labels[i], 12, true);
            t.setGravity(Gravity.CENTER);
            t.setTextColor(section.equals(key) ? Ui.PRIMARY : Ui.MUTED);
            item.addView(t);
            if (section.equals(key)) {
                View dot = new View(this);
                dot.setBackground(Ui.bg(Ui.LIME, 4));
                LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(Ui.dp(18), Ui.dp(3));
                dp.topMargin = Ui.dp(3);
                item.addView(dot, dp);
            }
            nav.addView(item, new LinearLayout.LayoutParams(0, Ui.dp(47), 1));
            item.setOnClickListener(v -> {
                section = key;
                activeFilter = "";
                render();
            });
        }
        root.addView(nav);
        setContentView(root);
    }

    void foods(String filter) {
        if (section.equals("Home")) {
            LinearLayout hero = new LinearLayout(this);
            hero.setOrientation(1);
            hero.setPadding(Ui.dp(18), Ui.dp(18), Ui.dp(18), Ui.dp(17));
            hero.setBackground(Ui.bg(Ui.PRIMARY, 22));
            TextView eyebrow = Ui.text("YOUR NEXT FAVOURITE", 10, true);
            eyebrow.setTextColor(Ui.LIME);
            hero.addView(eyebrow);
            TextView title = Ui.text("Good bites.\nGreat days.", 28, true);
            title.setTextColor(Ui.WHITE);
            hero.addView(title);
            TextView sub = Ui.text("Freshly made favourites, picked for you.", 13, false);
            sub.setTextColor(Ui.MINT);
            hero.addView(sub);
            Ui.space(hero, 8);
            TextView cta = Ui.text("EXPLORE THE MENU     ↗", 12, true);
            cta.setTextColor(Ui.LIME);
            hero.addView(cta);
            hero.setOnClickListener(v -> {
                section = "Categories";
                activeFilter = "";
                render();
            });
            body.addView(hero);
            Ui.animate(hero);
            Ui.space(body, 18);
        }
        LinearLayout titleRow = new LinearLayout(this);
        titleRow.setGravity(Gravity.CENTER_VERTICAL);
        titleRow.setOrientation(0);
        TextView title = Ui.text(section.equals("Categories") ? "Explore menu" : "Popular right now", 21, true);
        titleRow.addView(title, new LinearLayout.LayoutParams(0, -2, 1));
        TextView count = Ui.text("FRESH PICKS", 10, true);
        count.setTextColor(Ui.GREEN);
        titleRow.addView(count);
        body.addView(titleRow);
        Ui.space(body, 9);
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.setHorizontalScrollBarEnabled(false);
        LinearLayout cats = new LinearLayout(this);
        cats.setOrientation(0);
        for (String cat : new String[]{"All", "Pizza", "Burger", "Meals", "Dessert", "Drinks"}) {
            TextView chip = Ui.text(cat, 13, true);
            boolean selected = (filter.isEmpty() && cat.equals("All")) || filter.equals(cat);
            chip.setTextColor(selected ? Ui.WHITE : Ui.PRIMARY);
            chip.setPadding(Ui.dp(15), Ui.dp(10), Ui.dp(15), Ui.dp(10));
            chip.setBackground(Ui.bg(selected ? Ui.PRIMARY : Ui.LAV, 24));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-2, -2);
            cp.rightMargin = Ui.dp(7);
            cats.addView(chip, cp);
            chip.setOnClickListener(v -> {
                section = "Categories";
                activeFilter = cat.equals("All") ? "" : cat;
                render();
            });
        }
        hsv.addView(cats);
        body.addView(hsv);
        Ui.space(body, 12);
        SQLiteDatabase d = db.getReadableDatabase();
        Cursor c = d.rawQuery("SELECT _id,name,category,price FROM foods " + (filter.isEmpty() ? "" : "WHERE category=? ") + "ORDER BY name", filter.isEmpty() ? null : new String[]{filter});
        int index = 0;
        while (c.moveToNext()) {
            String n = c.getString(1), cat = c.getString(2);
            double price = c.getDouble(3);
            productCard(n, cat, price, index++);
        }
        c.close();
        if (index == 0) body.addView(Ui.text("No dishes in this category yet.", 14, false));
    }

    String emoji(String cat, String name) {
        if (cat.equals("Pizza")) return "🍕";
        if (cat.equals("Burger")) return "🍔";
        if (cat.equals("Dessert")) return "🍰";
        if (cat.equals("Drinks")) return "🥤";
        if (name.toLowerCase().contains("pasta")) return "🍝";
        if (name.toLowerCase().contains("wrap")) return "🌯";
        return "🥗";
    }

    void productCard(String name, String category, double price, int index) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(0);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(Ui.dp(12), Ui.dp(11), Ui.dp(11), Ui.dp(11));
        card.setBackground(Ui.strokeBg(Ui.WHITE, 18, Ui.BORDER));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = Ui.dp(10);
        card.setLayoutParams(p);
        TextView picture = Ui.text(emoji(category, name), 34, true);
        picture.setGravity(Gravity.CENTER);
        picture.setBackground(Ui.bg(Ui.LAV, 16));
        card.addView(picture, new LinearLayout.LayoutParams(Ui.dp(76), Ui.dp(76)));
        LinearLayout details = new LinearLayout(this);
        details.setOrientation(1);
        details.setPadding(Ui.dp(12), 0, Ui.dp(6), 0);
        TextView cat = Ui.text(category.toUpperCase(), 9, true);
        cat.setTextColor(Ui.GREEN);
        details.addView(cat);
        details.addView(Ui.text(name, 16, true));
        TextView desc = Ui.text("Freshly prepared · chef's pick", 11, false);
        desc.setTextColor(Ui.MUTED);
        details.addView(desc);
        TextView cost = Ui.text("₹" + (int) price, 17, true);
        cost.setTextColor(Ui.PRIMARY);
        details.addView(cost);
        card.addView(details, new LinearLayout.LayoutParams(0, -2, 1));
        Button add = Ui.smallButton("+ Add");
        add.setTextColor(Ui.WHITE);
        card.addView(add);
        add.setOnClickListener(v -> {
            ContentValues cv = new ContentValues();
            cv.put("item", name);
            cv.put("qty", 1);
            db.getWritableDatabase().insertWithOnConflict("cart_items", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
            db.getWritableDatabase().execSQL("UPDATE cart_items SET qty=qty+1 WHERE item=?", new Object[]{name});
            Ui.toast(name + " added to cart");
            add.setText("✓ Added");
            add.animate().scaleX(1.06f).scaleY(1.06f).setDuration(120).withEndAction(() -> add.animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();
        });
        body.addView(card);
        Ui.animate(card);
    }

    void showCart() {
        Ui.heading(body, "Your basket", "The good stuff you've picked.");
        Cursor rows = db.getReadableDatabase().rawQuery("SELECT item,qty FROM cart_items ORDER BY item", null);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        while (rows.moveToNext()) {
            names.add(rows.getString(0));
            quantities.add(rows.getInt(1));
        }
        rows.close();
        double total = 0;
        for (int i = 0; i < names.size(); i++) {
            String n = names.get(i);
            int q = quantities.get(i);
            Cursor c = db.getReadableDatabase().rawQuery("SELECT category,price FROM foods WHERE name=?", new String[]{n});
            double price = 0;
            String cat = "Food";
            if (c.moveToFirst()) {
                cat = c.getString(0);
                price = c.getDouble(1);
            }
            c.close();
            total += price * q;
            LinearLayout card = Ui.card();
            card.setOrientation(0);
            card.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
            cp.bottomMargin = Ui.dp(10);
            card.setLayoutParams(cp);
            TextView em = Ui.text(emoji(cat, n), 30, true);
            card.addView(em);
            LinearLayout info = new LinearLayout(this);
            info.setOrientation(1);
            info.setPadding(Ui.dp(10), 0, Ui.dp(5), 0);
            info.addView(Ui.text(n, 15, true));
            TextView pr = Ui.text("₹" + (int) price + " each", 12, false);
            pr.setTextColor(Ui.MUTED);
            info.addView(pr);
            card.addView(info, new LinearLayout.LayoutParams(0, -2, 1));
            LinearLayout qty = new LinearLayout(this);
            qty.setGravity(Gravity.CENTER);
            qty.setBackground(Ui.bg(Ui.LAV, 22));
            TextView minus = Ui.text("−", 20, true);
            minus.setGravity(Gravity.CENTER);
            qty.addView(minus, new LinearLayout.LayoutParams(Ui.dp(28), Ui.dp(34)));
            TextView qv = Ui.text("" + q, 14, true);
            qv.setGravity(Gravity.CENTER);
            qty.addView(qv, new LinearLayout.LayoutParams(Ui.dp(20), Ui.dp(34)));
            TextView plus = Ui.text("+", 18, true);
            plus.setGravity(Gravity.CENTER);
            qty.addView(plus, new LinearLayout.LayoutParams(Ui.dp(28), Ui.dp(34)));
            card.addView(qty);
            minus.setOnClickListener(v -> {
                if (q <= 1)
                    db.getWritableDatabase().delete("cart_items", "item=?", new String[]{n});
                else
                    db.getWritableDatabase().execSQL("UPDATE cart_items SET qty=qty-1 WHERE item=?", new Object[]{n});
                render();
            });
            plus.setOnClickListener(v -> {
                db.getWritableDatabase().execSQL("UPDATE cart_items SET qty=qty+1 WHERE item=?", new Object[]{n});
                render();
            });
            body.addView(card);
        }
        if (names.isEmpty()) {
            LinearLayout empty = Ui.card();
            empty.setGravity(Gravity.CENTER);
            TextView icon = Ui.text("🧺", 46, true);
            icon.setGravity(Gravity.CENTER);
            empty.addView(icon);
            TextView title = Ui.text("Your basket is taking a break", 18, true);
            title.setGravity(Gravity.CENTER);
            empty.addView(title);
            TextView desc = Ui.text("Explore the menu and add something delicious.", 13, false);
            desc.setTextColor(Ui.MUTED);
            desc.setGravity(Gravity.CENTER);
            empty.addView(desc);
            Button browse = Ui.button("Explore menu");
            empty.addView(browse);
            browse.setOnClickListener(v -> {
                section = "Categories";
                render();
            });
            body.addView(empty);
        }
        Ui.space(body, 8);
        LinearLayout totalCard = Ui.card();
        TextView label = Ui.text("TOTAL", 10, true);
        label.setTextColor(Ui.MUTED);
        totalCard.addView(label);
        TextView totalText = Ui.text("₹" + (int) total, 27, true);
        totalText.setTextColor(Ui.PRIMARY);
        totalCard.addView(totalText);
        body.addView(totalCard);
        Ui.space(body, 10);
        Button checkout = Ui.button("Place order  →");
        body.addView(checkout);
        checkout.setOnClickListener(v -> {
            Cursor c = db.getReadableDatabase().rawQuery("SELECT item,qty FROM cart_items", null);
            ArrayList<String> items = new ArrayList<>();
            ArrayList<Integer> qtys = new ArrayList<>();
            while (c.moveToNext()) {
                items.add(c.getString(0));
                qtys.add(c.getInt(1));
            }
            c.close();
            if (items.isEmpty()) {
                Ui.toast("Add an item before checkout");
                return;
            }
            SQLiteDatabase d = db.getWritableDatabase();
            for (int i = 0; i < items.size(); i++) {
                String n = items.get(i);
                Cursor pc = d.rawQuery("SELECT price FROM foods WHERE name=?", new String[]{n});
                double price = 0;
                if (pc.moveToFirst()) price = pc.getDouble(0);
                pc.close();
                ContentValues cv = new ContentValues();
                cv.put("item", n);
                cv.put("qty", qtys.get(i));
                cv.put("price", price);
                d.insert("orders", null, cv);
            }
            d.delete("cart_items", null, null);
            new AlertDialog.Builder(this).setTitle("Order confirmed ✓").setMessage("Your demo order has been saved on this device.").setPositiveButton("Continue shopping", (a, b) -> {
                section = "Home";
                render();
            }).show();
        });
    }

    void account() {
        Ui.heading(body, "Your profile", "Your details for a smoother checkout.");
        LinearLayout avatar = Ui.card();
        avatar.setGravity(Gravity.CENTER);
        avatar.setPadding(Ui.dp(18), Ui.dp(18), Ui.dp(18), Ui.dp(18));
        profileImage = new ImageView(this);
        profileImage.setImageResource(android.R.drawable.ic_menu_camera);
        profileImage.setBackground(Ui.bg(Ui.MINT, 50));
        profileImage.setPadding(Ui.dp(20), Ui.dp(20), Ui.dp(20), Ui.dp(20));
        avatar.addView(profileImage, new LinearLayout.LayoutParams(Ui.dp(92), Ui.dp(92)));
        TextView picture = Ui.text("Add a profile photo", 13, true);
        picture.setTextColor(Ui.PRIMARY);
        avatar.addView(picture);
        body.addView(avatar);
        Ui.space(body, 12);
        EditText name = Ui.field("Full name");
        body.addView(name);
        EditText email = Ui.field("Email address");
        email.setInputType(33);
        body.addView(email);
        EditText address = Ui.field("Delivery address");
        body.addView(address);
        EditText phone = Ui.field("Contact number");
        phone.setInputType(3);
        body.addView(phone);
        EditText dob = Ui.field("Date of Birth (DD/MM/YYYY)");
        body.addView(dob);
        Button save = Ui.button("Save profile");
        body.addView(save);
        save.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String e = email.getText().toString().trim();
            if (n.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                Ui.toast("Enter your name and a valid email");
                return;
            }
            String a = address.getText().toString().trim();
            String c = phone.getText().toString().trim();
            String d = dob.getText().toString().trim();

            SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
            sp.edit().putString("name", n).putString("email", e).putString("address", a).putString("contact", c).putString("dob", d).apply();

            ContentValues cv = new ContentValues();
            cv.put("name", n);
            cv.put("email", e);
            cv.put("address", a);
            cv.put("contact", c);
            cv.put("dob", d);
            db.getWritableDatabase().insertWithOnConflict("users", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Ui.toast("Profile saved successfully ✓");
        });
        Ui.space(body, 8);
        Button camera = Ui.smallButton("Capture profile picture");
        body.addView(camera);
        camera.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 101);
                return;
            }
            openCamera();
        });

        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if (sp.contains("name")) {
            name.setText(sp.getString("name", ""));
            email.setText(sp.getString("email", ""));
            address.setText(sp.getString("address", ""));
            phone.setText(sp.getString("contact", ""));
            dob.setText(sp.getString("dob", ""));
        } else {
            Cursor user = db.getReadableDatabase().rawQuery("SELECT name,email,address,contact,dob FROM users ORDER BY _id DESC LIMIT 1", null);
            if (user.moveToFirst()) {
                name.setText(user.getString(0));
                email.setText(user.getString(1));
                address.setText(user.getString(2));
                phone.setText(user.getString(3));
                dob.setText(user.getString(4));
            }
            user.close();
        }

        String savedPhoto = sp.getString("profile_photo", null);
        if (savedPhoto != null) {
            try {
                byte[] photoBytes = android.util.Base64.decode(savedPhoto, android.util.Base64.DEFAULT);
                android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
                if (bmp != null) {
                    profileImage.setImageBitmap(bmp);
                    profileImage.setBackground(null);
                    profileImage.setPadding(0, 0, 0, 0);
                }
            } catch (Exception ignored) {
            }
        }
    }

    void openCamera() {
        try {
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, 22);
        } catch (Exception e) {
            Ui.toast("Camera not available on this device");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else if (requestCode == 101) {
            Ui.toast("Camera permission denied");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Object o = data.getExtras().get("data");
            if (o instanceof android.graphics.Bitmap) {
                android.graphics.Bitmap bmp = (android.graphics.Bitmap) o;
                if (profileImage != null) {
                    profileImage.setImageBitmap(bmp);
                    profileImage.setBackground(null);
                    profileImage.setPadding(0, 0, 0, 0);
                }
                java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
                bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream);
                byte[] photoBytes = stream.toByteArray();
                String encoded = android.util.Base64.encodeToString(photoBytes, android.util.Base64.DEFAULT);
                getSharedPreferences("user_prefs", MODE_PRIVATE).edit().putString("profile_photo", encoded).apply();

                ContentValues cv = new ContentValues();
                cv.put("photo", photoBytes);
                db.getWritableDatabase().update("users", cv, null, null);
                Ui.toast("Profile photo updated ✓");
            }
        }
    }
}
