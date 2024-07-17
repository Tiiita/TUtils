package de.tiiita;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class FileDownloader {
    private File file;

    /**
     * Using this method you can make an easy get http request to download a file from
     * an url. It also allows you to log the progress with a custom log logic.
     *
     * @param url            the url to the file that will be downloaded.
     * @param progressLogger the log logic if logProgress is enabled. Give null if you do not want logging
     */
    public void download(@NotNull String url, @Nullable Consumer<Integer> progressLogger) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
                handleResponse(response, progressLogger);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the downloaded file
     * @return the file or null of {@link #download(String, Consumer)} was not called or did not complete the
     * download successful.
     */

    @Nullable
    public File getDownloadedFile() {
        return file;
    }


    public String getProgressBar(int progress) {
        StringBuilder barBuilder = new StringBuilder();

        for (int i = 0; i < progress; i++) {
            barBuilder.append("|");
        }

        return barBuilder.toString();
    }

    private void handleResponse(CloseableHttpResponse response, Consumer<Integer> progressLogger) throws IOException {
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

    private void readContent(InputStream inputStream, FileOutputStream outputStream, long contentLength, Consumer<Integer> progressLogger) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        long totalBytesRead = 0;
        int percentCompleted = 0;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
            int newPercentCompleted = (int) (totalBytesRead * 100 / contentLength);
            if (progressLogger != null && newPercentCompleted > percentCompleted) {
                percentCompleted = newPercentCompleted;
                progressLogger.accept(percentCompleted);
            }
        }
    }
}
