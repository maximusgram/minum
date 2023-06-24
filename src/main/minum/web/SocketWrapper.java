package minum.web;

import minum.logging.ILogger;
import minum.logging.Logger;
import minum.utils.StacktraceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import static minum.Constants.SOCKET_TIMEOUT_MILLIS;

/**
 * This wraps Sockets to make them more particular to our use case
 */
public class SocketWrapper implements ISocketWrapper, AutoCloseable {

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream writer;
    private final ILogger logger;
    private final Server server;

    public SocketWrapper(Socket socket, ILogger logger) throws IOException {
        this(socket, null, logger);
    }

    public SocketWrapper(Socket socket, Server server, ILogger logger) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);
        this.inputStream = socket.getInputStream();
        writer = socket.getOutputStream();
        this.logger = logger;
        this.server = server;
    }

    @Override
    public void send(String msg) throws IOException {
        writer.write(msg.getBytes());
    }

    @Override
    public void send(byte[] bodyContents) throws IOException {
        writer.write(bodyContents);
    }

    @Override
    public void sendHttpLine(String msg) throws IOException {
        logger.logTrace(() -> String.format("%s sending: \"%s\"", this, Logger.showWhiteSpace(msg)));
        send(msg + WebEngine.HTTP_CRLF);
    }

    @Override
    public String getLocalAddr() {
        return socket.getLocalAddress().getHostAddress();
    }

    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    @Override
    public SocketAddress getRemoteAddrWithPort() {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public String getRemoteAddr() {
        return socket.getInetAddress().getHostAddress();
    }

    @Override
    public void close() throws IOException {
        logger.logTrace(() -> "close called on " + this + ". Stacktrace:" + StacktraceUtils.stackTraceToString(Thread.currentThread().getStackTrace()));
        socket.close();
        if (server != null) server.removeMyRecord(this);
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * Note that since we are indicating just the remote address
     * as the unique value, in cases like tests where we are operating as
     * sometimes server or client, you might see the server as the remote.
     */
    @Override
    public String toString() {
        return "(SocketWrapper for remote address: " + this.getRemoteAddrWithPort().toString() + ")";
    }
}