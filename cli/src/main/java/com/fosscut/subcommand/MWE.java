package com.fosscut.subcommand;

import com.fosscut.FossCut;
import com.fosscut.mwe.OrTools;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.PropertiesVersionProvider;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(name = "mwe", versionProvider = PropertiesVersionProvider.class)
public class MWE {

    @ParentCommand
    private FossCut fossCut;

    @Command(name = "ortools")
    public void status() {
        LogFormatter logFormatter = new LogFormatter(fossCut.getQuietModeRequested());
        logFormatter.configure();

        OrTools.main();
    }

}
