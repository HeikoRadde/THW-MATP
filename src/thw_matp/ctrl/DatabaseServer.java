package thw_matp.ctrl;

import org.h2.tools.Server;

import java.sql.SQLException;

public class DatabaseServer extends Database {

    public DatabaseServer(String db_name) throws SQLException {
        super();
        this.m_server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-baseDir", get_db_full_path("")).start();
        connect(get_url(), db_name);
        Settings.getInstance().set_remote(this.m_server.getURL().substring(this.m_server.getURL().lastIndexOf('/')+1, this.m_server.getURL().lastIndexOf(':')), Integer.toString(this.m_server.getPort()));
    }

    public void shutdown() {
        this.m_server.shutdown();
    }

    public String get_url() {
        System.out.println("URL: " + this.m_server.getURL());
        return this.m_server.getURL();
    }

    private Server m_server;
}
