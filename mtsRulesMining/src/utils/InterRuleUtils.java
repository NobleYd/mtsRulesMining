package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pattern.InterPattern;
import utils.RunningUtils.Setting;

public class InterRuleUtils {

	private static Log log = LogFactory.getLog(InterRuleUtils.class);

	static class RankRule {
		private String rule;
		private Double rank;

		public String getRule() {
			return rule;
		}

		public void setRule(String rule) {
			this.rule = rule;
		}

		public Double getRank() {
			return rank;
		}

		public void setRank(Double rank) {
			this.rank = rank;
		}

		public RankRule(String rule, Double rank) {
			super();
			this.rule = rule;
			this.rank = rank;
		}

		public RankRule() {
			super();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((rule == null) ? 0 : rule.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RankRule other = (RankRule) obj;
			if (rule == null) {
				if (other.rule != null)
					return false;
			} else if (!rule.equals(other.rule))
				return false;
			return true;
		}

	}

	private static Set<RankRule> rules_ranked_by_lift = new TreeSet<RankRule>(new Comparator<RankRule>() {
		@Override
		public int compare(RankRule o1, RankRule o2) {
			return o2.getRank().compareTo(o1.getRank());
		}
	});
	private static Set<RankRule> rules_ranked_by_conf = new TreeSet<RankRule>(new Comparator<RankRule>() {
		@Override
		public int compare(RankRule o1, RankRule o2) {
			return o2.getRank().compareTo(o1.getRank());
		}
	});

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
					double cosine = Math.sqrt((1.0 * support * support) / (leftSupport * rightSupport));
					if (conf >= minConf) {
						String ruleDesc = "R" + (++rulesNumber) + ": " + left + "  -->  " + right + ", supL: " + leftSupport + ", supR: " + rightSupport + ", sup: " + support + ", conf: " + conf
								+ ", lift: " + lift + ", cosine: " + cosine;
						bw.write(ruleDesc + System.lineSeparator());

						rules_ranked_by_lift.add(new RankRule(ruleDesc, lift));
						rules_ranked_by_conf.add(new RankRule(ruleDesc, conf));

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

		outputRankedRules(setting);

		log.info("start output ranked rule information");

		log.info("end---end---end---end---end---end---end---end---end---end---end---");
	}

	public static void outputRankedRules(Setting setting) throws IOException {
		log.info("start output ranked rules ...");
		String outPutFilePath1 = setting.getOutputFileDir().endsWith("/") ? setting.getOutputFileDir() : setting.getOutputFileDir() + "/";
		outPutFilePath1 = outPutFilePath1 + "InterRules_ranked_confidence.txt";
		// Output
		BufferedWriter bw_conf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath1))));
		for (RankRule rankRule : rules_ranked_by_conf) {
			bw_conf.write(rankRule.getRule() + System.lineSeparator());
		}
		bw_conf.flush();
		bw_conf.close();

		String outPutFilePath2 = setting.getOutputFileDir().endsWith("/") ? setting.getOutputFileDir() : setting.getOutputFileDir() + "/";
		outPutFilePath2 = outPutFilePath2 + "InterRules_ranked_lift.txt";
		BufferedWriter bw_lift = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath2))));
		for (RankRule rankRule : rules_ranked_by_lift) {
			bw_lift.write(rankRule.getRule() + System.lineSeparator());
		}
		bw_lift.flush();
		bw_lift.close();

		log.info("end---end---end---end---end---end---end---end---end---end---end---");
	}

}
