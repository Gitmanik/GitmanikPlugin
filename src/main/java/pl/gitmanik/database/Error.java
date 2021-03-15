package pl.gitmanik.database;

import pl.gitmanik.GitmanikPlugin;

import java.util.logging.Level;

public class Error {
    public static void execute(GitmanikPlugin plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(GitmanikPlugin plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}