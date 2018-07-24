package com.example.iz_test.handzforhire;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		//System.out.println("hhhhhhhhhhhhh:headerTitle:::"+headerTitle);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		if(groupPosition % 8 == 0) {
			convertView.setBackgroundColor(Color.parseColor("#FF87FA"));
		}
		if(groupPosition % 8 == 1) {
			convertView.setBackgroundColor(Color.parseColor("#BED2EA"));
		}
		if(groupPosition % 8 == 2) {
			convertView.setBackgroundColor(Color.parseColor("#FF4B13"));
		}
		if(groupPosition % 8 == 3) {
			convertView.setBackgroundColor(Color.parseColor("#FFFB86"));
		}
		if(groupPosition % 8 == 4) {
			convertView.setBackgroundColor(Color.parseColor("#00D034"));
		}
		if(groupPosition % 8 == 5) {
			convertView.setBackgroundColor(Color.parseColor("#FFC834"));
		}
		if(groupPosition % 8 == 6) {
			convertView.setBackgroundColor(Color.parseColor("#AA84FA"));
		}
		if(groupPosition % 8 == 7) {
			convertView.setBackgroundColor(Color.parseColor("#6BAEFB"));
		}
		/*TextView lblListHeader1 = (TextView) convertView.findViewById(R.id.lblListHeader1);
		lblListHeader1.setTypeface(null, Typeface.BOLD);
		lblListHeader1.setText("COACHING");
		TextView lblListHeader2 = (TextView) convertView.findViewById(R.id.lblListHeader2);
		lblListHeader2.setTypeface(null, Typeface.BOLD);
		lblListHeader2.setText("HOLIDAYS");
		TextView lblListHeader3 = (TextView) convertView.findViewById(R.id.lblListHeader3);
		lblListHeader3.setTypeface(null, Typeface.BOLD);
		lblListHeader3.setText("INSIDE THE HOME");
		TextView lblListHeader4 = (TextView) convertView.findViewById(R.id.lblListHeader4);
		lblListHeader4.setTypeface(null, Typeface.BOLD);
		lblListHeader4.setText("OUTSIDE THE HOME");
		TextView lblListHeader5 = (TextView) convertView.findViewById(R.id.lblListHeader5);
		lblListHeader5.setTypeface(null, Typeface.BOLD);
		lblListHeader5.setText("PERSONAL SERVICES");
		TextView lblListHeader6 = (TextView) convertView.findViewById(R.id.lblListHeader6);
		lblListHeader6.setTypeface(null, Typeface.BOLD);
		lblListHeader6.setText("PET CARE");
		TextView lblListHeader7 = (TextView) convertView.findViewById(R.id.lblListHeader7);
		lblListHeader7.setTypeface(null, Typeface.BOLD);
		lblListHeader7.setText("TUTORING");*/

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
