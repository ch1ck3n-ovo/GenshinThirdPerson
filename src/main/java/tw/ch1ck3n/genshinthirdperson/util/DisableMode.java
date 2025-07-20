package tw.ch1ck3n.genshinthirdperson.util;

public enum DisableMode {

    SKIP("skip"), DISABLE("disable");

    public final String key;
    DisableMode(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "text.autoconfig.genshinthirdperson.option.thirdPersonFrontView.disableMode." + this.key;
    }
}