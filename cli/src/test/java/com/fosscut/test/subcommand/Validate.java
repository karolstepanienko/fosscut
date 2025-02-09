package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.TestDefaults;
import com.fosscut.util.Messages;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.Utils;

public class Validate {

    @Test public void validateCommand() {
        RepetitiveTests.testHelpWithOrderPath(new Command("validate"));
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("validate -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("validate --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("validate -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("validate --version"));
    }

    @Test public void orderFileIsADirectory() {
        Command command = new Command("validate " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_DIRECTORY));
        command.run();
        assert(command.getOutput().equals(Messages.ORDER_FILE_IS_A_DIRECTORY_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

    @Test public void orderFileDoesNotExist() {
        Command command = new Command("validate thisFileDoesNotExist");
        command.run();
        assert(command.getOutput().equals(Messages.ORDER_FILE_DOES_NOT_EXIST_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

    @Test public void validateFromRedis() {
        Command command = new Command("validate "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().contains(Messages.ORDER_VALID));
        assertEquals(0, command.getExitCode());
    }

    @Test public void validateRedisProtocolException() {
        Command command = new Command("validate -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH_PROTOCOL_EXCEPTION);
        command.run();
        assert(command.getOutput().equals(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_PROTOCOL_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

    @Test public void validateRedisHostnameException() {
        Command command = new Command("validate -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH_HOSTNAME_EXCEPTION);
        command.run();
        assert(command.getOutput().equals(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_HOSTNAME_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

    @Test public void validateRedisPortException() {
        Command command = new Command("validate -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH_PORT_EXCEPTION);
        command.run();
        assert(command.getOutput().equals(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_PORT_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

    @Test public void validateRedisIdentifierException() {
        Command command = new Command("validate -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH_IDENTIFIER_EXCEPTION);
        command.run();
        assert(command.getOutput().equals(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_IDENTIFIER_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonPositiveInputLength() {
        Command command = new Command("validate " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONPOSITIVE_INPUT_LENGTH));
        command.run();
        assert(command.getOutput().contains(Messages.NONPOSITIVE_INPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonPositiveInputLengthQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONPOSITIVE_INPUT_LENGTH));
        command.run();
        assert(command.getOutput().equals(Messages.NONPOSITIVE_INPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonPositiveOutputLength() {
        Command command = new Command("validate " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONPOSITIVE_OUTPUT_LENGTH));
        command.run();
        assert(command.getOutput().contains(Messages.NONPOSITIVE_OUTPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonPositiveOutputLengthQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONPOSITIVE_OUTPUT_LENGTH));
        command.run();
        assert(command.getOutput().equals(Messages.NONPOSITIVE_OUTPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonNegativeInputCount() {
        Command command = new Command("validate " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONNEGATIVE_INPUT_COUNT));
        command.run();
        assert(command.getOutput().contains(Messages.NONNEGATIVE_INPUT_COUNT_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonNegativeInputCountQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONNEGATIVE_INPUT_COUNT));
        command.run();
        assert(command.getOutput().equals(Messages.NONNEGATIVE_INPUT_COUNT_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonNegativeOutputCount() {
        Command command = new Command("validate " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONNEGATIVE_OUTPUT_COUNT));
        command.run();
        assert(command.getOutput().contains(Messages.NONNEGATIVE_OUTPUT_COUNT_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonNegativeOutputCountQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_NONNEGATIVE_OUTPUT_COUNT));
        command.run();
        assert(command.getOutput().equals(Messages.NONNEGATIVE_OUTPUT_COUNT_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void outputLongerThanInput() {
        Command command = new Command("validate " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_OUTPUT_LONGER_THAN_INPUT));
        command.run();
        assert(command.getOutput().contains(Messages.OUTPUT_LONGER_THAN_INPUT_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void outputLongerThanInputQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_OUTPUT_LONGER_THAN_INPUT));
        command.run();
        assert(command.getOutput().equals(Messages.OUTPUT_LONGER_THAN_INPUT_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void sumInputLengthLongerThanSumOutputLength() {
        Command command = new Command("validate " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_INPUT_COUNT));
        command.run();
        assert(command.getOutput().contains(Messages.OUTPUT_SUM_LONGER_THAN_INPUT_SUM_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void sumInputLengthLongerThanSumOutputLengthQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(TestDefaults.FAIL_VALIDATION_INPUT_COUNT));
        command.run();
        assert(command.getOutput().equals(Messages.OUTPUT_SUM_LONGER_THAN_INPUT_SUM_ERROR));
        assertEquals(1, command.getExitCode());
    }

}
