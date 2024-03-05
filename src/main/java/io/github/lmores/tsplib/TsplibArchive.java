package io.github.lmores.tsplib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
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
  /** Path to the zip archive containing all TSP instances of the TSPLIB. */
  private static final String TSP_ARCHIVE_RESOURCE = "tsp.zip";

  // public static void main(String[] args) throws IOException {
  //   final TspInstance inst = readTspInstance("bier127.tsp");
  //   System.out.println(inst.getName());
  // }

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
    try (final ZipFile zip = new ZipFile(getTmpTspArchiveFile().toFile())) {
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
    if (tspArchive == null) {
      tspArchive = new ZipFile(getTmpTspArchiveFile().toFile());
    }

    final ZipEntry entry = tspArchive.getEntry(filename);
    if (entry == null)  throw new IOException("File '" + filename + "' not found in TSPLIB archive");
    return tspArchive.getInputStream(entry);
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
    try (final ZipFile zip = new ZipFile(getTmpTspArchiveFile().toFile())) {
      final ZipEntry entry = zip.getEntry(filename);
      if (entry == null)  throw new IOException("File '" + filename + "' not found in tsp archive");
      return TspInstance.from(TsplibFileData.read(zip.getInputStream(entry)));
    }
  }

  // ===============================================================================================
  // Private helpers
  // ===============================================================================================

  private static Path tmpTspArchiveFile = null;
  private static Path getTmpTspArchiveFile() throws IOException {
    if (tmpTspArchiveFile != null)  return tmpTspArchiveFile;

    final URI ref;
    try {
      ref = TsplibArchive.class.getResource(TSP_ARCHIVE_RESOURCE).toURI();
    } catch (final URISyntaxException e) {
      throw new IOException("Cannot locate tsp instance archive", e);
    }
    final String scheme = ref.getScheme();

    // When this package is loaded as a .jar file, resources are located inside 
    // it and the jar archive must be registered as a FileSystem to access them.
    if ("jar".equalsIgnoreCase(scheme)) {
      final String[] parts = ref.toString().split("!", 2);
      try {
        FileSystems.newFileSystem(URI.create(parts[0]), Collections.emptyMap());
      } catch (final FileSystemAlreadyExistsException e) { /* no-op */ }
      
      final Path tmpDestDir = Path.of(System.getProperty("java.io.tmpdir")).resolve("tsplib");
      Files.createDirectories(tmpDestDir);

      final Path tmpFile = tmpDestDir.resolve("tsp.zip");
      Files.deleteIfExists(tmpFile);

      tmpTspArchiveFile = Files.copy(Path.of(ref), tmpFile);
      try {
        tmpTspArchiveFile.toFile().deleteOnExit();
      } catch (final SecurityException e) { /* no-op */ }

    } else if ("file".equalsIgnoreCase(scheme)) {
      tmpTspArchiveFile = Path.of(ref);

    } else {
      throw new IOException("Cannot load internal archive 'tsp.zip', unhandled scheme: " + scheme);
    }

    return tmpTspArchiveFile;
  }
}
