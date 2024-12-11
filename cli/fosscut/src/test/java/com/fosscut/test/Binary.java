package com.fosscut.test;

import org.junit.Test;

import com.fosscut.util.Defaults;

public class Binary {
    @Test public void fosscutBinaryExists() {
        assert(Defaults.getFosscutBinaryPath().exists());
    }

    @Test public void fosscutBinaryCanExecute() {
        assert(Defaults.getFosscutBinaryPath().canExecute());
    }
}
