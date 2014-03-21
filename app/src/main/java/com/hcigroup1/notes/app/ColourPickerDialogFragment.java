package com.hcigroup1.notes.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

/**
 * Created by ray on 3/20/14.
 */
public class ColourPickerDialogFragment extends DialogFragment
{
    public interface ColourPickerDialogListener
    {
        public void onDialogClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ColourPickerDialogListener mListener;

    protected int selectedColour;
    protected CharSequence[] colourNames;
    protected int[] colourValues;
    final protected DialogFragment that = this;

    public ColourPickerDialogFragment()
    {
        super();
        colourNames = new CharSequence[]{"Black", "Red", "Green", "Blue"};
        colourValues = new int[] {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ColourPickerDialogListener) activity;
        }
        catch (ClassCastException e)
        {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement ColourPickerDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_colour)
                .setItems(colourNames, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        selectedColour = colourValues[which];
                        mListener.onDialogClick(that);
                    }
                });
        return builder.create();
    }

    public int getSelectedColour()
    {
        return selectedColour;
    }
}
