package com.example.loginpage.ui.catalog;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loginpage.R;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProductDescriptionFormatter {
    private static final Pattern NUMBERED_HEADING = Pattern.compile("^\\d+[.)]\\s+.*");
    private static final Pattern BULLET = Pattern.compile("^[•*-]\\s+.*");
    private static final Pattern INLINE_LABEL = Pattern.compile("(?m)^[^:\\n]{2,40}:");

    private ProductDescriptionFormatter() {}

    public static void render(Context context, LinearLayout container, String description) {
        container.removeAllViews();
        if (description == null || description.trim().isEmpty()) {
            addSection(context, container, null, "Chưa có mô tả sản phẩm.");
            return;
        }

        String heading = null;
        StringBuilder body = new StringBuilder();
        String text = description.replace("\r\n", "\n").trim();
        for (String rawLine : text.split("\n")) {
            String line = rawLine.trim();
            if (isSectionHeading(line)) {
                if (heading != null || body.length() > 0) {
                    addSection(context, container, heading, body.toString());
                    body.setLength(0);
                }
                heading = line;
            } else {
                body.append(line).append('\n');
            }
        }
        addSection(context, container, heading, body.toString());
    }

    private static boolean isSectionHeading(String line) {
        if (line.length() < 5 || line.length() > 80) return false;
        boolean hasLetter = false;
        for (int index = 0; index < line.length(); index++) {
            if (Character.isLetter(line.charAt(index))) {
                hasLetter = true;
                break;
            }
        }
        return hasLetter && line.equals(line.toUpperCase(Locale.forLanguageTag("vi-VN")));
    }

    public static void addSection(Context context, LinearLayout container,
                                   String heading, String body) {
        View section = LayoutInflater.from(context)
                .inflate(R.layout.item_product_description_section, container, false);
        TextView title = section.findViewById(R.id.sectionHeading);
        View accent = section.findViewById(R.id.sectionAccent);
        TextView content = section.findViewById(R.id.sectionBody);

        if (heading == null || heading.isEmpty()) {
            title.setVisibility(View.GONE);
            accent.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) content.getLayoutParams();
            params.topMargin = 0;
            content.setLayoutParams(params);
        } else {
            title.setText(heading);
        }
        content.setText(formatBody(context, body));
        container.addView(section);
    }

    private static CharSequence formatBody(Context context, String description) {
        String text = description.trim()
                .replaceAll("[ \\t]+(?=\\d+[.)][ \\t]+)", "\n")
                .replaceAll("[ \\t]+(?=•[ \\t]+)", "\n");
        SpannableStringBuilder result = new SpannableStringBuilder();
        int bulletIndent = (int) (24 * context.getResources().getDisplayMetrics().density);

        for (String rawLine : text.split("\n")) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                if (result.length() > 1 && result.charAt(result.length() - 2) != '\n') {
                    result.append('\n');
                }
                continue;
            }

            boolean numberedHeading = NUMBERED_HEADING.matcher(line).matches();
            boolean bullet = BULLET.matcher(line).matches();
            if (bullet) line = line.substring(1).trim();
            if (numberedHeading && result.length() > 0) result.append('\n');

            int start = result.length();
            result.append(line).append('\n');
            int end = result.length();

            if (numberedHeading) {
                result.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                result.setSpan(new RelativeSizeSpan(1.15f), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (bullet) {
                result.setSpan(new BulletSpan(bulletIndent), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        Matcher labels = INLINE_LABEL.matcher(result);
        while (labels.find()) {
            result.setSpan(new StyleSpan(Typeface.BOLD), labels.start(), labels.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return result;
    }
}
