package com.fosscut.test;

import org.junit.Test;

import com.fosscut.util.Utils;

public class Binary {
    @Test public void fosscutBinaryExists() {
        assert(Utils.getFosscutBinaryPath().exists());
    }

    @Test public void fosscutBinaryCanExecute() {
        assert(Utils.getFosscutBinaryPath().canExecute());
    }
}
