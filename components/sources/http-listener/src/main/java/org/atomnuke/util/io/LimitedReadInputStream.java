package org.atomnuke.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author zinic
 */
public class LimitedReadInputStream extends InputStream {

   private final InputStream delegateStream;
   private final AtomicLong bytesRead;
   private final long maxReadLength;

   public LimitedReadInputStream(InputStream delegateStream, long readLength) {
      this.delegateStream = delegateStream;
      this.maxReadLength = readLength;

      bytesRead = new AtomicLong();
   }

   @Override
   public int read() throws IOException {
      if (bytesRead.incrementAndGet() > maxReadLength) {
         throw new ReadLimitException("Input stream has already read: " + maxReadLength + "bytes - aborting.");
      }

      return delegateStream.read();
   }

   @Override
   public long skip(long n) throws IOException {
      if (bytesRead.addAndGet(n) > maxReadLength) {
         throw new ReadLimitException("Input stream has already read: " + maxReadLength + "bytes - aborting.");
      }

      return delegateStream.skip(n);
   }

   @Override
   public int read(byte[] b) throws IOException {
      return delegateStream.read(b);
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      return delegateStream.read(b, off, len);
   }

   @Override
   public int available() throws IOException {
      return delegateStream.available();
   }

   @Override
   public void close() throws IOException {
      delegateStream.close();
   }

   @Override
   public synchronized void mark(int readlimit) {
      delegateStream.mark(readlimit);
   }

   @Override
   public synchronized void reset() throws IOException {
      delegateStream.reset();
   }

   @Override
   public boolean markSupported() {
      return delegateStream.markSupported();
   }
}
