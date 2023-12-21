package com.shanebeestudios.backup;

import com.shanebeestudios.backup.utils.ZipFiles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@SuppressWarnings("unused")
public class ServerBackup extends JavaPlugin {

    private static CommandSender console;

    @Override
    public void onEnable() {
        console = Bukkit.getConsoleSender();
        getCommand("zipbackup").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        long start = System.currentTimeMillis();
        ZipFiles zipper = new ZipFiles();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            zipper.zipDirectory(new File("."), "server-backup.zip");
            long finish = System.currentTimeMillis() - start;
            print(sender, "&6Finished zipping in &b" + finish / 1000 + " seconds");
        });

        return true;
    }

    public static void print(String message) {
        print(console, message);
    }

    @SuppressWarnings("deprecation")
    public static void print(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
