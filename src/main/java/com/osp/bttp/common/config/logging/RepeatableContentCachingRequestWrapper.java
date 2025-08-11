package com.osp.bttp.common.config.logging;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

/**
 * @author sangnk
 * @Created 31/10/2024 - 8:55 SA
 * @project = bttp
 * @_ Mô tả:
 */
public class RepeatableContentCachingRequestWrapper extends ContentCachingRequestWrapper {
    private SimpleServletInputStream inputStream;

    public RepeatableContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() {
        return this.inputStream;
    }

    public String readInputAndDuplicate() throws IOException {
        if (inputStream == null) {
            byte[] body = super.getInputStream().readAllBytes();
            this.inputStream = new SimpleServletInputStream(body);
        }
        return new String(super.getContentAsByteArray());
    }
}