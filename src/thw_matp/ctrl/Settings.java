/*
    Copyright (c) 2021 Heiko Radde
    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
    documentation files (the "Software"), to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
    to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of
    the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
    THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package thw_matp.ctrl;

import javax.print.PrintService;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Singleton class for settings of the program
 */
public class Settings {

    /**
     * @return Path to the database to use
     */
    public Path get_path_db() {
        return this.path_db;
    }

    /**
     * @param new_path Update the path to the database to use
     */
    public void set_path_db(Path new_path) {
        this.path_db = new_path;
    }

    /**
     * @return Path to the location where to save the generated protocols
     */
    public Path get_path_protocols() {
        return this.path_protocols;
    }

    /**
     * @param new_path Update the path to the location where to save the generated protocols
     */
    public void set_path_protocols(Path new_path) {
        this.path_protocols = new_path;
    }

    /**
     * @return Check if the startup is complete
     */
    public boolean startup_done() {
        return this.startup_done;
    }

    /**
     * @param b Set if the startup is complete
     */
    public void startup_done(boolean b) {
        this.startup_done = b;
    }

    /**
     * @param b Set if the database is local on the PC or remote on another PC
     */
    public void db_is_local(boolean b) {
        this.local_database = b;
    }

    /**
     * @return Check if the database is local on the PC or remote on another PC
     */
    public boolean db_is_local() {
        return this.local_database;
    }

    /**
     *              Set the connection info for remote database operations
     * @param ip    IP of the remote PC
     * @param port  Remote port to use
     */
    public void set_remote(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * @return Get the IP of the remote PC to use
     */
    public String get_ip() {
        return this.ip;
    }

    /**
     * @return Get the remote port to use
     */
    public String get_port() {
        return this.port;
    }

    public void set_printer(PrintService printer) {
        this.printer = printer;
    }

    public PrintService get_printer() {
        return this.printer;
    }

    public String get_version() {
        return this.version;
    }

    // SINGLETON mechanic

    private static final class InstanceHolder {
        static final Settings INSTANCE = new Settings();
    }

    /**
     * Class initialised with paths to the users home directory and with no network informations
     */
    private Settings() {
        this.path_db = Paths.get(System.getProperty("user.home")).toAbsolutePath();
        this.path_protocols = Paths.get(System.getProperty("user.home")).toAbsolutePath();
        this.startup_done = false;
        this.printer = null;
        this.version = "1.1.0";
    }

    /**
     * @return Get the singleton instance of this class
     */
    public static Settings getInstance() {
        return InstanceHolder.INSTANCE;
    }


    private Path path_db;
    private Path path_protocols;
    private boolean startup_done;
    private boolean local_database;
    private String ip;
    private String port;
    private PrintService printer;
    private String version;
}
