import java.util.*;
import java.io.*;

/**
 * Holds all the questions for the players
 */
public class QuestionBank {
	private ArrayList<Question> q;

	/**
	 * Constructor for the QuestionBank where questions are loaded in from text
	 * files
	 * 
	 * @author - Kaitlyn and Ritali
	 **/
	public QuestionBank() {
		try {
			q = new ArrayList<Question>();
			// Fill blank questions
			BufferedReader scan = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("assets/questions.txt")));
			String line = scan.readLine();
			while (line != null) {
				// Contains data for each question
				ArrayList<String> qproblem = new ArrayList<String>();
				ArrayList<String[]> qchoices = new ArrayList<String[]>();
				ArrayList<String> qanswer = new ArrayList<String>();
				String qmethod = "";
				// Scans problem and output
				readAnswerChoices(line, scan, qproblem, qchoices, qanswer);
				line = scan.readLine();
				// Scans actual method
				qmethod = readMethod(line, scan, qmethod);
				q.add(new Question(qproblem, qchoices, qanswer, qmethod));
				line = scan.readLine();
			}
			scan.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Helper method to read in problems for answer choices
	 * 
	 * @author - Ritali
	 * @param -    l - line to be read in
	 * @param scan - BufferedReader used for scanning txt file
	 * @param qp   - the ArrayList storing the problems
	 * @param qc   - the ArrayList storing the String[] arrays of answer choices
	 * @param qa   - the ArrayList holding the answer for the respective question in
	 *             qp
	 **/
	private void readAnswerChoices(String l, BufferedReader scan, ArrayList<String> qp, ArrayList<String[]> qc,
			ArrayList<String> qa) {
		try {
			if (l.equals("&&&")) {
				return;
			} else {
				// First line is the question
				String newl = "";
				boolean space = false;
				for (int i = 1; i <= l.length(); i++) {
					if (i % 40 == 0) {
						space = true;
					}
					if (space && l.substring(i - 1, i).equals(" ")) {
						newl += l.substring(i - 1, i) + "\n";
						space = false;
					} else {
						newl += l.substring(i - 1, i);
					}
				}
				l = newl;
				qp.add(l);
				l = scan.readLine();
				// Following line is possible choices
				String[] tempchoices = l.split("\\.\\.\\.");
				qc.add(tempchoices);
				l = scan.readLine();
				// Last line is true answer
				qa.add(l);
				l = scan.readLine();
				readAnswerChoices(l, scan, qp, qc, qa);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * helper method to read in the method provided for questions
	 * 
	 * @author - Ritali
	 * @param l    - line to be read in
	 * @param scan - BufferedReader used for scanning txt file
	 * @param qm   - String for the method
	 * @return - method String
	 **/

	private String readMethod(String l, BufferedReader scan, String qm) {
		try {
			if (l == null || l.equals("&&&")) {
				if (l == null) {
					return qm + "\n" + l;
				} else {
					return qm;
				}
			}
			qm += "\n" + l;
			l = scan.readLine();
			return readMethod(l, scan, qm);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Fetches a random question for the player
	 * 
	 * @author - Ritali
	 * @param finalQ - true if the question is the final one, false if not
	 */
	public void getRandomQuestion(boolean finalQ) {
		int index = (int) (Math.random() * q.size());
		if (finalQ) {
			Main.getScreen().finalFight(q.get(index).getRandomQValues());
		} else {
			Main.getScreen().displayQ(q.get(index).getRandomQValues());
		}
	}
}