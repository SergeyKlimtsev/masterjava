package ru.javaops.masterjava.service.mail.util;

import com.sun.xml.ws.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CloseShieldInputStream;
import ru.javaops.masterjava.service.mail.Attach;
import ru.javaops.masterjava.service.mail.ByteArrayAttach;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Attachments {
    public static Attach getAttach(String name, InputStream inputStream) {
        return new Attach(name, new DataHandler(new InputStreamDataSource(inputStream)));
    }

    public static ByteArrayAttach getByteArrayAttach(String name, InputStream inputStream) {
        try {
            return new ByteArrayAttach(name, IOUtils.toByteArray(inputStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Attach toPlainAttach(ByteArrayAttach byteArrayAttach) {
        return getAttach(byteArrayAttach.getName(), new ByteArrayInputStream(byteArrayAttach.getData()));
    }
    //    http://stackoverflow.com/questions/2830561/how-to-convert-an-inputstream-to-a-datahandler
    //    http://stackoverflow.com/a/5924019/548473

    @AllArgsConstructor
    private static class InputStreamDataSource implements DataSource {
        private InputStream inputStream;

        @Override
        public InputStream getInputStream() throws IOException {
            return new CloseShieldInputStream(inputStream);
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        @Override
        public String getName() {
            return "";
        }
    }
}
