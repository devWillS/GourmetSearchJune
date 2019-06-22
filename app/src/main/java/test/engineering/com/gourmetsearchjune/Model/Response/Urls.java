package test.engineering.com.gourmetsearchjune.Model.Response;

import java.io.Serializable;

public class Urls implements Serializable {

    private String pc;

    public Urls() {

    }

    public Urls(String pc) {
        this.pc = pc;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }


}
