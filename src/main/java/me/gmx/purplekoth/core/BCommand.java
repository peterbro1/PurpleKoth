package me.gmx.purplekoth.core;


import me.gmx.purplekoth.PurpleKoth;

import java.util.ArrayList;

public class BCommand {

    public PurpleKoth main;
    public ArrayList<BSubCommand> subcommands;

    public BCommand(PurpleKoth ins) {
        this.main = ins;
        subcommands = new ArrayList<BSubCommand>();
    }

}
