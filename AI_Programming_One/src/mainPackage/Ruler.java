package mainPackage;

public class Ruler {
	
	Ruler() {
		
	}
	
	double checkRule(String pos, String word) {
		
		if (word.equals("amy")) {
			if (pos.equals("noun")) {
				return 0.1;
			}
		}
		
		if (word.equals("dinner")) {
			if (pos.equals("noun")) {
				return 0.2;
			}
		}
		
		if (word.equals("fish")) {
			if (pos.equals("noun")) {
				return 0.2;
			}
		}
		
		if (word.equals("streams")) {
			if (pos.equals("noun")) {
				return 0.1;
			}
			
			if (pos.equals("verb")) {
				return 0.1;
			}
		}
		
		if (word.equals("swims")) {
			if (pos.equals("noun")) {
				return 0.2;
			}
		}		
		
		if (word.equals("tuesday")) {
			if (pos.equals("noun")) {
				return 0.2;
			}
		}
		
		if (word.equals("for")) {
			if (pos.equals("prep")) {
				return 0.5;
			}
		}
		
		if (word.equals("in")) {
			if (pos.equals("prep")) {
				return 0.3;
			}
		}
		
		if (word.equals("on")) {
			if (pos.equals("prep")) {
				return 0.2;
			}
		}
		
		if (word.equals("ate")) {
			if (pos.equals("verb")) {
				return 0.7;
			}
		}
		
		if (word.equals("swim")) {
			if (pos.equals("verb")) {
				return 0.2;
			}
		}
		
		if (word.equals("fish")) {
			if (pos.equals("noun")) {
				return 0.3;
			}			
		}
		
		return 0;
	}
	
	double checkRule(String pos, String child1, String child2) {
		
		if (pos.equals("S")) {
			if (child1.equals("noun")) {
				if (child2.equals("verb")) {
					return 0.2;
				}
				
				if (child2.equals("VerbAndObject")) {
					return 0.3;
				}
				
				if (child2.equals("VPWithPPList")) {
					return 0.1;
				}				
			}
			
			if (child1.equals("NP")) {
				if (child2.equals("verb")) {
					return 0.2;
				}
				
				if (child2.equals("VerbAndObject")) {
					return 0.1;
				}
				
				if (child2.equals("VPWithPPList")) {
					return 0.1;
				}
			}			
		}
		
		if (pos.equals("NP")) {
			if (child1.equals("noun")) {
				if (child2.equals("PP")) {
					return 0.8;
				}
				
				if (child2.equals("PPList")) {
					return 0.2;
				}
			}
		}
		
		if (pos.equals("PP")) {
			if (child1.equals("prep")) {
				if (child2.equals("noun")) {
					return 0.6;
				}
				
				if (child2.equals("NP")) {
					return 0.4;
				}
			}
		}
		
		if (pos.equals("PPList")) {
			if (child1.equals("PP")) {
				if (child2.equals("PP")) {
					return 0.6;
				}
				
				if (child2.equals("PPList")) {
					return 0.4;
				}
			}
		}
		
		
		if (pos.equals("VerbAndObject")) {
			if (child1.equals("verb")) {
				if (child2.equals("noun")) {
					return 0.5;
				}
				
				if (child2.equals("NP")) {
					return 0.5;
				}
			}
		}
		
		if (pos.equals("VPWithPPList")) {
			if (child1.equals("verb")) {
				
				if (child2.equals("PP")) {
					return 0.3;
				}
				
				if (child2.equals("PPList")) {
					return 0.1;
				}
			}
			
			if (child1.equals("VerbAndObject")) {
				if (child2.equals("PP")) {
					return 0.4;
				}
				
				if (child2.equals("PPList")) {
					return 0.2;
				}
			}
		}
		
		return 0;
	}
	
	String key(int num) {
		
		switch(num) {
			case 0:
				return "S";
			case 1:
				return "NP";
			case 2:
				return "PP";
			case 3:
				return "PPList";
			case 4:
				return "VerbAndObject";
			case 5:
				return "VPWithPPList";
			case 6:
				return "noun";
			case 7:
				return "prep";
			case 8:
				return "verb";
			default:
				return null;
		}
	}
}