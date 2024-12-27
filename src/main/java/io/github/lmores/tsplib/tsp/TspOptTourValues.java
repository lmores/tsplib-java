package io.github.lmores.tsplib.tsp;

import java.util.HashMap;
import java.util.Map;

public class TspOptTourValues {
  private static final Map<String, Integer> TSP_OPT_TOUR_VALUES = buildValues();

  private TspOptTourValues() { /* no-op */ }

  public static int get(final String name) {
    return TSP_OPT_TOUR_VALUES.get(name);
  }

  public static Map<String, Integer> getAll(final String name) {
    return new HashMap<>(TSP_OPT_TOUR_VALUES);
  }

  private static Map<String, Integer> buildValues() {
    final Map<String, Integer> values = new HashMap<>(256);
    values.put("a280", 2579);
    values.put("ali535", 202339);
    values.put("att48", 10628);
    values.put("att532", 27686);
    values.put("bayg29", 1610);
    values.put("bays29", 2020);
    values.put("berlin52", 7542);
    values.put("bier127", 118282);
    values.put("brazil58", 25395);
    values.put("brd14051", 469385);
    values.put("brg180", 1950);
    values.put("burma14", 3323);
    values.put("ch130", 6110);
    values.put("ch150", 6528);
    values.put("d198", 15780);
    values.put("d493", 35002);
    values.put("d657", 48912);
    values.put("d1291", 50801);
    values.put("d1655", 62128);
    values.put("d2103", 80450);
    values.put("d15112", 1573084);
    values.put("d18512", 645238);
    values.put("dantzig42", 699);
    values.put("dsj1000", 18659688);  // EUC_2D version
    values.put("dsj1000_euc", 18659688);  // EUC_2D version
    values.put("dsj1000_ceil", 18660188); // CEIL_2D version
    values.put("eil51", 426);
    values.put("eil76", 538);
    values.put("eil101", 629);
    values.put("fl417", 11861);
    values.put("fl1400", 20127);
    values.put("fl1577", 22249);
    values.put("fl3795", 28772);
    values.put("fnl4461", 182566);
    values.put("fri26", 937);
    values.put("gil262", 2378);
    values.put("gr17", 2085);
    values.put("gr21", 2707);
    values.put("gr24", 1272);
    values.put("gr48", 5046);
    values.put("gr96", 55209);
    values.put("gr120", 6942);
    values.put("gr137", 69853);
    values.put("gr202", 40160);
    values.put("gr229", 134602);
    values.put("gr431", 171414);
    values.put("gr666", 294358);
    values.put("hk48", 11461);
    values.put("kroA100", 21282);
    values.put("kroB100", 22141);
    values.put("kroC100", 20749);
    values.put("kroD100", 21294);
    values.put("kroE100", 22068);
    values.put("kroA150", 26524);
    values.put("kroB150", 26130);
    values.put("kroA200", 29368);
    values.put("kroB200", 29437);
    values.put("lin105", 14379);
    values.put("lin318", 42029);
    values.put("linhp318", 41345);
    values.put("nrw1379", 56638);
    values.put("p654", 34643);
    values.put("pa561", 2763);
    values.put("pcb442", 50778);
    values.put("pcb1173", 56892);
    values.put("pcb3038", 137694);
    values.put("pla7397", 23260728);
    values.put("pla33810", 66048945);
    values.put("pla85900", 142382641);
    values.put("pr76", 108159);
    values.put("pr107", 44303);
    values.put("pr124", 59030);
    values.put("pr136", 96772);
    values.put("pr144", 58537);
    values.put("pr152", 73682);
    values.put("pr226", 80369);
    values.put("pr264", 49135);
    values.put("pr299", 48191);
    values.put("pr439", 107217);
    values.put("pr1002", 259045);
    values.put("pr2392", 378032);
    values.put("rat99", 1211);
    values.put("rat195", 2323);
    values.put("rat575", 6773);
    values.put("rat783", 8806);
    values.put("rd100", 7910);
    values.put("rd400", 15281);
    values.put("rl1304", 252948);
    values.put("rl1323", 270199);
    values.put("rl1889", 316536);
    values.put("rl5915", 565530);
    values.put("rl5934", 556045);
    values.put("rl11849", 923288);
    values.put("si175", 21407);
    values.put("si535", 48450);
    values.put("si1032", 92650);
    values.put("st70", 675);
    values.put("swiss42", 1273);
    values.put("ts225", 126643);
    values.put("tsp225", 3916);
    values.put("u159", 42080);
    values.put("u574", 36905);
    values.put("u724", 41910);
    values.put("u1060", 224094);
    values.put("u1432", 152970);
    values.put("u1817", 57201);
    values.put("u2152", 64253);
    values.put("u2319", 234256);
    values.put("ulysses16", 6859);
    values.put("ulysses22", 7013);
    values.put("usa13509", 19982859);
    values.put("vm1084", 239297);
    values.put("vm1748", 336556);

    return values;
  }
}
