package alert.build.mobilebuildalert;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by evan on 10/30/2016.
 */
public class PastBuildsViewHolder extends RecyclerView.ViewHolder
{
    TextView buildNumber;
    TextView result;
    ImageView icon;

    public PastBuildsViewHolder(View v) {
        super(v);
        buildNumber = (TextView) v.findViewById(R.id.past_build_number);
        result = (TextView) v.findViewById(R.id.past_result);
        icon = (ImageView) v.findViewById(R.id.status_icon);

    }
}
