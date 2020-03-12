package matej.jamb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import matej.jamb.constants.JambConstants;
import matej.jamb.models.Dice;
import matej.jamb.models.Paper;
import matej.jamb.models.enums.BoxType;
import matej.jamb.models.enums.RowType;

public class GameApplication {

	private static ImageIcon[] diceImageIcons;

	private JFrame frame;
	private static Map<Integer, JButton> bDiceMap;
	private static ArrayList<ArrayList<JButton>> bBoxPaperList;
	private static JButton bRollDice;

	private static JButton bAnnounce;
	private static int announcement;
	private static boolean announcing;

	private static JButton bWriteDown;

	private static Map<Integer, Dice> diceMap;
	private static Paper paper;
	private static int diceRolls;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameApplication window = new GameApplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameApplication() {
		frame = new JFrame("Jamb");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setMinimumSize(new Dimension(JambConstants.FRAME_WIDTH_MIN, JambConstants.FRAME_HEIGHT_MIN));
		frame.setResizable(true);

		diceImageIcons = new ImageIcon[6];
		diceMap = new HashMap<>();
		paper = new Paper();
		announcement = -1;
		announcing = false;
		diceRolls = 0;
		for (int i = 0; i < JambConstants.NUM_OF_DICE; i++) {
			diceMap.put(i, new Dice());
		}
		System.out.println(diceMap);
		for (int i = 0; i < diceImageIcons.length; i++) {
			try {
				diceImageIcons[i] = new ImageIcon(ImageIO.read(getClass().getResource("/resources/dice" + (i+1) + ".bmp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		initializeButtons();
		addComponents();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//		System.out.println(dim);
		frame.setLocation(dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeButtons() {

		bDiceMap = new HashMap<>();
		bBoxPaperList = new ArrayList<ArrayList<JButton>>();
		//		bBoxArray = new JButton[JambConstants.NUM_OF_BOXES][JambConstants.NUM_OF_ROWS];

		JButton bDice;
		for (Map.Entry<Integer, Dice> entry : diceMap.entrySet()) {
			bDice = new JButton();
			bDice.setIcon(diceImageIcons[entry.getValue().getCurrNum()-1]);
			bDice.setBackground(Color.LIGHT_GRAY);
			bDice.setEnabled(false);
			bDice.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
					entry.getValue().setReserved(!entry.getValue().isReserved());
					bDiceMap.get(entry.getKey()).setBackground(entry.getValue().isReserved() ? Color.GRAY : Color.LIGHT_GRAY);
				}
			});
			bDiceMap.put(entry.getKey(), bDice);
		}

		for (int i = 0; i < JambConstants.NUM_OF_ROWS; i++) {
			ArrayList<JButton> bBoxRowList = new ArrayList<>();
			for (int j = 0; j < JambConstants.NUM_OF_BOXES; j++) {
				JButton button = new JButton();
				button.setBackground(Color.WHITE);
				button.setForeground(Color.BLUE);
				button.setEnabled(false);
				bBoxRowList.add(j, button);
			}
			bBoxPaperList.add(i, bBoxRowList);
		}

		// Button for rolling dice
		bRollDice = new JButton("ROLL DICE");
		bRollDice.setBackground(Color.WHITE);
		bRollDice.setForeground(Color.BLACK);
		bRollDice.setEnabled(true);
		bRollDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DiceRollAnimation worker = new DiceRollAnimation(bDiceMap, 10, diceMap);

				bWriteDown.setEnabled(false);
				bAnnounce.setEnabled(false);
				try {
					worker.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}

				diceRolls++;
			}
		});

		// Button for announcement of box
		bAnnounce = new JButton("ANNOUNCE");
		bAnnounce.setBackground(Color.WHITE);
		bAnnounce.setForeground(Color.BLACK);
		bAnnounce.setEnabled(false);
		bAnnounce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				toggleButtons();
				System.out.println(announcing);
				for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
					for (JButton bBox : bBoxRowList) {
						if (bBoxPaperList.indexOf(bBoxRowList) == RowType.ANNOUNCE.ordinal()) {
							if (paper.getRow(RowType.ANNOUNCE).getBox(BoxType.values()[bBoxRowList.indexOf(bBox)]).isAvailable()) {
								bBox.setEnabled(announcing);
							} else {
								bBox.setBackground(Color.LIGHT_GRAY);
							}
						} else {
							if (announcing)	bBox.setBackground(Color.LIGHT_GRAY);
							else bBox.setBackground(Color.WHITE);
						}
					}
				}
			}

			private void toggleButtons() {
				bWriteDown.setEnabled(!bWriteDown.isEnabled());
				bRollDice.setEnabled(!bRollDice.isEnabled());
				announcing = !announcing;
				setDiceEnabled(true, false);
			}
		});

		// Button for writing down dice result
		bWriteDown = new JButton("WRITE DOWN");
		bWriteDown.setBackground(Color.WHITE);
		bWriteDown.setForeground(Color.BLACK);
		bWriteDown.setEnabled(false);
		bWriteDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				bAnnounce.setEnabled(false);
				bRollDice.setEnabled(false);
				if (announcement != -1) {
					endTurn();
				} else {
					toggleButtons();
					for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
						for (JButton bBox : bBoxRowList) {
							if (bBoxPaperList.indexOf(bBoxRowList) == RowType.ANNOUNCE.ordinal() && diceRolls > 1) continue;
							if (paper.getRow(RowType.values()[bBoxPaperList.indexOf(bBoxRowList)]).getBox(BoxType.values()[bBoxRowList.indexOf(bBox)]).isAvailable())
								bBox.setEnabled(true);
							else 
								bBox.setBackground(Color.LIGHT_GRAY);
						}
					}
				}
			}

			private void toggleButtons() {
				if (diceRolls < JambConstants.NUM_OF_ROLLS) {
					bRollDice.setEnabled(!bRollDice.isEnabled());
					bAnnounce.setEnabled(!bAnnounce.isEnabled());
					setDiceEnabled(true, false);
				}
			}
		});

		// Box action listeners
		for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
			for (JButton bBox : bBoxRowList) {
				bBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						RowType rowType = RowType.values()[bBoxPaperList.indexOf(bBoxRowList)];
						BoxType boxType = BoxType.values()[bBoxRowList.indexOf(bBox)];
						if (announcing) {
							announcement = boxType.ordinal();
							bWriteDown.setEnabled(true);
							bRollDice.setEnabled(true);
							bAnnounce.setEnabled(false);
							setDiceEnabled(true, false);
							for (ArrayList<JButton> r : bBoxPaperList) {
								for (JButton b : r) {
									if (b != bBox) {
										b.setBackground(Color.LIGHT_GRAY);
									}
									b.setEnabled(false);
								}
							}
						} else {
							paper.getRow(rowType).writeDown(new ArrayList<Dice>(diceMap.values()), boxType.ordinal());
							bBox.setText(Integer.toString(paper
									.getRow(rowType)
									.getBox(boxType)
									.getValue()));
							endTurn();
						}
					}
				});
			}
		}
	}

	/**
	 * Resets all dice.
	 * @param enable if true enables all dice
	 * @param reset if true sets all dice to reserved
	 */
	protected static void setDiceEnabled(boolean enable, boolean reset) {
		for (Map.Entry<Integer, JButton> entry : bDiceMap.entrySet()) {
			entry.getValue().setEnabled(enable);
			if (reset) {
				diceMap.get(entry.getKey()).setReserved(false);
			}

			entry.getValue().setBackground(diceMap.get(entry.getKey()).isReserved() ? Color.GRAY : Color.LIGHT_GRAY);

		}
	}

	protected static void endTurn() {

		if (announcement != -1) {
			paper.getRow(RowType.ANNOUNCE).writeDown(new ArrayList<Dice>(diceMap.values()), announcement);
			bBoxPaperList.get(RowType.ANNOUNCE.ordinal()).get(announcement)
			.setText(Integer.toString(paper
					.getRow(RowType.ANNOUNCE)
					.getBox(BoxType.values()[announcement])
					.getValue()));
		}

		// Disable all box buttons
		for (ArrayList<JButton> bList : bBoxPaperList) {
			for (JButton b : bList) {
				b.setEnabled(false);
				b.setBackground(Color.WHITE);
			}
		}

		// Reset box button texts
		for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
			for (JButton bBox : bBoxRowList) {
				RowType rowType = RowType.values()[bBoxPaperList.indexOf(bBoxRowList)];
				BoxType boxType = BoxType.values()[bBoxRowList.indexOf(bBox)];
				if(!paper.getRow(rowType).getBox(boxType).isWritten()) {
					bBox.setText("");
				}
			}
		}

		// reset dice
		diceRolls = 0;
		setDiceEnabled(false, true);

		// enable only dice roll button
		bAnnounce.setEnabled(false);
		bWriteDown.setEnabled(false);
		bRollDice.setEnabled(true);
		announcing = false;
		announcement = -1;
	}

	/**
	 * Add the contents to the frame.
	 */
	private void addComponents() {

		Container pane = frame.getContentPane();

		JPanel east = new JPanel(new FlowLayout());
		JPanel west = new JPanel(new FlowLayout());
		JPanel dicePanel = new JPanel(new FlowLayout());
		JPanel paperPanel = new JPanel(new GridBagLayout());
		JPanel south = new JPanel(new FlowLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.ipadx = JambConstants.BOX_WIDTH;
		gbc.ipady = JambConstants.BOX_HEIGHT;

		for (Map.Entry<Integer, JButton> entry : bDiceMap.entrySet()) {
			dicePanel.add(entry.getValue());
		}

		for (int i = 0; i < JambConstants.NUM_OF_ROWS; i++) {
			gbc.gridwidth = 1;
			gbc.gridx = i;
			for (int j = 0; j < JambConstants.NUM_OF_BOXES; j++) {
				gbc.gridy = j;
				if (i == 3) gbc.gridwidth = GridBagConstraints.REMAINDER;
				paperPanel.add(bBoxPaperList.get(i).get(j), gbc);
			}
		}

		east.add(bRollDice);
		west.add(bAnnounce);
		south.add(bWriteDown);

		pane.add(east, BorderLayout.EAST);
		pane.add(west, BorderLayout.WEST);
		pane.add(paperPanel, BorderLayout.CENTER);
		pane.add(dicePanel, BorderLayout.NORTH);
		pane.add(south, BorderLayout.SOUTH);
		frame.pack();
	}

	/**
	 * 
	 * SwingWorker class that changes the icons for dice while rolling for animation
	 */
	static class DiceRollAnimation extends SwingWorker<Void, Integer> {

		Map<Integer, JButton> bDiceMap;
		int max;
		Map<Integer, Dice> diceMap;
		Integer[] chunks;

		public DiceRollAnimation(Map<Integer, JButton> bDiceMap, int max, Map<Integer, Dice> diceMap) {
			this.bDiceMap = bDiceMap;
			this.max = max;
			this.diceMap = diceMap;
			this.chunks = new Integer[diceMap.size()];
		}

		@Override
		public Void doInBackground() throws Exception {

			//			System.out.println("dretva radi");
			for (int i = 0; i < max; i++) {

				Thread.sleep(100);

				for (Map.Entry<Integer, Dice> entry : diceMap.entrySet()) {
					if (!entry.getValue().isReserved()) entry.getValue().roll();
					//					System.out.println(entry.getKey() + ". = " + entry.getValue().getCurrNum());
					chunks[entry.getKey()] = (entry.getValue().getCurrNum());
				}
				//				System.out.println("saljem " + Arrays.toString(chunks));
				publish(chunks);
			}
			return null;
		}

		@Override
		public void done() {
			bWriteDown.setEnabled(true);
			if (diceRolls == 1) {
				bAnnounce.setEnabled(true);
				setDiceEnabled(true, false);
			} else if (diceRolls == JambConstants.NUM_OF_ROLLS) {
				if (announcement == -1) {
					bRollDice.setEnabled(false);
					bWriteDown.setEnabled(true);
				} else {
					endTurn();
				}
			}
		}
		@Override
		protected void process(List<Integer> chunks) {
			//			System.out.println("radim");
			ImageIcon icon;
			for (Map.Entry<Integer, JButton> entry : bDiceMap.entrySet()) {
				//				System.out.println(index + " " + chunks.get(index));
				icon = diceImageIcons[chunks.get(entry.getKey())-1];
				icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH));
				entry.getValue().setIcon(icon);
			}
		}
	}

	//	private void playTurn() {
	//
	//		int diceThrows = 0;
	//		String announcement = "";
	//		Map<Integer, String> availBoxMap;
	//		RowType rowType;
	//		BoxType boxType;
	//		while(diceThrows <= JambConstants.NUM_OF_ROLLS) {
	//			int input = 0;
	//			if (diceThrows == 0) {
	//				input = InputChecker.checkInput(1, 1, "option number (1 - throw dice)");
	//				//				input = 1;
	//			} else if (diceThrows == 1 && announcement.isEmpty()) {
	//				input = InputChecker.checkInput(1, 4, "option number (1 - throw dice, 2 - keep some dice, 3 - write down, 4 - announce)");
	//				//				input = 3;
	//			} else if (diceThrows == JambConstants.NUM_OF_ROLLS) {
	//				input = InputChecker.checkInput(3, 3, "option number (3 - write down)");
	//			} else {
	//				input = InputChecker.checkInput(1, 3, "option number (1 - throw dice, 2 - keep some dice, 3 - write down");
	//			}
	//			switch (input) {
	//			case 1:
	//				//				throwDice();
	//				diceThrows++;
	//				break;
	//			case 2:
	//				System.out.println(diceList);
	//				String diceForKeep = String.format("%5s", Integer.toBinaryString(InputChecker.checkInput(1, 31, "dice that you want to keep (select already kept dice to unkeep)"))
	//						.replace(' ', '0'));
	//				for (int i = 0; i < diceForKeep.length(); i++) {
	//					if (diceForKeep.charAt(i) == '1') {
	//						diceList.get(i).setReserved(!diceList.get(i).isReserved());
	//					}
	//				}
	//				System.out.println(diceList);
	//				break;
	//			case 3:
	//				System.out.println("\nAvailable boxes are:\n");
	//				availBoxMap = paper.getAvailBoxMap(diceThrows, announcement);
	//				for (Integer boxIndex : availBoxMap.keySet()) {
	//					System.out.println(boxIndex + ". " + availBoxMap.get(boxIndex).toString());
	//				}
	//				input = InputChecker.checkInput(1, availBoxMap.size(), "index of box");
	//				rowType = RowType.valueOf(availBoxMap.get(input).split(" ")[0]);
	//				boxType = BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
	//				paper.getRow(rowType).writeDown(diceList, boxType.ordinal());
	//				System.out.println(paper);
	//				return;
	//			case 4:
	//				System.out.println("\nAvailable announcements are:\n");
	//				announcement = "ANNOUNCE";
	//				availBoxMap = paper.getAvailBoxMap(diceThrows, announcement);
	//				for (Integer boxIndex : availBoxMap.keySet()) {
	//					System.out.println(boxIndex + ". " + availBoxMap.get(boxIndex).toString());
	//				}
	//				System.out.println(diceList);
	//				input = InputChecker.checkInput(1, availBoxMap.size(), "index of announcement");
	//				announcement += " " + BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
	//				break;
	//			}
	//		}
	//
	//		System.out.println("\n");
	//	}
}
