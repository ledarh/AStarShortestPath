import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/*
 * Class DelivB does the work for deliverable DelivB of the Prog340
 * 
 * The goal of DelivB is to solve the coin change problem recursively and by the Dynamic Programming technique.
 * DelivB takes a graph with no edges represented as a text file as input. Each Node represents a denomination
 * and the value of the Node is the value of that denomination. As DelivB is a generalized solution to the
 * coin change problem, it outputs the minimum amount of each denomination, and total number of denominations used,
 * for each value between 1 and 100 units of currency in a charted fashion. Though this upper bound can be changed by editing the
 * 'm' int variable in DelivB(). Both the recursive (minChngRecur()) and dynamic programming (minChngDP()) solutions used assume there are an arbitrarily 
 * large number of each denomination available.
 * 
 * @deprecated buildPseudoDP() method no longer required after changes were made to minChngRecur(). See minChngRecur() JavaDocs
 * for further information
 * 
 * @author Mike Stein (IO Boilerplate), Kelly Higgins
 * 
 */

public class DelivB {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;

	public DelivB(File in, Graph gr) {
		inputFile = in;
		g = gr;

		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring(0, inputFileName.length() - 4); // Strip off ".txt"
		String outputFileName = baseFileName.concat("_out.txt");
		outputFile = new File(outputFileName);
		if (outputFile.exists()) { // For retests
			outputFile.delete();
		}

		try {
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}

		// Scanner scan = new Scanner(System.in);
		// System.out.print("Amount of currency to make change for: ");
		// String amtStr = "";
		// int amt = 0;
		// try {
		// amtStr = scan.next();
		// amt = Integer.parseInt(amtStr);
		// } catch (Exception e) {
		// System.out.println("Please enter the amount of currency as a number.");
		// }

		// System.out.println("Change to give via recursion:");
		// output.println("Change to give via recursion:");

		// if (amt == 0) {
		// System.out.println("No change is needed to be given for 0 currency!");
		// } else {

		// }
		// scan.close();

		HashMap<Integer, Integer> dp = new HashMap<Integer, Integer>(); // required number of coins to make up key
		HashMap<Integer, Integer> reqv = new HashMap<Integer, Integer>(); // required coins(val) to make up amt of
																			// currency(key)
		HashMap<Integer, Integer> coinsReq = new HashMap<Integer, Integer>(); // required coins(val) to make up amt of
																				// currency(key) used to track this data
																				// in the recursive solution

		// int v[] = { 1, 50, 25, 10, 5 };
		int v[] = buildValArr(g.getNodeList());
		int m = 100;

		minChngRecur(v, coinsReq, reqv);

		String chgChartRecur = buildChart(m, v, coinsReq, reqv);
		System.out.println("Recursive Solution\n\n" + chgChartRecur);
		output.println("Recursive Solution\n\n" + chgChartRecur);

		reqv = new HashMap<Integer, Integer>(); // reset required coin hashmap
		minChngDP(m, v, dp, reqv);
		String chgChartDP = buildChart(m, v, dp, reqv);
		System.out.println("\n\nDynamic Programming Solution\n\n" + chgChartDP);
		output.println("\n\nDynamic Programming Solution\n\n" + chgChartDP);

		output.flush();
	}

	/*
	 * Boilerplate method converting the input graph into an array of denominations
	 */
	private int[] buildValArr(ArrayList<Node> nodeList) {
		int v[] = new int[nodeList.size()];
		for (int i = 0; i < nodeList.size(); i++) {
			try {
				v[i] = Integer.parseInt(nodeList.get(i).getVal());
			} catch (Exception e) {
				System.out.println("Value of Node at index " + i + " is not an integer!");
			}
		}
		return v;
	}

	/*
	 * Wrapper for Recursive solution
	 * 
	 * See overloaded version of same method name for the coin-change logic. The
	 * method containing the recursive coin-chsnge logic was written only to return
	 * the minimum number of coins required to make change for amount m (albeit
	 * while logging which denominations, v[], are used) I decided it would be more
	 * succinct and easier to read/look back on if I were to write the required loop
	 * in this seperate method.
	 * 
	 * @param m the amount of currency to find the minimum number of 'coins'
	 * required to make change for
	 * 
	 * @param v[] denominations of such currency
	 * 
	 * @param dp map<int,int> to write total # of coins req for each m. Not utilized
	 * as a true dp table in this method, used only for storing total required coin
	 * count
	 * 
	 * @param reqv HashMap of required denominations for each value 0 -> m
	 * 
	 * @return numCoins number of coins required to make change of amount m
	 */
	private void minChngRecur(int v[], HashMap<Integer, Integer> coinsReq, HashMap<Integer, Integer> reqv) {
		/*
		 * Loop is employed to populate our coinsReq and reqv maps so that we have this
		 * data to print out the chart.
		 */
		for (int i = 1; i <= 100; i++) {
			coinsReq.put(i, minChngRecur(i, v, coinsReq, reqv));
		}
	}

	/*
	 * Recursive solution
	 * "Quick and dirty" greedy approach using highest value coin as much as
	 * possible at each subproblem.
	 *
	 * @param m the amount of currency to find the minimum number of 'coins'
	 * required to make change for
	 * 
	 * @param v[] denominations of such currency
	 * 
	 ****** neither HashMap variables are utilized as a true dp table in this method and
	 ***** are used only for storing total required coin count
	 * 
	 * @param coinsReq map<int,int> to write total # of coins required to make
	 * change for each m.
	 * 
	 * @param reqv HashMap of required denominations for each value 0 -> m
	 * 
	 * @return numCoins number of coins required to make change of amount m
	 */
	private int minChngRecur(int m, int v[], HashMap<Integer, Integer> coinsReq, HashMap<Integer, Integer> reqv) {
		Arrays.sort(v); // reorder value arr ascending
		int numCoins = 0;
		int curr = m;
		for (int k = v.length - 1; k >= 0; k--) {
			while (curr >= v[k]) {
				curr = curr - v[k];
				reqv.put(m, v[k]);
				numCoins++;
			}
		}
		if (curr == 0) {
			coinsReq.put(m, numCoins);
			return numCoins;
		} else {
			return -1;
		}
	}

	/*
	 * Dynamic Programming solution
	 * 
	 * Top-down approach achieved from "memoizing" the recursive algorithm
	 * 
	 * See report note at end of file
	 * 
	 * Complexity: O(n)
	 * 
	 * @param m the amount of currency to find the minimum number of 'coins'
	 * required to make change for
	 * 
	 * @param v[] array of denomination values
	 * 
	 * @param dp the HashMap dp 'table' for storing previously calculated results.
	 * Passed from DelivB() so we have this data later
	 * 
	 * @param reqv HashMap of required denominations for each value 0 -> m
	 * 
	 * @return number of 'coins' required to give change for amount m
	 */
	private int minChngDP(int m, int v[], Map<Integer, Integer> dp, Map<Integer, Integer> reqv) {
		Arrays.sort(v); // reorder value arr ascending
		// int numCoins = 0;

		// check if m is = any denomination in v (base case)
		for (int i = 0; i < v.length; i++) {
			if (m == v[i]) {
				reqv.put(m, v[i]);
				dp.put(m, 1);
				return 1; // trigger incr of req. coins further up tree
			}
		}
		// check if suboroblem has been computed
		if (dp.containsKey(m)) {
			// reqv.put(m, dp.get(m)); // iteration requires coin m, add to reqv map
			return dp.get(m);
		}
		int answer = Integer.MAX_VALUE;
		int deductedAns;
		int potentialAns;
		// begin memoization loop
		for (int k = v.length - 1; k >= 0; k--) {
			if (m > v[k]) { // denomination is smaller than m needed
				deductedAns = minChngDP(m - v[k], v, dp, reqv); // step down by v[k]
				potentialAns = deductedAns + 1; // other k may yield less coins
				if (potentialAns < answer) { // potential gives minimum coins used
					answer = potentialAns;
					reqv.put(m, v[k]); // save denomination to required coins map for charting
				}
			}
		}
		dp.put(m, answer); // save num coins used for charting
		return dp.get(m);
	}

	/*
	 * Boilerplate charting method
	 * 
	 * @param m the largest figure of currency which to create the chart from 0->m
	 * for
	 * 
	 * @param v[] array of denomination values
	 * 
	 * @param dp original dp table required for number of coins required
	 * 
	 * @param reqv HashMap of required denominations for each value 0 -> m
	 * 
	 * @return string formatted to alignment
	 */
	private String buildChart(int m, int[] v, HashMap<Integer, Integer> dp, HashMap<Integer, Integer> reqv) {
		String out = "";
		ArrayList<Integer> reqDenoms = reqDenomsPseudoSerialize(v, readReqMap(m, reqv)); // readReqMap truncates map to
																							// list of coins to make up
																							// m
		// print header
		out += String.format("%s%8s", "Change", "Coins");
		for (int i : v) {
			out += String.format("%10s", i + "c");
		}
		out += "\n"; // [m, coinsReq, #of(n...k)]
		for (int i = 1; i <= m; i++) { // [m, coinsreq]
			out += (i < 10) ? String.format("%d%12d", i, dp.get(i))
					: (i <= 99) ? String.format("%d%11d", i, dp.get(i)) : String.format("%d%10d", i, dp.get(i));
			reqDenoms = reqDenomsPseudoSerialize(v, readReqMap(i, reqv)); // for each i (m) we need a new reqDenoms, we
																			// already hold the data in reqv
			for (int z = 0; z < v.length; z++) { // [#of(n...k)]
				out += String.format("%10d", reqDenoms.get(z));
			}
			out += "\n";
		}
		// out += String.format("%15", reqDenoms.toString());
		// Change Coins 1c 5c 10c 25c 50c

		return out;
	}

	/*
	 * Creates and returns an ArrayList from 'reqDenoms' of the number of each
	 * denomination v for each
	 * 
	 * 
	 * This method essentially sums up the number of instances of each v existing in
	 * reqDenoms and populates the reqDenoms list with zeros for where
	 * currency amount m does not require any of 'coin' k
	 * 
	 * @param v[] array of denomination values
	 * 
	 * @param reqDenoms raw required denominations list from output readReqMap(...)
	 * 
	 * @return ArrayList<int> usable by buldChart(...)
	 * 
	 */
	private ArrayList<Integer> reqDenomsPseudoSerialize(int v[], ArrayList<Integer> reqDenoms) {
		ArrayList<Integer> serializedReqDenoms = new ArrayList<Integer>();
		for (int i : v) {
			serializedReqDenoms.add(Collections.frequency(reqDenoms, i));
		}
		return serializedReqDenoms;
	}

	/*
	 * ** PASSED MAP INTO RECURSIVE SOLUTION METHOD SO THIS METHOD IS NO LONGER
	 * ** NEEDED **
	 * 
	 * Count occurences of value v in reqv hashmap
	 * 
	 * Sumns up number of required 'coins' of each denomination, and assigns this
	 * sum as the value for key m.
	 * Required to avoid overly complicating buildChart(...) method.
	 * 
	 */
	private HashMap<Integer, Integer> buildPseudoDP(int m, int v[], HashMap<Integer, Integer> reqv) {
		HashMap<Integer, Integer> dp = new HashMap<Integer, Integer>();
		// dp = reqv.entrySet().stream().collect(Collectors.groupingBy(reqv.keySet(),
		// Collectors.counting()));
		// reqv.forEach((key, k) -> dp.put(key, Collections.frequency((Collection<?>)
		// reqv, k)));
		reqv.forEach((k, d) -> {
			if (dp.containsKey(d)) {

				// If d is present in dp,
				// incrementing it's count by 1
				dp.put(d, dp.get(d) + 1);
			} else {
				// d not present yet, add it
				dp.put(d, 1);
			}
		});
		// I KNOW this has to be possible with Collections/groupingBy/or something...
		return dp;
	}

	/*
	 * Converts map of coins required to produce value to ArrayList where 0's are
	 * included
	 * 
	 * 
	 * @param m the amount of currency which to truncate reqv for to find what coins
	 * to make it up
	 * 
	 * @param reqv HashMap of required denominations for each value 0 -> m
	 * 
	 * @return reqDenoms ArrayList of required denominations
	 */
	private ArrayList<Integer> readReqMap(int m, HashMap<Integer, Integer> reqv) {
		Stack<Integer> reqStk = new Stack<Integer>();
		// simplest way to populate any given denomination is with itself
		// upon readout (this method) of a higher order denomination, we can say the
		// given denomination can be populated by reading its value as it's key,
		// subsequently until the recursion chain bottoms out where the denomination is
		// itself the simplilest way to distribute/produce it.

		// We simply follow the 'chain of production' down the line (adding each
		// required 'coin' to the reqStk stack) of each way to minimally produce val k
		// until we reach the link where the cheapest way to produce 'coin' c is with
		// c itself.
		int curr_m = m;
		int curr_v;
		while (curr_m > 0) {
			curr_v = reqv.get(curr_m);
			reqStk.push(curr_v);
			curr_m = curr_m - curr_v;
		}

		// pop out (reverse) required denomination stack
		ArrayList<Integer> reqDenoms = new ArrayList<Integer>();

		while (!reqStk.isEmpty()) {
			reqDenoms.add(reqStk.pop());
		}

		return reqDenoms;
	}

	/*
	 * Thought I needed this method but I was not adding 'coin' c
	 * as required to produce val k, which is why I was having issues
	 */
	private void populateSelfMap(int[] v, Map<Integer, Integer> reqv) {
		for (int k = 0; k < v.length - 1; k++) {
			reqv.put(v[k], v[k]);
		}
	}

	// private void sliceRequired()
}

/*
 * REPORT
 * top-down memoization
 * 
 * Even though For this program we are ultimately interested in solutions for
 * every value 0->n, I decided to use a top-down approach because it felt more
 * intuitive as I was already thinking of where the DP table calls could be when
 * writing the recursive solution.
 * 
 * 
 */
