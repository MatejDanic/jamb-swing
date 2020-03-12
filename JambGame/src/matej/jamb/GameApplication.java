package matej.jamb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
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
	private static ImageIcon[] boxImageIcons;

	private JFrame frame;
	private static Map<Integer, JButton> bDiceMap;
	private static ArrayList<ArrayList<JButton>> bBoxPaperList;
	private static boolean paperEnabled;
	private static JButton bRollDice;
	private static JButton bAnnounce;
	private static int announcement;
	private static boolean announcing;

	private static Map<Integer, JButton> bOtherMap;

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
		frame.setMinimumSize(new Dimension(JambConstants.FRAME_WIDTH, JambConstants.FRAME_HEIGHT));
		frame.setResizable(false);

		paper = new Paper();
		announcement = -1;
		announcing = false;
		paperEnabled = false;
		diceRolls = 0;

		diceMap = new HashMap<>();
		for (int i = 0; i < JambConstants.NUM_OF_DICE; i++) {
			diceMap.put(i, new Dice());
		}
		//		System.out.println(diceMap);

		diceImageIcons = new ImageIcon[6];
		for (int i = 0; i < diceImageIcons.length; i++) {
			try {
				diceImageIcons[i] = new ImageIcon(ImageIO.read(getClass().getResource("/dice/" + (i+1) + ".bmp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		boxImageIcons = new ImageIcon[9];
		for (int i = 0; i < boxImageIcons.length; i++) {
			try {
				boxImageIcons[i] = new ImageIcon(ImageIO.read(getClass().getResource("/box/" + (i+1) + ".bmp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//		System.out.println("Initializing...");
		initialize();
		//		System.out.println("Initializing successful");
		//		System.out.println("Adding components...");
		addComponents();
		//		System.out.println("Adding components successful");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//		System.out.println(frame.getSize());
		frame.setLocation(dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2);
	}

	/**
	 * Initialize buttons and labels of the frame.
	 */
	private void initialize() {

		// Buttons for dice
		bDiceMap = new HashMap<>();
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

		// Button for rolling dice
		bRollDice = new JButton("ROLL DICE");
		bRollDice.setBackground(Color.WHITE);
		bRollDice.setForeground(Color.BLACK);
		bRollDice.setEnabled(true);

		bRollDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				DiceRollAnimation worker = new DiceRollAnimation(bDiceMap, 10, diceMap);
				setPaperEnabled(false);
//				bWriteDown.setEnabled(false);
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
				setPaperEnabled(!isPaperEnabled());
//				bWriteDown.setEnabled(!bWriteDown.isEnabled());
				bRollDice.setEnabled(!bRollDice.isEnabled());
				announcing = !announcing;
				setDiceEnabled(true, false);
			}
		});

		// Button for writing down dice result
//		bWriteDown = new JButton("WRITE DOWN");
//		bWriteDown.setBackground(Color.WHITE);
//		bWriteDown.setForeground(Color.BLACK);
//		bWriteDown.setEnabled(false);
//
//		bWriteDown.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent event) {
//
//				if (announcement != -1) {
//					endTurn();
//				} else {
//					toggleButtons();
//					for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
//						for (JButton bBox : bBoxRowList) {
//							if (bBoxPaperList.indexOf(bBoxRowList) == RowType.ANNOUNCE.ordinal() && diceRolls > 1) continue;
//							if (paper.getRow(RowType.values()[bBoxPaperList.indexOf(bBoxRowList)]).getBox(BoxType.values()[bBoxRowList.indexOf(bBox)]).isAvailable())
////							if (writing)	{
////								bBox.setBackground(Color.LIGHT_GRAY);
////							else bBox.setBackground(Color.WHITE);
//								bBox.setEnabled(true);
////							else 
////								bBox.setBackground(Color.LIGHT_GRAY);
//						}
//					}
//				}
//			}
//
//			private void toggleButtons() {
//				bRollDice.setEnabled(!bRollDice.isEnabled());
//				bAnnounce.setEnabled(!bAnnounce.isEnabled());
////				writing = !writing;
//				for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
//					for (JButton bBox : bBoxRowList) {
//						if (bBoxPaperList.indexOf(bBoxRowList) == RowType.ANNOUNCE.ordinal() && diceRolls > 1) continue;
//						if (paper.getRow(RowType.values()[bBoxPaperList.indexOf(bBoxRowList)]).getBox(BoxType.values()[bBoxRowList.indexOf(bBox)]).isAvailable())
//							bBox.setEnabled(false);
//						else 
//							bBox.setBackground(Color.WHITE);
//					}
//				}
//				if (diceRolls < JambConstants.NUM_OF_ROLLS) {
//					setDiceEnabled(true, false);
//				} else {
//					setDiceEnabled(false, true);
//				}
//				
//			}
//		});

		// Paper box buttons
		bBoxPaperList = new ArrayList<ArrayList<JButton>>();
		for (int i = 0; i < JambConstants.NUM_OF_ROWS; i++) {

			ArrayList<JButton> bBoxRowList = new ArrayList<>();
			for (int j = 0; j < JambConstants.NUM_OF_BOXES; j++) {

				JButton button = new JButton();
				button.setBackground(Color.WHITE);
				button.setFont(new Font("Arial", Font.BOLD, 20));
				button.setEnabled(false);
				button.setPreferredSize(new Dimension(JambConstants.BOX_WIDTH, JambConstants.BOX_HEIGHT));

				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						RowType rowType = RowType.values()[bBoxPaperList.indexOf(bBoxRowList)];
						BoxType boxType = BoxType.values()[bBoxRowList.indexOf(button)];
						if (announcing) {
							announcement = boxType.ordinal();
							setPaperEnabled(true);
							bRollDice.setEnabled(true);
							bAnnounce.setEnabled(false);
							setDiceEnabled(true, false);
							for (ArrayList<JButton> r : bBoxPaperList) {
								for (JButton b : r) {
									if (b != button) {
										b.setBackground(Color.LIGHT_GRAY);
									}
									b.setEnabled(false);
								}
							}
						} else {
							paper.getRow(rowType).writeDown(new ArrayList<Dice>(diceMap.values()), boxType.ordinal());
							button.setText(Integer.toString(paper
									.getRow(rowType)
									.getBox(boxType)
									.getValue()));
							endTurn();
						}
					}
				});
				bBoxRowList.add(j, button);
			}
			bBoxPaperList.add(i, bBoxRowList);
		}

		// Other buttons
		bOtherMap = new HashMap<>();
		for (int i = 0; i < 36; i++) {
			JButton bOther = new JButton();
			bOther.setBackground(Color.WHITE);
			if (i <= 5) {
				bOther.setIcon(boxImageIcons[i]);
			} else if (i >= 6 && i <= 15) {
				if (i == 6 || i == 9 || i == 15) {
					bOther.setBackground(new Color(176, 196, 222));
				}
				System.out.println(i);
				bOther.setText("<html><font size=4><center>" + paper.getRowStringList().get(i-6) + "</center></font></html>");
			} else if (i == 16) {
				bOther.setIcon(boxImageIcons[6]);
			} else if(i == 20) {
				bOther.setIcon(boxImageIcons[7]);
			} else if(i == 24) {
				bOther.setIcon(boxImageIcons[8]);
			} else if (i == 28) {
				bOther.setText("<html><font size=2><center>NAJAVA</center></font></html>");
			} else if (i == 17 || i == 21 || i == 25 || i == 29 || i == 32
					|| i == 18 || i == 22 || i == 26 || i == 30 || i == 33
					|| i == 19 || i == 23 || i == 27 || i == 31 || i == 34 || i == 35) {
				bOther.setBackground(new Color(176, 196, 222));
				bOther.setText("0");
			}
			bOther.setPreferredSize(new Dimension(JambConstants.BOX_WIDTH, JambConstants.BOX_HEIGHT));
			bOtherMap.put(i, bOther);
		}
	}

	protected boolean isPaperEnabled() {
		return paperEnabled;
	}

	protected static void setPaperEnabled(boolean b) {
		for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
			for (JButton bBox : bBoxRowList) {
				if (bBoxPaperList.indexOf(bBoxRowList) == RowType.ANNOUNCE.ordinal() && diceRolls > 1) continue;
				if (paper.getRow(RowType.values()[bBoxPaperList.indexOf(bBoxRowList)]).getBox(BoxType.values()[bBoxRowList.indexOf(bBox)]).isAvailable())
					bBox.setEnabled(b);
			}
		}
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

		for (Map.Entry<Integer, JButton> entry : bDiceMap.entrySet()) {
			dicePanel.add(entry.getValue());
		}

		for (int i = 0; i < 6; i++) {
			gbc.gridx = i;
			if (i == 0) {
				for (int j = 0; j < 16; j++) {
					gbc.gridy = j+1;
					paperPanel.add(bOtherMap.get(j), gbc);
				}
			} else if (i == 5) {
				gbc.gridy = 7;
				paperPanel.add(bOtherMap.get(32), gbc);
				gbc.gridy = 10;
				paperPanel.add(bOtherMap.get(33), gbc);
				gbc.gridy = 16;
				paperPanel.add(bOtherMap.get(34), gbc);
			} else {
				gbc.gridy = 0;
				for (int j = 0; j < 13; j++) {
					if (j == 0) {
						paperPanel.add(bOtherMap.get(16 + 4*(i-1)), gbc);
						gbc.gridy++;
					} else if (j == 6) {
						paperPanel.add(bOtherMap.get(17 + 4*(i-1)), gbc);
						gbc.gridy++;
					} else if (j == 8) {
						paperPanel.add(bOtherMap.get(18 + 4*(i-1)), gbc);
						gbc.gridy++;
					}
					paperPanel.add(bBoxPaperList.get(i-1).get(j), gbc);
					gbc.gridy++;
					if (j == 12) {
						paperPanel.add(bOtherMap.get(19 + 4*(i-1)), gbc);
					}
				}
			}

			if (i == 4) {
				gbc.gridy = 17;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				paperPanel.add(bOtherMap.get(35), gbc);
				gbc.gridwidth = 1;
			}
		}

		east.add(bRollDice);
		east.add(bAnnounce);
//		south.add(bWriteDown);

		pane.add(east, BorderLayout.EAST);
		pane.add(west, BorderLayout.WEST);
		pane.add(paperPanel, BorderLayout.CENTER);
		pane.add(dicePanel, BorderLayout.NORTH);
		pane.add(south, BorderLayout.SOUTH);
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

			for (int i = 0; i < max; i++) {

				Thread.sleep(100);
				for (Map.Entry<Integer, Dice> entry : diceMap.entrySet()) {
					if (!entry.getValue().isReserved()) entry.getValue().roll();
					chunks[entry.getKey()] = (entry.getValue().getCurrNum());
				}
				publish(chunks);
			}
			return null;
		}

		@Override
		public void done() {

			setPaperEnabled(true);
//			bWriteDown.setEnabled(true);
			if (diceRolls == 1) {
				bAnnounce.setEnabled(true);
				setDiceEnabled(true, false);
			} else if (diceRolls == JambConstants.NUM_OF_ROLLS) {
				if (announcement == -1) {
					bRollDice.setEnabled(false);
					setPaperEnabled(true);

				} else {
					endTurn();
				}
			}
		}
		@Override
		protected void process(List<Integer> chunks) {

			ImageIcon icon;
			for (Map.Entry<Integer, JButton> entry : bDiceMap.entrySet()) {
				icon = diceImageIcons[chunks.get(entry.getKey())-1];
				icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH));
				entry.getValue().setIcon(icon);
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
		int upperScore, lowerScore, middleScore, upperSum, middleSum, lowerSum;
		upperSum = 0;
		middleSum = 0;
		lowerSum = 0;
		for (ArrayList<JButton> bBoxRowList : bBoxPaperList) {
			for (JButton bBox : bBoxRowList) {
				RowType rowType = RowType.values()[bBoxPaperList.indexOf(bBoxRowList)];
				BoxType boxType = BoxType.values()[bBoxRowList.indexOf(bBox)];
				if(!paper.getRow(rowType).getBox(boxType).isWritten()) {
					bBox.setText("");
				}
			}
			
			// calculate scores
			int i = bBoxPaperList.indexOf(bBoxRowList);
//			System.out.println(i);
			upperScore = paper.getRow(RowType.values()[i]).getUpperScore();
			upperSum += upperScore;
			bOtherMap.get(17+4*i).setText(Integer.toString(upperScore));
			middleScore = paper.getRow(RowType.values()[i]).getMiddleScore();
			middleSum += middleScore;
			bOtherMap.get(18+4*i).setText(Integer.toString(middleScore));
			lowerScore = paper.getRow(RowType.values()[i]).getLowerScore();
			lowerSum += lowerScore;
			bOtherMap.get(19+4*i).setText(Integer.toString(lowerScore));
		}
		bOtherMap.get(32).setText(Integer.toString(upperSum));
		bOtherMap.get(33).setText(Integer.toString(middleSum));
		bOtherMap.get(34).setText(Integer.toString(lowerSum));
		bOtherMap.get(35).setText(Integer.toString(upperSum+middleSum+lowerSum));
		
		// reset dice
		diceRolls = 0;
		setDiceEnabled(false, true);
		
		// enable dice roll button, disable all else
		bRollDice.setEnabled(true);
		bAnnounce.setEnabled(false);
		setPaperEnabled(false);
		announcing = false;
		announcement = -1;
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