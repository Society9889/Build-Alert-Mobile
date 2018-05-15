package alert.build.mobilebuildalert.data;

/**
 * Created by evan on 10/30/2016.
 */
public class Build {

    private long buildNumber;
    private String status;

    public Build(){

    }

    public Build(Long BuildNumber, String Status ){
        this.buildNumber = BuildNumber;
        this.status = Status;
    }
    public long getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(long buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
