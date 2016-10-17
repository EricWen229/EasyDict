package main.model.dict;

import main.wordinfo.WordInfo;

import java.util.*;

class Trie {

	private TrieNode root;

	private static Trie trie = null;

	private Trie() {
		this.root = new TrieNode();
	}

	static Trie createTrie() {
		if (Trie.trie == null) {
			Trie.trie = new Trie();
		}
		return Trie.trie;
	}

	private static boolean validString(String str) {
		if (str.length() == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); ++ i) {
			if (!TrieNode.validChar(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	boolean insert(String str, WordInfo info) {
		if (!validString(str)) {
			return false;
		}
		TrieNode currentNode = root;
		for (int i = 0; i < str.length(); ++ i) {
			currentNode = currentNode.addChild(str.charAt(i));
		}
		currentNode.setEndOfWord();
		currentNode.addInfo(info);
		return true;
	}

	void printTrie() {
		StringBuffer strBuf = new StringBuffer();
		dfsPrintAll(this.root, strBuf);
	}

	private static void dfsPrintAll(TrieNode node, StringBuffer strBuf) {
		if (node.isEndOfWord()) {
			System.out.println(strBuf.toString());
		}
		if (!node.haveChild()) {
			return;
		}
		TrieNodeIterator nodeIterator = node.createIterator();
		while (nodeIterator.hasNext()) {
			int pos = nodeIterator.getPosition();
			char ch = TrieNode.getChar(pos);
			strBuf.append(ch);
			TrieNode nextNode = nodeIterator.next();
			dfsPrintAll(nextNode, strBuf);
			strBuf.deleteCharAt(strBuf.length() - 1);
		}
	}

	ArrayList<String> searchWithCommonPrefix(String target) {
		ArrayList<String> results = new ArrayList<>();
		StringBuffer strBuf = new StringBuffer();
		dfsWithCommonPrefix(this.root, target, 0, results, strBuf);
		return results;
	}

	private static void dfsWithCommonPrefix(TrieNode node, String target, int currPos, ArrayList<String> results, StringBuffer strBuf) {
		while (currPos < target.length()) { // to be matched
			TrieNode next = node.findChar(target.charAt(currPos));
			if (next == null) {
				return;
			}
			strBuf.append(target.charAt(currPos));
			++ currPos;
			node = next;
		}
		if (node.isEndOfWord()) {
			results.add(strBuf.toString());
		}
		if (!node.haveChild()) {
			return;
		}
		TrieNodeIterator nodeIterator = node.createIterator();
		while (nodeIterator.hasNext()) {
			int pos = nodeIterator.getPosition();
			char ch = TrieNode.getChar(pos);
			TrieNode nextNode = nodeIterator.next();
			strBuf.append(ch);
			dfsWithCommonPrefix(nextNode, target, currPos, results, strBuf);
			strBuf.deleteCharAt(strBuf.length() - 1);
		}
	}

	ArrayList<String> searchWithEditDist(String target, int editDist) {
		ArrayList<String> results = new ArrayList<>();
		StringBuffer strBuf = new StringBuffer();
		dfsWithEditDist(this.root, target, 0, results, strBuf, editDist);
		return results;
	}

	private static void dfsWithEditDist(TrieNode node, String target, int currPos, ArrayList<String> results, StringBuffer strBuf, int editDist) {
		boolean targetComplete = currPos == target.length() && editDist == 0;
		boolean wordFoundInTrie = node.isEndOfWord();
		boolean fitRestOfTarget = editDist > 0 && target.length() - strBuf.length() == editDist;
		boolean endOfTrie = !node.haveChild();
		boolean endOfTarget = currPos > target.length();

		if (wordFoundInTrie
				&& (targetComplete || fitRestOfTarget)) {
			results.add(strBuf.toString());
		}

		if (targetComplete || endOfTrie || endOfTarget) {
			return;
		}

		// then let's deal with edit distance!
		if (editDist < 0) {
			System.exit(-1);
		}
		else if (editDist > 0) {

			// FIRST CASE
			// delete one character, ++ currPos, strBuf unchanged, -- editDist
			dfsWithEditDist(node, target, currPos + 1, results, strBuf, editDist - 1);

			TrieNodeIterator nodeIterator = node.createIterator();
			while (nodeIterator.hasNext()) {
				int pos = nodeIterator.getPosition();
				char ch = TrieNode.getChar(pos);
				TrieNode nextNode = nodeIterator.next();

				// SECOND CASE
				// insert or append one character, currPos unchanged, append to strBuf, -- editDist
				strBuf.append(ch);
				dfsWithEditDist(nextNode, target, currPos, results, strBuf, editDist - 1);
				strBuf.deleteCharAt(strBuf.length() - 1);

				if (currPos < target.length()) {
					// THIRD CASE
					// modify one character, ++ currPos, append to strBuf, -- editDist
					if (ch != target.charAt(currPos)) {
						strBuf.append(ch);
						dfsWithEditDist(nextNode, target, currPos + 1, results, strBuf, editDist - 1);
						strBuf.deleteCharAt(strBuf.length() - 1);
					}

					// yield edit distance to next search
					if (ch == target.charAt(currPos)) {
						strBuf.append(ch);
						dfsWithEditDist(nextNode, target, currPos + 1, results, strBuf, editDist);
						strBuf.deleteCharAt(strBuf.length() - 1);
					}
				}
			}
		}
		else {
			// precise match with editDist = 0
			TrieNode nextNode = node.findChar(target.charAt(currPos));
			if (nextNode != null) {
				strBuf.append(target.charAt(currPos));
				dfsWithEditDist(nextNode, target, currPos + 1, results, strBuf, 0);
				strBuf.deleteCharAt(strBuf.length() - 1);
			}
		}
	}

}
