package io.github.lmores.tsplib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import io.github.lmores.tsplib.atsp.AtspInstance;
import io.github.lmores.tsplib.hcp.HcpInstance;
import io.github.lmores.tsplib.sop.SopInstance;
import io.github.lmores.tsplib.tsp.TspInstance;
import io.github.lmores.tsplib.vrp.VrpInstance;

/**
 * Methods to load the TSPLIB instances shipped with this package.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public class TsplibArchive {
  private static final String ARCHIVE_RESOURCE_NAME = "/io/github/lmores/tsplib/__archive__";
  private static final String ATSP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/atsp";
  private static final String HCP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/hcp";
  private static final String SOP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/sop";
  private static final String TSP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/tsp";
  private static final String VRP_ARCHIVE_RESOURCE_NAME = ARCHIVE_RESOURCE_NAME + "/vrp";

  private static final String[] ATSP_FILENAMES = new String[] {
    "br17.atsp",            "ft53.atsp",            "ft70.atsp",            "ftv33.atsp",
    "ftv35.atsp",           "ftv38.atsp",           "ftv44.atsp",           "ftv47.atsp",
    "ftv55.atsp",           "ftv64.atsp",           "ftv70.atsp",           "ftv170.atsp",
    "kro124p.atsp",         "p43.atsp",             "rbg323.atsp",          "rbg358.atsp",
    "rbg403.atsp",          "rbg443.atsp",          "ry48p.atsp"
  };
  private static final String[] HCP_FILENAMES = new String[] {
    "alb1000.hcp",          "alb1000.opt.tour",     "alb2000.hcp",          "alb2000.opt.tour",
    "alb3000a.hcp",         "alb3000a.opt.tour",    "alb3000b.hcp",         "alb3000b.opt.tour",
    "alb3000c.hcp",         "alb3000c.opt.tour",    "alb3000d.hcp",         "alb3000d.opt.tour",
    "alb3000e.hcp",         "alb3000e.opt.tour",    "alb4000.hcp",          "alb4000.opt.tour",
    "alb5000.hcp",          "alb5000.opt.tour"
  };
  private static final String[] SOP_FILENAMES = new String[] {
    "br17.10.sop",          "br17.12.sop",          "ESC07.sop",            "ESC11.sop",
    "ESC12.sop",            "ESC25.sop",            "ESC47.sop",            "ESC63.sop",
    "ESC78.sop",            "ft53.1.sop",           "ft53.2.sop",           "ft53.3.sop",
    "ft53.4.sop",           "ft70.1.sop",           "ft70.2.sop",           "ft70.3.sop",
    "ft70.4.sop",           "kro124p.1.sop",        "kro124p.2.sop",        "kro124p.3.sop",
    "kro124p.4.sop",        "p43.1.sop",            "p43.2.sop",            "p43.3.sop",
    "p43.4.sop",            "prob.42.sop",          "prob.100.sop",         "rbg048a.sop",
    "rbg050c.sop",          "rbg109a.sop",          "rbg150a.sop",          "rbg174a.sop",
    "rbg253a.sop",          "rbg323a.sop",          "rbg341a.sop",          "rbg358a.sop",
    "rbg378a.sop",          "ry48p.1.sop",          "ry48p.2.sop",          "ry48p.3.sop",
    "ry48p.4.sop"
  };
  private static final String[] TSP_FILENAMES = new String[] {
    "a280.opt.tour",        "a280.tsp",             "ali535.tsp",           "att48.opt.tour",
    "att48.tsp",            "att532.tsp",           "bayg29.opt.tour",      "bayg29.tsp",
    "bays29.opt.tour",      "bays29.tsp",           "berlin52.opt.tour",    "berlin52.tsp",
    "bier127.tsp",          "brazil58.tsp",         "brd14051.tsp",         "brg180.opt.tour",
    "brg180.tsp",           "burma14.tsp",          "ch130.opt.tour",       "ch130.tsp",
    "ch150.opt.tour",       "ch150.tsp",            "d198.tsp",             "d493.tsp",
    "d657.tsp",             "d1291.tsp",            "d15112.tsp",           "d1655.tsp",
    "d18512.tsp",           "d2103.tsp",            "dantzig42.tsp",        "dsj1000.tsp",
    "eil51.opt.tour",       "eil51.tsp",            "eil76.opt.tour",       "eil76.tsp",
    "eil101.opt.tour",      "eil101.tsp",           "fl417.tsp",            "fl1400.tsp",
    "fl1577.tsp",           "fl3795.tsp",           "fnl4461.tsp",          "fri26.opt.tour",
    "fri26.tsp",            "gil262.tsp",           "gr17.tsp",             "gr21.tsp",
    "gr24.opt.tour",        "gr24.tsp",             "gr48.opt.tour",        "gr48.tsp",
    "gr96.opt.tour",        "gr96.tsp",             "gr120.opt.tour",       "gr120.tsp",
    "gr137.tsp",            "gr202.opt.tour",       "gr202.tsp",            "gr229.tsp",
    "gr431.tsp",            "gr666.opt.tour",       "gr666.tsp",            "hk48.tsp",
    "kroA100.opt.tour",     "kroA100.tsp",          "kroA150.tsp",          "kroA200.tsp",
    "kroB100.tsp",          "kroB150.tsp",          "kroB200.tsp",          "kroC100.opt.tour",
    "kroC100.tsp",          "kroD100.opt.tour",     "kroD100.tsp",          "kroE100.tsp",
    "lin105.opt.tour",      "lin105.tsp",           "lin318.tsp",           "linhp318.tsp",
    "nrw1379.tsp",          "p654.tsp",             "pa561.opt.tour",       "pa561.tsp",
    "pcb442.opt.tour",      "pcb442.tsp",           "pcb1173.tsp",          "pcb3038.tsp",
    "pla33810.opt.tour",    "pla33810.tsp",         "pla7397.tsp",          "pla85900.tsp",
    "pr76.opt.tour",        "pr76.tsp",             "pr107.tsp",            "pr124.tsp",
    "pr136.tsp",            "pr144.tsp",            "pr152.tsp",            "pr226.tsp",
    "pr264.tsp",            "pr299.tsp",            "pr439.tsp",            "pr1002.opt.tour",
    "pr1002.tsp",           "pr2392.opt.tour",      "pr2392.tsp",           "rat99.tsp",
    "rat195.tsp",           "rat575.tsp",           "rat783.tsp",           "rd100.opt.tour",
    "rd100.tsp",            "rd400.tsp",            "rl1304.tsp",           "rl1323.tsp",
    "rl1889.tsp",           "rl5915.tsp",           "rl5934.tsp",           "rl11849.tsp",
    "si175.tsp",            "si535.tsp",            "si1032.tsp",           "st70.opt.tour",
    "st70.tsp",             "swiss42.tsp",          "ts225.tsp",            "tsp225.opt.tour",
    "tsp225.tsp",           "u159.tsp",             "u574.tsp",             "u724.tsp",
    "u1060.tsp",            "u1432.tsp",            "u1817.tsp",            "u2152.tsp",
    "u2319.tsp",            "ulysses16.opt.tour",   "ulysses16.tsp",        "ulysses22.opt.tour",
    "ulysses22.tsp",        "usa13509.tsp",         "vm1084.tsp",           "vm1748.tsp",
    "xray.problems"
  };
  private static final String[] VRP_FILENAMES = new String[] {
    "att48.vrp",            "eil7.vrp",             "eil13.vrp",            "eil22.vrp",
    "eil23.vrp",            "eil30.vrp",            "eil31.vrp",            "eil33.vrp",
    "eil51.vrp",            "eilA76.vrp",           "eilA101.vrp",          "eilB76.vrp",
    "eilB101.vrp",          "eilD76.vrp",           "eilC76.vrp",           "gil262.vrp"
  };

  /** This class contains only static methods and no instance is allowed. */
  private TsplibArchive() { /* no-op */ }

  // ==============================================================================================
  // Methods to read instance file names from resource directories
  //
  // Originally, methods named `extractXxxFilenames()` were meant to read the actual list
  // of available files from the corresponding `Xxx`resource folder, however, this is quite
  // difficult when the executable is distributed as a JAR archive. The current solution is
  // to hard code the content of each resource folder (i.e. the filenames).
  // ==============================================================================================

  /**
   * Returns the names of all files for the Asymmetric Travelling Salesman
   * Problem available in the archive from the TSPLIB official website.
   *
   * The extension of the included files is either {@code .atsp} or
   * {@code [.opt].tour}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error occurs
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractAtspFilenames() throws IOException {
    return Arrays.copyOf(ATSP_FILENAMES, ATSP_FILENAMES.length);
  }

  /**
   * Returns the names of all files for the Hamiltonian Cycle Problem
   * available in the archive from the TSPLIB official website.
   *
   * The extension of the included files is either {@code .hcp} or
   * {@code [.opt].tour}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error occurs
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractHcpFilenames() throws IOException {
    return Arrays.copyOf(HCP_FILENAMES, HCP_FILENAMES.length);
  }

  /**
   * Returns the names of all files for the Sequential Ordering Problem
   * available in the archive from the TSPLIB official website.
   *
   * The extension of the included files is {@code .sop}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error occurs
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractSopFilenames() throws IOException {
    return Arrays.copyOf(SOP_FILENAMES, SOP_FILENAMES.length);
  }

  /**
   * Returns the names of all files for the Travelling Salesman Problem
   * available in the archive from the TSPLIB official website.
   *
   * The extension of the included files is either {@code .tsp},
   * {@code [.opt].tour} or {@code .problems}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error occurs
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractTspFilenames() throws IOException {
    return Arrays.copyOf(TSP_FILENAMES, TSP_FILENAMES.length);
  }

  /**
   * Returns the names of all files for the Vehicle Routing Problem
   * available in the archive from the TSPLIB official website.
   *
   * The extension of the included files is {@code .vrp}.
   *
   * @return              the file names
   * @throws IOException  if an I/O error occurs
   * @see  <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/ALL_tsp.tar.gz">
   *         The complete official archive
   *       </a>
   */
  public static String[] extractVrpFilenames() throws IOException {
    return Arrays.copyOf(VRP_FILENAMES, VRP_FILENAMES.length);
  }

  // ==============================================================================================
  // Methods to read the content of instance files from resource directories
  // ==============================================================================================

  /**
   * Returns an input stream reading from the specified ATSP instance file.
   *
   * The file name must be among those returned by {@link extractAtspFilenames}.
   * The ownership of the input stream is passed to the caller that must
   * properly close it when it is no longer needed.
   *
   * @param filename      the name of the instance file (e.g. "br17.atsp")
   * @return              the ATSP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static InputStream getAtspFileInputStream(final String filename) throws IOException {
    return TsplibArchive.class.getResourceAsStream(ATSP_ARCHIVE_RESOURCE_NAME + "/" + filename);
  }

  /**
   * Returns an input stream reading from the specified HCP instance file.
   *
   * The file name must be among those returned by {@link extractHcpFilenames}.
   * The ownership of the input stream is passed to the caller that must
   * properly close it when it is no longer needed.
   *
   * @param filename      the name of the instance file (e.g. "alb1000.hcp")
   * @return              the HCP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static InputStream getHcpFileInputStream(final String filename) throws IOException {
    return TsplibArchive.class.getResourceAsStream(HCP_ARCHIVE_RESOURCE_NAME + "/" + filename);
  }

  /**
   * Returns an input stream reading from the specified SOP instance file.
   *
   * The file name must be among those returned by {@link extractSopFilenames}.
   * The ownership of the input stream is passed to the caller that must
   * properly close it when it is no longer needed.
   *
   * @param filename      the name of the instance file (e.g. "br17.10.sop")
   * @return              the SOP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static InputStream getSopFileInputStream(final String filename) throws IOException {
    return TsplibArchive.class.getResourceAsStream(SOP_ARCHIVE_RESOURCE_NAME + "/" + filename);
  }

  /**
   * Returns an input stream reading from the specified TSP instance file.
   *
   * The file name must be among those returned by {@link extractTspFilenames}.
   * The ownership of the input stream is passed to the caller that must
   * properly close it when it is no longer needed.
   *
   * @param filename      the name of the instance file (e.g. "a280.tsp")
   * @return              the TSP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static InputStream getTspFileInputStream(final String filename) throws IOException {
    return TsplibArchive.class.getResourceAsStream(TSP_ARCHIVE_RESOURCE_NAME + "/" + filename);
  }

  /**
   * Returns an input stream reading from the specified VRP instance file.
   *
   * The file name must be among those returned by {@link extractVrpFilenames}.
   * The ownership of the input stream is passed to the caller that must
   * properly close it when it is no longer needed.
   *
   * @param filename      the name of the instance file (e.g. "att48.vrp")
   * @return              the VRP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static InputStream getVrpFileInputStream(final String filename) throws IOException {
    return TsplibArchive.class.getResourceAsStream(VRP_ARCHIVE_RESOURCE_NAME + "/" + filename);
  }

  // ==============================================================================================
  // Methods to read the content of instance files from resource directories
  // ==============================================================================================

  /**
   * Reads an ATSP instance from the TSPLIB archive.
   *
   * The file name must be among those returned by {@link extractAtspFilenames}.
   *
   * @param filename      the name of the instance file (e.g. "br17.atsp")
   * @return              the ATSP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static AtspInstance loadAtspInstance(final String filename) throws IOException {
    return AtspInstance.from(TsplibFileData.read(getAtspFileInputStream(filename)));
  }

  /**
   * Reads a HCP instance from the TSPLIB archive.
   *
   * The file name must be among those returned by {@link extractHcpFilenames}
   * and it must end in {@code .hcp} (files ending in {@code .tour} are not
   * supported by this function).
   *
   * @param filename      the name of the instance file (e.g. "alb1000.hcp")
   * @return              the HCP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static HcpInstance loadHcpInstance(final String filename) throws IOException {
    return HcpInstance.from(TsplibFileData.read(getHcpFileInputStream(filename)));
  }

  /**
   * Reads a SOP instance from the TSPLIB archive.
   *
   * The file name must be among those returned by {@link extractSopFilenames}.
   *
   * @param filename      the name of the instance file (e.g. "br17.10.sop")
   * @return              the SOP instance corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static SopInstance loadSopInstance(final String filename) throws IOException {
    return SopInstance.from(TsplibFileData.read(getSopFileInputStream(filename)));
  }

  /**
   * Reads a TSP instance from the TSPLIB archive.
   *
   * The file name must be among those returned by {@link extractTspFilenames}
   * and it must end in {@code .tsp} (files ending in {@code .tour} or
   * {@code .problems} are not supported by this function).
   *
   * @param filename      the name of the instance file (e.g. "a280.tsp")
   * @return              the TSP instance corresponding to the given filename
   * @throws IOException  if an I/O error occurs
   */
  public static TspInstance loadTspInstance(final String filename) throws IOException {
    return TspInstance.from(TsplibFileData.read(getTspFileInputStream(filename)));
  }

  /**
   * Reads a VRP instance from the TSPLIB archive.
   *
   * The file name must be among those returned by {@link extractTspFilenames}.
   *
   * @param filename      the name of the instance file (e.g. "att48.vrp")
   * @return              the VRP instance corresponding to the given filename
   * @throws IOException  if an I/O error occurs
   */
  public static VrpInstance loadVrpInstance(final String filename) throws IOException {
    return VrpInstance.from(TsplibFileData.read(getVrpFileInputStream(filename)));
  }

  // ==============================================================================================
  // Methods to read the content of solutions files from resource directories
  // ==============================================================================================

  /**
   * Reads a set of solutions (tours) for the HCP in the TSPLIB archive.
   *
   * The file name must be among those returned by {@link extractTspFilenames}
   * and it must end in {@code .tour} (files ending in {@code .hcp} are not
   * supported by this function).
   *
   * @param filename      the name of the file (e.g. "alb1000.opt.tour")
   * @return              the solutions corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static Solutions loadHcpTour(final String filename) throws IOException {
    return Solutions.from(TsplibFileData.read(getHcpFileInputStream(filename)));
  }

  /**
   * Reads a set of solutions (tours) for the TSP in the TSPLIB archive.
   *
   * The file name must be among those returned by {@link extractTspFilenames}
   * and it must end in {@code .tour} (files ending in {@code .tsp} or
   * {@code .problems} are not supported by this function).
   *
   * @param filename      the name of the instance file (e.g. "a280.opt.tour")
   * @return              the solutions corresponding to the given file name
   * @throws IOException  if an I/O error occurs
   */
  public static Solutions loadTspTour(final String filename) throws IOException {
    return Solutions.from(TsplibFileData.read(getTspFileInputStream(filename)));
  }
}
