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

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Database class which is also available via a network connection
 */
public class DatabaseServer extends Database {

    /**
     *                      Initialise with a local database, while being accessable via a network connection
     * @param db_name       Name of the database to use. The path to it is retrieved from {@link thw_matp.ctrl.Settings}
     * @throws SQLException
     */
    public DatabaseServer(String db_name) throws SQLException {
        super(db_name);

        this.m_server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-baseDir", get_db_full_path("")).start();
        connect(get_url(), db_name);
        Settings.getInstance().set_remote(this.m_server.getURL().substring(this.m_server.getURL().lastIndexOf('/')+1, this.m_server.getURL().lastIndexOf(':')), Integer.toString(this.m_server.getPort()));
    }

    /**
     * Close the database
     */
    public void shutdown() {
        this.m_server.shutdown();
    }

    /**
     * @return Get the URL to the opened database
     */
    public String get_url() {
        System.out.println("URL: " + this.m_server.getURL());
        return this.m_server.getURL();
    }

    private Server m_server;
}
