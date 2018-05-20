package nodomain.knu2018.bandutils.export;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import nodomain.knu2018.bandutils.model.ActivityTrack;

public interface ActivityTrackExporter {
    @NonNull
    String getDefaultFileName(@NonNull ActivityTrack track);

    void performExport(ActivityTrack track, File targetFile) throws IOException, GPXTrackEmptyException;

    class GPXTrackEmptyException extends Exception {
    }
}
