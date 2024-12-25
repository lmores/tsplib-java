package io.github.lmores.tsplib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.github.lmores.tsplib.tour.Tour;
import io.github.lmores.tsplib.tsp.TspInstance;

/**
 * Methods to load the TSPLIB instances shipped with this package.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public class TsplibArchive {
  private static final String ARCHIVE_RESOURCE_NAME = "__archive__";
  private static final String ATSP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/atsp";
  private static final String HCP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/hcp";
  private static final String SOP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/sop";
  private static final String TSP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/tsp";
  private static final String VRP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/vrp";

  /** This class contains only static methods and no instance is allowed. */
  private TsplibArchive() { /* no-op */ }

  /**
   * Returns the names of all files inside the archive for the Asymmetric
   * Travelling Salesman Problem available on the TSPLIB official website.
   *
   * The extension of the included files is either {@code .atsp} or
   * {@code [.opt].tour}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error has occurred
   * @throws URISyntaxException
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractAtspFilenames() throws IOException {
    return readResourceDirContent(ATSP_ARCHIVE_RESOURCE_NAME);
  }

  /**
   * Returns the names of all files inside the archive for the
   * Sequential Ordering Problem available on the TSPLIB official website.
   *
   * The extension of the included files is either {@code .hcp} or
   * {@code [.opt].tour}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error has occurred
   * @throws URISyntaxException
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractSopFilenames() throws IOException {
    return readResourceDirContent(SOP_ARCHIVE_RESOURCE_NAME);
  }

  /**
   * Returns the names of all files inside the archive for the
   * Hamiltonian Cycle Problem available on the TSPLIB official website.
   *
   * The extension of the included files is either {@code .hcp} or
   * {@code [.opt].tour}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error has occurred
   * @throws URISyntaxException
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractHcpFilenames() throws IOException {
    return readResourceDirContent(HCP_ARCHIVE_RESOURCE_NAME);
  }

  /**
   * Returns the names of all files inside the archive for the
   * Vehicle Routing Problem available on the TSPLIB official website.
   *
   * The extension of the included files is either {@code .hcp} or
   * {@code [.opt].tour}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error has occurred
   * @throws URISyntaxException
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractVrpFilenames() throws IOException {
    return readResourceDirContent(VRP_ARCHIVE_RESOURCE_NAME);
  }

  /**
   * Returns the names of all files inside the archive for the
   * Travelling Salesman Problem available on the TSPLIB official website.
   *
   * The extension of the included files is either {@code .tsp},
   * {@code [.opt].tour} or {@code .problems}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error has occurred
   * @throws URISyntaxException
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractTspFilenames() throws IOException {
    return readResourceDirContent(TSP_ARCHIVE_RESOURCE_NAME);
  }

  /**
   * Returns an input stream reading from the specified TSP instance file.
   * The file name can be any among those returned by
   * {@link extractTspFilenames}.
   * The ownership of the input stream is passed to the caller that must
   * properly close it when it is no longer needed.
   *
   * @param filename      the name of the instance file (e.g. "a280.tsp")
   * @return              the TSP instance corresponding to the given filename
   * @throws IOException  if an I/O error has occurred
   */
  public static InputStream getTspFileInputStream(final String filename) throws IOException {
    return TsplibArchive.class.getResourceAsStream(TSP_ARCHIVE_RESOURCE_NAME + "/" + filename);
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
  public static TspInstance loadTspInstance(final String filename) throws IOException {
    return TspInstance.from(TsplibFileData.read(getTspFileInputStream(filename)));
  }

  /**
   * Reads a tour instance from the TSPLIB archive.
   *
   * The provided file name must be among those returned by
   * {@link extractTspFilenames} and it must end in {@code .tsp} (files ending
   * in {@code .tour} or {@code .problems}) are not supported by this function).
   *
   * @param filename      the name of the instance file (e.g. "a280.tsp")
   * @return              the TSP instance corresponding to the given filename
   * @throws IOException  if an I/O error has occurred
   */
  public static Tour loadTspTour(final String filename) throws IOException {
    return Tour.from(TsplibFileData.read(getTspFileInputStream(filename)));
  }

  // ===============================================================================================
  // Private helpers
  // ===============================================================================================

  private static String[] readResourceDirContent(final String dir) throws IOException {
    final List<String> filenames = new ArrayList<>();

    try (
      final InputStream in = Main.class.getResourceAsStream(dir);
      final BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))
    ) {
      String fname;
      while ((fname = br.readLine()) != null) {
        filenames.add(fname);
      }
    }

    return filenames.toArray(new String[0]);
  }
}
