package com.interview.crm.common.export;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ExportStrategy<T> {
    String getFormat();

    String getMimeType();

    String getFileExtension();

    ByteArrayInputStream export(List<T> data) throws IOException;
}
