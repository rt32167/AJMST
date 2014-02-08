package com.ajmst.android.ui;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class TableLayoutUtil {
	public static void append(TableLayout table, List<String> values) {
		table.setStretchAllColumns(true);
		Context ct = table.getContext();
		TableRow tablerow = new TableRow(ct);

		for (int i = 0; i < values.size(); i++) {
			TextView testview = new TextView(ct);
			testview.setText(values.get(i));
			testview.setGravity(Gravity.LEFT);
			testview.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			tablerow.addView(testview);
		}
		table.addView(tablerow);
/*		table.addView(tablerow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));*/
	}
}
