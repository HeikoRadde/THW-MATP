package thw_matp.ctrl;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {

    public Path get_path_db() {
        return this.path_db;
    }

    public void set_path_db(Path new_path) {
        this.path_db = new_path;
    }
    public Path get_path_protocols() {
        return this.path_protocols;
    }

    public void set_path_protocols(Path new_path) {
        this.path_protocols = new_path;
    }

    public boolean startup_done() {
        return this.startup_done;
    }

    public void startup_done(boolean b) {
        this.startup_done = b;
    }

    // SINGLETON mechanic

    private static final class InstanceHolder {
        static final Settings INSTANCE = new Settings();
    }

    private Settings() {
        this.path_db = Paths.get("").toAbsolutePath();
        this.path_protocols = Paths.get("").toAbsolutePath();
        this.startup_done = false;
    }

    public static Settings getInstance() {
        return InstanceHolder.INSTANCE;
    }


    private Path path_db;
    private Path path_protocols;
    private boolean startup_done;
}
