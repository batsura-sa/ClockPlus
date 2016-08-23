package com.philliphsu.clock2.alarms;

import android.view.ViewGroup;
import android.widget.TextView;

import com.philliphsu.clock2.Alarm;
import com.philliphsu.clock2.DaysOfWeek;
import com.philliphsu.clock2.OnListItemInteractionListener;
import com.philliphsu.clock2.R;
import com.philliphsu.clock2.util.AlarmController;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.philliphsu.clock2.DaysOfWeek.NUM_DAYS;

/**
 * Created by Phillip Hsu on 5/31/2016.
 */
public class CollapsedAlarmViewHolder extends BaseAlarmViewHolder {

    @Bind(R.id.countdown) AlarmCountdown mCountdown;
    @Bind(R.id.recurring_days) TextView mDays; // TODO: use `new DateFormatSymbols().getShortWeekdays()` to set texts

    @Deprecated // TODO: Delete this, the only usage is from AlarmsAdapter (SortedList), which is not used anymore.
    public CollapsedAlarmViewHolder(ViewGroup parent, OnListItemInteractionListener<Alarm> listener) {
        super(parent, R.layout.item_collapsed_alarm, listener, null);
    }

    public CollapsedAlarmViewHolder(ViewGroup parent, OnListItemInteractionListener<Alarm> listener,
                                    AlarmController alarmController) {
        super(parent, R.layout.item_collapsed_alarm, listener, alarmController);
    }

    @Override
    public void onBind(Alarm alarm) {
        super.onBind(alarm);
        // TOneverDO: do custom binding before super call, or else NPEs.
        bindCountdown(alarm.isEnabled(), alarm.ringsAt());
        bindDays(alarm);
    }

    private void bindCountdown(boolean enabled, long ringsAt) {
        if (enabled) {
            mCountdown.setBase(ringsAt);
            mCountdown.start();
            mCountdown.setVisibility(VISIBLE);
        } else {
            mCountdown.stop();
            mCountdown.setVisibility(GONE);
        }
    }

    @Override
    protected void bindLabel(boolean visible, String label) {
        // Should also be visible even if label has zero length so mCountdown is properly positioned
        // next to mLabel. That is, mCountdown's layout position is dependent on mLabel being present.

        // The countdown is visible if the alarm is enabled. We must keep this invariant in sync
        // with our bindCountdown() logic. If we test against the
        // visibility of the countdown view itself, we will find it is always visible
        // at this point, because bindCountdown() has not been called yet. As such, that is
        // not a valid solution. We unfortunately
        // cannot change the order of the view binding done in onBind().
        super.bindLabel(visible || getAlarm().isEnabled(), label);
    }

    private void bindDays(Alarm alarm) {
        int num = alarm.numRecurringDays();
        String text;
        if (num == NUM_DAYS) {
            text = getContext().getString(R.string.every_day);
        } else if (num == 0) {
            text = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0 /* Ordinal days*/; i < NUM_DAYS; i++) {
                // What day is at this position in the week?
                int weekDay = DaysOfWeek.getInstance(getContext()).weekDayAt(i);
                if (alarm.isRecurring(weekDay)) {
                    sb.append(DaysOfWeek.getLabel(weekDay)).append(", ");
                }
            }
            // Cut off the last comma and space
            sb.delete(sb.length() - 2, sb.length());
            text = sb.toString();
        }
        bindDays(num > 0, text);
    }

    private void bindDays(boolean visible, String text) {
        setVisibility(mDays, visible);
        mDays.setText(text);
    }
}