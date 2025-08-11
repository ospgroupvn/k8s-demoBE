package com.osp.bttp.common.config.logging;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sangnk
 * @Created 31/10/2024 - 8:56 SA
 * @project = bttp
 * @_ Mô tả:
 */
public class SimpleServletInputStream extends ServletInputStream {
    private InputStream delegate;

    public SimpleServletInputStream(byte[] data) {
        this.delegate = new ByteArrayInputStream(data);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return this.delegate.read();
    }
}