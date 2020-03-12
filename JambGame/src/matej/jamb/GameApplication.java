package matej.jamb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

//import matej.jamb.checkers.InputChecker;
import matej.jamb.constants.JambConstants;
import matej.jamb.models.Dice;
import matej.jamb.models.Paper;
//import matej.jamb.models.enums.BoxType;
//import matej.jamb.models.enums.RowType;

public class GameApplication {

	private static ImageIcon[] diceImageIcons;

	private JFrame frame;
	private Map<Integer, JButton> bDiceMap;
	private JButton[][] bBoxArray;
	private JButton bThrowDice, bAnnounce, bWriteDown;

	private Map<Integer, Dice> diceMap;
	private Paper paper;
	private int diceThrows;
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
		diceThrows = 0;
		for (int i = 0; i < JambConstants.NUM_OF_DICE; i++) {
			diceMap.put(i, new Dice());
			
		}
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
		bBoxArray = new JButton[JambConstants.NUM_OF_BOXES][JambConstants.NUM_OF_ROWS];
		JButton bDice;
		for (Map.Entry<Integer, Dice> entry : diceMap.entrySet()) {
			bDice = new JButton(/*Integer.toString(entry.getValue().getCurrNum())*/);
			bDice.setIcon(diceImageIcons[entry.getValue().getCurrNum()-1]);
			bDice.setBackground(Color.LIGHT_GRAY);
			//			System.out.println(bDice.getColorModel());
			bDice.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					entry.getValue().setReserved(!entry.getValue().isReserved());
					if (entry.getValue().isReserved()) {
						bDiceMap.get(entry.getKey()).setBackground(Color.GRAY);
					} else {
						bDiceMap.get(entry.getKey()).setBackground(Color.LIGHT_GRAY);

					}

				}
			});
			bDiceMap.put(entry.getKey(), bDice);
		}

		bThrowDice = new JButton("Throw dice");
		bThrowDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DiceThrowAnimation worker = new DiceThrowAnimation(bDiceMap, 10, diceMap);
				try {
					worker.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		bAnnounce = new JButton("Announce");
		bWriteDown = new JButton("Write down");


		for (int i = 0; i < JambConstants.NUM_OF_BOXES; i++) {
			for (int j = 0; j < JambConstants.NUM_OF_ROWS; j++) {
				bBoxArray[i][j] = new JButton();
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
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.ipadx = JambConstants.BOX_WIDTH;
		gbc.ipady = JambConstants.BOX_HEIGHT;

		for (Map.Entry<Integer, JButton> entry : bDiceMap.entrySet()) {
			dicePanel.add(entry.getValue());
		}

		for (int i = 0; i < JambConstants.NUM_OF_BOXES; i++) {
			gbc.gridy = i;
			for (int j = 0; j < JambConstants.NUM_OF_ROWS; j++) {
				gbc.gridwidth = 1;
				gbc.gridx = j;
				if (j == 3) gbc.gridwidth = GridBagConstraints.REMAINDER;
				paperPanel.add(bBoxArray[i][j], gbc);
			}
		}
		east.add(bThrowDice);
		west.add(bAnnounce);
		south.add(bWriteDown);

		pane.add(east, BorderLayout.EAST);
		pane.add(west, BorderLayout.WEST);
		pane.add(paperPanel, BorderLayout.CENTER);
		pane.add(dicePanel, BorderLayout.NORTH);
		pane.add(south, BorderLayout.SOUTH);
		frame.pack();
	}


	static class DiceThrowAnimation extends SwingWorker<Void, Integer> {

		Map<Integer, JButton> bDiceMap;
		int max;
		Map<Integer, Dice> diceMap;
		Integer[] chunks;

		public DiceThrowAnimation(Map<Integer, JButton> bDiceMap, int max, Map<Integer, Dice> diceMap) {
			this.bDiceMap = bDiceMap;
			this.max = max;
			this.diceMap = diceMap;
			this.chunks = new Integer[diceMap.size()];
			System.out.println("stvorena dretva");
		}

		@Override
		public Void doInBackground() throws Exception {

			System.out.println("dretva radi");
			for (int i = 0; i < max; i++) {
				
				Thread.sleep(100);
				
				for (Map.Entry<Integer, Dice> entry : diceMap.entrySet()) {
					if (!entry.getValue().isReserved()) entry.getValue().roll();
//					System.out.println(entry.getKey() + ". = " + entry.getValue().getCurrNum());
					chunks[entry.getKey()] = (entry.getValue().getCurrNum());
				}
				System.out.println("saljem " + Arrays.toString(chunks));
				publish(chunks);
			}
			return null;
		}

		@Override
		protected void process(List<Integer> chunks) {
			int index = 0;
			System.out.println("radim");
			ImageIcon icon;
			for (Map.Entry<Integer, JButton> entry : bDiceMap.entrySet()) {
				System.out.println(index + " " + chunks.get(index));
				icon = diceImageIcons[chunks.get(entry.getKey())-1];
				icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH));
				entry.getValue().setIcon(icon);
				index++;
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
	//		while(diceThrows <= JambConstants.NUM_OF_THROWS) {
	//			int input = 0;
	//			if (diceThrows == 0) {
	//				input = InputChecker.checkInput(1, 1, "option number (1 - throw dice)");
	//				//				input = 1;
	//			} else if (diceThrows == 1 && announcement.isEmpty()) {
	//				input = InputChecker.checkInput(1, 4, "option number (1 - throw dice, 2 - keep some dice, 3 - write down, 4 - announce)");
	//				//				input = 3;
	//			} else if (diceThrows == JambConstants.NUM_OF_THROWS) {
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
