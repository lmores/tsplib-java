package io.github.lmores.tsplib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.github.lmores.tsplib.tsp.TspInstance;

/**
 * A collection of utilities to load the TSPLIB instances shipped with this
 * package.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public class TsplibArchive {
  /** Path to the archive containing all TSP instances of the TSPLIB. */
  private static final Path TSP_ARCHIVE;
  static {
    try {
      TSP_ARCHIVE = Path.of(TsplibArchive.class.getResource("tsp.zip").toURI());
    } catch (final URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  // TODO: improve
  /** Internal reference to the ZipFile object used to read TSP instances from
   * the {@link #TSP_ARCHIVE}.
   * This object is created the first time a TSP instance must be read from the
   * archive and it is kept thereafter.
   *
   * This is a workaround due to the fact that closing this object
   * automatically closes any {@link java.io.InputStream} (reading a
   * {@link java.util.zip.ZipEntry}) we returned to the caller.
   */
  private static ZipFile tspArchive = null;

  /** This class contains only static methods and no instance is allowed. */
  private TsplibArchive() { /* no-op */ }

  /** Creates a ZipFile instance linked to the internal {@link #TSP_ARCHIVE}
   * the first this method is called and always returns the same object
   * thereafter.
   */
  private static ZipFile getTspArchive() throws ZipException, IOException {
    if (tspArchive == null) {
      tspArchive = new ZipFile(TSP_ARCHIVE.toFile());
    }

    return tspArchive;
  }

  /**
   * Returns the names of all files inside the TSPLIB archive for the
   * Travelling Salesman Problem available on the TSPLIB official website.
   *
   * The extension of the included files is either {@code .tsp},
   * {@code [.opt].tour} or {@code .problems}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error has occurred
   * @see     <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *            The complete official archive
   *          </a>
   */
  public static String[] extractTspFilenames() throws IOException {
    try (final ZipFile zip = new ZipFile(TSP_ARCHIVE.toFile())) {
      final List<String> files = new ArrayList<>(256);
      final Enumeration<? extends ZipEntry> entries = zip.entries();
      while (entries.hasMoreElements()) {
        files.add(entries.nextElement().getName());
      }
      return files.toArray(new String[files.size()]);
    }
  }

  /**
   * Returns an input stream reading from the specified TSP instance file.
   * The file name can be any among those returned by
   * {@link extractTspFilenames}.
   * The ownership of the input stream is passed to the caller who must
   * properly close it when it is no longer needed.
   *
   * @param filename      the name of the instance file (e.g. "a280.tsp")
   * @return              the TSP instance corresponding to the given filename
   * @throws IOException  if an I/O error has occurred
   */
  public static InputStream getTspFileInputStream(final String filename) throws IOException {
    final ZipFile archive = getTspArchive();
    final ZipEntry entry = archive.getEntry(filename);
    if (entry == null)  throw new IOException("File '" + filename + "' not found in TSPLIB archive");
    return archive.getInputStream(entry);
  }

  /**
   * Reads a TSP instance from the TSPLIB archive.
   *
   * The provided file name must be among those returned by
   * {@link extractTspFilenames} and it must end in {@code .tsp} (files ending
   * in {@code .tour} or {@code .problems}) are not supported by this function).
   *
   * @param filename      the name of the instance file (e.g. "a280.tsp")
   * @return              the TSP instance corresponding to the given filename
   * @throws IOException  if an I/O error has occurred
   */
  public static TspInstance readTspInstance(final String filename) throws IOException {
    return TspInstance.from(TsplibFileData.read(getTspFileInputStream(filename)));
  }
}
