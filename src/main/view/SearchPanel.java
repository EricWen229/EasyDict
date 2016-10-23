package main.view;

// ================================
// Built-in modules

import java.awt.BorderLayout;
import javax.swing.JPanel;

// ================================
// User-defined modules

import main.controller.SearchPanelController;

// ================================
// Class SearchPanel

public class SearchPanel extends JPanel {

	private SearchInputBox inputBox;
	private ResultList resultList;

	SearchPanel() {
		super();

		this.inputBox = new SearchInputBox();
		this.resultList = new ResultList();

		this.setLayout(new BorderLayout());
		this.add(this.inputBox, BorderLayout.NORTH);
		this.add(this.resultList, BorderLayout.CENTER);

		this.inputBox.getDocument().addDocumentListener(new SearchPanelController(this));
	}

	void adjust() {
		this.inputBox.adjust();
		this.resultList.adjust();
	}

	public SearchInputBox getInputBox() {
		return this.inputBox;
	}

	public ResultList getResultList() {
		return this.resultList;
	}

}
