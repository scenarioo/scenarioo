package org.scenarioo.validator;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * This can also be used as command line tool in order to validate any scenarioo documentation folder (e.g. produced by another writer API like scenarioo-js)
 */
public class ScenariooValidatorRunner {

    private static final Logger LOGGER = Logger.getLogger(ScenariooValidatorRunner.class);

    public static void main(String[] args) throws InterruptedException, ParseException {

        CommandLine line = parseLineOrThrow(args);

        File docuDirectory = new File(line.getArgList().get(0));
        boolean doCleanDerivedFiles = line.hasOption("c");

        boolean successful = new ScenariooValidator(docuDirectory, doCleanDerivedFiles).validate();

        if (!successful) {
            throw new RuntimeException("Validation failed. See log output for more information!");
        } else {
            LOGGER.info(String.format("Validation successful for %s", docuDirectory.getAbsolutePath()));
        }
    }

    private static CommandLine parseLineOrThrow(String[] args) throws ParseException {

        Options options = new Options();
        options.addOption(Option.builder("c")
                .hasArg(false)
                .longOpt("clean-derived")
                .desc("If set, derived files will be deleted before validation")
                .optionalArg(true)
                .build());

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);

            if (line.getArgs().length < 1) {
                throw new ParseException("Please specify input directory");
            }

            return line;
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("scenarioo-validator [-c] <pathToDirectory>", options);
            throw pe;
        }
    }

}
