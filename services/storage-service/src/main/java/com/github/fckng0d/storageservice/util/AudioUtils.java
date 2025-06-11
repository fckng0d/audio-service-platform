package com.github.fckng0d.storageservice.util;

import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.PointerPointer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioUtils {

    static {
        avutil.av_log_set_level(avutil.AV_LOG_QUIET); // отключить FFmpeg логи
    }

    public static int getDurationInSeconds(byte[] audioBytes) {
        File tempFile = null;
        try {
            // Записать во временный файл
            tempFile = File.createTempFile("audio_", ".tmp");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(audioBytes);
            }

            AVFormatContext pFormatCtx = avformat.avformat_alloc_context();

            if (avformat.avformat_open_input(pFormatCtx, tempFile.getAbsolutePath(), null, null) != 0) {
                throw new RuntimeException("Не удалось открыть аудиофайл");
            }

            if (avformat.avformat_find_stream_info(pFormatCtx, (PointerPointer<?>) null) < 0) {
                throw new RuntimeException("Не удалось прочитать информацию о потоке");
            }

            long durationMicroseconds = pFormatCtx.duration();
            int seconds = (int) (durationMicroseconds / 1_000_000);

            avformat.avformat_close_input(pFormatCtx);
            return seconds;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при работе с временным файлом", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.deleteOnExit();
            }
        }
    }
}
