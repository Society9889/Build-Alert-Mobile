package alert.build.mobilebuildalert;

import com.firebase.client.Firebase;

/**
 * Created by evan on 10/10/2016.
 */
public class MobileBuildAlert extends android.app.Application{

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
