package de.tiiita;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileDownloader {

  private final File file;

  public FileDownloader() {
    try {
      this.file = Files.createTempFile("temp-filedownloader", "").toFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Using this method you can make an easy get http request to download a file from a URL. It also
   * allows you to log the progress with a custom log logic.
   *
   * @param url            the URL to the file that will be downloaded.
   * @param progressLogger the log logic if logProgress is enabled. Give null if you do not want
   *                       logging
   */
  public void download(@NotNull String url, @Nullable Consumer<Integer> progressLogger) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
        handleResponse(response, progressLogger);
        if (response.getEntity().getContentLength() != file.length()) {
          throw new RuntimeException("The downloaded file does not match the wanted size.");
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the downloaded file
   *
   * @return the file which contains the downloaded data or an empty file if
   * {@link #download(String, Consumer)} was not called or did not complete the download
   * successfully.
   */
  public File get() {
    return file;
  }

  /**
   * Unzips the downloaded file into a folder named "extracted" in the same directory.
   *
   * @return the path to the extracted folder
   */
  public File getAsUnzippedDir(String extractDirPath) {
    try (ZipFile zipFile = new ZipFile(file)) {
      zipFile.extractAll(extractDirPath);
      return new File(extractDirPath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  private void handleResponse(CloseableHttpResponse response, Consumer<Integer> progressLogger)
      throws IOException {
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      long contentLength = entity.getContentLength();
      try (InputStream inputStream = entity.getContent();
          FileOutputStream outputStream = new FileOutputStream(file)) {
        readContent(inputStream, outputStream, contentLength, progressLogger);
      }
      EntityUtils.consume(entity);
    } else {
      throw new IOException("No content to download");
    }
  }

  private void readContent(InputStream inputStream, FileOutputStream outputStream,
      long contentLength, Consumer<Integer> progressLogger) throws IOException {
    byte[] buffer = new byte[4096];
    int bytesRead;
    long totalBytesRead = 0;
    int percentCompleted = 0;

    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
      totalBytesRead += bytesRead;
      if (contentLength > 0) {
        int newPercentCompleted = (int) (totalBytesRead * 100 / contentLength);
        if (progressLogger != null && newPercentCompleted > percentCompleted) {
          percentCompleted = newPercentCompleted;
          progressLogger.accept(percentCompleted);
        }
      }
    }
  }
}
