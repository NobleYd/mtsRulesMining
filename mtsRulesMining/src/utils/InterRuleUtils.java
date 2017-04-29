package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pattern.InterPattern;
import utils.RunningUtils.Setting;

public class InterRuleUtils {

	private static Log log = LogFactory.getLog(InterRuleUtils.class);

	public static void generate(List<Map<InterPattern, InterPattern>> interFPss, Setting setting) throws IOException {
		log.info("start---start---start---start---start---start---start---start---");
		String outPutFilePath = setting.getOutputFileDir().endsWith("/") ? setting.getOutputFileDir() : setting.getOutputFileDir() + "/";
		outPutFilePath = outPutFilePath + "InterRules.txt";
		double minConf = setting.getMinConf();
		// Output
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));

		for (int blockNumber = 2; blockNumber < interFPss.size(); blockNumber++) {
			bw.write(System.lineSeparator() + System.lineSeparator() + "Rules with Block number: " + blockNumber + System.lineSeparator());
			int rulesNumber = 0;
			for (InterPattern interFp : interFPss.get(blockNumber).values()) {
				int support = interFp.getPositions().size();
				for (int i = 1; i <= blockNumber - 1; i++) {
					InterPattern left = interFp.subPattern(0, i);
					InterPattern right = interFp.subPattern(i, blockNumber);
					int leftSupport = interFPss.get(i).get(left).getPositions().size();
					int rightSupport = interFPss.get(blockNumber - i).get(right).getPositions().size();
					double conf = 1.0 * support / leftSupport;
					double lift = (1.0 * support / setting.dataNumber) / ((1.0 * leftSupport / setting.dataNumber) * (1.0 * rightSupport / setting.dataNumber));
					if (conf >= minConf) {
						bw.write("R" + (++rulesNumber) + ": " + left + "  -->  " + right + ", supL: " + leftSupport + ", supR: " + rightSupport + ", sup: " + support + ", conf: " + conf + ", lift: "
								+ lift + System.lineSeparator());
						// bw.write(left + " --> " + right + ", conf = support / preSupport = " +
						// support + " / " + preSupport + " = " + conf + System.lineSeparator());

						// After find the first rule, then continue with next pattern.
						// Since we think the other rules is contained in this rule.
						// Because if this rule's conf is ok, then of course other is ok.
						// This is because conf = support(not change) / preSupport(lower and lower)
						break;
					}
				}

			}
		}

		bw.flush();
		bw.close();
		log.info("end---end---end---end---end---end---end---end---end---end---end---");
	}

}
