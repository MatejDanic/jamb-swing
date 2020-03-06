package matej.jamb.paper;

import java.util.ArrayList;
import java.util.List;

import matej.jamb.dice.Dice;

public class Row {

	public int[] zapis;
	private Boolean[] otvoren;
	private boolean finished;
	private RowType type;

	public Boolean isRowFinished() {
		return finished;
	}

	public RowType getType() {
		return type;
	}
	
	public Row(RowType type) {
		this.zapis = new int[13];
		this.otvoren = new Boolean[13];
		this.type = type;
		for (int i = 0; i < otvoren.length; i++) {
			zapis[i] = 0;
			otvoren[i] = false;
		}
		switch (type) {
		case DOWNWARD:
			otvoren[0] = true;
			break;
		case UPWARD:
			otvoren[otvoren.length-1] = true;
			break;
		case ANYDIR:
			for (int i = 0; i < otvoren.length; i++) {
				otvoren[i] = true;
			}
			break;
		default:
			break;
		}
	}

	public int getUpperScore() {
		int score = 0;
		for (int i = 0; i < 6; i++) {
			score += zapis[i];
		}
		return score;
	}

	public int getMiddleScore() {
		return 0;
	}
	public int getLowerScore() {
		int zbroj = 0;
		for (int i = 8; i < 13; i++) {
			zbroj += zapis[i];
		}
		return zbroj;
	}

	public int getRazlika() {
		return (zapis[6] - zapis[7]);
	}

	public int getScore() {
		int zbrojDo60 = getUpperScore();
		int razlika = getDifference();
		int zbrojVeliki = getZbrojVeliki();
		return (zbrojDo60 >= 60 ? zbrojDo60: zbrojDo60+30) + razlika * zapis[0] + zbrojVeliki;
	}

	public void zapisi(List<Dice> diceList, int RowniBr) {
		int zbroj = 0;
		if (RowniBr >= 0 && RowniBr <= 5) {
			for (Dice k : diceList) {
				int trenutniRez = k.getCurrNum();
				if (trenutniRez == RowniBr+1) {
					zbroj += trenutniRez;
				}
			}
			zapis[RowniBr] = zbroj;
		} else if (RowniBr >= 6 && RowniBr <= 7) {
			for (Dice k : diceList) {
				zbroj += k.getCurrNum();
			}
			zapis[RowniBr] = zbroj;
		} else if (RowniBr >= 8) {
			switch (RowniBr) {
			case 8:
				zapis[RowniBr] = provjeriTris(diceList);
			case 9:
				zapis[RowniBr] = provjeriSkalu(diceList);
			case 10:
				zapis[RowniBr] = provjeriFull(diceList);
			case 11:
				zapis[RowniBr] = provjeriPoker(diceList);
			case 12:
				zapis[RowniBr] = provjeriJamb(diceList);
			}
		}
		otvoren[RowniBr] = false;

		if (RowniBr > 0 && RowniBr < 12) {
			if (type == RowType.DOWNWARD) {
				otvoren[RowniBr+1] = true;
			} else if(type == RowType.UPWARD) {
				otvoren[RowniBr-1] = true;
			}
		}

	}

	public int provjeriTris(List<Dice> diceList) {
		int rezultat = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int zbroj = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					zbroj += k2.getCurrNum();
				}
			}
			if (broj == 3) {
				rezultat = zbroj;
				break;
			}
		}
		return rezultat;
	}

	public int provjeriSkalu(List<Dice> diceList) {
		int rezultat = 0;
		List<Integer> skala = new ArrayList<>();
		skala.add(2);
		skala.add(3);
		skala.add(4);
		skala.add(5);
		List<Integer> brojevi = new ArrayList<>();
		for (Dice k : diceList) {
			brojevi.add(k.getCurrNum());
		}
		if (brojevi.containsAll(skala)){
			if (brojevi.contains(1)) {
				rezultat = 35;
			} else if (brojevi.contains(6))
				rezultat = 45;
		}
		return rezultat;
	}

	public int provjeriFull(List<Dice> diceList) {
		int rezultat = 0;
		int zbroj2 = 0; 
		int zbroj3 = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int zbroj = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					zbroj += k2.getCurrNum();
				}
			}
			if (broj == 2) {
				zbroj2 = zbroj;
			} else if (broj == 3) {
				zbroj3 = zbroj;
			}
			if (zbroj2 != 0 && zbroj3 != 0) {
				rezultat = zbroj2 + zbroj3;
				break;
			}
		}
		return rezultat;
	}

	public int provjeriPoker(List<Dice> diceList) {
		int rezultat = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int zbroj = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					zbroj += k2.getCurrNum();
				}
			}
			if (broj == 4) {
				rezultat = zbroj;
				break;
			}
		}
		return rezultat;
	}

	public int provjeriJamb(List<Dice> diceList) {
		int rezultat = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int zbroj = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					zbroj += k2.getCurrNum();
				}
			}
			if (broj == 5) {
				rezultat = zbroj;
				break;
			}
		}
		return rezultat;
	}
}
