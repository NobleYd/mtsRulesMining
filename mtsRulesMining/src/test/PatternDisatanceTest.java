package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cluster.DistanceFunction;
import cluster.hierarchical.DataObject;
import pattern.IntraPattern;

public class PatternDisatanceTest {

	@Test
	public void testDistance() {
		DistanceFunction<IntraPattern> levenshteinDistcance = DistanceFunction.levenshteinDistcance();
		DistanceFunction<IntraPattern> dtwDistcance = DistanceFunction.DtwDistcance(3);
		DistanceFunction<IntraPattern> dtw2Distcance = DistanceFunction.DtwDistcance(null);
		DistanceFunction<IntraPattern> lcsDistcance = DistanceFunction.LcsDistance(3);

		List<DataObject<IntraPattern>> objs = new ArrayList<DataObject<IntraPattern>>();
		objs.add(new DataObject<IntraPattern>(new IntraPattern(0, "udud")));
		objs.add(new DataObject<IntraPattern>(new IntraPattern(0, "uuduud")));

		for (DataObject<IntraPattern> obj1 : objs) {
			for (DataObject<IntraPattern> obj2 : objs) {
				System.out.println(obj1.getValue().getPattern() + " ---> " + obj2.getValue().getPattern());
				//System.out.println("\tlev:\t" + levenshteinDistcance.distance(obj1, obj2));
				//System.out.println("\tdtw:\t" + dtwDistcance.distance(obj1, obj2));
				System.out.println("\tdtw2:\t" + dtw2Distcance.distance(obj1, obj2));
				//System.out.println("\tlcs:\t" + lcsDistcance.distance(obj1, obj2));
			}
		}

	}

}
