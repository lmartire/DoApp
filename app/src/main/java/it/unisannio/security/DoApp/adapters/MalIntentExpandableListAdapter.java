package it.unisannio.security.DoApp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import it.unisannio.security.DoApp.R;
import it.unisannio.security.DoApp.model.ExceptionReport;
import it.unisannio.security.DoApp.model.MalIntent;

/**
 * Created by antonio on 27/01/17.
 */

public class MalIntentExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> exceptionTypes;
    private HashMap<String, List<MalIntent>> mapReports;

    public MalIntentExpandableListAdapter(Context context, List<String> exceptionTypes, HashMap<String, List<MalIntent>> reports){
        this.context = context;
        this.exceptionTypes = exceptionTypes;
        this.mapReports = reports;
    }

    @Override
    public int getGroupCount() {
        return this.exceptionTypes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mapReports.get(this.exceptionTypes.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.exceptionTypes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mapReports.get(this.exceptionTypes.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_string_entry_generic, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textViewEntry);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = ((MalIntent) getChild(groupPosition, childPosition)).toString();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_string_entry_generic, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.textViewEntry);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
